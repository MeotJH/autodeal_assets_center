package auto_deal.center.cmm.init;

import auto_deal.center.coin.service.CoinRdbSyncManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitSyncManager {

    private final CoinRdbSyncManager coinRdbSyncManager;

    @PostConstruct
    public void init() {
//        coinRdbSyncManager.updateCoinToDb();
//        coinRdbSyncManager.initCoinName();
//        coinRdbSyncManager.init3MAvgPrice();
//        coinRdbSyncManager.initNowPrices();
    }
}
