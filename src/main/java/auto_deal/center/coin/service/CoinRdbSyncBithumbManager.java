package auto_deal.center.coin.service;

import auto_deal.center.api.coin.CoinPrice;
import auto_deal.center.api.coin.model.CoinApiRslt;
import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.model.CoinInitModel;
import auto_deal.center.coin.repository.CoinRepository;
import auto_deal.center.quant.model.QuantModel;
import auto_deal.center.quant.model.TrendFollowModel;
import auto_deal.center.quant.service.QuantType;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CoinRdbSyncBithumbManager implements CoinRdbSyncManager {

    private final CoinPrice coinPrice;
    private final CoinRepository coinRepository;
    private final QuantType quantType;


    public void updateCoinToDb(){
        CoinApiRslt prices = coinPrice.getPrices();
        prices.getData().get("BTC").getMax_price();

        // DB에 값을 넣는다.
        Map<String, CoinApiRslt.Coin> data = prices.getData();
        data.forEach( (ticker,val) -> {
            Coin coin = null;
            Coin coinByTicker = coinRepository.findCoinByTicker(ticker);

            if( coinByTicker != null ){
                coin = coinByTicker.updateCoinData(val);
            }else{
                coin = Coin.builder()
                        .ticker(ticker)
                        .opening_price(val.getOpening_price())
                        .closing_price(val.getClosing_price())
                        .min_price(val.getMin_price())
                        .max_price(val.getMax_price())
                        .units_traded(val.getUnits_traded())
                        .acc_trade_value(val.getAcc_trade_value())
                        .prev_closing_price(val.getPrev_closing_price())
                        .units_traded_24H(val.getUnits_traded_24H())
                        .acc_trade_value_24H(val.getAcc_trade_value_24H())
                        .fluctate_24H(val.getFluctate_24H())
                        .fluctate_rate_24H(val.getFluctate_rate_24H())
                        .build();
            }

            coinRepository.save(coin);

        });
        coinRepository.flush();
    }

    @Override
    public void initCoinName(){
        CoinInitModel.values();

        for (CoinInitModel each :CoinInitModel.values()){
            String name = each.name();
            Coin coin = null;
            Coin coinByTicker = coinRepository.findCoinByTicker(name);

            if( coinByTicker != null ){
                coin = coinByTicker.updateCoinName(each);
            }else{
                coin = Coin.builder()
                        .ticker(each.name())
                        .korea(each.getKorea())
                        .english(each.getEnglish())
                        .build();
            }
            coinRepository.save(coin);
        }
    }


    @Override
    public void init3MAvgPrice() {
        List<QuantModel> all = quantType.getAll(); // 1분 40초
        for (QuantModel each: all) {
            TrendFollowModel cast = TrendFollowModel.class.cast(each);
            // 하나씩 가져오는.... 이게 맞나??
            Coin coinByTicker = coinRepository.findCoinByTicker(cast.getTicker());
            coinByTicker.update3MAvgPrice(cast.getTargetPrice());
            coinRepository.save(coinByTicker);
        }
        //TODO coinRdbSyncManger 와 QuantType 결과 간의 간극 어떻게 해결?!?!
    }

    @Override
    public Boolean initNowPrices() {
        JSONObject nowPrices = coinPrice.getNowPrices();

        if(nowPrices.getString("status").equals("0000")){
            JSONObject data = nowPrices.getJSONObject("data");
            List<Coin> all = coinRepository.findAll();
            for (Coin each: all) {
                JSONObject coinData = data.getJSONObject(each.getTicker());
                Double nowAskPrice = coinData
                                    .getJSONArray("asks")
                                    .getJSONObject(0)
                                    .getDouble("price");
                Coin afterUpdate = each.updateNowPrice(nowAskPrice);
                coinRepository.save(afterUpdate);
            }

            return true;
        }else{
            return false;
        }
    }


}
