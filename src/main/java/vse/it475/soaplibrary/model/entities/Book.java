package vse.it475.soaplibrary.model.entities;

import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by hofmanix on 30/04/2017.
 */
public class Book {
    @Id
    private String id;
    private String name;
    private String authorId;
    private List<BookCopy> copies;

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
}
