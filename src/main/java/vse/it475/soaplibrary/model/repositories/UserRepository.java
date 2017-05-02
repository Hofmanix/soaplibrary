package vse.it475.soaplibrary.model.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import vse.it475.soaplibrary.model.entities.User;

/**
 * Created by hofmanix on 30/04/2017.
 */
public interface UserRepository extends MongoRepository<User, String> {
}
