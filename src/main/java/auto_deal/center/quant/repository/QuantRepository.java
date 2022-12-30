package auto_deal.center.quant.repository;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuantRepository extends JpaRepository<Quant, Long> {

    List<Quant> findByQuantType(String QuantType);

    Quant findByQuantTypeAndUsers(String QuantType, Users users);
}
