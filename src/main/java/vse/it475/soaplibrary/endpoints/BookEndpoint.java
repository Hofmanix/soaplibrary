package vse.it475.soaplibrary.endpoints;

import io.spring.guides.gs_producing_web_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import vse.it475.soaplibrary.model.repositories.BookRepository;

/**
 * Created by hofmanix on 29/04/2017.
 */
@Endpoint
public class BookEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    @Autowired
    private BookRepository bookRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBooks")
    @ResponsePayload
    public GetBooksResponse getBooks(@RequestPayload GetBooksRequest request) {
        throw new NotImplementedException();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBook")
    @ResponsePayload
    public GetBookResponse getBook(@RequestPayload GetBookRequest request) {
        return new GetBookResponse();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "borrowBook")
    @ResponsePayload
    public ErrorResponse borrowBook(@RequestPayload BorrowBookRequest request) {
        throw new NotImplementedException();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "returnBook")
    @ResponsePayload
    public ErrorResponse returnBook(@RequestPayload ReturnBookRequest request) {
        throw new NotImplementedException();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "bookBook")
    @ResponsePayload
    public ErrorResponse bookBook(@RequestPayload BookBookRequest request) {
        throw new NotImplementedException();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "cancelBooking")
    @ResponsePayload
    public ErrorResponse cancelBooking(@RequestPayload CancelBookingRequest request) {
        throw new NotImplementedException();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addBook")
    @ResponsePayload
    public ErrorResponse addBook(@RequestPayload AddBookRequest request) {
        throw new NotImplementedException();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "removeBook")
    @ResponsePayload
    public ErrorResponse removeBook(@RequestPayload RemoveBookRequest request) {
        throw new NotImplementedException();
    }
}
