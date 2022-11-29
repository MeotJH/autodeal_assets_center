package auto_deal.center.init;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitCoinService {

    private final CoinRepository coinRepository;

    public void init(){

        Coin coin = new Coin();
        //coin.setCoinOne("BitCoin", "BTC");
        coinRepository.save(coin);
        coinRepository.flush();
    }
}
