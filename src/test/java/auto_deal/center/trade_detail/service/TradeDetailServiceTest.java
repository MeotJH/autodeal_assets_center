package auto_deal.center.trade_detail.service;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.repository.QuantRepository;
import auto_deal.center.quant.service.QuantService;
import auto_deal.center.trade_detail.domain.TradeDetail;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TradeDetailServiceTest {

    @Autowired
    private TradeDetailService tradeDetailService;

    @Autowired
    private QuantRepository quantRepository;
    
    @Test
    @DisplayName( "trade_detail 테이블 저장 테스트한다.")
    @Transactional(rollbackFor = Exception.class)
    void tradeDetailSaveTest(){
        //given
        String quantType = "TREND_FOLLOW";
        String coinKorean = "비트코인";
        Quant one = quantRepository.save(Quant.builder().quantType("TREND_FOLLOW").regdate(LocalDateTime.now()).build());

        //when
//        TradeDetail tradeDetailSaved = tradeDetailService.saveTradeDetail(one, coinKorean);
//        String coinTicker = tradeDetailSaved.getCoinTicker();
//
//        //then
//        Assertions.assertThat(coinTicker).isEqualTo("BTC");
    }
    

}