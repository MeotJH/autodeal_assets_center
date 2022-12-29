package auto_deal.center.trade_detail.service;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.model.QuantModel;
import auto_deal.center.trade_detail.trend_follow.domain.TrendFollow;

public interface TradeDetailService {

    QuantModel processQuant(String text, Quant quant);

    TrendFollow saveOne(TrendFollow trendFollow);
}
