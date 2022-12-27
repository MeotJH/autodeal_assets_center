package auto_deal.center.trade_detail.service;

import auto_deal.center.quant.domain.Quant;

public interface TradeDetailService {

    <T> T saveTradeDetail(Long chatId, String text, Quant quant, Class<T> cls);
}
