package vse.it475.soaplibrary.endpoints;

import io.spring.guides.gs_producing_web_service.AuthenticatedRequest;
import io.spring.guides.gs_producing_web_service.AuthorResponse;
import io.spring.guides.gs_producing_web_service.GetAuthorsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import vse.it475.soaplibrary.model.repositories.AuthorRepository;

import java.util.stream.Collectors;

/**
 * Created by hofmanix on 30/04/2017.
 */
@Endpoint
public class AuthorEndpoint {

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    @Autowired
    private AuthorRepository authorRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAuthors")
    @ResponsePayload
    public GetAuthorsResponse getAuthors() {
        GetAuthorsResponse response = new GetAuthorsResponse();
        response.setStatus("ok");
        response.getAuthors().addAll(authorRepository.findAll().stream().map(author -> {
            AuthorResponse authorResponse = new AuthorResponse();
            authorResponse.setId(author.getId());
            authorResponse.setName(author.getName());
            return authorResponse;
        }).collect(Collectors.toList()));

        return response;
    }
}
