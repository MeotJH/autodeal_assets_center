package auto_deal.center.telegram.service;

import auto_deal.center.cmm.model.CommonModel;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.dto.QuantModel;
import auto_deal.center.quant.service.QuantType;
import auto_deal.center.quant.service.TrendFollow;
import auto_deal.center.talk.domain.Talk;
import auto_deal.center.talk.repository.TalkRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.trade_detail.domain.TradeDetail;
import auto_deal.center.trade_detail.model.TradeDetailTalk;
import auto_deal.center.trade_detail.service.TradeDetailService;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReturnMessage {

    private final TradeDetailService tradeDetailService;
    private final Map<String,QuantType> quantTypes;

    public String process(CommonModel model,String text){
        String message = error();

        if( model.get() == null){
            return telegramBotMessage(text);
        }

        TradeDetail tradeDetail = TradeDetail.class.cast(model.get());

        if(model.get() != null && tradeDetail.getId() != null){
            message = sendTradeMessage(tradeDetail);
        }

        return message;
    }

    public String error(){
        return "에러가 났습니다.";
    }

    private String telegramBotMessage(String text){
        // 봇 커맨드에 맞는 답변을 가져온다
        return Arrays.stream(TelegramBotMessage.values())
                .filter(v -> text.equals(v.getCodeEn()) || text.equals(v.getCodeKr()) )
                .findAny().orElseGet( () -> TelegramBotMessage.EMPTY ).getMessage();
    }

    private String sendTradeMessage(TradeDetail tradeDetail){
        String coinTicker = tradeDetail.getCoinTicker();
        String quantType = tradeDetail.getQuant().getQuantType();

        TelegramBotMessage telegramBotMessage = Arrays.stream(TelegramBotMessage.values()).filter(val -> val.name().equals(quantType)).findFirst().get();

        QuantModel quantModel = quantTypes.get(telegramBotMessage.getBeanNm()).get(coinTicker);

        return quantModel.toRsltStr();

    }

}
