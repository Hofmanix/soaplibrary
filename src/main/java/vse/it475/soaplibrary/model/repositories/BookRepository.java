package vse.it475.soaplibrary.model.repositories;

import io.spring.guides.gs_producing_web_service.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * Created by hofmanix on 29/04/2017.
 */
public interface BookRepository extends MongoRepository<Book, String> {
}
