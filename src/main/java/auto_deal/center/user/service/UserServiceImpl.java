package auto_deal.center.user.service;

import auto_deal.center.quant.service.QuantService;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final QuantService quantService;

    @Override
    public Boolean Process(Long chatId, String text) {
        // TODO 뭐할지 보내고 그다음에 코인을 보내는데 그거 어떻게 파악해서 저장?!
        this.saveUserTalk(chatId, text);
        return Boolean.TRUE;
    }

    // 이 메소드에서 연관관계 주인이 왜 중요한지 알아냈다.
    private boolean saveUserTalk(Long chatId, String text){
        // TODO 저장 전에 유저 존재하는지 확인하는 코드
        if( isUserExist(chatId) ){
            saveUserRequst(chatId, text);
        }else{
            saveNewUser(chatId, text);
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
    private void saveUserRequst(Long chatId, String text){
        Users one = userRepository.findUserOneByChatId(chatId);
        if( one != null){
            one.changRegDate();
            Users save = userRepository.save(one);
            quantService.saveQuantByEnum(text,save);
        }
    }
    
    // 새로운 유저라면 저장한다
    private void saveNewUser(Long chatId, String text) {
        // one을 저장하고
        Users userSaved = userRepository.save(
                Users
                        .builder()
                        .chatId(chatId)
                        .regDate(LocalDateTime.now())
                        .build()
        );

        // fk의 주인인 Many를 저장하면 자동으로
        quantService.saveQuantByEnum(text,userSaved);
    }

}
