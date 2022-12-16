package auto_deal.center.api.service;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import auto_deal.center.coin.service.CoinDataScheduler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CoinDataSchedulerTest {

    @Autowired
    private CoinDataScheduler coinDataScheduler;

    @Autowired
    private CoinRepository coinRepository;

    @Test
    @DisplayName( "배치가 도는지에 관한 테스트한다.")
    void setPerHourSyncTest() {

        //given
        try {
            TimeUnit.SECONDS.sleep(30);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        String bitcoinTicker = "BTC";
        //when
        Coin coinByTicker = coinRepository.findCoinByTicker(bitcoinTicker);
        //then
        Assertions.assertThat(coinByTicker.getTicker()).isEqualTo(bitcoinTicker);
    }


}