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
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeDetailService {

    private final TradeDetailRepository tradeDetailRepository;
    private final CoinRepository coinRepository;
    private final TalkRepository talkRepository;

    public CommonModel saveTradeDetail(String text, Users userOne){

        TradeDetailTalk model = new TradeDetailTalk();

        List<Talk> talks = talkRepository.findByUsersOrderByRegDateDesc(userOne);

        //유저의 마지막 대화중 봇에 적어놓은 '/xxx' 기능을 마지막으로 가져온다.
        Talk talk = talks.stream().filter(v -> v.getContent().contains("/")).findFirst().get();

        // telegrambotmessage에서 해당 값과 같은 enum상수를 가져온다.
        String name = Arrays.stream(TelegramBotMessage.values()).filter(v -> v.getCodeKr().equals(talk.getContent()) || v.getCodeEn().equals(talk.getContent()) ).findFirst().get().name();

        // 위 enum에 맞는 quant투자법을 Quant DB에서 가져온다.
        Quant quant = userOne.getQuants().stream().filter(v -> v.getQuantType().equals(name)).findFirst().get();

        TradeDetail tradeDeatail = TradeDetail.builder().build();
        Coin coinByKorea = coinRepository.findCoinByKorea(text);

        // 한국어로 DB에 init되어있는 코인객체를 가져와 결국 TB_TRADE_DETAIL 에 넣어준다.
       if( coinByKorea != null){
            tradeDeatail = TradeDetail.builder()
                                    .coinTicker(coinByKorea.getTicker())
                                    .regdate(LocalDateTime.now())
                                    .build();
            tradeDeatail.setQuant(quant);
            TradeDetail save = tradeDetailRepository.save(tradeDeatail);
            model.set(save);
       }

        return model;
    }
}
