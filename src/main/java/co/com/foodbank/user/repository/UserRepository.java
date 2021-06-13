package co.com.foodbank.user.repository;

import java.util.Collection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import co.com.foodbank.user.exception.ContributionNotFoundException;
import co.com.foodbank.user.v1.model.User;

/**
 * @author mauricio.londono@gmail.com co.com.foodbank.user.repository 15/05/2021
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{'email': ?0}")
    Collection<User> findByEmail(String email) throws ContributionNotFoundException;

    @Query("{'cuil': ?0}")
    User finByCuit(Long cuit) throws ContributionNotFoundException;

    @Query("{'dni': ?0}")
    User finByDni(Long dni) throws ContributionNotFoundException;



}
