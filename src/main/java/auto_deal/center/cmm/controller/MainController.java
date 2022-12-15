package auto_deal.center.cmm.controller;

import auto_deal.center.cmm.model.CommonModel;
import auto_deal.center.talk.domain.Talk;
import auto_deal.center.talk.service.TalkService;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.telegram.service.ReturnMessage;
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

    public void process(Long chatId, String text){
        Boolean exist = userService.isUserExist(chatId, text);
        userService.process(chatId, text);
        if(exist){
        }else{
            // 1.
            returnMessage.process(chatId,talkService.saveTalk(chatId, text));
        }

        // 유저로 보내라
        // 새유저면 유저 저장, Enum이면 토크 저장 => return Enum에 맞는 값 보내기 => 1. 끝
        /*
            올드유저면
                Enum이면 토크 저장, Quant 저장 => return enum에 맞는 값 보내기
                COIN이면 토크 에서 전 ENUM 가져와서 trade_detail 저장 => return API로 퀀트 리턴
         */
    }
}
