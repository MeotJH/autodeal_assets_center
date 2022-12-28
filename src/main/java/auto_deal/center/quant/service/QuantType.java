package auto_deal.center.quant.service;

import auto_deal.center.quant.model.QuantModel;

import java.util.List;

public interface QuantType {
    <T extends QuantModel> T get(String ticker, Class<T> cls);
    List<QuantModel> getAll();


}
