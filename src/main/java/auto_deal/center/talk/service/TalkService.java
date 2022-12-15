package auto_deal.center.talk.service;

import auto_deal.center.talk.domain.Talk;
import auto_deal.center.talk.repository.TalkRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TalkService {

    private final TalkRepository talkRepository;
    private final UserService userService;

    public Talk saveTalk(String text, Users savedUser){
        Talk talkOne = Talk.builder().content(text).regDate(LocalDateTime.now()).build();
        talkOne.setUser(savedUser);
        return talkRepository.save(talkOne);
    }

    public TelegramBotMessage saveTalk(Long chatId, String text){

        if( TelegramBotMessage.getEquals(text) != TelegramBotMessage.EMPTY ){
            Talk talkOne = Talk.builder().content(text).regDate(LocalDateTime.now()).build();
            Users user = userService.getUser(chatId).orElseGet(() -> new Users());
            talkOne.setUser(user);
            talkRepository.save(talkOne);
        }

        return TelegramBotMessage.getEquals(text);
    }
}
