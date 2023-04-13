package auto_deal.center.quant.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StopLossModel implements QuantModel{

    private String ticker;
    private Double price;
    private Double stopLossPercent;

    private Double stopLossPrice;
    private String result;

    @Override
    public String toRsltStr() {

        this.stopLossPrice = price * stopLossPercent;

        if( this.price > this.stopLossPrice ){
            this.result = "계속 보유";
        }else{
            this.result = "손절매";
        }

        return "[손절매투자법 알림] \n"+ticker+"의 현재가는 "+price+"이고, 손절선 비율은 "+ stopLossPercent+"입니다. 따라서, 이번달 중 "+stopLossPrice+"이하라면 매도해야합니다.";
    }
}
