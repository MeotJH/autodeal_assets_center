package auto_deal.center.quant.service;

import auto_deal.center.api.coin.CoinPrice;
import auto_deal.center.api.coin.SixMonthSection;
import auto_deal.center.cmm.model.NoticeMessageModel;
import auto_deal.center.coin.service.CoinService;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.model.QuantModel;
import auto_deal.center.quant.model.StopLossModel;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.trade_detail.stop_loss.domain.StopLoss;
import auto_deal.center.trade_detail.stop_loss.repository.StopLossRepository;
import auto_deal.center.trade_detail.trend_follow.domain.TrendFollow;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class QuantStopLoss implements QuantType{

    private final SixMonthSection sixMonthSection;

    private final QuantService quantService;

    private final CoinPrice coinPrice;

    private final CoinService coinService;

    private final StopLossRepository stopLossRepository;

    @Override
    public <T extends QuantModel> T get(String ticker, Class<T> cls) {

        Double stopLossPercent = getStopLossPercent(ticker);
        Double dayOfMonthPrice = getDayOfMonthPrice(ticker);
        Double nowPrice = getNowPrice(ticker);

        return cls.cast(getModel(ticker, stopLossPercent, dayOfMonthPrice, nowPrice, getStopLossResult(stopLossPercent,nowPrice,dayOfMonthPrice)));
    }

    @Override
    public List<QuantModel> getAll() {
        return null;
    }

    @Override
    public List<NoticeMessageModel> notice() {
        List<NoticeMessageModel> noticeMessageModels = new ArrayList<>();

        //Quant테이블에서 QuantType이 StopLoss인 녀석들을 모두 가져와 해당하는 알림을 보내주어야 한다.
        for (Quant each: quantService.getAll(TelegramBotMessage.STOP_LOSS)) {

            NoticeMessageModel.NoticeMessageModelBuilder noticeMessageModelBuilder = NoticeMessageModel.builder().chatId(each.getUsers().getChatId());
            List<QuantModel> noticeModels = addNoticeModels(each);
            noticeMessageModels.add( noticeMessageModelBuilder.quantModels(noticeModels).build() );

        }
        return noticeMessageModels;
    }

    private Double getStopLossPercent(String ticker) {
        Map<String, Double> sections = sixMonthSection.get(ticker);
        Double averageOfSixMonth = 0.0;
        for (String each: sections.keySet()) {
            averageOfSixMonth = averageOfSixMonth + sections.get(each);
        }

        // 0.1 배수를 곱해서 이것보다 떨어지면 손절선을 잡는 부분이라 이 0.1을 변수화 하는것 생각해보기
        return Math.round( ((averageOfSixMonth / 6) * 0.1 )*1000)/1000.0;
    }

    private double getNowPrice(String ticker) {
        return Math.round(coinPrice
                .getNowPrice(ticker)
                .getJSONObject("data")
                .getJSONArray("bids")
                .getJSONObject(0)
                .getDouble("price"));
    }

    private double getDayOfMonthPrice(String ticker) {
        return Math.round(sixMonthSection.getMonthFirstPrice(ticker));
    }

    private String getStopLossResult(Double stopLossPercent,Double nowPrice,Double dayOfMonthPrice) {
        Double stopLossPrice = dayOfMonthPrice * (stopLossPercent/100);
        stopLossPrice = dayOfMonthPrice - stopLossPrice;

        String result;

        // ST01 이면 손절매 ST00 이면 계속보유
        // TODO 공통 코드화
        if( nowPrice > stopLossPrice ){
            result = "ST00";
        }else{
            result = "ST01";
        }

        return result;
    }

    private StopLossModel getModel(String ticker, Double stopLossPercent, Double dayOfMonthPrice, Double nowPrice,  String result) {
        return StopLossModel.builder()
                .ticker(ticker)
                .dayOfMonthPrice(dayOfMonthPrice)
                .nowPrice(nowPrice)
                .stopLossPercent(stopLossPercent)
                .result(result)
                .count(0)
                .build();
    }

    private List<QuantModel> addNoticeModels(Quant each) {
        List<QuantModel> quantModels = new ArrayList<>();
        for(StopLoss inEach: each.getStopLosses()){

            StopLossModel model = getStopLossModel(inEach);
            // 손절매 가격이 현재가격보다 높고 알림을 3번이하로 했다면 알려야한다.
            if( model.getStopLossPrice() > model.getDayOfMonthPrice() && (inEach.getCount() < 3) ){
                quantModels.add(model);
                saveStopLossCnt(inEach);
            }
        }
        return quantModels;
    }

    private StopLossModel getStopLossModel(StopLoss inEach) {

        String coinTicker = inEach.getCoinTicker();
        Double nowPrice = coinService.getCoin(coinTicker).getNow_price();
        Double initPrice = inEach.getInitPrice();
        Double stopLossPercent = inEach.getStopLossPercent();
        Double targetPrice = initPrice * (stopLossPercent/100);
        targetPrice = initPrice - targetPrice;

        StopLossModel model = StopLossModel.builder()
                .ticker(coinTicker)
                .dayOfMonthPrice(inEach.getDayOfMonthPrice())
                .nowPrice(nowPrice)
                .stopLossPercent(stopLossPercent)
                .stopLossPrice(targetPrice)
                .result("ST01")
                .count(inEach.getCount())
                .build();

        return model;
    }

    private void saveStopLossCnt(StopLoss inEach) {
        Integer count = inEach.getCount();
        inEach.updateCount(count++);
        stopLossRepository.save(inEach);
    }

}
