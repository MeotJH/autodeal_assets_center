package auto_deal.center.user.service;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.service.QuantService;
import auto_deal.center.talk.domain.Talk;
import auto_deal.center.talk.service.TalkService;
import auto_deal.center.trade_detail.domain.TradeDetail;
import auto_deal.center.trade_detail.repository.TradeDetailRepository;
import auto_deal.center.trade_detail.service.TradeDetailService;
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

    private final TradeDetailService tradeDetailService;

    @Override
    public Boolean Process(Long chatId, String text) {
        return this.saveUserTalk(chatId, text);
    }

    // 이 메소드에서 연관관계 주인이 왜 중요한지 알아냈다.
    private boolean saveUserTalk(Long chatId, String text){
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
            talkService.saveTalk(text,save);
            Quant quant = quantService.saveQuantByEnum(text, save);

            if( quant.getId() == null){
                tradeDetailService.saveTradeDetail(text, save);
            }
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
        talkService.saveTalk(text,userSaved);
    }

}
