package auto_deal.center.api.service;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import auto_deal.center.coin.service.CoinRdbSyncManager;
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
        coinRdbSyncManager.updateCoinToDb();
        String coinTicker = "BTC";

        //when
        List<Coin> all = coinRepository.findAll();
        Coin one = coinRepository.findCoinByTicker(coinTicker);

        //then
        Assertions.assertThat(one.getTicker()).isEqualTo(coinTicker);
    }

    @Test
    @DisplayName( "코인 이름 세팅 테스트 한다.")
    void setCoinNameInitTest(){
        //given
        coinRdbSyncManager.initCoinName();
        String coinTicker = "BTC";

        //when
        Coin one = coinRepository.findCoinByTicker(coinTicker);

        //then
        String rsltName = "비트코인";
        Assertions.assertThat(one.getKorea()).isEqualTo(rsltName);
    }

    @Test
    @DisplayName( " 현재 코인 가격 가져오는 테스트한다.")
    void initNowPricesTest(){
        //given
        coinRdbSyncManager.initNowPrices();
        //when

        //then
    }


}