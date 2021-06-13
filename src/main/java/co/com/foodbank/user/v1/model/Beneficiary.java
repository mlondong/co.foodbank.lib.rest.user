package co.com.foodbank.user.v1.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import co.com.foodbank.address.dto.IAddress;
import co.com.foodbank.user.model.IBeneficiary;


@Document(collection = "User")
@TypeAlias("Beneficiary")
public class Beneficiary extends User implements IBeneficiary {


    public String socialReason;
    public String category;
    public int size;

    /**
     * Default constructor.
     */
    public Beneficiary() {

    }

    /**
     * Constructor with parameters.
     * 
     * @param name
     * @param email
     * @param address
     * @param password
     * @param phones
     * @param state
     * @param socialReason
     * @param category
     * @param size
     */
    public Beneficiary(String name, String email, IAddress address,
            String password, String phones, boolean state, String socialReason,
            String category, int size) {
        super(name, email, password, phones, state);
        this.socialReason = socialReason;
        this.category = category;
        this.size = size;
    }



    @Override
    public String getSocialReason() {
        return socialReason;
    }

    @Override
    public String getCategory() {
        return category;
    }


    @Override
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSocialReason(String socialReason) {
        this.socialReason = socialReason;
    }



    @Override
    public String toString() {
        return "Beneficiary [socialReason=" + socialReason + ", category="
                + category + ", size=" + size + "]";
    }

}
