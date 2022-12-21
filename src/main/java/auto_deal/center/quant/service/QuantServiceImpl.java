package auto_deal.center.quant.service;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.service.CoinService;
import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.repository.QuantRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.trade_detail.domain.TradeDetail;
import auto_deal.center.user.domain.Users;
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
    private final CoinService coinService;

    public Quant saveQuantByEnum(TelegramBotMessage tbm, Users users) {
        if(isExist(tbm,users)){
            return quantRepository.findByQuantTypeAndUsers(tbm.name(),users);
        }

        Quant quantOne = Quant.builder().quantType(tbm.name()).regdate(LocalDateTime.now()).build();
        quantOne.setUser(users);
        return quantRepository.save(quantOne);
    }

    public void notice(String quantType){
        List<Quant> byQuantType = quantRepository.findByQuantType(quantType);
        for (Quant quant: byQuantType) {
            List<TradeDetail> tradeDetails = quant.getTradeDetails();
            for (TradeDetail tradeDetail: tradeDetails) {
                //Coin coin = coinService.getCoin(tradeDetail.getCoinTicker()).update3MAvgPrice();
            }
        }
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
