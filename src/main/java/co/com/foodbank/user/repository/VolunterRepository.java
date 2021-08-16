package co.com.foodbank.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import co.com.foodbank.user.exception.UserNotFoundException;
import co.com.foodbank.user.v1.model.Volunter;

/**
 * @author mauricio.londono@gmail.com co.com.foodbank.user.repository 16/05/2021
 */
@Repository
public interface VolunterRepository extends MongoRepository<Volunter, String> {

    @Query("{$and: [{'id': ?0}, {'dni': ?1}]}")
    Volunter findVolunteer(String id, Long valueOf)
            throws UserNotFoundException;


}
