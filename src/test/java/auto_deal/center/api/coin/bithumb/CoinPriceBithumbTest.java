package auto_deal.center.api.coin.bithumb;

import auto_deal.center.api.coin.CoinPrice;
import auto_deal.center.api.coin.model.CoinApiRslt;
import auto_deal.center.api.coin.model.CoinOhlcvRslt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CoinPriceBithumbTest {

    @Autowired
    private CoinPrice coinPrice;

    @Test
    @DisplayName("코인 현재가들 가져와야 한다")
    void getPrice(){
        //given
        CoinApiRslt prices = coinPrice.getPrices();

        //when
        boolean isBTC = prices.getData().containsKey("BTC");

        //then
        Assertions.assertThat(isBTC).isEqualTo( true );
    }

    @Test
    @DisplayName("특정 코인 전체 기간 가격을 가져와야 한다")
    void getOhlcvTest(){
        //given
        String BitCoin = "BTC";
        CoinOhlcvRslt prices = coinPrice.getOhlcv(BitCoin);

        //when
        String todayTimestampStr = prices.getData().get(prices.getData().size()-1).get(0);
        Long todayTimestamp = Long.parseLong(todayTimestampStr);
        LocalDateTime today = LocalDateTime.ofInstant(Instant.ofEpochMilli(todayTimestamp), TimeZone.getDefault().toZoneId());

        //then
        Assertions.assertThat(today.getDayOfWeek()).isEqualTo( LocalDateTime.now().getDayOfWeek() );
    }


    
}