package auto_deal.center.quant.service;

import auto_deal.center.api.coin.CoinPrice;
import auto_deal.center.api.coin.model.CoinOhlcvRslt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrendFollow {

    private final CoinPrice coinPrice;

    public Boolean get(String ticker){
        Boolean isBuy = Boolean.FALSE;
        CoinOhlcvRslt rslt = coinPrice.getOhlcv("BTC");

        if(isStatusOk(rslt)){
            List<List<String>> priceHistory = rslt.getData();
            Long Price = getThreeMonthTrendFollowPrice(priceHistory);
            isBuy = defineBuyOrNot(ticker, Price);
        }

        return isBuy;
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
        for (int i = threeMonthBefore; i < today; i++) {
            Long temp = Long.parseLong(priceHistory.get(i).get(2));
            sum = sum + temp;
        }

        Long threeMonthTrendFollowPrice  = sum / (today - threeMonthBefore);
        return threeMonthTrendFollowPrice;
    }

    @NotNull
    private Boolean defineBuyOrNot(String ticker, Long price) {
        Boolean isBuy;
        long nowPrice = coinPrice
                .getNowPrice(ticker)
                .getJSONObject("data")
                .getJSONArray("bids")
                .getJSONObject(0)
                .getLong("price");


        log.info("::::::::: coin now price :::: {} :::::",nowPrice);
        log.info("::::::::: coin trend-follow price :::: {} :::::",price);

        if( price < nowPrice ){
            isBuy = Boolean.TRUE;
        }else{
            isBuy = Boolean.FALSE;
        }
        return isBuy;
    }

}
