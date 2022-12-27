package auto_deal.center.trend_follow.service;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.trade_detail.service.TradeDetailService;
import auto_deal.center.trend_follow.domain.TrendFollow;
import auto_deal.center.trend_follow.repository.TrendFollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Primary
public class TrendFollowService<T> implements TradeDetailService{

    private final TrendFollowRepository trendFollowRepository;
    private final CoinRepository coinRepository;

    @Override
    public <T> T saveTradeDetail(Long chatId, String text, Quant quant, Class<T> cls){
        Coin beforeOpt = coinRepository.findCoinByKorea(text);
        Coin coin = Optional.ofNullable(beforeOpt).orElseThrow(() -> new RuntimeException("해당하는 한글 코인이 없습니다."));

        //이미 있는거라면 있는거 리턴
        Optional<TrendFollow> byCoinTicker = Optional.ofNullable(trendFollowRepository.findByCoinTickerAndQuant(coin.getTicker(),quant));
        if(byCoinTicker.isPresent()){
            return cls.cast(byCoinTicker.get());
        }

        // 한국어로 DB에 init되어있는 코인객체를 가져와 TB_TRADE_DETAIL 에 넣어준다.
        TrendFollow tradeDeatail = TrendFollow.builder()
                .coinTicker(coin.getTicker())
                .regdate(LocalDateTime.now())
                .build();
        tradeDeatail.setQuant(quant);
        trendFollowRepository.save(tradeDeatail);
        return cls.cast(trendFollowRepository.save(tradeDeatail));
    }
}
