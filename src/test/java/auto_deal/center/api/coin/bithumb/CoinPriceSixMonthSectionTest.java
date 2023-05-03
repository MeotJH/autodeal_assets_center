package auto_deal.center.api.coin.bithumb;

import auto_deal.center.api.coin.SixMonthSection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CoinPriceSixMonthSectionTest {

    @Autowired
    public SixMonthSection sixMonthSection;

    @DisplayName("이번달 시작일 시가로 가져온 코인가격이 0원이 아니어야 한다.")
    @Test
    void getMonthFirstPrice() {
        //given //when
        Double btc = sixMonthSection.getMonthFirstPrice("BTC");

        //then
        Assertions.assertThat(btc).isNotEqualTo(0.0);
    }
}