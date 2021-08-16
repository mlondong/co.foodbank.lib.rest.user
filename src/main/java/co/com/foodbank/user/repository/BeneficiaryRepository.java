package co.com.foodbank.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import co.com.foodbank.user.exception.UserNotFoundException;
import co.com.foodbank.user.v1.model.Beneficiary;

/**
 * @author mauricio.londono@gmail.com co.com.foodbank.user.repository 16/08/2021
 */
@Repository
public interface BeneficiaryRepository
        extends MongoRepository<Beneficiary, String> {

    @Query("{$and: [{'id': ?0}, {'socialReason': ?1}]}")
    Beneficiary findBeneficiary(String id, String socialReason)
            throws UserNotFoundException;

}
