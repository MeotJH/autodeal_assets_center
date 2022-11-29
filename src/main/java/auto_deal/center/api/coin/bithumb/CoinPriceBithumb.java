package auto_deal.center.api.coin.bithumb;

import auto_deal.center.api.coin.CoinPrice;
import auto_deal.center.api.coin.model.CoinApiRslt;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

// 빗썸 API사용하기 위한 구현체
@Service
@Slf4j
public class CoinPriceBithumb implements CoinPrice {
    @Value("${bithumb.url}")
    private String bithumbUrl;

    private RestTemplate restTemplate;

    private CoinPriceBithumb(){
        restTemplate = new RestTemplate();
    }

    // 현재 코인의 모든 값을 받아온다.
    @Override
    public CoinApiRslt getPrices() {
        String nowPricePath = "/public/ticker/ALL_KRW";
        String coinNowPriceUrl =  bithumbUrl + nowPricePath;

        HttpHeaders headers  = new HttpHeaders();
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(coinNowPriceUrl, HttpMethod.GET, entity, Object.class);

        CoinApiRslt coinApiRslt = makeObjToCoin(response.getBody());
        log.info("CoinPriceBithumb.getPrice :::::::::: coinApiRslt ::::::::::::: {} ", coinApiRslt.toString());

        return coinApiRslt;
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
}
