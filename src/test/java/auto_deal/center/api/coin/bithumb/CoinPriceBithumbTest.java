package auto_deal.center.api.coin.bithumb;

import auto_deal.center.api.coin.CoinPrice;
import auto_deal.center.api.coin.model.CoinApiRslt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CoinPriceBithumbTest {

    @Autowired
    private CoinPrice coinPrice;

    @Test
    @DisplayName( "코인 현재가들 가져와야 한다")
    void getPrice(){
        //given
        CoinApiRslt prices = coinPrice.getPrices();

        //when
        boolean isBTC = prices.getData().containsKey("BTC");

        //then
        Assertions.assertThat( isBTC).isEqualTo( true );
    }
    
}