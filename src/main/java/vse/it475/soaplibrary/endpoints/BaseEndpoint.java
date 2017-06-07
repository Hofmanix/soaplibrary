package vse.it475.soaplibrary.endpoints;

import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;
import vse.it475.soaplibrary.model.entities.User;
import vse.it475.soaplibrary.model.repositories.UserRepository;

/**
 * Created by nikita on 07/06/2017.
 */
@Component
public class BaseEndpoint {

    @Autowired
    private UserRepository userRepository;

    protected User checkToken(String token) {
        String decodedToken = new String(Base64.decode(StringUtils.getBytesUtf8(token)));
        String[] idAndToken = decodedToken.split(":");
        return userRepository.findOne(idAndToken[0]);
    }
}
