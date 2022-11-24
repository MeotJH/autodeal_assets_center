package auto_deal.center.api.coin.bithumb;

import auto_deal.center.api.coin.CoinPrice;
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
    @DisplayName( "test coin prices for now")
    void getPrice(){
        //given , //when
        coinPrice.getPrices();
        
        //then
    }
    
}