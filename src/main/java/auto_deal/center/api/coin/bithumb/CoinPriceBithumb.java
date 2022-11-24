package auto_deal.center.api.coin.bithumb;

import auto_deal.center.api.coin.CoinPrice;
import auto_deal.center.api.coin.model.CoinApiRslt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
//@RequiredArgsConstructor
public class CoinPriceBithumb implements CoinPrice {
    @Value("${bithumb.url}")
    private String bithumbUrl;

    private RestTemplate restTemplate;

    private CoinPriceBithumb(){
        restTemplate = new RestTemplate();
    }

    @Override
    public List<CoinApiRslt> getPrices() {
        String nowPricePath = "/public/ticker/ALL_KRW";
        String coinNowPriceUrl =  bithumbUrl + nowPricePath;


        HttpHeaders headers  = new HttpHeaders();
        HttpEntity entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(coinNowPriceUrl, HttpMethod.GET, entity, Map.class);
        log.info("CoinPriceBithumb.getPrice :::::::::: response.getBody() ::::::::::::: {} ", response.getBody());

        List<CoinApiRslt> coinApiRslts = new ArrayList<>();
        return coinApiRslts;
    }
}
