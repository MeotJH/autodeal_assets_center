package auto_deal.center.user.service;

import auto_deal.center.cmm.model.CommonModel;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.service.QuantService;
import auto_deal.center.talk.service.TalkService;
import auto_deal.center.trade_detail.model.TradeDetailTalk;
import auto_deal.center.trade_detail.service.TradeDetailService;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final QuantService quantService;
    private final TalkService talkService;
    private final TradeDetailService tradeDetailService;

    @Override
    public Users process(Long chatId, String text) {
        return this.saveUserTalk(chatId, text);
    }

    @Override
    public Boolean isUserExist(Long chatId) {
        Boolean exist = false;
        Optional<Users> userDefine = Optional.ofNullable(userRepository.findUserOneByChatId(chatId));
        if(userDefine.isPresent()){
            exist = true;
        }else{
            exist = false;
        }
        return exist;
    }

    private Users saveUserTalk(Long chatId, String text){
        Users users = new Users();
        if( isUserExist(chatId) ){
            users = saveUserRequst(chatId, text);
        }else{
            users = saveNewUser(chatId, text);
        }
        return users;
    }
    
    // 존재하던 유저라면 응답을 업데이트한다
    private Users saveUserRequst(Long chatId, String text){
        Users one = Optional.ofNullable(userRepository.findUserOneByChatId(chatId)).orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));
        one.changRegDate();
        return userRepository.save(one);
    }
    
    // 새로운 유저라면 저장한다
    private Users saveNewUser(Long chatId, String text) {
        // one을 저장하고
        Users userSaved = userRepository.save(
                Users
                    .builder()
                    .chatId(chatId)
                    .regDate(LocalDateTime.now())
                    .build()
        );

        return userSaved;
    }

}
