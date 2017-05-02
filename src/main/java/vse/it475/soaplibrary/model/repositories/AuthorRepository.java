package vse.it475.soaplibrary.model.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import vse.it475.soaplibrary.model.entities.Author;

/**
 * Created by hofmanix on 02/05/2017.
 */
public interface AuthorRepository extends MongoRepository<Author, String> {
}
