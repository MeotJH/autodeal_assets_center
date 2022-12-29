package auto_deal.center.quant.service;

import auto_deal.center.api.coin.CoinPrice;
import auto_deal.center.api.coin.model.CoinOhlcvRslt;
import auto_deal.center.cmm.model.NoticeMessageModel;
import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.service.CoinService;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.model.QuantModel;
import auto_deal.center.quant.model.TrendFollowModel;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.trade_detail.service.TradeDetailService;
import auto_deal.center.trade_detail.trend_follow.domain.TrendFollow;
import auto_deal.center.trade_detail.trend_follow.repository.TrendFollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Quant투자 관련 계산위한 구현체
 */
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class QuantTrendFollow implements QuantType {

    private final CoinPrice coinPrice;
    private final CoinService coinService;
    private final QuantService quantService;
    private final TrendFollowRepository trendFollowRepository;

    @Override
    public <T extends QuantModel> T get(String ticker, Class<T> cls){
        Boolean isBuy = Boolean.FALSE;
        CoinOhlcvRslt rslt = coinPrice.getOhlcv(ticker);
        QuantModel model = null;

        if(isStatusOk(rslt)){
            List<List<String>> priceHistory = rslt.getData();
            Long Price = getThreeMonthTrendFollowPrice(priceHistory);
            model = defineBuyOrNot(ticker, Price);
        }

        return cls.cast(model);
    }

    @Override
    public List<QuantModel> getAll(){
        List<QuantModel> models = new ArrayList<>();
        QuantModel model = null;
        for (Coin each : coinService.getAllTicker()) {
            CoinOhlcvRslt rslt = coinPrice.getOhlcv(each.getTicker());
            if(isStatusOk(rslt)){
                List<List<String>> priceHistory = rslt.getData();
                Long price = getThreeMonthTrendFollowPrice(priceHistory);
                model = defineBuyOrNot(each.getTicker(), price);
                models.add(model);
            }
        }
        return models;
    }

    /**
     * 해당 퀀트에 해당하는 알림 있는지 확인
     */
    public List<NoticeMessageModel> notice(){
        List<NoticeMessageModel> noticeMessageModels = new ArrayList<>();
        for (Quant each: quantService.getAll()) {

            NoticeMessageModel.NoticeMessageModelBuilder noticeMessageModelBuilder = NoticeMessageModel.builder().chatId(each.getUsers().getChatId());
            if( each.getQuantType().equals(TelegramBotMessage.TREND_FOLLOW.name()) ){
                List<QuantModel> noticeModels = addNoticeModels(each);
                noticeMessageModels.add( noticeMessageModelBuilder.quantModels(noticeModels).build() );
            }

        }
        return noticeMessageModels;
    }

    private boolean isStatusOk(CoinOhlcvRslt rslt) {
        return rslt.getStatus().equals("0000");
    }

    @NotNull
    private Long getThreeMonthTrendFollowPrice(List<List<String>> priceHistory) {
        int today = priceHistory.size() - 1;
        int threeMonthBefore = today - 90;

        Long sum = 0L;
        int cnt = 0;

        if( threeMonthBefore < 0){
            return 0L;
        }

        for (int i = threeMonthBefore; i < today; i++) {
            Long temp = Long.parseLong(String.valueOf(Math.round(Double.parseDouble(priceHistory.get(i).get(2)))));
            sum = sum + temp;
        }

        Long threeMonthTrendFollowPrice  = sum / (today - threeMonthBefore);
        return threeMonthTrendFollowPrice;
    }

    @NotNull
    private QuantModel defineBuyOrNot(String ticker, Long price) {
        Boolean isBuy;
        long nowPrice = Math.round(coinPrice
                .getNowPrice(ticker)
                .getJSONObject("data")
                .getJSONArray("bids")
                .getJSONObject(0)
                .getDouble("price"));


        log.info("::::::::: coin now price :::: {} :::::",nowPrice);
        log.info("::::::::: coin trend-follow price :::: {} :::::",price);

        if( price < nowPrice ){
            isBuy = Boolean.TRUE;
        }else{
            isBuy = Boolean.FALSE;
        }
        
        return TrendFollowModel.builder().ticker(ticker).targetPrice(price).nowPrice(nowPrice).isBuy(isBuy).build();
    }

    private List<QuantModel> addNoticeModels(Quant each) {
        List<QuantModel> quantModels = new ArrayList<>();
        for(TrendFollow inEach: each.getTrendFollows()){

            Coin coin = coinService.getCoin(inEach.getCoinTicker());
            Long threeMonthAvgPrice = coin.getThree_month_avg_price();
            Double nowPrice = coin.getNow_price();
            Boolean isBuy = Math.floor(nowPrice) > threeMonthAvgPrice ? Boolean.TRUE : Boolean.FALSE;

            if(isBuy != inEach.getIsBuy()){
                // 지금 bool값이랑 이전 bool값이랑 다르면 노티스
                TrendFollowModel model = TrendFollowModel
                        .builder()
                        .isBuy(isBuy)
                        .nowPrice((long) Math.floor(nowPrice))
                        .targetPrice(threeMonthAvgPrice)
                        .ticker(inEach.getCoinTicker())
                        .build();
                quantModels.add(model);
                
                // 매도 매수 값이 과거와 바뀌었으면 재설정
                saveTrendFollow(inEach, isBuy);

            }

        }
        return quantModels;
    }

    private void saveTrendFollow(TrendFollow inEach, Boolean isBuy) {
        inEach.updateIsBuy(isBuy);
        trendFollowRepository.save(inEach);
    }

}
