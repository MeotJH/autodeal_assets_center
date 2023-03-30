package auto_deal.center.api.coin.bithumb;

import auto_deal.center.api.coin.MonthPrice;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class MonthCoinPrice implements MonthPrice {

    @Value("${bithumb.url}")
    private String bithumbUrl;

    @Override
    public JSONObject get(String ticker) {
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject = getBeforeException(ticker);
            Boolean aBoolean = parseDateToMonth(jsonObject);

        }catch (Exception e){
            e.printStackTrace();
            //log.info(e.getMessage());
        }finally {
            return jsonObject;
        }


    }

    private Boolean parseDateToMonth(JSONObject jsonObject) {
        JSONArray datas = jsonObject.getJSONArray("data");
        int lastIdx =datas.length() -1;

        Double startPrices = 0.0;
        Double highPrices = 0.0;
        Double lowPrices = 0.0;

        Double daySection = 0.0;
        for (int i = 0; i < 30; i++) {
            JSONArray jsonArray = datas.getJSONArray(lastIdx - i);
            Double startPrice = Double.parseDouble(jsonArray.getString(1));
            Double highPrice = Double.parseDouble(jsonArray.getString(3));
            Double lowPrice = Double.parseDouble(jsonArray.getString(4));

            daySection = daySection + (((highPrice - lowPrice) / startPrice)) * 100;

            startPrices = startPrices + startPrice;
            highPrices = highPrices + highPrice;
            lowPrices = lowPrices + lowPrice;
        }

        Double monthSection = ((highPrices - lowPrices) / startPrices) * 100;
        daySection = daySection / 30;

        if( daySection == monthSection ){
            return true;
        }else{
            return false;
        }
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

    private void parseDateToMonth() {
    }

    private String getUrl(String order_currency){
        // https://api.bithumb.com/public/candlestick/BTC_KRW/1m => 1일 캔들 한국돈으로 BTC를
        return bithumbUrl + "/public/candlestick/" + order_currency + "_KRW" + "/24h";
    }
}
