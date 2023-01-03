package auto_deal.center.talk.service;

import auto_deal.center.coin.domain.Coin;
import auto_deal.center.coin.repository.CoinRepository;
import auto_deal.center.talk.domain.Talk;
import auto_deal.center.talk.repository.TalkRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.user.domain.Users;
import auto_deal.center.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TalkService {

    private final TalkRepository talkRepository;
    private final CoinRepository coinRepository;

    public TelegramBotMessage saveTalk(Users user, String text){
        Optional<Coin> coinByTicker = coinRepository.findOptionalCoinByTicker(text);

        if( TelegramBotMessage.getEquals(text) != TelegramBotMessage.EMPTY && !coinByTicker.isPresent() ){
            Talk talkOne = Talk.builder().content(text).regDate(LocalDateTime.now()).build();
            talkOne.setUser(user);
            talkRepository.save(talkOne);
        }

        return TelegramBotMessage.getEquals(text);
    }

    //유저의 마지막 대화중 봇에 적어놓은 '/xxx' 기능을 마지막으로 가져온다.
    public TelegramBotMessage getPrevTalk(Users user){
        List<Talk> talks = talkRepository.findByUsersOrderByRegDateDesc(user);
        Talk talk = talks.stream().filter(v -> v.getContent().contains("/")).findFirst().orElseGet( () -> Talk.builder().content("empty").build() );
        return TelegramBotMessage.getEquals(talk.getContent());
    }

}
