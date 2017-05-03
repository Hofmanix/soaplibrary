package vse.it475.soaplibrary.model.entities;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * Created by hofmanix on 30/04/2017.
 */
public class Book {
    @Id
    private String id;
    private String name;
    private String isbn;
    private Date published;
    private String language;
    private String location;
    private List<String> keywords;
    private String authorId;
    private List<BookCopy> copies;
    private List<BookingBook> bookings;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPublished( Date published ) { this.published = published; }

    public Date getPublished() { return published; }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }


    public List<BookCopy> getCopies() {
        return copies;
    }

    public void setCopies(List<BookCopy> copies) {
        this.copies = copies;
    }

    public String getIsbn() { return isbn; }

    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getLanguage() { return language; }

    public void setLanguage(String language) { this.language = language; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public List<String> getKeywords() { return keywords; }

    public void setKeywords(List<String> keywords) { this.keywords = keywords; }


    public List<BookingBook> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingBook> bookings) {
        this.bookings = bookings;
    }
}
