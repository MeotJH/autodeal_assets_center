package auto_deal.center.telegram.service;

import auto_deal.center.cmm.controller.MainProcessor;
import auto_deal.center.cmm.model.CommonModel;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.telegram.model.TelegramBotManager;
import auto_deal.center.user.service.UserService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {


    private final TelegramBotManager telegramBotManager;
    private final MainProcessor mainProcessor;
    private final ReturnMessage returnMessage;
    private TelegramBot telegramBot;

    @PostConstruct
    private void activeAfterConstruct(){
        this.telegramBot = telegramBotManager.getTelegramBot();
        this.activeListener();
    }


    //리스너를 등록해 놓는 메소드
    private void activeListener(){
        this.telegramBot.setUpdatesListener(updates -> {

            // ... process updates
            this.processUpdates();

            // return 모든 작업이 끝났다고 보내준다
            return UpdatesListener.CONFIRMED_UPDATES_ALL;

        });
    }

    // 봇에게 보낸 대화에 맞는 답변을 보낸다.
    @Transactional( rollbackFor = Exception.class )
    protected void processUpdates(){
        // 봇에게 보낸 대화들을 가져온다
        GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);
        GetUpdatesResponse updatesResponse = telegramBot.execute(getUpdates);

        // 루프를 돌며 대화에 맞는 답변을 한다.
        List<Update> updates = updatesResponse.updates();
        for(Update each: updates){
            Long chatId = 0L;
            String text = each.message().text();

            try{
                chatId = each.message().from().id();
                //Domain 관련 서비스 시작부분
                mainProcessor.process(chatId,text);
            }catch(Exception e){
                e.printStackTrace();
                log.error(e.getMessage());
                log.error(e.toString());
                returnMessage.process(chatId, TelegramBotMessage.getEquals(text));
            }

        }
    }



}
