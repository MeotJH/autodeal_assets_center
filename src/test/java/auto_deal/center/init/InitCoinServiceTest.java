package auto_deal.center.init;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InitCoinServiceTest {

    @Autowired
    InitCoinService initCoinService;

    @Autowired
    CoinRepository coinRepository;

    @Test
    @DisplayName( "coinInsertTest.")
    void JpaSaveTest(){
        //given
        Coin coin = new Coin();

        //when
        initCoinService.init();
        coinRepository.flush();

        //then
        coin.setCoinOne("BitCoin","BTC");
        Coin coinOne = coinRepository.findOne(Example.of(coin)).get();
        Assertions.assertThat( coinOne.getTicker() ).isEqualTo( "BTC" );
    }
    
}