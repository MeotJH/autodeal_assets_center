package auto_deal.center.coin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("CoinDataScheduler")
@RequiredArgsConstructor
public class CoinDataScheduler {

    private final CoinRdbSyncManager coinRdbSyncManager;

    // 서버 기동된 후 1초에 처음 시작하고 24시간에 한번
    // TODO - 서버 기동된 후 1초에 처음 시작하고 매일밤 24시에 한번으로 바꾸기
    @Scheduled(fixedDelay = 24*60*60*1000, initialDelay = 1000)
    public void syncDataPerDay(){
        coinRdbSyncManager.updateCoinToDb();
    }

    @Scheduled(fixedDelay = 24*60*60*1000, initialDelay = 10000)
    public void syncDataThreeMonthAvgPrc(){
        coinRdbSyncManager.init3MAvgPri();
    }
}
