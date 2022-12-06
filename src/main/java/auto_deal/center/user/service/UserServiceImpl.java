package auto_deal.center.user.service;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.repository.QuantRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final QuantRepository quantRepository;

    @Override
    public Boolean Process(Long chatId, String text) {

        this.saveUserTalk(chatId, text);
        return Boolean.TRUE;

        //text에 상승장, 비트코인 과 같은 문구가 옴
    }

    // 이 메소드에서 연관관계 주인이 왜 중요한지 알아냈다.
    private boolean saveUserTalk(Long chatId, String text){
        // one을 저장하고
        Users userOne = Users.builder().chatId(chatId).regDate(LocalDateTime.now()).build();
        userRepository.save(userOne);
        
        // fk의 주인인 Many를 저장하면 자동으로
        Quant quantOne = getQuant(text);
        quantOne.setUser(userOne);
        quantRepository.save(quantOne);

        return Boolean.TRUE;
    }
    
    // 연관관계 주인의 중요성을 이해하지 못했을때의 코드
    private boolean saveUserTalkOld(Long chatId, String text){

        Quant quantOne = getQuant(text);
        quantRepository.save(quantOne);

        Users userOne = Users.builder().chatId(chatId).regDate(LocalDateTime.now()).build();
        userOne.addQuant(quantOne);
        userRepository.save(userOne);

        quantOne.setUser(userOne);
        quantRepository.save(quantOne);

        return Boolean.TRUE;
    }

    //ENUM 에 넣어놓은 대화에 해당하는 타입이 있다면 그것으로 가져온다.
    private Quant getQuant(String text) {
        String quantType = new String();
        for(TelegramBotMessage each :TelegramBotMessage.values()){
            if( each.getCodeKr().equals(text) || each.getCodeEn().equals(text) ){
                quantType = each.name();
            }
        }
        return Quant.builder().quantType(quantType.toString()).regdate(LocalDateTime.now()).build();
    }
}
