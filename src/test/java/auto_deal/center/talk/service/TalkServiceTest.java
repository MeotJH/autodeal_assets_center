package auto_deal.center.talk.service;

import auto_deal.center.talk.domain.Talk;
import auto_deal.center.talk.repository.TalkRepository;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TalkServiceTest {

    @Autowired
    private TalkService talkService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TalkRepository talkRepository;

    @Test
    @DisplayName( "talk 넣어지는지 테스트한다.")
    @Transactional(rollbackFor = Exception.class)
    void testInserTalk(){
        //given
        String text = "토크토크";
        Long chatid = 123123L;

        //when
        Users saved = userRepository.save(Users.builder().chatId(chatid).build());
        Talk talkSaved = talkService.saveTalk(text,saved);
        Talk talk = talkRepository.findById(talkSaved.getId()).orElseGet( () -> Talk.builder().content("토크토크아님").build());

        //then
        Assertions.assertThat(talk.getContent()).isEqualTo(text);
    }

    @Test
    @DisplayName( "save한 talk를 user에 넣어지는지 테스트한다.")
    @Transactional(rollbackFor = Exception.class)
    void saveTalkAfterUserTest(){
        //given
        String text = "토크토크";
        Long chatid = 123123L;
        Users saved = userRepository.save(Users.builder().chatId(chatid).build());
        Talk talkSaved = talkService.saveTalk(text,saved);
        Talk talk = talkRepository.findById(talkSaved.getId()).get();

        //when
        Users users = userRepository.findById(saved.getId()).get();

        //then
        Assertions.assertThat(users.getTalks().get(0).getContent()).isEqualTo(text);
    }


}