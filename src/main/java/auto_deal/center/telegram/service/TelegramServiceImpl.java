package auto_deal.center.telegram.service;

import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.telegram.service.TelegramService;
import auto_deal.center.user.service.UserService;
import auto_deal.center.user.service.UserServiceImpl;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class TelegramServiceImpl implements TelegramService {

    TelegramBot telegramBot;
    TelegramBotMessage telegramBotMessage;

    private final UserService userService;

    public TelegramServiceImpl(UserService userService){
        this.telegramBot = new TelegramBot("5692669704:AAH4N_20QLZHskRN_cnDXDSWaFqHTe1y3VA");
        this.activeListener();
        this.userService = userService;
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
            
            //DB도입 시작부분
            userService.Process(chatId,text);

            // 봇 커맨드에 맞는 답변을 가져온다
            telegramBotMessage = Arrays.stream(TelegramBotMessage.values())
                    .filter(v -> text.equals(v.getCodeEn()) || text.equals(v.getCodeKr()) )
                    .findAny().orElseGet( () -> TelegramBotMessage.EMPTY );

            // 메세지를 보낸다.
            SendMessage request = new SendMessage(chatId, telegramBotMessage.getMessage())
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true)
                    .replyToMessageId(1)
                    .replyMarkup(new ForceReply());

            // sync
            SendResponse sendResponse = telegramBot.execute(request);
            boolean ok = sendResponse.isOk();
            Message message = sendResponse.message();

        }
    }


}
