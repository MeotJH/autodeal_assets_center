package auto_deal.center.quant.service.strategy;

import auto_deal.center.api.coin.CoinPrice;
import auto_deal.center.api.coin.model.CoinOhlcvRslt;
import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrendFollow implements QuantStrategy{

    private final CoinPrice coinPrice;

    private final CoinRepository coinRepository;

    @Override
    public Long get(String ticker) {
        CoinOhlcvRslt ohlcv = coinPrice.getOhlcv(ticker);

        int today = ohlcv.getData().size();
        int month3 = today - 90;
        
        Long temp = 0L;
        List<List<String>> data = ohlcv.getData();
        for (int i = month3; i < today; i++) {
            Long closingPrice = Long.parseLong(data.get(i).get(2));// 2번 인덱스가 종가
            temp = temp + closingPrice;
        }

        Long threeMonthTrend = temp / (today - month3);
        Coin coinByTicker = coinRepository.findCoinByTicker(ticker);
        //todo threeMonthTrend 보다 높으면 사고 낮으면 팔아라 만들기



        return temp / (today - month3);
    }
}
