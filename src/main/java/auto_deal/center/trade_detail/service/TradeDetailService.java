package auto_deal.center.trade_detail.service;

import auto_deal.center.cmm.model.CommonModel;
import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.talk.domain.Talk;
import auto_deal.center.talk.repository.TalkRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.trade_detail.domain.TradeDetail;
import auto_deal.center.trade_detail.model.TradeDetailTalk;
import auto_deal.center.trade_detail.repository.TradeDetailRepository;
import auto_deal.center.user.domain.Users;
import lombok.Getter;
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

        // 한국어로 DB에 init되어있는 코인객체를 가져와 결국 TB_TRADE_DETAIL 에 넣어준다.
        TradeDetail tradeDeatail = TradeDetail.builder()
                                                .coinTicker(coin.getTicker())
                                                .regdate(LocalDateTime.now())
                                                .build();
        tradeDeatail.setQuant(quant);
        return tradeDetailRepository.save(tradeDeatail);
    }
}
