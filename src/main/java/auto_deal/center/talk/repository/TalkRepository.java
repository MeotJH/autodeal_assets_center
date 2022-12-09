package auto_deal.center.talk.repository;

import auto_deal.center.talk.domain.Talk;
import auto_deal.center.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalkRepository extends JpaRepository<Talk,Long> {
}
