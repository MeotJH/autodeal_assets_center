package auto_deal.center.cmm.controller;

import auto_deal.center.telegram.message.TelegramBotMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MainProcessorTest {

    @Autowired
    private MainProcessor mainProcessor;

    @Test
    void returnProcess() {
        //given
        List<String> texts = new ArrayList<>();
        Long chatId = 12345L;
        texts.add("/help");
        texts.add("/trend");
        texts.add("비트코인");
        texts.add("이더리움");

        //when
        for (String each: texts) {
            mainProcessor.process(chatId,each);
        }

        mainProcessor.returnProcess(TelegramBotMessage.TREND_FOLLOW);
        //then
    }
}