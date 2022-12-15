package auto_deal.center.telegram.service;

import auto_deal.center.cmm.model.CommonModel;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.trade_detail.model.TradeDetailTalk;
import auto_deal.center.user.service.UserService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        /* TODO 환경으로 뺴기
        5692669704:AAH4N_20QLZHskRN_cnDXDSWaFqHTe1y3VA -> quant_alarm_bot => 운영으로 사용 하기
        5722974705:AAF60xPxcmm_PgxjD2bsPWKUoYl5ftu3V1c -> quant_two_bot => local에서 사용
        5973588170:AAFjTa06Nj4x8_qPYCeHDLvuZ3SGbLYhoNg -> 현준이 아이디로 만든 봇
         */
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
    @Transactional( rollbackFor = Exception.class )
    protected void processUpdates(){
        // 봇에게 보낸 대화들을 가져온다
        GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);
        GetUpdatesResponse updatesResponse = telegramBot.execute(getUpdates);

        // 루프를 돌며 대화에 맞는 답변을 한다.
        List<Update> updates = updatesResponse.updates();
        for(Update each: updates){

            Long chatId = 0L;

            //Long chatId = each.message().chat().id();
            //SendResponse response = telegramBot.execute(new SendMessage(chatId, "Hello!"));

            String rsltMsg = null;
            try{
                chatId = each.message().from().id();
                String text = each.message().text();
                //DB도입 시작부분
                CommonModel processed = userService.Process(chatId, text);
                rsltMsg = returnMessage.process(processed,text);

            }catch(Exception e){
                e.printStackTrace();
                log.info(e.getMessage());
                rsltMsg = returnMessage.error();
            }finally {
                // 메세지를 보낸다.
                sendMessage(chatId, rsltMsg);
            }

        }
    }

    private void sendMessage(Long chatId, String rsltMsg) {

        SendMessage request = new SendMessage(chatId, rsltMsg)
                                    .disableWebPagePreview(true);

        SendResponse sendResponse = telegramBot.execute(request);
        boolean ok = sendResponse.isOk();
        Message message = sendResponse.message();
    }


}
