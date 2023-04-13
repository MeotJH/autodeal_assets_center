package auto_deal.center.api.coin;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Map;

@SpringBootTest
class MonthCoinPriceTest {

    @Autowired
    public SixMonthSection monthPrice;

    @Test
    @DisplayName("특정 코인의 가장 마지막달 평균주가가 0이 아니어야 한다.")
    void get() {
        //given
        String ticker = "BTC";

        //when
        Map<String, Double> map = monthPrice.get(ticker);
        LocalDateTime now = LocalDateTime.now();
        String yearLastMonth = now.getYear() + Integer.toString(now.getMonthValue()-1);
        Double aDouble = map.get(yearLastMonth);

        //then
        Assertions.assertThat(aDouble).isNotEqualTo(0.0);
    }
}