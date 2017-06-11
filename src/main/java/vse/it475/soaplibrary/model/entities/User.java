package vse.it475.soaplibrary.model.entities;

import io.spring.guides.gs_producing_web_service.UserRole;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hofmanix on 30/04/2017.
 */
public class User {
    @Id
    private String id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String email;
    private Date dateOfBirth;
    private UserRole role;
    private List<Book> tokens;

    public User() {
        tokens = new ArrayList<>();
    }

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<Book> getTokens() {
        return tokens;
    }

    public void setTokens(List<Book> tokens) {
        this.tokens = tokens;
    }
}
