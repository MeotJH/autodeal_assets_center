package auto_deal.center.trade_detail.stop_loss.repository;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.trade_detail.stop_loss.domain.StopLoss;
import auto_deal.center.trade_detail.trend_follow.domain.TrendFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopLossRepository extends JpaRepository<StopLoss, Long> {

    StopLoss findByCoinTickerAndQuant(String coinTicker, Quant quant);
}
