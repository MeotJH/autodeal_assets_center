package auto_deal.center.api.coin.bithumb;

import auto_deal.center.api.coin.MonthPrice;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
@Service
public class MonthCoinPrice implements MonthPrice {

    @Value("${bithumb.url}")
    private String bithumbUrl;

    @Override
    public Map get(String ticker) {
        JSONObject jsonObject = new JSONObject();
        Map<String, Double> stringDoubleMap = new HashMap<>();
        try{
            jsonObject = getBeforeException(ticker);
            stringDoubleMap = parseDatasPercentByMap(jsonObject);

        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            return stringDoubleMap;
        }

    }

    private Map<String, Double> parseDatasPercentByMap(JSONObject jsonObject) {
        JSONArray datas = jsonObject.getJSONArray("data");
        HashMap<String, ArrayList<JSONArray>> targetMonth = parseEveryDataToTartgetMonth(datas);
        String[] monthKeyArr = getKeysByArr(targetMonth);
        return calculatePriceToPercent(targetMonth, monthKeyArr);
    }

    private JSONObject getBeforeException(String ticker) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = getUrl(ticker);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        return new JSONObject( client.newCall(request).execute().body().string() );
    }

    @NotNull
    private HashMap<String, ArrayList<JSONArray>> parseEveryDataToTartgetMonth(JSONArray datas) {
        HashMap<String, ArrayList<JSONArray>> map = new HashMap<>();

        for (int i = 0; i < datas.length(); i++) {
            JSONArray jsonArray = datas.getJSONArray(i);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(jsonArray.getLong(0)), TimeZone.getDefault().toZoneId());
            Integer year = localDateTime.getYear();
            Integer nowYear = LocalDateTime.now().getYear();

            if( nowYear.equals(year) || nowYear.equals(year+1)){
                Integer dayOfMonth = localDateTime.getMonthValue();
                String yearMonth = year + Integer.toString(dayOfMonth);
                if(map.containsKey(yearMonth)){
                    ArrayList<JSONArray> mapInArr = map.get(yearMonth);
                    mapInArr.add(jsonArray);
                    map.put(yearMonth, mapInArr);
                }else{
                    ArrayList<JSONArray> mapInArr = new ArrayList<>();
                    mapInArr.add(jsonArray);
                    map.put(yearMonth, mapInArr);
                }

            }

        }
        return map;
    }

    @NotNull
    private String[] getKeysByArr(HashMap<String, ArrayList<JSONArray>> targetMonth) {
        LocalDateTime now = LocalDateTime.now();
        int monthValue = now.getMonthValue() -1;
        int year = now.getYear();

        //일단 배열로 하지만 순서가 필요하다면 리팩토링 가능할것 같다.
        String[] monthKeyArr = new String[6];
        for (int i = 0; i < 6; i++) {

            String yearMonth = year + Integer.toString(monthValue);

            if(targetMonth.containsKey(yearMonth)){
                monthKeyArr[i] = yearMonth;
            }

            //달을 Loop돌때마다 하나씩 뺴주다가 년도가 바뀌면 년도를 뺴고 달을 12로 바꾸어 작년 데이터를 가져와 총 6개를 채워야한다.
            monthValue--;
            if(monthValue == 0){
                year--;
                monthValue = 12;
            }
        }
        return monthKeyArr;
    }



    @NotNull
    private Map<String, Double> calculatePriceToPercent(HashMap<String, ArrayList<JSONArray>> datas, String[] KeyArr) {
        Map<String,Double> monthSectionMap = new HashMap<>();
        for (String monthKey: KeyArr) {
            ArrayList<JSONArray> monthData = datas.get(monthKey);

            Double startPrices = 0.0;
            Double highPrices = 0.0;
            Double lowPrices = 0.0;

            for (int i = 0; i < 30; i++) {
                JSONArray dailyData = monthData.get(0);
                Double startPrice = Double.parseDouble(dailyData.getString(1));
                Double highPrice = Double.parseDouble(dailyData.getString(3));
                Double lowPrice = Double.parseDouble(dailyData.getString(4));

                startPrices = startPrices + startPrice;
                highPrices = highPrices + highPrice;
                lowPrices = lowPrices + lowPrice;
            }

            Double monthSection = ((highPrices - lowPrices) / startPrices) * 100;
            monthSectionMap.put(monthKey, monthSection);

        }
        return monthSectionMap;
    }

    private String getUrl(String order_currency){
        // https://api.bithumb.com/public/candlestick/BTC_KRW/1m => 1일 캔들 한국돈으로 BTC를
        return bithumbUrl + "/public/candlestick/" + order_currency + "_KRW" + "/24h";
    }
}
