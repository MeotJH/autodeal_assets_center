package auto_deal.center.cmm.controller;

import auto_deal.center.coin.service.CoinServiceImpl;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.service.QuantService;
import auto_deal.center.talk.service.TalkService;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.telegram.service.ReturnMessage;
import auto_deal.center.trade_detail.service.TradeDetailService;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MainProcessor {
    private final UserService userService;
    private final TalkService talkService;
    private final ReturnMessage returnMessage;
    private final QuantService quantService;
    private final CoinServiceImpl coinService;
    private final TradeDetailService tradeDetailService;
    public void process(Long chatId, String text){
        Boolean exist = userService.isUserExist(chatId);
        Users processedUser = userService.process(chatId, text);
        if(exist){

            TelegramBotMessage equals = TelegramBotMessage.getEquals(text);
            if( equals != TelegramBotMessage.EMPTY ){ // enum이다
                TelegramBotMessage message = talkService.saveTalk(processedUser, text);
                quantService.saveQuantByEnum(message, processedUser);
                returnMessage.process(chatId,message);
            }else if( coinService.isExist(text) ){ // coin 이다
                TelegramBotMessage prevTalk = talkService.getPrevTalk(processedUser);
                Quant quant = quantService.saveQuantByEnum(prevTalk, processedUser);
                returnMessage.process( tradeDetailService.saveTradeDetail(chatId, text, quant) );
            }else{
                returnMessage.process(chatId, TelegramBotMessage.EMPTY);
            }

        }else{
            returnMessage.process(chatId,talkService.saveTalk(processedUser, text));
        }

    }
}
