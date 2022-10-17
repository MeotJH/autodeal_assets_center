package auto_deal.center.quant.controller;

import auto_deal.center.quant.dto.BullInfo;
import auto_deal.center.quant.service.QuantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
public class QuantController {

    private final QuantService quantService;

    @PutMapping
    public String getBullMarketInfo(@RequestBody List<BullInfo> bullInfos){
        quantService.getBullMarketInfo(bullInfos);
        return null;
    }
}