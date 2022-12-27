package auto_deal.center.trend_follow.repository;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.trend_follow.domain.TrendFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrendFollowRepository extends JpaRepository<TrendFollow, Long> {

    TrendFollow findByCoinTickerAndQuant(String coinTicker, Quant quant);
}
