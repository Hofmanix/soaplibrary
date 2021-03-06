package vse.it475.soaplibrary.endpoints;

import io.spring.guides.gs_producing_web_service.*;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import vse.it475.soaplibrary.model.entities.User;
import vse.it475.soaplibrary.model.repositories.UserRepository;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.Base64;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hofmanix on 30/04/2017.
 */
@Endpoint
public class UserEndpoint extends BaseEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    @Autowired
    private UserRepository userRepository;

    /**
     * Method for creating new user account
     * @param request
     * @return
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createAccountRequest")
    @ResponsePayload
    public CreateAccountResponse createAccount(@RequestPayload CreateAccountRequest request) {
        CreateAccountResponse response = new CreateAccountResponse();
        AccountRequest accountRequest = request.getAccount();
        if(accountRequest.getDateOfBirth() != null && accountRequest.getDateOfBirth().isValid() &&
                accountRequest.getEmail() != null && !accountRequest.getEmail().trim().isEmpty() &&
                accountRequest.getName() != null && !accountRequest.getName().trim().isEmpty() &&
                accountRequest.getPassword() != null && !accountRequest.getPassword().trim().isEmpty() &&
                accountRequest.getSurname() != null && !accountRequest.getSurname().trim().isEmpty() &&
                accountRequest.getUsername() != null && !accountRequest.getUsername().trim().isEmpty()) {
            if(userRepository.findByUsername(accountRequest.getUsername()) != null) {
                response.setStatus("err");
                response.setError("User with this username already exists.");
                return response;
            } else if (userRepository.findByEmail(accountRequest.getEmail()) != null) {
                response.setStatus("err");
                response.setError("User with this email already exists");
                return response;
            }

            User user = new User();
            user.setEmail(accountRequest.getEmail());
            user.setDateOfBirth(accountRequest.getDateOfBirth().toGregorianCalendar().getTime());
            user.setName(accountRequest.getName());
            user.setPassword(BCrypt.hashpw(accountRequest.getPassword(), BCrypt.gensalt()));
            user.setRole(UserRole.USER);
            user.setSurname(accountRequest.getSurname());
            user.setUsername(accountRequest.getUsername());
            userRepository.save(user);

            response.setStatus("ok");
            response.setAccount(toAccountResponse(user));
            return response;

        } else {
            response.setError("You have to fill all fields");
            response.setStatus("err");
            return response;
        }
    }

    /**
     * Method for account editation, has to be own account or user has to be administrator
     * @param request
     * @return
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "editAccountRequest")
    @ResponsePayload
    public EditAccountResponse editAccount(@RequestPayload EditAccountRequest request) {
        EditAccountResponse response = new EditAccountResponse();
        AccountRequest accounteditRequest = request.getAccount();

        User user = checkToken(request.getToken());
        if(user == null) {
            response.setStatus("err");
            response.setError("User not logged in");
            return response;
        }
        if(user.getId() != null && !user.getId().equals(accounteditRequest.getId()) && user.getRole() != UserRole.ADMINISTRATOR) {
            response.setStatus("err");
            response.setError("You can edit only your account");
            return response;
        }

        if(accounteditRequest.getDateOfBirth() != null && accounteditRequest.getDateOfBirth().isValid() &&
                accounteditRequest.getEmail() != null && !accounteditRequest.getEmail().trim().isEmpty() &&
                accounteditRequest.getName() != null && !accounteditRequest.getName().trim().isEmpty() &&
                accounteditRequest.getPassword() != null && !accounteditRequest.getPassword().trim().isEmpty() &&
                accounteditRequest.getSurname() != null && !accounteditRequest.getSurname().trim().isEmpty() &&
                accounteditRequest.getUsername() != null && !accounteditRequest.getUsername().trim().isEmpty()) {
            if (!user.getId().equals(accounteditRequest.getId())) {
                user = userRepository.findOne(accounteditRequest.getId());
            }

            user.setEmail(accounteditRequest.getEmail());
            user.setDateOfBirth(accounteditRequest.getDateOfBirth().toGregorianCalendar().getTime());
            user.setName(accounteditRequest.getName());
            user.setPassword(BCrypt.hashpw(accounteditRequest.getPassword(), BCrypt.gensalt()));
            user.setRole(UserRole.USER);
            user.setSurname(accounteditRequest.getSurname());
            user.setUsername(accounteditRequest.getUsername());
            userRepository.save(user);

            response.setStatus("ok");
            response.setAccount(toAccountResponse(user));
            return response;
        } else {
            response.setError("You have to fill all fields");
            response.setStatus("err");
            return response;
        }
    }

    /**
     * Logging user in and sending token for future communication
     * @param request
     * @return
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "loginRequest")
    @ResponsePayload
    public LoginResponse login(@RequestPayload LoginRequest request) {
        LoginResponse response = new LoginResponse();
        User user = userRepository.findByUsername(request.getUsername());
        if(user == null){
            response.setStatus("err");
            response.setError("User with this username doesn't exist.");
        } else if(!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            response.setStatus("err");
            response.setError("Wrong password.");
        } else {
            try {
                response.setStatus("ok");
                response.setAccount(toAccountResponse(user));

                String token = user.getId() + ":" + BCrypt.hashpw((new Date()).getTime() + user.getId(), BCrypt.gensalt());
                token = Base64.getEncoder().encodeToString(StringUtils.getBytesUtf8(token));
                response.setToken(token);

            } catch (Exception ex) {
                response.setStatus("err");
                response.setError(ex.getMessage());
            }
        }
        return response;
    }

    /**
     * Admin can set specific role to user
     * @param request
     * @return
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "setUserRoleRequest")
    @ResponsePayload
    public SetUserRoleResponse setUserRole(@RequestPayload SetUserRoleRequest request) {
        User user = checkToken(request.getToken());
        if(user == null || user.getRole() != UserRole.ADMINISTRATOR) {
            SetUserRoleResponse response = new SetUserRoleResponse();
            response.setStatus("err");
            response.setError("You have to be logged in as administrator");
            return response;
        }
        User changeUser = userRepository.findOne(request.getUserId());
        if(changeUser == null) {
            SetUserRoleResponse response = new SetUserRoleResponse();
            response.setStatus("err");
            response.setError("User was not found");
            return response;
        }

        changeUser.setRole(request.getUserRole());
        userRepository.save(changeUser);

        SetUserRoleResponse response = new SetUserRoleResponse();
        response.setStatus("ok");
        return response;
    }

    /**
     * Crates logged user admin
     * This method has to be deleted on the production!
     * @param request
     * @return
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "makeUserAdminRequest")
    @ResponsePayload
    public MakeUserAdminResponse makeUserAdmin(@RequestPayload MakeUserAdminRequest request) {
        User user = checkToken(request.getToken());
        MakeUserAdminResponse response = new MakeUserAdminResponse();

        if(user == null) {
            response.setStatus("err");
            response.setError("User not logged in");
            return response;
        }

        user.setRole(UserRole.ADMINISTRATOR);
        userRepository.save(user);
        response.setStatus("ok");
        return response;
    }


    public AccountResponse toAccountResponse(User user) {
        AccountResponse accountResponse = new AccountResponse();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(user.getDateOfBirth().getTime());

        try {
            accountResponse.setDateOfBirth(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        } catch (DatatypeConfigurationException ex) {
            accountResponse.setDateOfBirth(null);
        }
        accountResponse.setId(user.getId());
        accountResponse.setName(user.getName());
        accountResponse.setSurname(user.getSurname());
        accountResponse.setUsername(user.getUsername());
        accountResponse.setEmail(user.getEmail());

        return accountResponse;
    }
}
