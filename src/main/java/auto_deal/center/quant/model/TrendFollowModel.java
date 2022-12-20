package auto_deal.center.quant.model;

import lombok.Builder;
import lombok.Getter;

@Builder
public class TrendFollowModel implements QuantModel{

    private String ticker;
    private Long nowPrice;

    @Getter
    private Long targetPrice;
    private Boolean isBuy;

    @Override
    public String toRsltStr(){
        String result;
        if(isBuy == true){
            result = "매수";
        }else{
            result = "매도";
        }
        return "[추세투자법 알림] \n"+ticker+"의 현재가는 "+nowPrice+"이고, 추세매매법상 3개월간 평균이동선의 가격은 "+ targetPrice+"입니다. 따라서, "+result+"해야 합니다.";
    }

}
