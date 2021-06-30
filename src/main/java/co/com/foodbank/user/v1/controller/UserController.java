package co.com.foodbank.user.v1.controller;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.webjars.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import co.com.foodbank.user.dto.BeneficiaryDTO;
import co.com.foodbank.user.dto.ProviderDTO;
import co.com.foodbank.user.dto.VolunterDTO;
import co.com.foodbank.user.exception.UserErrorException;
import co.com.foodbank.user.exception.UserNotFoundException;
import co.com.foodbank.user.model.IBeneficiary;
import co.com.foodbank.user.model.IProvider;
import co.com.foodbank.user.model.IUser;
import co.com.foodbank.user.model.IVolunter;
import co.com.foodbank.user.service.UserService;
import co.com.foodbank.vault.sdk.exception.SDKVaultServiceException;
import co.com.foodbank.vault.sdk.exception.SDKVaultServiceIllegalArgumentException;

/**
 * Class to handle all operations in User.
 * 
 * @author mauricio.londono@gmail.com co.com.foodbank.user.v1.controller
 *         14/05/2021
 */
@Controller
public class UserController {


    @Autowired
    public UserService service;

    @Autowired
    public ModelMapper modelMapper;



    /**
     * Method to find all users
     * 
     * @return {@code Collection<IUser>}
     */
    public Collection<IUser> findAll() throws UserNotFoundException {
        return service.findAll();
    }

    /**
     * Method to search User by email.
     * 
     * @param email
     * @return {@code Collection<IUser>}
     * @throws NotFoundException
     */
    public Collection<IUser> findByEmail(String email)
            throws UserNotFoundException {
        return service.findByEmail(email);
    }

    /**
     * Method to find a provider by Cuit.
     * 
     * @param cuit
     * @return {@code Collection<IProvider>}
     * @throws NotFoundException
     * @throws NumberFormatException
     */
    public IUser findByCuit(String cuit)
            throws NumberFormatException, UserNotFoundException {
        return service.findByCuit(cuit);
    }

    /**
     * Method to search Volunter by dni.
     * 
     * @param dni
     * @return {@code Collection<IVolunter>}
     * @throws NotFoundException
     * @throws NumberFormatException
     * 
     */
    public IUser findByDni(String dni) throws UserNotFoundException {
        return service.findByDni(dni);
    }


    /**
     * @param user
     * @return
     */
    public IBeneficiary create(IBeneficiary user) {
        return service.create(user);
    }


    /**
     * @param dto
     * @return
     */
    public IVolunter createVolunter(@Valid VolunterDTO dto)
            throws UserNotFoundException {
        return modelMapper.map(service.createVolunter(dto), IVolunter.class);
    }

    /**
     * Method to create a Provider.
     * 
     * @param dto
     * @return {@code IProvider}
     */
    public IProvider createProvider(@Valid ProviderDTO dto)
            throws JsonMappingException, JsonProcessingException,
            SDKVaultServiceException, SDKVaultServiceIllegalArgumentException {
        return modelMapper.map(service.createProvider(dto), IProvider.class);
    }



    /**
     * Method to create Beneficiary.
     * 
     * @param dto
     * @return {@code IBeneficiary}
     */
    public IBeneficiary createBeneficiary(@Valid BeneficiaryDTO dto)
            throws UserNotFoundException {

        return modelMapper.map(service.createBeneficiary(dto),
                IBeneficiary.class);
    }



    /**
     * Method to update a beneficiary.
     * 
     * @param dto
     * @param sreason
     * @param category
     * @param size
     * @return {@code IBeneficiary}
     * @throws UserErrorException
     */
    public IBeneficiary updateBeneficiary(@Valid BeneficiaryDTO dto, String _id)
            throws NotFoundException, UserNotFoundException,
            UserErrorException {
        return modelMapper.map(service.updateBeneficiary(dto, _id),
                IBeneficiary.class);
    }



    /**
     * Method to update a Provider.
     * 
     * @param dto
     * @param _id
     * @return {@code IProvider }
     * @throws UserErrorException
     * @throws NotFoundException
     * @throws SDKVaultServiceIllegalArgumentException
     * @throws SDKVaultServiceException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    public IProvider updateProvider(@Valid ProviderDTO dto, String _id)
            throws UserNotFoundException, NotFoundException, UserErrorException,
            JsonMappingException, JsonProcessingException,
            SDKVaultServiceException, SDKVaultServiceIllegalArgumentException {
        return modelMapper.map(service.updateprovider(dto, _id),
                IProvider.class);
    }

    /**
     * Methodto update a Voluntear
     * 
     * @param dto
     * @param id
     * @return {@code IVolunter}
     * @throws UserErrorException
     * @throws NotFoundException
     */
    public IVolunter updateVolunter(@Valid VolunterDTO dto, String _id)
            throws UserNotFoundException, NotFoundException,
            UserErrorException {
        return modelMapper.map(service.updateVolunter(dto, _id),
                IVolunter.class);
    }

    /**
     * Find User by Id.
     * 
     * @param _id
     * @return {@code IUser}
     */
    public IUser findById(@NotBlank @NotNull String _id)
            throws UserNotFoundException {
        return service.findById(_id);
    }



}
