package auto_deal.center.quant.model;

import lombok.Builder;
import lombok.Getter;

import java.text.DecimalFormat;

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

        DecimalFormat formatter = new DecimalFormat("#,###,###.0");
        String formattedNumber = formatter.format(price); // => 이번달 시가로 바꾸어야함

        this.stopLossPrice = price * (stopLossPercent/100);
        this.stopLossPrice = this.price - this.stopLossPrice;
        String stopLossPriceStr = formatter.format(this.stopLossPrice);

        if( this.price > this.stopLossPrice ){
            this.result = "계속 보유";
        }else{
            this.result = "손절매";
        }

        return "[손절매투자법 알림] \n"+ticker+"의 현재가는 "+formattedNumber+"이고, 손절선 비율은 "+ stopLossPercent+"% 입니다. 이번달 손절매 타겟가는 "+stopLossPriceStr+"이고, "+ this.result +"해야합니다.";
    }
}
