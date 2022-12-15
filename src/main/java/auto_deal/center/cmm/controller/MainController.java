package auto_deal.center.cmm.controller;

import auto_deal.center.cmm.model.CommonModel;
import auto_deal.center.coin.service.CoinServiceImpl;
import auto_deal.center.quant.service.QuantService;
import auto_deal.center.talk.domain.Talk;
import auto_deal.center.talk.service.TalkService;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.telegram.service.ReturnMessage;
import auto_deal.center.trade_detail.service.TradeDetailService;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.repository.UserRepository;
import auto_deal.center.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final TalkService talkService;
    private final ReturnMessage returnMessage;
    private final QuantService quantService;
    private final CoinServiceImpl coinService;

    private final TradeDetailService tradeDetailService;
    public void process(Long chatId, String text){
        Boolean exist = userService.isUserExist(chatId, text);
        userService.process(chatId, text);
        if(exist){
            // 2.
            TelegramBotMessage equals = TelegramBotMessage.getEquals(text);
            if( equals != TelegramBotMessage.EMPTY ){ // enum이다
            }else if( coinService.isExist(text) ){ // coin 이다
                TelegramBotMessage prevTalk = talkService.getPrevTalk(chatId);
                //quantService.saveQuantByEnum();
                //quantService.saveQuantByEnum()
            }
        }else{
            // 1.
            returnMessage.process(chatId,talkService.saveTalk(chatId, text));
        }

        // 유저로 보내라
        // 새유저면 유저 저장, Enum이면 토크 저장 => return Enum에 맞는 값 보내기 => 1. 끝
        /*
            올드유저면
                Enum이면 토크 저장, Quant 저장 => return enum에 맞는 값 보내기
                COIN이면 토크 에서 전 ENUM 가져와서 Enum에 맞는 Quant저장 후 trade_detail 저장 => return API로 퀀트 리턴
         */
    }
}
