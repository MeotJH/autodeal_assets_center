package auto_deal.center.coin.service;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService{

    private final CoinRepository coinRepository;

    @Override
    public Boolean isExist(String coinKor){
        Boolean exist = false;
        Optional<Coin> optional = Optional.ofNullable(coinRepository.findCoinByKorea(coinKor));
        if(optional.isPresent()){
            exist = true;
        }else {
            exist = false;
        }

        return exist;
    }

    @Override
    public Coin save(Coin coin) {
        return coinRepository.save(coin);
    }

    @Override
    public List<Coin> getAllTicker() {
        return coinRepository.findAll();
    }
}
