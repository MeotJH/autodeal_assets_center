package auto_deal.center.coin.repository;

import auto_deal.center.coin.domain.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    Coin findCoinByTicker(String ticker);
    Optional<Coin> findOptionalCoinByTicker(String ticker);
    Coin findCoinByKorea(String korea);

}
