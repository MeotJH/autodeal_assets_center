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

    private int count;

    @Override
    public String toRsltStr() {

        DecimalFormat formatter = new DecimalFormat("#,###,###.0");
        String nowPriceStr = formatter.format(price); // => 이번달 시가로 바꾸어야함

        if( stopLossPrice == null){
            this.stopLossPrice = price * (stopLossPercent/100);
            this.stopLossPrice = this.price - this.stopLossPrice;
        }
        String stopLossPriceStr = formatter.format(this.stopLossPrice);
        
        //TODO 코드화 하고 뺴야하는 부분
        String resultStr; 
        if( this.result.equals("ST00") ){
            resultStr = "계속보유";
        }else{
            resultStr = "손절매";
        }

        return "[손절매투자법 알림] \n"+ticker+"의 현재가는 "+nowPriceStr+"이고, 손절선 비율은 "+ stopLossPercent+"% 입니다. 이번달 손절매 타겟가는 "+stopLossPriceStr+"이고, "+ resultStr +"해야합니다. \n" +
                "결과가 바뀐다면 알림을 드리겠습니다 ("+count+"/3) ";
    }
}
