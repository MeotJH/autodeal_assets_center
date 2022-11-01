package auto_deal.center.quant.repository;

import auto_deal.center.quant.domain.Quant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantRepository extends JpaRepository<Quant, Long> {
}
