package co.com.foodbank.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import co.com.foodbank.user.v1.model.Beneficiary;

@Repository
public interface BeneficiaryRepository
        extends MongoRepository<Beneficiary, String> {



}
