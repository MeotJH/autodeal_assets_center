package auto_deal.center.telegram.service;

import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.user.service.UserService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class TelegramServiceImpl implements TelegramService {

    TelegramBot telegramBot;

    private final UserService userService;
    private final ReturnMessage returnMessage;


    public TelegramServiceImpl(UserService userService, ReturnMessage returnMessage){
        this.telegramBot = new TelegramBot("5692669704:AAH4N_20QLZHskRN_cnDXDSWaFqHTe1y3VA");
        this.activeListener();
        this.userService = userService;
        this.returnMessage = returnMessage;
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
    private void processUpdates(){
        // 봇에게 보낸 대화들을 가져온다
        GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);
        GetUpdatesResponse updatesResponse = telegramBot.execute(getUpdates);

        // 루프를 돌며 대화에 맞는 답변을 한다.
        List<Update> updates = updatesResponse.updates();
        for(Update each: updates){

            Long chatId = each.message().from().id();
            String text = each.message().text();

            String rsltMsg = null;
            try{
                //DB도입 시작부분
                userService.Process(chatId, text);
                rsltMsg = returnMessage.process(chatId,text);

            }catch(Exception e){
                rsltMsg = returnMessage.error();
            }finally {

                // 메세지를 보낸다.
                sendMessage(chatId, rsltMsg);
            }

        }
    }

    private void sendMessage(Long chatId, String rsltMsg) {

        SendMessage request = new SendMessage(chatId, rsltMsg)
                                    .parseMode(ParseMode.HTML)
                                    .disableWebPagePreview(true)
                                    .disableNotification(true)
                                    .replyToMessageId(1)
                                    .replyMarkup(new ForceReply());

        SendResponse sendResponse = telegramBot.execute(request);
        boolean ok = sendResponse.isOk();
        Message message = sendResponse.message();
    }


}
