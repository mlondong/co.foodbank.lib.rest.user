package co.com.foodbank.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import co.com.foodbank.user.v1.model.Volunter;

/**
 * @author mauricio.londono@gmail.com co.com.foodbank.user.repository 16/05/2021
 */
@Repository
public interface VolunterRepository extends MongoRepository<Volunter, String> {


}
