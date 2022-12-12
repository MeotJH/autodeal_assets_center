package auto_deal.center.user.service;

import auto_deal.center.quant.service.QuantService;
import auto_deal.center.talk.domain.Talk;
import auto_deal.center.talk.service.TalkService;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final QuantService quantService;
    private final TalkService talkService;

    @Override
    public Boolean Process(Long chatId, String text) {
        return this.saveUserTalk(chatId, text);
    }

    // 이 메소드에서 연관관계 주인이 왜 중요한지 알아냈다.
    private boolean saveUserTalk(Long chatId, String text){
        Talk talk = talkService.saveTalk(text);
        if( isUserExist(chatId) ){
            saveUserRequst(chatId, talk);
        }else{
            saveNewUser(chatId, talk);
        }
        return Boolean.TRUE;
    }
    
    // chat 로 유저 존재하는지 bool값
    private Boolean isUserExist(Long chatId){
        Users users = userRepository.findUserOneByChatId(chatId);
        if(users != null){
            return true;
        }else{
            return false;
        }
    }
    
    // 존재하던 유저라면 응답을 업데이트한다
    private void saveUserRequst(Long chatId, Talk talk){
        Users one = userRepository.findUserOneByChatId(chatId);
        if( one != null){
            one.changRegDate();
            one.changeTalk(talk);
            Users save = userRepository.save(one);
            quantService.saveQuantByEnum(talk.getContent(),save);
        }
    }
    
    // 새로운 유저라면 저장한다
    private void saveNewUser(Long chatId, Talk talk) {
        // one을 저장하고
        Users userSaved = userRepository.save(
                Users
                        .builder()
                        .chatId(chatId)
                        .talk(talk)
                        .regDate(LocalDateTime.now())
                        .build()
        );

        // fk의 주인인 Many를 저장하면 자동으로
        quantService.saveQuantByEnum(talk.getContent(),userSaved);
    }

}
