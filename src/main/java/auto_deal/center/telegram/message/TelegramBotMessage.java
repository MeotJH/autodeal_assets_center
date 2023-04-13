package auto_deal.center.telegram.message;

import lombok.Getter;

@Getter
public enum TelegramBotMessage {

    START_MESSAGE("퀀트투자 정보 소통 봇! \n" +
            "/help 사용 방법을 조회합니다. \n" +
            "/추세, /trend 추세 투자법 기능 설정합니다. \n" +
            "/손절매, /stoploss 손절매 투자전략 기능 설정합니다. \n" +
            "/은퇴자금, /retire 추세 투자법 기능 설정합니다. \n"
            ,"/start", "/help"),

    STOP_LOSS("손절매 투자전략 기능입니다. \n" +
            "원하는 코인명을 풀네임으로 입력해주세요 \n" +
            "예) 비트코인, 이더리움...", "/stoploss", "/손절매","quantStopLoss"),

    TREND_FOLLOW("추세 투자법 기능입니다. \n" +
            "원하는 코인명을 풀네임으로 입력해 주세요 \n" +
            "예) 비트코인, 이더리움... \n" +
            "해당 투자기능 상세 사용법 \n" +
            " https://meotlog.tistory.com/69 ", "/trend", "/추세","quantTrendFollow"),

    RETIRE_MONEY("은퇴자금 계산기 기능입니다. \n" +
            "해당 URL로 이동하셔서 은퇴자금을 알아보세요 \n" +
            "https://meotlog.tistory.com/70 ", "/retire", "/은퇴자금"),
    EMPTY("해당하는 요청이 없습니다. \n" +
            "/help 로 사용 방법을 조회해주세요. ","empty", "empty");

    private final String message;
    private final String codeEn;
    private final String codeKr;
    private String beanNm;

    TelegramBotMessage(String message, String codeEn, String codeKr, String beanNm) {
        this.message = message;
        this.codeEn = codeEn;
        this.codeKr = codeKr;
        this.beanNm = beanNm;
    }

    TelegramBotMessage(String message, String codeEn, String codeKr) {
        this.message = message;
        this.codeEn = codeEn;
        this.codeKr = codeKr;
    }

    public static TelegramBotMessage getEquals(String text){
        TelegramBotMessage telegramBotMessage = null;
        for (TelegramBotMessage each : TelegramBotMessage.values()){
            if(text.equals(each.getCodeKr()) || text.equals(each.getCodeEn())){
                telegramBotMessage = each;
                break;
            }
        }

        if( telegramBotMessage == null ){
            telegramBotMessage = TelegramBotMessage.EMPTY;
        }

        return telegramBotMessage;
    }

}
