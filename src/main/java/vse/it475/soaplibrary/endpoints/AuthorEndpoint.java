package vse.it475.soaplibrary.endpoints;

import io.spring.guides.gs_producing_web_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import vse.it475.soaplibrary.model.entities.Author;
import vse.it475.soaplibrary.model.repositories.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hofmanix on 30/04/2017.
 */
@Endpoint
public class AuthorEndpoint {

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    @Autowired
    private AuthorRepository authorRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAuthorsRequest")
    @ResponsePayload
    public GetAuthorsResponse getAuthors() {
        GetAuthorsResponse response = new GetAuthorsResponse();
        response.setStatus("ok");
        response.getAuthors().addAll(authorRepository.findAll().stream().map(author -> {
            System.out.println(author.getName());
            AuthorResponse authorResponse = new AuthorResponse();
            authorResponse.setId(author.getId());
            authorResponse.setName(author.getName());
            return authorResponse;
        }).collect(Collectors.toList()));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addAuthorsRequest")
    @ResponsePayload
    public AddAuthorsResponse addAuthors(@RequestPayload AddAuthorsRequest request) {
        List<Author> authors = authorRepository.save(request.getAuthors().stream()
                .filter(authorResponse -> authorResponse.getName() != null && !authorResponse.getName().trim().isEmpty())
                .map(authorResponse -> {
                    Author author = new Author();
                    author.setName(authorResponse.getName());
                    return author;
                }).collect(Collectors.toList()));

        AddAuthorsResponse response = new AddAuthorsResponse();
        response.setStatus("ok");
        response.getAuthorIds().addAll(authors.stream().map(Author::getId).collect(Collectors.toList()));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "removeAuthorRequest")
    @ResponsePayload
    public IdResponse removeAuthor() {
        throw new sun.reflect.generics.reflectiveObjects.NotImplementedException();
    }

}
