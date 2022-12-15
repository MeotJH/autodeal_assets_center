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
    public Boolean isUserExist(Long chatId, String text) {
        Boolean exist = false;
        Optional<Users> userDefine = Optional.ofNullable(userRepository.findUserOneByChatId(chatId));
        if(userDefine.isPresent()){
            exist = true;
        }else{
            exist = false;
        }
        return exist;
    }

    @Override
    public Optional<Users> getUser(Long chatId) {
        return Optional.ofNullable(userRepository.findUserOneByChatId(chatId));
    }

    @Override
    public CommonModel Process(Long chatId, String text) {
        return null;//this.saveUserTalk(chatId, text);
    }

    // 이 메소드에서 연관관계 주인이 왜 중요한지 알아냈다.
    private Users saveUserTalk(Long chatId, String text){
        Users users = new Users();
        if( isUserExist(chatId) ){
            users = saveUserRequst(chatId, text);
        }else{
            users = saveNewUser(chatId, text);
        }
        return users;
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
    private Users saveUserRequst(Long chatId, String text){
        Users one = userRepository.findUserOneByChatId(chatId);
        CommonModel model = new TradeDetailTalk();
        if( one != null){
            one.changRegDate();
            one = userRepository.save(one);
            talkService.saveTalk(text,one);
            Quant quant = quantService.saveQuantByEnum(text, one);

            if( quant.getId() == null){
                model = tradeDetailService.saveTradeDetail(text, one);
            }
        }
        return one;
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

        // fk의 주인인 Many를 저장하면 자동으로
        quantService.saveQuantByEnum(text,userSaved);
        talkService.saveTalk(text,userSaved);

        return userSaved;
    }

}
