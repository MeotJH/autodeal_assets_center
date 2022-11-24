package auto_deal.center.coin.repository;

import auto_deal.center.coin.domain.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, Long> {
}
