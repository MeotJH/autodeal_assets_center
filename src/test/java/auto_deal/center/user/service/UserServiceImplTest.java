package auto_deal.center.user.service;

import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    public UserService userService;

    @Autowired
    public UserRepository userRepository;
    
    @Test
    @DisplayName( "유저 프로세스 인서트테스트한다.")
    @Transactional(rollbackFor = Exception.class)
    void processTest(){
        //given
        Long chatId = 1212312312L;
        String text = TelegramBotMessage.BULL_MARKET.getCodeEn();

        //when
        userService.Process(chatId,text);
        Users userOne = userRepository.findUserOneByChatId(chatId);

        //then
        Assertions.assertThat(userOne.getQuants().get(0).getQuantType()).isEqualTo(TelegramBotMessage.BULL_MARKET.toString());

    }

    @Test
    @DisplayName( "유저 프로세스 인서트테스트한다.")
    @Transactional(rollbackFor = Exception.class)
    void processTestNoEnumType(){
        //given
        Long chatId = 1212312312L;
        String text = "안녕하세요?";

        //when
        userService.Process(chatId,text);
        Users userOne = userRepository.findUserOneByChatId(chatId);

        //then
        Assertions.assertThat(userOne.getQuants().get(0).getQuantType()).isNotEqualTo(text);

    }
    

}