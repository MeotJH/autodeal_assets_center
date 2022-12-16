package auto_deal.center.coin.service;

import auto_deal.center.coin.domain.Coin;

import java.util.List;

public interface CoinService {

    public Boolean isExist(String coinKor);

    public Coin save(Coin coin);

    public List<Coin> getAllTicker();
}
