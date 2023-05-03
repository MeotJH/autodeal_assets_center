package auto_deal.center.api.coin;

import java.util.Map;

public interface SixMonthSection {

    Map<String,Double> get(String ticker);

    Double getMonthFirstPrice(String ticker);
}
