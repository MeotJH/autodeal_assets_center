package auto_deal.center.trade_detail.repository;

import auto_deal.center.trade_detail.domain.TradeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeDetailRepository extends JpaRepository<TradeDetail,Long> {

    TradeDetail findByCoinTicker(String coinTicker);
}
