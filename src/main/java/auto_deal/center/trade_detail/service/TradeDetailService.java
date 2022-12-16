package auto_deal.center.trade_detail.service;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.trade_detail.domain.TradeDetail;
import auto_deal.center.trade_detail.repository.TradeDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TradeDetailService {

    private final TradeDetailRepository tradeDetailRepository;
    private final CoinRepository coinRepository;

    public TradeDetail saveTradeDetail(Long chatId, String text,Quant quant){
        TradeDetail rslt = TradeDetail.builder().build();
        Coin beforeOpt = coinRepository.findCoinByKorea(text);
        Coin coin = Optional.ofNullable(beforeOpt).orElseThrow(() -> new RuntimeException("해당하는 한글 코인이 없습니다."));
        
        //이미 있는거라면 있는거 리턴
        Optional<TradeDetail> byCoinTicker = Optional.ofNullable(tradeDetailRepository.findByCoinTicker(coin.getTicker()));
        if(byCoinTicker.isPresent()){
            return byCoinTicker.get();
        }

        // 한국어로 DB에 init되어있는 코인객체를 가져와 TB_TRADE_DETAIL 에 넣어준다.
        TradeDetail tradeDeatail = TradeDetail.builder()
                                                .coinTicker(coin.getTicker())
                                                .regdate(LocalDateTime.now())
                                                .build();
        tradeDeatail.setQuant(quant);
        return tradeDetailRepository.save(tradeDeatail);
    }
}
