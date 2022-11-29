package auto_deal.center.coin.service;

import auto_deal.center.api.coin.CoinPrice;
import auto_deal.center.api.coin.model.CoinApiRslt;
import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("quartz")
@RequiredArgsConstructor
public class CoinDataScheduler {

    private final CoinRdbSyncManager coinRdbSyncManager;

    //@Scheduled(cron="10*****")
    private void syncDataPerHour(){
        coinRdbSyncManager.setCoinToDb();
    }
}
