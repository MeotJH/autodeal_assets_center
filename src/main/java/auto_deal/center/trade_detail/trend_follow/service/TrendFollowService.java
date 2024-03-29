package auto_deal.center.trade_detail.trend_follow.service;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.model.QuantModel;
import auto_deal.center.quant.model.TrendFollowModel;
import auto_deal.center.quant.service.QuantType;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.trade_detail.service.TradeDetailService;
import auto_deal.center.trade_detail.trend_follow.repository.TrendFollowRepository;
import auto_deal.center.trade_detail.trend_follow.domain.TrendFollow;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * DB 통신 위한 TrendFollow 구현체
 * @param <T>
 */
@Service
@RequiredArgsConstructor
@Primary
public class TrendFollowService<T> implements TradeDetailService{

    private final TrendFollowRepository trendFollowRepository;
    private final CoinRepository coinRepository;
    private final Map<String, QuantType> quantTypes;

    @Override
    public QuantModel processQuant(String text, Quant quant) {
        // exception
        Coin coin = Optional.ofNullable( coinRepository.findCoinByKorea(text) ).orElseThrow(() -> new RuntimeException("해당하는 한글 코인이 없습니다."));

        //메세지 리턴할 값 및 평균이동값 가져온것
        TrendFollowModel trendFollowModel = quantTypes.get(TelegramBotMessage.valueOf(TelegramBotMessage.class,quant.getQuantType()).getBeanNm())
                                                      .get(coin.getTicker(),TrendFollowModel.class);

        //trendFollow 저장
        saveTrendFollow(
                    coin.getTicker()
                    ,trendFollowModel.getIsBuy()
                    ,quant
                    );
        return trendFollowModel;
    }


    private TrendFollow saveTrendFollow(String ticker, Boolean isBuy, Quant quant){
        
        // 중복되면 넣지 않기 위해서
        Optional<TrendFollow> byCoinTicker = Optional.ofNullable(trendFollowRepository.findByCoinTickerAndQuant(ticker,quant));
        if(byCoinTicker.isPresent()){
            return trendFollowRepository.save(byCoinTicker.get());
        }

        // 한국어로 DB에 init되어있는 코인객체를 가져와 TB_TREND_FOLLOW 에 넣어준다.
        TrendFollow trendFollow = TrendFollow.builder()
                .coinTicker(ticker)
                .regdate(LocalDateTime.now())
                .isBuy(isBuy)
                .build();

        trendFollow.setQuant(quant);
        return trendFollowRepository.save(trendFollow);
    }
}
