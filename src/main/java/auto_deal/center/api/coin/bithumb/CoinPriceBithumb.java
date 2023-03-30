package auto_deal.center.api.coin.bithumb;

import auto_deal.center.api.coin.CoinPrice;
import auto_deal.center.api.coin.model.CoinApiRslt;
import auto_deal.center.api.coin.model.CoinOhlcvRslt;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

// 빗썸 API사용하기 위한 구현체
// TODO 라이브러리로 만들어서 openSource로 깃허브만들고 MavenCentral에 배포하기
@Service
@Slf4j
public class CoinPriceBithumb implements CoinPrice {
    @Value("${bithumb.url}")
    private String bithumbUrl;

    private RestTemplate restTemplate;

    private org.api.bithumb.CoinPriceBithumb coinPriceBithumb;

    public CoinPriceBithumb() throws IOException {
        restTemplate = new RestTemplate();
        this.coinPriceBithumb = new org.api.bithumb.CoinPriceBithumb();
    }

    /**
     * 현재 코인의 모든 값을 받아온다.
     * @return
     */
    @Override
    public CoinApiRslt getPriceData() {
        String nowPricePath = "/public/ticker/ALL_KRW";
        String coinNowPriceUrl =  bithumbUrl + nowPricePath;

        HttpHeaders headers  = new HttpHeaders();
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(coinNowPriceUrl, HttpMethod.GET, entity, Object.class);

        CoinApiRslt coinApiRslt = makeObjToCoin(response.getBody());
        log.info("CoinPriceBithumb.getPrice :::::::::: coinApiRslt ::::::::::::: {} ", coinApiRslt.toString());

        return coinApiRslt;
    }

    /**
     * ohlcv = 당일 시가, 고가, 저가, 종가, 거래량에 대한 데이터
     * @param ticker
     * @return ohlcv
     */
    @Override
    public CoinOhlcvRslt getOhlcv(String ticker) {
        String nowPricePath = "/public/candlestick/"+ticker+"_KRW/24h";
        String coinNowPriceUrl =  bithumbUrl + nowPricePath;

        HttpHeaders headers  = new HttpHeaders();
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<CoinOhlcvRslt> response = restTemplate.exchange(coinNowPriceUrl, HttpMethod.GET, entity, CoinOhlcvRslt.class);
        return response.getBody();
    }

    /**
     * 모든 가격들 가져오기
     * @return
     */
    @Override
    public JSONObject getNowPrices(){

        JSONObject jsonObject = null;
        try {
            jsonObject = getNowPriceBeforeException("NO_TICKER");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    @Override
    public JSONObject getNowPrice(String ticker){

        JSONObject jsonObject = null;
        try {
            jsonObject = getNowPriceBeforeException(ticker);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    // Map으로 리턴받은 결과값을 자료객체로 변환한다.
    private CoinApiRslt makeObjToCoin(Object response) {
        ObjectMapper mapper = new ObjectMapper();

        Map coinApiRsltMap = Map.class.cast(response);
        Map coinApiRsltInData = Map.class.cast(coinApiRsltMap.get("data"));
        coinApiRsltMap.put("date", coinApiRsltInData.get("date").toString());
        coinApiRsltInData.remove("date");

        CoinApiRslt coinApiRslt = mapper.convertValue(coinApiRsltMap, CoinApiRslt.class);
        return coinApiRslt;
    }

    private JSONObject getNowPriceBeforeException(String ticker) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url;
        if(ticker.equals("NO_TICKER")){
            url = getNowPriceUrl();
        }else{
            url = getNowPriceUrl(ticker);
        }

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        return new JSONObject( client.newCall(request).execute().body().string() );
    }

    @NotNull
    private String getNowPriceUrl(String ticker) {
        return bithumbUrl + "/public/orderbook/" + ticker + "_KRW";
    }

    @NotNull
    private String getNowPriceUrl() {
        return bithumbUrl + "/public/orderbook/ALL_KRW";
    }

}
