package co.com.foodbank.user.v1.model;

import java.util.Collection;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import co.com.foodbank.address.dto.IAddress;
import co.com.foodbank.user.interfaces.IProvider;
import co.com.foodbank.vault.dto.IVault;

/**
 * @author mauricio.londono@gmail.com co.com.foodbank.user.v1.model 15/05/2021
 */
@Document(collection = "User")
@TypeAlias("Provider")
public class Provider extends User implements IProvider {

    @Indexed(unique = true)
    public Long cuil;
    public String legalRepresentation;
    public Collection<IVault> sucursal; // podria ser un builder

    /**
     * Default constructor
     */
    public Provider() {

    }


    /**
     * Constructor with all parameters
     * 
     * @param name
     * @param email
     * @param address
     * @param password
     * @param phones
     * @param state
     * @param cuil
     * @param legalRepresentation
     * @param sucursal
     */
    public Provider(String name, String email, IAddress address,
            String password, String phones, boolean state, Long cuil,
            String legalRepresentation, Collection<IVault> sucursal) {
        super(name, email, password, phones, state);
        this.cuil = cuil;
        this.legalRepresentation = legalRepresentation;
        this.sucursal = sucursal;
    }



    @Override
    public Long getCuil() {
        return cuil;
    }

    public void setCuil(Long cuil) {
        this.cuil = cuil;
    }

    @Override
    public String getLegalRepresentation() {
        return legalRepresentation;
    }

    public void setLegalRepresentation(String legalRepresentation) {
        this.legalRepresentation = legalRepresentation;
    }

    @Override
    public Collection<IVault> getSucursal() {
        return sucursal;
    }

    public void setSucursal(Collection<IVault> sucursal) {
        this.sucursal = sucursal;
    }

    @Override
    public String toString() {
        return "Provider [cuil=" + cuil + ", legalRepresentation="
                + legalRepresentation + ", sucursal=" + sucursal + "]";
    }



}
