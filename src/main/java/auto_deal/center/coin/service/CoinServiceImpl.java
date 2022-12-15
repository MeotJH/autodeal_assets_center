package auto_deal.center.coin.service;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoinServiceImpl {

    private final CoinRepository coinRepository;

    public Boolean isExist(String ticker){
        Boolean exist = false;
        Optional<Coin> optional = Optional.ofNullable(coinRepository.findCoinByTicker(ticker));
        if(optional.isPresent()){
            exist = true;
        }else {
            exist = false;
        }

        return exist;
    }
}
