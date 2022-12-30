package auto_deal.center.quant.service.strategy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(rollbackFor = Exception.class)
class TrendFollowTest {

    @Autowired
    private TrendFollow trendFollow;

    @Test
    @DisplayName("추세투자법 가격을 가져온다")
    void getTest(){

        //given
        String bitcoinTic = "BTC";
        //when
        Long trendFollowPrice = trendFollow.get(bitcoinTic);
        //then
        Assertions.assertThat(trendFollowPrice).isGreaterThan(20000000L);

    }

}