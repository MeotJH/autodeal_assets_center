package auto_deal.center.quant.service;

import auto_deal.center.cmm.model.NoticeMessageModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuantStopLossTest {

    @Autowired
    public QuantStopLoss quantStopLoss;

    @Test
    @DisplayName( "noticeModel에서 물어본 USER가 존재하는가.")
    void noticeTest(){
        //given
        List<NoticeMessageModel> notice = quantStopLoss.notice();

        //when
        NoticeMessageModel noticeMessageModel = notice.get(0);
        Long chatId = noticeMessageModel.getChatId();

        //then
        Assertions.assertThat(chatId).isGreaterThan(0);
    }
}