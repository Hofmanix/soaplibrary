package vse.it475.soaplibrary.model.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import vse.it475.soaplibrary.model.entities.Book;

/**
 * Created by hofmanix on 29/04/2017.
 */
public interface BookRepository extends MongoRepository<Book, String> {
}
