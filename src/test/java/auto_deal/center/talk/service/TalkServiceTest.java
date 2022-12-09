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
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(rollbackFor = Exception.class)
class TalkServiceTest {

    @Autowired
    private TalkService talkService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TalkRepository talkRepository;

    @Test
    @DisplayName( "talk 넣어지는지 테스트한다.")
    void testInserTalk(){
        //given
        String text = "토크토크";

        //when
        talkService.saveTalk(text);
        Talk talk = talkRepository.findById(1L).get();

        //then
        Assertions.assertThat(talk.getContent()).isEqualTo(text);
    }

    @Test
    @DisplayName( "save한 talk를 user에 넣어지는지 테스트한다.")
    void saveTalkAfterUserTest(){
        //given
        String text = "토크토크";
        talkService.saveTalk(text);
        Talk talk = talkRepository.findById(1L).get();

        Long chatId = 12312L;
        Users usersOne = Users.builder().chatId(chatId).talk(talk).build();

        //when
        userRepository.save(usersOne);
        Users users = userRepository.findById(1L).get();

        //then
        Assertions.assertThat(users.getTalk().getContent()).isEqualTo(text);
    }


}