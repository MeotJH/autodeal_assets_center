package auto_deal.center.coin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("CoinDataScheduler")
@RequiredArgsConstructor
public class CoinDataScheduler {

    private final CoinRdbSyncManager coinRdbSyncManager;

    // 서버 기동된 후 5초에 처음 시작하고 24시간에 한번
    // TODO - 서버 기동된 후 4초에 처음 시작하고 매일밤 24시에 한번으로 바꾸기
    @Scheduled(fixedDelay = 24*60*60*1000, initialDelay = 5000)
    public void syncDataPerHour(){
        coinRdbSyncManager.updateCoinToDb();
    }
}
