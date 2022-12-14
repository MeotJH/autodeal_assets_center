package auto_deal.center.trade_detail.model;

import auto_deal.center.cmm.model.CommonModel;
import auto_deal.center.trade_detail.domain.TradeDetail;

public class TradeDetailTalk implements CommonModel {

    private TradeDetail tradeDetail;

    @Override
    public TradeDetail get(){
        return TradeDetail.class.cast(tradeDetail);
    }

    public void set(TradeDetail tradeDetail){
        this.tradeDetail = tradeDetail;
    }
}
