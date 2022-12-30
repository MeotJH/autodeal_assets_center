package auto_deal.center.quant.callback;

import auto_deal.center.cmm.controller.MainProcessor;
import auto_deal.center.telegram.message.TelegramBotMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuantCaller {

    private final MainProcessor mainProcessor;
    
    //콜백함수
    public void isCalled(Boolean check){
        if(check){
            mainProcessor.returnProcess(TelegramBotMessage.TREND_FOLLOW);
        }
    }

}
