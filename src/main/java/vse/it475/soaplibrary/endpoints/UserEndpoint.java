package vse.it475.soaplibrary.endpoints;

import io.spring.guides.gs_producing_web_service.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by hofmanix on 30/04/2017.
 */
@Endpoint
public class UserEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createAccount")
    @ResponsePayload
    public ErrorResponse createAccount(@RequestPayload CreateAccountRequest request) {
        throw new NotImplementedException();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "editAccount")
    @ResponsePayload
    public ErrorResponse editAccount(@RequestPayload CreateAccountRequest request) {
        throw new NotImplementedException();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "login")
    @ResponsePayload
    public LoginResponse login(@RequestPayload LoginRequest request) {
        throw new NotImplementedException();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "setUserRole")
    @ResponsePayload
    public ErrorResponse setUserRole(@RequestPayload SetUserRoleRequest request) {
        throw new NotImplementedException();
    }
}
