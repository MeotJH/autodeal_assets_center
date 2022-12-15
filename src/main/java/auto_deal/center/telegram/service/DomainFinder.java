package auto_deal.center.telegram.service;

import org.springframework.stereotype.Service;

@Service
public class DomainFinder {


    public void process(Long chatId, String text){
            // 유저로 보내라
            // 새유저면 유저 저장, Enum이면 토크 저장 => return Enum에 맞는 값 보내기
            /*
                올드유저면 Enum이면 토크 저장, Quant 저장 => return enum에 맞는 값 보내기
                         COIN이면 토크 에서 전 ENUM 가져와서 trade_detail 저장 => return API로 퀀트 리턴
             */
    }
}
