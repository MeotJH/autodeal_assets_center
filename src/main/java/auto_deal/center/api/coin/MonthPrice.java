package auto_deal.center.api.coin;

import org.json.JSONObject;

import java.util.Map;

public interface MonthPrice {

    Map<String,Double> get(String ticker);
}
