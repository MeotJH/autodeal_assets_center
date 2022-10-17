package auto_deal.center.quant.service;

import auto_deal.center.quant.dto.BullInfo;

import java.util.List;

public interface QuantService {

    public String getBullMarketInfo(List<BullInfo> bullInfos);
}
