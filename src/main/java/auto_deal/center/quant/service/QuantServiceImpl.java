package auto_deal.center.quant.service;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.dto.BullInfo;
import auto_deal.center.quant.repository.QuantRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.user.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuantServiceImpl implements QuantService{

    private final QuantRepository quantRepository;

    @Override
    public String getBullMarketInfo(List<BullInfo> bullInfos) {
        return null;
    }

    public void saveQuantByEnum(String text, Users userOne) {
        String quantType = new String();
        for(TelegramBotMessage each :TelegramBotMessage.values()){
            if( each.getCodeKr().equals(text) || each.getCodeEn().equals(text) ){
                quantType = each.name();
            }
        }
        Quant quantOne = Quant.builder().quantType(quantType.toString()).regdate(LocalDateTime.now()).build();
        quantOne.setUser(userOne);
        quantRepository.save(quantOne);
    }
}
