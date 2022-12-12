package auto_deal.center.talk.repository;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.talk.domain.Talk;
import auto_deal.center.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TalkRepository extends JpaRepository<Talk,Long> {

    List<Talk> findByUsersOrderByRegDateDesc(Users users);
}
