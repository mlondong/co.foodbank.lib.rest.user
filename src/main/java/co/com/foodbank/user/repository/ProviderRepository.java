package co.com.foodbank.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import co.com.foodbank.user.exception.UserNotFoundException;
import co.com.foodbank.user.v1.model.Provider;

/**
 * @author mauricio.londono@gmail.com co.com.foodbank.user.repository 18/05/2021
 */
@Repository
public interface ProviderRepository extends MongoRepository<Provider, String> {

    @Query("{'sucursal.id': ?0 }")
    Provider findBySucursal(String id) throws UserNotFoundException;

}
