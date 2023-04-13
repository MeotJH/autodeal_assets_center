package auto_deal.center.trade_detail.stop_loss.service;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.model.QuantModel;
import auto_deal.center.quant.model.StopLossModel;
import auto_deal.center.quant.model.TrendFollowModel;
import auto_deal.center.quant.service.QuantType;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.trade_detail.service.TradeDetailService;
import auto_deal.center.trade_detail.stop_loss.domain.StopLoss;
import auto_deal.center.trade_detail.stop_loss.repository.StopLossRepository;
import auto_deal.center.trade_detail.trend_follow.domain.TrendFollow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StopLossService implements TradeDetailService {

    private final CoinRepository coinRepository;

    private final StopLossRepository stopLossRepository;

    private final Map<String, QuantType> quantTypes;

    @Override
    public QuantModel processQuant(String text, Quant quant) {
        // exception
        Coin coin = Optional.ofNullable( coinRepository.findCoinByKorea(text) ).orElseThrow(() -> new RuntimeException("해당하는 한글 코인이 없습니다."));

        StopLossModel stopLossModel = quantTypes.get(TelegramBotMessage.valueOf(TelegramBotMessage.class, quant.getQuantType()).getBeanNm())
                                                .get(coin.getTicker(), StopLossModel.class);

        save(coin.getTicker(),quant,stopLossModel);


        return stopLossModel;
    }

    private StopLoss save(String ticker,Quant quant,StopLossModel stopLossModel){

        // 중복되면 넣지 않기 위해서
        Optional<StopLoss> byCoinTicker = Optional.ofNullable(stopLossRepository.findByCoinTickerAndQuant(ticker,quant));
        if(byCoinTicker.isPresent()){
            return stopLossRepository.save(byCoinTicker.get());
        }

        // 한국어로 DB에 init되어있는 코인객체를 가져와 TB_TREND_FOLLOW 에 넣어준다.
        StopLoss stopLoss = StopLoss.builder()
                                    .coinTicker(ticker)
                                    .initPrice(stopLossModel.getPrice())
                                    .stopLossPercent(stopLossModel.getStopLossPercent())
                                    .regdate(LocalDateTime.now())
                                    .build();

        stopLoss.setQuant(quant);
        return stopLossRepository.save(stopLoss);

    }


}
