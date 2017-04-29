package vse.it475.soaplibrary.endpoints;

import io.spring.guides.gs_producing_web_service.GetBookRequest;
import io.spring.guides.gs_producing_web_service.GetBookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import vse.it475.soaplibrary.model.repositories.LibraryRepository;

/**
 * Created by hofmanix on 29/04/2017.
 */
public class LibraryEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    @Autowired
    private LibraryRepository libraryRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload
    public GetBookResponse getCountry(@RequestPayload GetBookRequest request) {
        return new GetBookResponse();
    }
}
