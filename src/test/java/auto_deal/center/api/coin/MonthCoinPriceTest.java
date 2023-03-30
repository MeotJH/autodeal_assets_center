package auto_deal.center.api.coin;

import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MonthCoinPriceTest {

    @Autowired
    public MonthPrice monthPrice;

    @Test
    @DisplayName("특정 코인 한달 기준 데이터들을 가져와야 한다.")
    void get() {
        //given
        String ticker = "BTC";

        //when
        JSONObject jsonObject = monthPrice.get(ticker);

        //then
        Assertions.assertThat(jsonObject.getString("status")).isEqualTo("0000");
    }

    @Test
    @DisplayName("특정 코인 한달 기준 데이터의 가장 마지막 아이템이 현재 달과 날짜가 같아야한다.")
    void getAnd() {
        //given
        String ticker = "BTC";

        //when
        JSONObject jsonObject = monthPrice.get(ticker);
        JSONArray data = jsonObject.getJSONArray("data");
        JSONArray recentData = data.getJSONArray(data.length() - 1);
        int timeStamp = recentData.getInt(0);
        LocalDateTime systemLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), TimeZone.getDefault().toZoneId());
        int monthValue = systemLocalDateTime.getMonthValue();

        //then
        Assertions.assertThat(monthValue).isEqualTo(3);
    }
}