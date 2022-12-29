package auto_deal.center.quant.service;

import auto_deal.center.coin.service.CoinService;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.model.QuantModel;
import auto_deal.center.quant.repository.QuantRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
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
    public List<Quant> getAll(){
        return quantRepository.findAll();
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
