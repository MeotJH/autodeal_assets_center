package auto_deal.center.quant.service;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.dto.BullInfo;
import auto_deal.center.quant.repository.QuantRepository;
import auto_deal.center.talk.repository.TalkRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.trade_detail.service.TradeDetailService;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QuantServiceImpl implements QuantService{

    private final QuantRepository quantRepository;

    public Quant saveQuantByEnum(TelegramBotMessage tbm, Users users) {
        if(isExist(tbm)){
            return quantRepository.findByQuantType(tbm.name());
        }

        Quant quantOne = Quant.builder().quantType(tbm.name()).regdate(LocalDateTime.now()).build();
        quantOne.setUser(users);
        return quantRepository.save(quantOne);
    }

    private Boolean isExist(TelegramBotMessage tbm) {
        Optional<Quant> opt = Optional.ofNullable(quantRepository.findByQuantType(tbm.name()));
        if(opt.isPresent()){
            return true;
        }else{
            return false;
        }
    }

}
