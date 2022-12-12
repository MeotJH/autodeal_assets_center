package auto_deal.center.trade_detail.service;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.talk.domain.Talk;
import auto_deal.center.talk.repository.TalkRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.trade_detail.domain.TradeDetail;
import auto_deal.center.trade_detail.repository.TradeDetailRepository;
import auto_deal.center.user.domain.Users;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeDetailService {

    private final TradeDetailRepository tradeDetailRepository;
    private final CoinRepository coinRepository;

    private final TalkRepository talkRepository;

    @Getter
    private TradeDetail tradeDetail = TradeDetail.builder().build();

    public TradeDetail saveTradeDetail(String text, Users userOne){

        List<Talk> talks = talkRepository.findByUsersOrderByRegDateDesc(userOne);
        Talk talk = talks.stream().filter(v -> v.getContent().contains("/")).findFirst().get();
        String name = Arrays.stream(TelegramBotMessage.values()).filter(v -> v.getCodeKr().equals(talk.getContent()) || v.getCodeEn().equals(talk.getContent()) ).findFirst().get().name();
        Quant quant = userOne.getQuants().stream().filter(v -> v.getQuantType().equals(name)).findFirst().get();

        TradeDetail tradeDeatail = TradeDetail.builder().build();
        Coin coinByKorea = coinRepository.findCoinByKorea(text);

        if( coinByKorea != null){
            tradeDeatail = TradeDetail.builder()
                                    .coinTicker(coinByKorea.getTicker())
                                    .regdate(LocalDateTime.now())
                                    .build();
            tradeDeatail.setQuant(quant);
        }

        tradeDetail = tradeDetailRepository.save(tradeDeatail);
        return tradeDetail;
    }
}
