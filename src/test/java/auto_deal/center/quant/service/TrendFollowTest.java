package auto_deal.center.quant.service;

import auto_deal.center.quant.model.QuantModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TrendFollowTest {

    @Autowired
    public TrendFollow trendFollow;

    @Test
    @DisplayName( "추세상승 구매 테스트한다.")
    public void getTest(){
        //given
        String bitcoinTicker = "BTC";

        //when
        QuantModel quantModel = trendFollow.get(bitcoinTicker);

        //then
        Assertions.assertThat(quantModel.toRsltStr()).isNotNull();
    }

}