package co.com.foodbank.user.v1.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import co.com.foodbank.address.dto.IAddress;
import co.com.foodbank.user.interfaces.IVolunter;
import co.com.foodbank.vehicule.dto.IVehicule;


/**
 * @author mauricio.londono@gmail.com co.com.foodbank.user.v1.model 15/05/2021
 */
@Document(collection = "User")
@TypeAlias("Volunter")
public class Volunter extends User implements IVolunter {

    @Indexed(unique = true)
    public Long dni;
    public IVehicule vehicule;


    /**
     * default constructor.
     */
    public Volunter() {

    }


    /**
     * Constructor with parameters
     * 
     * @param name
     * @param email
     * @param address
     * @param password
     * @param phones
     * @param state
     * @param dni
     * @param vehicule
     */
    public Volunter(String name, String email, IAddress address,
            String password, String phones, boolean state, Long dni,
            IVehicule vehicule) {
        super(name, email, password, phones, state);
        this.dni = dni;
        // this.vehicule = vehicule;
    }

    @Override
    public Long getDni() {
        return dni;
    }

    @Override
    public IVehicule getVehicule() {
        return vehicule;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    @Override
    public String toString() {
        return "Volunter [dni=" + dni + "]";
    }

    public void setVehicule(IVehicule vehicule) {
        this.vehicule = vehicule;
    }


}
