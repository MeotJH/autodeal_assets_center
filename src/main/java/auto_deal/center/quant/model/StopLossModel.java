package auto_deal.center.quant.model;

import lombok.Builder;
import lombok.Getter;

import java.text.DecimalFormat;

@Builder
@Getter
public class StopLossModel implements QuantModel{

    private String ticker;
    private Double nowPrice;
    private Double dayOfMonthPrice;
    private Double stopLossPercent;
    private Double stopLossPrice;
    private String result;

    private int count;

    @Override
    public String toRsltStr() {

        DecimalFormat formatter = new DecimalFormat("#,###,###.0");
        String dayOfMonthPriceStr = formatter.format(dayOfMonthPrice); // => 이번달 시가로 바꾸어야함
        String nowPriceStr = formatter.format(nowPrice); // => 이번달 시가로 바꾸어야함

        if( stopLossPrice == null){
            this.stopLossPrice = dayOfMonthPrice * (stopLossPercent/100);
            this.stopLossPrice = this.dayOfMonthPrice - this.stopLossPrice;
        }
        String stopLossPriceStr = formatter.format(this.stopLossPrice);
        
        //TODO 코드화 하고 뺴야하는 부분
        String resultStr; 
        if( this.result.equals("ST00") ){
            resultStr = "계속보유";
        }else{
            resultStr = "손절매";
        }

        return "[손절매투자법 알림] \n" +
                "ticker 명       " +ticker+"\n" +
                "이번달 시가      "+dayOfMonthPriceStr +"원\n" +
                "손절선 비율      "+ stopLossPercent+"% \n" +
                "손절매 타겟가     "+stopLossPriceStr+" 원\n" +
                "현재가           "+ nowPriceStr+" 원\n" +
                "결과            " + resultStr +"\n" +
                "("+count+"/3) ";
    }
}
