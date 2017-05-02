package vse.it475.soaplibrary.endpoints;

import io.spring.guides.gs_producing_web_service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import vse.it475.soaplibrary.model.entities.Author;
import vse.it475.soaplibrary.model.entities.Book;
import vse.it475.soaplibrary.model.repositories.AuthorRepository;
import vse.it475.soaplibrary.model.repositories.BookRepository;

import java.util.stream.Collectors;

/**
 * Created by hofmanix on 29/04/2017.
 */
@Endpoint
public class BookEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBooks")
    @ResponsePayload
    public GetBooksResponse getBooks(@RequestPayload GetBooksRequest request) {
        GetBooksResponse response = new GetBooksResponse();
        response.getBooks().addAll(bookRepository.findAll().stream().filter(book -> {
            if (request.getAuthorsId().size() > 0) {
                if (!request.getAuthorsId().contains(book.getAuthorId())) {
                    return false;
                }
            }
            if (request.getBookName() != null && !request.getBookName().trim().equals("")) {
                if (!StringUtils.containsIgnoreCase(book.getName(), request.getBookName())) {
                    return false;
                }
            }
            return !request.isAvailable() || book.getCopies().stream().anyMatch(copy -> !copy.isBorrowed());

        }).map(this::convertToBookResponse).collect(Collectors.toList()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBook")
    @ResponsePayload
    public GetBookResponse getBook(@RequestPayload GetBookRequest request) {
        Book book = bookRepository.findOne(request.getId());
        GetBookResponse response = new GetBookResponse();
        if (book == null) {
            response.setStatus("err");
            response.setError("No book with given id was found.");
        } else {
            response.setStatus("ok");
            response.setBook(convertToBookResponse(book));
        }
        return response;
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
        Book book = new Book();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "removeBook")
    @ResponsePayload
    public ErrorResponse removeBook(@RequestPayload RemoveBookRequest request) {
        throw new NotImplementedException();
    }

    private BookResponse convertToBookResponse(Book book) {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(book.getId());

        Author author = authorRepository.findOne(book.getAuthorId());
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setId(author.getId());
        authorResponse.setName(author.getName());

        bookResponse.setAuthor(authorResponse);
        bookResponse.setAvailable((int)book.getCopies().stream().filter(copy -> !copy.isBorrowed()).count());

        return bookResponse;
    }
}
