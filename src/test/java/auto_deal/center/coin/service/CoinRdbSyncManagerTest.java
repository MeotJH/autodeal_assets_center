package auto_deal.center.coin.service;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CoinRdbSyncManagerTest {

    @Autowired
    private CoinRdbSyncManager coinRdbSyncManager;
    @Autowired
    private CoinRepository coinRepository;

    @Test
    @DisplayName( "통신해 가져와 Db에 넣은값 테스트한다.")
    void setCoinToDbTest(){
        //given
        coinRdbSyncManager.setCoinToDb();
        String coinTicker = "BTC";

        //when
        List<Coin> all = coinRepository.findAll();
        Coin one = coinRepository.findCoinByTicker(coinTicker);

        //then
        Assertions.assertThat(one.getTicker()).isEqualTo(coinTicker);
    }

}