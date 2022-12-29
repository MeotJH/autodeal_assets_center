package auto_deal.center.telegram.service;

import auto_deal.center.quant.model.QuantModel;
import auto_deal.center.quant.service.QuantType;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.telegram.model.TelegramBotManager;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReturnMessage {
    private final TelegramBotManager telegramBotManager;

    public void process(Long chatId,TelegramBotMessage telegramBotMessage){
        SendMessage request = new SendMessage(chatId, telegramBotMessage.getMessage())
                .disableWebPagePreview(true);
        telegramBotManager.getTelegramBot().execute(request);
    }

    public SendResponse process(Long chatId,QuantModel quantModel){

        SendMessage request = new SendMessage(chatId, quantModel.toRsltStr())
                .disableWebPagePreview(true);
        return telegramBotManager.getTelegramBot().execute(request);
    }

    public void error(Long chatId){
        SendMessage request = new SendMessage(chatId, "에러가 났습니다.")
                .disableWebPagePreview(true);
        SendResponse sendResponse = telegramBotManager.getTelegramBot().execute(request);
        boolean ok = sendResponse.isOk();
        Message message = sendResponse.message();
    }

}
