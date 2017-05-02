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
    private String[] keywords;
    private String authorId;
    private List<String> coAuthorId;
    private List<BookCopy> copies;
    private List<Reader> requests;

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

    public List<String> getCoAuthorId () { return coAuthorId; }

    public void setCoAuthorId( List<String> coAuthorId ) { this.coAuthorId = coAuthorId; }

    public List<BookCopy> getCopies() {
        return copies;
    }

    public void setCopies(List<BookCopy> copies) {
        this.copies = copies;
    }

    public String getIsin() { return isbn; }

    public void setIsin(String isbn) { this.isbn = isbn; }

    public String getLanguage() { return language; }

    public void setLanguage(String language) { this.language = language; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public String[] getKeywords() { return keywords; }

    public void setKeywords(String[] keywords) { this.keywords = keywords; }

    public List<Reader> getRequests() { return requests; }

    public void setRequests(List<Reader> requests) { this.requests = requests; }
}
