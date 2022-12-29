package auto_deal.center.cmm.model;

import auto_deal.center.quant.model.QuantModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class NoticeMessageModel {
    Long chatId;
    List<QuantModel> quantModels;

}
