package auto_deal.center.coin.service;

import auto_deal.center.quant.callback.QuantCaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("CoinDataScheduler")
@Slf4j
@RequiredArgsConstructor
public class CoinDataScheduler {

    private final CoinRdbSyncManager coinRdbSyncManager;

    private final QuantCaller quantCaller;

    // 서버 기동된 후 1초에 처음 시작하고 24시간에 한번
    @Scheduled(cron = "0 5 0 * * *")
    public void syncDataPerDay(){
        coinRdbSyncManager.updateCoinToDb();
    }

    @Scheduled(cron = "0 5 0 * * *")
    public void syncDataThreeMonthAvgPrc(){
        coinRdbSyncManager.init3MAvgPrice();
    }

    // 매 10분 마다 코인 현재가 세팅
    @Scheduled(cron = "0 0/10 * * * *")
    public void syncCoinPrices(){
        Boolean isSuccess = coinRdbSyncManager.initNowPrices();
        log.info(":::::::::::::init Nowprices result is {}",isSuccess);
        quantCaller.isCalled(isSuccess);
    }
}
