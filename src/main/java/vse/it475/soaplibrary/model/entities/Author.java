package vse.it475.soaplibrary.model.entities;

import org.springframework.data.annotation.Id;

/**
 * Created by hofmanix on 02/05/2017.
 */
public class Author {
    @Id
    private String id;
    private String name;

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
}
