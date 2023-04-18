package auto_deal.center.quant.service;

import auto_deal.center.api.coin.CoinPrice;
import auto_deal.center.api.coin.SixMonthSection;
import auto_deal.center.cmm.model.NoticeMessageModel;
import auto_deal.center.quant.model.QuantModel;
import auto_deal.center.quant.model.StopLossModel;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuantStopLoss implements QuantType{

    private final SixMonthSection sixMonthSection;

    private final CoinPrice coinPrice;

    @Override
    public <T extends QuantModel> T get(String ticker, Class<T> cls) {

        Double stopLossPercent = getStopLossPercent(ticker);
        Double nowPrice = getNowPrice(ticker);
        return cls.cast(getModel(ticker, stopLossPercent, nowPrice));
    }

    @Override
    public List<QuantModel> getAll() {
        return null;
    }

    @Override
    public List<NoticeMessageModel> notice() {
        return null;
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
        JSONObject nowPrice = coinPrice
                .getNowPrice(ticker);
        return Math.round(coinPrice
                .getNowPrice(ticker)
                .getJSONObject("data")
                .getJSONArray("bids")
                .getJSONObject(0)
                .getDouble("price"));
    }

    private StopLossModel getModel(String ticker, Double stopLossPercent, Double nowPrice) {
        return StopLossModel.builder().ticker(ticker).price(nowPrice).stopLossPercent(stopLossPercent).build();
    }

}
