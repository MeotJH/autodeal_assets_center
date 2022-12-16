package auto_deal.center.coin.repository;

import auto_deal.center.coin.domain.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    Coin findCoinByTicker(String ticker);
    Coin findCoinByKorea(String korea);

    @Query("select new Coin(d.id, d.ticker) from Coin d")
    List<Coin> findAllTicker();
}
