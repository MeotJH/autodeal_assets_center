package auto_deal.center.quant.service;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.telegram.message.TelegramBotMessage;
import auto_deal.center.user.domain.Users;

public interface QuantService {
    Quant saveQuantByEnum(TelegramBotMessage tbm,Users user);
}
