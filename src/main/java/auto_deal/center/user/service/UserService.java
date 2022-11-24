package auto_deal.center.user.service;

import org.springframework.stereotype.Service;

public interface UserService {

    Boolean Process(Long chatId, String text);
}
