package auto_deal.center.telegram.model;

import com.pengrad.telegrambot.TelegramBot;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TelegramBotManager {
    @Value("${telegram.bot.quant_two_dev}")
    private String botKey;

    @Getter
    private TelegramBot telegramBot;

    @PostConstruct
    private void activeAfterConstruct(){
        this.telegramBot = new TelegramBot(botKey);
    }

}
