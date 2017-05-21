package vse.it475.soaplibrary.endpoints;

import io.spring.guides.gs_producing_web_service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import vse.it475.soaplibrary.model.entities.Author;
import vse.it475.soaplibrary.model.entities.Book;
import vse.it475.soaplibrary.model.entities.BookCopy;
import vse.it475.soaplibrary.model.entities.BookingBook;
import vse.it475.soaplibrary.model.repositories.AuthorRepository;
import vse.it475.soaplibrary.model.repositories.BookRepository;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hofmanix on 29/04/2017.
 */
@Endpoint
public class BookEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    private static final Logger LOGGER = LoggerFactory.getLogger(BookEndpoint.class);

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBooksRequest")
    @ResponsePayload
    public GetBooksResponse getBooks(@RequestPayload GetBooksRequest request) {
        GetBooksResponse response = new GetBooksResponse();
        response.getBooks().addAll(bookRepository.findAll().stream().filter(book -> {
            LOGGER.info(book.getName());
            if (request.getAuthorsId() != null && request.getAuthorsId().size() > 0) {
                if (!request.getAuthorsId().contains(book.getAuthorId())) {
                    LOGGER.info("Authors not in.");
                    LOGGER.info(request.getAuthorsId().get(0) + "");
                    return false;
                }
            }
            if (book.getName() != null && request.getBookName() != null && !request.getBookName().trim().equals("")) {
                if (!StringUtils.containsIgnoreCase(book.getName(), request.getBookName())) {
                    LOGGER.info("Name not in");
                    return false;
                }
            }
            return request.isAvailable() == null || !request.isAvailable() || book.getCopies().stream().anyMatch(copy -> !copy.isBorrowed());

        }).map(this::convertToBookResponse).collect(Collectors.toList()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBookRequest")
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

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "borrowBookRequest")
    @ResponsePayload
    public BorrowBookResponse borrowBook(@RequestPayload BorrowBookRequest request) {
        BorrowBookResponse response = new BorrowBookResponse();
        BookingBook requestedBook = bookRepository.findOne(request.getBookId();
        if (requestedBook == null ){
            response.setStatus("ok");
        } else {
            response.setStatus("err");
            response.setError("This book is already reserved.");
        }

       return response;

    }
/*
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "returnBook")
    @ResponsePayload
    public ReturnBookResponse returnBook(@RequestPayload ReturnBookRequest request) {
        ReturnBookResponse response = new ReturnBookResponse;
        BookCopy returnBook = bookRepository.findOne(request.getBookId();

        response.setReturnBook();
    }
*/
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "bookBookRequest")
    @ResponsePayload
    public ErrorResponse bookBook(@RequestPayload BookBookRequest request) {
        throw new NotImplementedException();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "cancelBooking")
    @ResponsePayload
    public ErrorResponse cancelBooking(@RequestPayload CancelBookingRequest request) {
        throw new NotImplementedException();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addBooksRequest")
    @ResponsePayload
    public AddBooksResponse addBooks(@RequestPayload AddBooksRequest request) {
        List<Book> books = bookRepository.save(request.getBooks().stream().filter(book -> {
            if(!authorRepository.exists(book.getAuthor().getId())) {
                return false;
            }
            if(book.getName() == null || book.getName().trim().equals("") || bookRepository.findByAuthorId(book.getAuthor().getId()).stream().map(Book::getName).anyMatch(name -> name != null && name.equalsIgnoreCase(book.getName()))) {
                return false;
            }
            if(book.getPublished() == null) {
                return false;
            }

            return true;
        }).map(bookResponse -> {
            Book book = new Book();
            book.setAuthorId(bookResponse.getAuthor().getId());
            book.setName(bookResponse.getName());
            book.setIsbn(bookResponse.getIsbn());
            book.setKeywords(bookResponse.getKeywords());
            book.setLanguage(bookResponse.getLanguage());
            book.setLocation(bookResponse.getLocation());
            book.setPublished(bookResponse.getPublished().toGregorianCalendar().getTime());
            List<BookCopy> copies = new ArrayList<>();
            for(int i = 0; i < bookResponse.getAvailable(); i++) {
                copies.add(new BookCopy());
            }
            book.setCopies(copies);

            return book;
        }).collect(Collectors.toList()));

        AddBooksResponse addBooksResponse = new AddBooksResponse();
        addBooksResponse.setStatus("ok");
        addBooksResponse.getBooks().addAll(books.stream().map(this::convertToBookResponse).collect(Collectors.toList()));
        return addBooksResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "removeBookRequest")
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
        bookResponse.setIsbn(book.getIsbn());
        bookResponse.setLanguage(book.getLanguage());
        bookResponse.setLocation(book.getLocation());
        bookResponse.setName(book.getName());
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(book.getPublished());
        try {
            bookResponse.setPublished(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        } catch (DatatypeConfigurationException ex) {
            bookResponse.setPublished(null);
        }

        return bookResponse;
    }

    private BorrowBookResponse
}
