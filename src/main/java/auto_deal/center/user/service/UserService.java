package auto_deal.center.user.service;

import auto_deal.center.cmm.model.CommonModel;
import auto_deal.center.user.domain.Users;

import java.util.Optional;

public interface UserService {

    Users process(Long chatId, String text);

    Boolean isUserExist(Long chatId);
}
