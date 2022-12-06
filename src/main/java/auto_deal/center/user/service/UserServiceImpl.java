package auto_deal.center.user.service;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.repository.QuantRepository;
import auto_deal.center.quant.service.QuantService;
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
    private final QuantService quantService;

    @Override
    public Boolean Process(Long chatId, String text) {
        this.saveUserTalk(chatId, text);
        return Boolean.TRUE;
    }

    // 이 메소드에서 연관관계 주인이 왜 중요한지 알아냈다.
    private boolean saveUserTalk(Long chatId, String text){
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
        return Boolean.TRUE;
    }
}
