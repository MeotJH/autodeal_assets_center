package auto_deal.center.telegram.service;

import auto_deal.center.quant.model.QuantModel;
import auto_deal.center.quant.service.QuantType;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.telegram.model.TelegramBotManager;
import auto_deal.center.trend_follow.domain.TrendFollow;
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
    private final Map<String,QuantType> quantTypes;

    public void process(Long chatId,TelegramBotMessage telegramBotMessage){
        SendMessage request = new SendMessage(chatId, telegramBotMessage.getMessage())
                .disableWebPagePreview(true);
        SendResponse sendResponse = telegramBotManager.getTelegramBot().execute(request);
        //boolean ok = sendResponse.isOk();
        //Message message = sendResponse.message();
    }

    public void process(TrendFollow trendFollow){
        String coinTicker = trendFollow.getCoinTicker();
        String quantType = trendFollow.getQuant().getQuantType();
        Long chatId = trendFollow.getQuant().getUsers().getChatId();

        QuantModel quantModel = quantTypes.get(TelegramBotMessage.valueOf(TelegramBotMessage.class, quantType).getBeanNm()).get(coinTicker);

        SendMessage request = new SendMessage(chatId, quantModel.toRsltStr())
                .disableWebPagePreview(true);
        SendResponse sendResponse = telegramBotManager.getTelegramBot().execute(request);
    }

    public void error(Long chatId){
        SendMessage request = new SendMessage(chatId, "에러가 났습니다.")
                .disableWebPagePreview(true);
        SendResponse sendResponse = telegramBotManager.getTelegramBot().execute(request);
        boolean ok = sendResponse.isOk();
        Message message = sendResponse.message();
    }

}
