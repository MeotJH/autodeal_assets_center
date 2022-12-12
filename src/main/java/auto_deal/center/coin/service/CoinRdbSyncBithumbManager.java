package auto_deal.center.coin.service;

import auto_deal.center.api.coin.CoinPrice;
import auto_deal.center.api.coin.model.CoinApiRslt;
import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.model.CoinInitModel;
import auto_deal.center.coin.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class CoinRdbSyncBithumbManager implements CoinRdbSyncManager {

    private final CoinPrice coinPrice;
    private final CoinRepository coinRepository;

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


}
