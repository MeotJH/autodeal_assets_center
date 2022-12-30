package auto_deal.center.coin.service;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(rollbackFor = Exception.class)
class CoinServiceTest {

    @Autowired
    private CoinService coinService;

    @Autowired
    private CoinRdbSyncManager coinRdbSyncManager;
    @Autowired
    private CoinRepository coinRepository;

    @Test
    @DisplayName( " 코인 모든 ticker가져오는 테스트한다.")
    void getAllTickerTest(){
        //given
        coinRdbSyncManager.updateCoinToDb();
        List<Coin> allTicker = coinService.getAllTicker();
        String ticker = allTicker.get(0).getTicker();
        //when
        Coin coinByTicker = coinRepository.findCoinByTicker(ticker);
        //then
        Assertions.assertThat(coinByTicker).isNotNull();
    }
    
}