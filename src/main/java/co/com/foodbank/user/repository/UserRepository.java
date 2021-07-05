package co.com.foodbank.user.repository;

import java.util.Collection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import co.com.foodbank.user.exception.UserNotFoundException;
import co.com.foodbank.user.v1.model.User;

/**
 * @author mauricio.londono@gmail.com co.com.foodbank.user.repository 15/05/2021
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{'email':{'$regex':'?0','$options':'i'}}")
    Collection<User> findByEmail(String email) throws UserNotFoundException;

    @Query("{'cuil': ?0}")
    User finByCuit(Long cuit) throws UserNotFoundException;

    @Query("{'dni': ?0}")
    User finByDni(Long dni) throws UserNotFoundException;


}
