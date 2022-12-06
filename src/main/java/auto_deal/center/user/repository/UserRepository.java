package auto_deal.center.user.repository;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {

    Users findUserOneByChatId(Long chatId);
}
