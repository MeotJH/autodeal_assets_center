package auto_deal.center.trend_follow.service;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.repository.QuantRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.trade_detail.trend_follow.domain.TrendFollow;
import auto_deal.center.trade_detail.trend_follow.repository.TrendFollowRepository;
import auto_deal.center.trade_detail.trend_follow.service.TrendFollowService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
class TrendFollowServiceTest {

    @Autowired
    TrendFollowService trendFollowService;
    @Autowired
    TrendFollowRepository trendFollowRepository;

    @Autowired
    QuantRepository quantRepository;

    @Test
    @DisplayName( " Quant 넣는 테스트한다.")
    @Transactional(rollbackFor = Exception.class)
    void processQuantTest(){
        //given
        Quant quant = Quant.builder()
                .quantType(TelegramBotMessage.TREND_FOLLOW.name())
                .regdate(LocalDateTime.now())
                .build();
        String tickerKor = "비트코인";
        String ticker = "BTC";
        quantRepository.save(quant);

        //when
        trendFollowService.processQuant(tickerKor, quant);
        TrendFollow trOne = trendFollowRepository.findByCoinTickerAndQuant(ticker, quant);

        //then
        Assertions.assertThat(trOne.getCoinTicker()).isEqualTo(ticker);
    }


}