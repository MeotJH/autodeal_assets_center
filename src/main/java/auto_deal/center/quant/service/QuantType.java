package auto_deal.center.quant.service;

import auto_deal.center.quant.model.QuantModel;

public interface QuantType {
    QuantModel get(String ticker);
}
