package auto_deal.center.quant.service;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.quant.dto.BullInfo;
import auto_deal.center.quant.repository.QuantRepository;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.user.domain.Users;

import java.time.LocalDateTime;
import java.util.List;

public interface QuantService {

    String getBullMarketInfo(List<BullInfo> bullInfos);

    void saveQuantByEnum(String text, Users user);
}
