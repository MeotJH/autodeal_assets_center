package auto_deal.center.trade_detail.service;

import auto_deal.center.quant.domain.Quant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TradeDetailServiceImpl implements TradeDetailService {

    public <T> T saveTradeDetail(Long chatId, String text, Quant quant, Class<T> cls) {
        return null;
    }

}
