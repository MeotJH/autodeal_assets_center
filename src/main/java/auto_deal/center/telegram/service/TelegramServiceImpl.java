package auto_deal.center.telegram.service;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TelegramServiceImpl implements TelegramService{

    TelegramBot telegramBot;

    public TelegramServiceImpl(){
        this.telegramBot = new TelegramBot("5692669704:AAH4N_20QLZHskRN_cnDXDSWaFqHTe1y3VA");
        this.sendMessageReturn();
    }

    public void sendMessageReturn(){
        GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);
        GetUpdatesResponse updatesResponse = telegramBot.execute(getUpdates);
        List<Update> updates = updatesResponse.updates();

        for(Update each: updates){
            Long chatId = each.message().from().id();

            SendMessage request = new SendMessage(chatId, "text")
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
