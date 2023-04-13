package auto_deal.center.cmm.controller;

import auto_deal.center.cmm.model.NoticeMessageModel;
import auto_deal.center.coin.service.CoinServiceImpl;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.model.QuantModel;
import auto_deal.center.quant.service.QuantService;
import auto_deal.center.quant.service.QuantType;
import auto_deal.center.talk.service.TalkService;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.telegram.service.ReturnMessage;
import auto_deal.center.trade_detail.service.TradeDetailService;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.service.UserService;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MainProcessor {
    private final UserService userService;
    private final TalkService talkService;
    private final ReturnMessage returnMessage;
    private final QuantService quantService;
    private final CoinServiceImpl coinService;
    private final Map<String, TradeDetailService> tradeDetailServices;
    private final Map<String, QuantType> quantTypes;
    public void process(Long chatId, String text){
        Boolean exist = userService.isUserExist(chatId);
        Users processedUser = userService.process(chatId, text);
        if(exist){

            TelegramBotMessage equals = TelegramBotMessage.getEquals(text);
            if( isExistedMessage(equals) ){ // enum이다
                TelegramBotMessage message = talkService.saveTalk(processedUser, text);
                quantService.saveQuantByEnum(message, processedUser);
                returnMessage.process( chatId, message );
            }else if( isExistedCoin(text) ){ // coin 이다
                TelegramBotMessage prevTalk = talkService.getPrevTalk(processedUser);
                Quant quant = quantService.saveQuantByEnum(prevTalk, processedUser);
                
                //TODO 공통코드 테이블 만들어서 해당부분 리팩토링 해야함
                String tempStr = prevTalk.name().equals("STOP_LOSS") ? "stopLossService" : "trendFollowService";

                returnMessage.process( chatId, tradeDetailServices.get(tempStr).processQuant(text, quant) );
            }else{
                returnMessage.process( chatId, TelegramBotMessage.EMPTY);
            }

        }else{
            returnMessage.process(chatId,talkService.saveTalk(processedUser, text));
        }

    }

    public void returnProcess(TelegramBotMessage telegramBotMessage){
        for (NoticeMessageModel each: quantTypes.get(telegramBotMessage.getBeanNm()).notice() ) {
            for(QuantModel inEach : each.getQuantModels()){
                returnMessage.process(each.getChatId(), inEach);
            }
        }
    }

    private boolean isExistedMessage(TelegramBotMessage equals) {
        return equals != TelegramBotMessage.EMPTY;
    }

    private Boolean isExistedCoin(String text) {
        return coinService.isExist(text);
    }
}
