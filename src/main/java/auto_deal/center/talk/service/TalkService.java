package auto_deal.center.talk.service;

import auto_deal.center.talk.domain.Talk;
import auto_deal.center.talk.repository.TalkRepository;
import auto_deal.center.user.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TalkService {

    private final TalkRepository talkRepository;

    public Talk saveTalk(String text, Users savedUser){
        Talk talkOne = Talk.builder().content(text).regDate(LocalDateTime.now()).build();
        talkOne.setUser(savedUser);
        return talkRepository.save(talkOne);
    }
}
