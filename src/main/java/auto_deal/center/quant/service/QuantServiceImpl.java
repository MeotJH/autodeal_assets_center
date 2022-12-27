package auto_deal.center.quant.service;

import auto_deal.center.coin.service.CoinService;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.model.QuantModel;
import auto_deal.center.quant.repository.QuantRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.trade_detail.domain.TradeDetail;
import auto_deal.center.user.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QuantServiceImpl implements QuantService{

    private final QuantRepository quantRepository;
    private final CoinService coinService;
    private final Map<String,QuantType> quantTypes;

    @Override
    public Quant saveQuantByEnum(TelegramBotMessage tbm, Users users) {
        if(isExist(tbm,users)){
            return quantRepository.findByQuantTypeAndUsers(tbm.name(),users);
        }

        Quant quantOne = Quant.builder().quantType(tbm.name()).regdate(LocalDateTime.now()).build();
        quantOne.setUser(users);
        return quantRepository.save(quantOne);
    }

    @Override
    public String notice(String quantType){
        TelegramBotMessage equals = TelegramBotMessage.valueOf(quantType);
        QuantType quantTypeObj = quantTypes.get(equals.getBeanNm());
        List<QuantModel> all = quantTypeObj.getAll();

        // todo 언제 알림할지 파악해야 한다. => 가격 가져와서
        return "fail";
    }



    private Boolean isExist(TelegramBotMessage tbm, Users users) {
        Optional<Quant> opt = Optional.ofNullable(quantRepository.findByQuantTypeAndUsers(tbm.name(),users));
        if(opt.isPresent()){
            return true;
        }else{
            return false;
        }
    }

}
