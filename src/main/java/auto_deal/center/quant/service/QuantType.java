package auto_deal.center.quant.service;

import auto_deal.center.quant.model.QuantModel;

import java.util.List;

public interface QuantType {
    QuantModel get(String ticker);
    List<QuantModel> getAll();
}
