package auto_deal.center.coin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("CoinDataScheduler")
@RequiredArgsConstructor
public class CoinDataScheduler {

    private final CoinRdbSyncManager coinRdbSyncManager;

    // 서버 기동된 후 1초에 처음 시작하고 30분에 한번
    @Scheduled(fixedDelay = 30*60*1000, initialDelay = 1000)
    public void syncDataPerHour(){
        coinRdbSyncManager.setCoinToDb();
    }
}
