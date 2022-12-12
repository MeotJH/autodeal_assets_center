package auto_deal.center.quant.service;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.talk.domain.Talk;
import auto_deal.center.trade_detail.repository.TradeDetailRepository;
import auto_deal.center.trade_detail.service.TradeDetailService;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.repository.UserRepository;
import auto_deal.center.user.service.UserService;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuantServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    void saveQuantByEnumTest() {



    }
}