package vse.it475.soaplibrary.endpoints;

import io.spring.guides.gs_producing_web_service.AuthenticatedRequest;
import io.spring.guides.gs_producing_web_service.GetAuthorsResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by hofmanix on 30/04/2017.
 */
@Endpoint
public class AuthorEndpoint {

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAuthors")
    @ResponsePayload
    public GetAuthorsResponse getAuthors(@RequestPayload AuthenticatedRequest request) {
        throw new NotImplementedException();
    }
}
