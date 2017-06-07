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
import vse.it475.soaplibrary.model.entities.*;
import vse.it475.soaplibrary.model.repositories.AuthorRepository;
import vse.it475.soaplibrary.model.repositories.BookRepository;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hofmanix on 29/04/2017.
 */
@Endpoint
public class BookEndpoint extends BaseEndpoint {
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
        User user = checkToken(request.getToken());
        if(user == null) {
            response.setStatus("err");
            response.setError("User not logged in");
            return response;
        }
        Book requestedBook = bookRepository.findOne(request.getBookId());
        List<BookCopy> copies = requestedBook.getCopies().stream().filter(bookCopy -> !bookCopy.isBorrowed()).collect(Collectors.toList());
        Date borrowFrom = new Date(request.getBeginDate().getMillisecond());
        Date borrowTo = new Date(request.getEndDate().getMillisecond());
        List<BookingBook> bookings = requestedBook.getBookings().stream().filter(bookingBook -> bookingBook.getBookFrom().getTime() < borrowTo.getTime()).collect(Collectors.toList());
        if(copies.size() <= bookings.size()) {
            response.setStatus("err");
            response.setStatus("No books are available.");
            return response;
        }

        BookCopy bookCopy = copies.get(0);
        bookCopy.setBorrowed(true);
        bookCopy.setBorrowerId(user.getId());
        bookCopy.setFrom(borrowFrom);
        bookCopy.setTo(borrowTo);
        bookRepository.save(requestedBook);
        response.setStatus("ok");
        return response;

    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "returnBook")
    @ResponsePayload
    public ReturnBookResponse returnBook(@RequestPayload ReturnBookRequest request) {
        ReturnBookResponse response = new ReturnBookResponse();
        User user = checkToken(request.getToken());
        if(user == null) {
            response.setStatus("err");
            response.setError("User not logged in");
            return response;
        }
        Book returnBook = bookRepository.findOne(request.getBookId());
        Optional<BookCopy> copy = returnBook.getCopies().stream().filter(bookCopy -> bookCopy.isBorrowed() && bookCopy.getBorrowerId().equals(user.getId())).findFirst();
        if (copy.isPresent()){
            BookCopy bookCopy = copy.get();
            bookCopy.setBorrowed(false);
            bookCopy.setBorrowerId(null);
            bookRepository.save(returnBook);
            response.setStatus("ok");
        }
        else {
            response.setStatus("err");
            response.setError("It's not possible to return");
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "bookBookRequest")
    @ResponsePayload
    public BookBookResponse bookBook(@RequestPayload BookBookRequest request) {
        BookBookResponse bookBookResponse = new BookBookResponse();
        User user = checkToken(request.getToken());
        if(user == null) {
            bookBookResponse.setStatus("err");
            bookBookResponse.setError("User not logged in");
            return bookBookResponse;
        }
        Book book = bookRepository.findOne(request.getBookId());
        List<BookCopy> copies = book.getCopies().stream().filter(bookCopy -> bookCopy.isBorrowed() && bookCopy.getTo().getTime() > request.getBeginDate().getMillisecond()).collect(Collectors.toList());
        List<BookingBook> bookingBooks = book.getBookings().stream().filter(bookingBook ->
                (bookingBook.getBookFrom().getTime() < request.getBeginDate().getMillisecond() &&
                 bookingBook.getBookTo().getTime() > request.getBeginDate().getMillisecond()   &&
                 bookingBook.getBookTo().getTime() < request.getEndDate().getMillisecond()) ||
                (bookingBook.getBookFrom().getTime() > request.getBeginDate().getMillisecond() &&
                 bookingBook.getBookTo().getTime() < request.getEndDate().getMillisecond()) ||
                (bookingBook.getBookFrom().getTime() < request.getBeginDate().getMillisecond() &&
                 bookingBook.getBookTo().getTime() > request.getEndDate().getMillisecond()) ||
                (bookingBook.getBookFrom().getTime() > request.getBeginDate().getMillisecond() &&
                 bookingBook.getBookFrom().getTime() < request.getEndDate().getMillisecond()   &&
                 bookingBook.getBookTo().getTime() > request.getEndDate().getMillisecond())).collect(Collectors.toList());
        if (copies.size() <= bookingBooks.size()){
            bookBookResponse.setStatus("err");
            bookBookResponse.setError("No book for reservation");
            return bookBookResponse;
        }

        BookingBook bookingBook = new BookingBook();
        bookingBook.setBookFrom(new Date(request.getBeginDate().getMillisecond()));
        bookingBook.setBookTo(new Date(request.getEndDate().getMillisecond()));
        bookingBook.setBookerId(user.getId());
        book.getBookings().add(bookingBook);
        bookRepository.save(book);
        bookBookResponse.setStatus("ok");
        return bookBookResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "cancelBooking")
    @ResponsePayload
    public CancelBookingResponse cancelBooking(@RequestPayload CancelBookingRequest request) {
        CancelBookingResponse cancelBookingResponse = new CancelBookingResponse();
        User user = checkToken(request.getToken());
        if(user == null) {
            cancelBookingResponse.setStatus("err");
            cancelBookingResponse.setError("User not logged in");
            return  cancelBookingResponse;
        }
        Book book = bookRepository.findOne(request.getBookId());
        Optional<BookingBook> bookingBook = book.getBookings().stream().filter(bookingBook1 -> bookingBook1.getBookerId().equals(user.getId())).findFirst();
        if (bookingBook.isPresent()) {
            book.getBookings().remove(bookingBook.get());
            bookRepository.save(book);
            cancelBookingResponse.setStatus("ok");
        } else {
            cancelBookingResponse.setStatus("err");
            cancelBookingResponse.setError("No book is booked");
        }
        return cancelBookingResponse;
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
    public RemoveBookResponse removeBook(@RequestPayload RemoveBookRequest request) {
        Book book = bookRepository.findOne(request.getBookId());
        RemoveBookResponse removeBookResponse = new RemoveBookResponse();
        if (book == null){
            removeBookResponse.setError("err");
            removeBookResponse.setStatus("No book found");
        } else {
            bookRepository.delete(book);
            removeBookResponse.setStatus("ok");
        }
        return removeBookResponse;
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
}
