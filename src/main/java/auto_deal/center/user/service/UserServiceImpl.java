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

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final QuantService quantService;
    private final TalkService talkService;
    private final TradeDetailService tradeDetailService;

    @Override
    public CommonModel Process(Long chatId, String text) {
        return this.saveUserTalk(chatId, text);
    }

    // 이 메소드에서 연관관계 주인이 왜 중요한지 알아냈다.
    private CommonModel saveUserTalk(Long chatId, String text){
        CommonModel model = new TradeDetailTalk();
        if( isUserExist(chatId) ){
            model = saveUserRequst(chatId, text);
        }else{
            saveNewUser(chatId, text);
        }
        return model;
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
    private CommonModel saveUserRequst(Long chatId, String text){
        Users one = userRepository.findUserOneByChatId(chatId);
        CommonModel model = new TradeDetailTalk();
        if( one != null){
            one.changRegDate();
            Users save = userRepository.save(one);
            talkService.saveTalk(text,save);
            Quant quant = quantService.saveQuantByEnum(text, save);

            if( quant.getId() == null){
                model = tradeDetailService.saveTradeDetail(text, save);
            }
        }
        return model;
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
