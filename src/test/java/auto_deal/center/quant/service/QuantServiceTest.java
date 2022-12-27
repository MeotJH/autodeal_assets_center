package auto_deal.center.quant.service;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.repository.QuantRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuantServiceTest {

    @Autowired
    private QuantService quantService;
    @Autowired
    private QuantRepository quantRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("quant가 저장되는지 확인한다.")
    @Transactional(rollbackFor = Exception.class)
    void saveQuantByEnumTest() {
        //given
        TelegramBotMessage tbm = TelegramBotMessage.TREND_FOLLOW;
        Users saved = userRepository.save(Users.builder().build());
        //when
        quantService.saveQuantByEnum(TelegramBotMessage.TREND_FOLLOW, saved);
        List<Quant> byQuantType = quantRepository.findByQuantType(tbm.name());

        //then
        Assertions.assertThat(byQuantType.get(0).getQuantType()).isEqualTo(tbm.name());
    }

    @Test
    @DisplayName("quant가 중복 저장되지 않게해야한다.")
    @Transactional(rollbackFor = Exception.class)
    void saveQuantByEnumUserBySameQuantOneTest() {
        //given
        TelegramBotMessage tbm = TelegramBotMessage.TREND_FOLLOW;
        Users saved = userRepository.save(Users.builder().build());

        //when
        Quant savedQuant = quantService.saveQuantByEnum(tbm, saved);
        Quant secondSavedQuant = quantService.saveQuantByEnum(tbm, saved);

        //then
        Assertions.assertThat(savedQuant.getId()).isEqualTo(secondSavedQuant.getId());
    }

    @Test
    @DisplayName("알림처리를 위한 notice를 가져와야 한다.")
    @Transactional(rollbackFor = Exception.class)
    void noticeTest() {
        //given
        String trend_follow = quantService.notice("TREND_FOLLOW");

        //when

        //then
        Assertions.assertThat(trend_follow).isEqualTo("true");

    }
}