package auto_deal.center.coin.repository;

import auto_deal.center.coin.domain.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    Coin findCoinByTicker(String ticker);
}
