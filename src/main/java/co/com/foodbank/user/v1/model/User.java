package co.com.foodbank.user.v1.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import co.com.foodbank.address.dto.interfaces.IAddress;
import co.com.foodbank.user.dto.interfaces.IUser;

/**
 * Abstract Class User to mapping an different users in the food bank.
 * 
 * @author mauricio.londono@gmail.com co.com.foodbank.user.model 14/05/2021
 */

@Document(collection = "User")
public class User implements IUser {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String phones;
    private boolean state = false;
    private IAddress address;



    /**
     * default constructor.
     */
    public User() {}

    /**
     * Constructor with parameters.
     * 
     * @param id
     * @param name
     * @param email
     * @param address
     * @param password
     * @param phones
     * @param state
     */
    public User(String name, String email, String password, String phones,
            boolean state) {
        super();
        this.name = name;
        this.email = email;
        // this.address = address;
        this.password = password;
        this.phones = phones;
        this.state = state;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public IAddress getAddress() {
        return address;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getPhones() {
        return phones;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isState() {
        return state;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(IAddress address) {
        this.address = address;
    }


    public void setPhones(String phones) {
        this.phones = phones;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setState(boolean state) {
        this.state = state;
    }



}
