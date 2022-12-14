package auto_deal.center.quant.service;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.dto.BullInfo;
import auto_deal.center.quant.repository.QuantRepository;
import auto_deal.center.talk.repository.TalkRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.trade_detail.service.TradeDetailService;
import auto_deal.center.user.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QuantServiceImpl implements QuantService{

    private final QuantRepository quantRepository;
    private final TradeDetailService tradeDetailService;

    private final TalkRepository talkRepository;

    @Override
    public String getBullMarketInfo(List<BullInfo> bullInfos) {
        return null;
    }

    public Quant saveQuantByEnum(String text, Users userOne) {
        Quant returnRslt = Quant.builder().build();
        TelegramBotMessage quantType = null;
        for(TelegramBotMessage each :TelegramBotMessage.values()){
            if( each.getCodeKr().equals(text) || each.getCodeEn().equals(text) ){
                quantType = each;
                break;
            }
        }

        if( quantType != null){
            Quant quantOne = Quant.builder().quantType(quantType.toString()).regdate(LocalDateTime.now()).build();
            quantOne.setUser(userOne);
            returnRslt = quantRepository.save(quantOne);
        }
        return returnRslt;
    }

}
