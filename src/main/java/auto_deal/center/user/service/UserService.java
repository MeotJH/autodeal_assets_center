package auto_deal.center.user.service;

import auto_deal.center.cmm.model.CommonModel;

public interface UserService {

    CommonModel Process(Long chatId, String text);
}
