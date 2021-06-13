package co.com.foodbank.user.restcontroller;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import co.com.foodbank.user.dto.BeneficiaryDTO;
import co.com.foodbank.user.dto.ProviderDTO;
import co.com.foodbank.user.dto.VolunterDTO;
import co.com.foodbank.user.exception.ContributionNotFoundException;
import co.com.foodbank.user.model.IBeneficiary;
import co.com.foodbank.user.model.IProvider;
import co.com.foodbank.user.model.IUser;
import co.com.foodbank.user.model.IVolunter;
import co.com.foodbank.user.v1.controller.UserController;
import co.com.foodbank.validaton.ValidateEmail;
import co.com.foodbank.vault.sdk.exception.SDKVaultServiceException;
import co.com.foodbank.vault.sdk.exception.SDKVaultServiceIllegalArgumentException;


/**
 * Class to handle all operations in User.
 * 
 * @author mauricio.londono@gmail.com co.com.foodbank.user.restcontroller
 *         14/05/2021
 */

@RestController
@RequestMapping(value = "/user")
@Validated
public class UserRestController {



    @Autowired
    public UserController controller;



    /**
     * Method to findAll users.
     * 
     * @return {@code ResponseEntity<IUser>}
     */
    @GetMapping(value = "/findAll")
    public Collection<IUser> findAllUsers() {
        return controller.findAll();
    }


    /**
     * Method to find Volunter by Dni.
     * 
     * @return {@code Collection<IUser>}
     * @throws NotFoundException
     * @throws NumberFormatException
     */
    @GetMapping(value = "/findDni/{dni}")
    public IUser findByDni(@PathVariable("dni") @Pattern(
            regexp = "^[0-9]{8,8}$") @NotBlank @NotNull @Size(min = 8,
                    max = 8) String dni)
            throws ContributionNotFoundException {
        return controller.findByDni(dni);
    }

    //

    /**
     * Method to find Provider by Cuit.
     * 
     * @return {@code Collection<IUser>}
     * @throws NotFoundException
     * @throws NumberFormatException
     */
    @GetMapping(value = "/findCuit/{cuit}")
    public IUser findByCuit(
            @PathVariable("cuit") @Pattern(
                    regexp = "^[0-9]{12,12}$") @NotBlank @NotNull @Size(
                            min = 12, max = 12) String cuit)
            throws ContributionNotFoundException {

        return controller.findByCuit(cuit);

    }



    /**
     * Method to find an user by email.
     * 
     * @param email
     * @return {@code ResponseEntity<IUser> }
     * @throws NotFoundException
     */
    @GetMapping(value = "/findByEmail/{email}")
    public Collection<IUser> findByEmail(
            @PathVariable("email") @Email @ValidateEmail @NotBlank @NotNull String email)
            throws ContributionNotFoundException {
        return controller.findByEmail(email);
    }


    /*********************************************************************************************************************/
    /**
     * Method to create an Volunter.
     * 
     * @param user
     * @return {@code ResponseEntity<IVolunter>}
     */
    @PostMapping(value = "/createVolunter",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<IVolunter> createVolunter(
            @RequestBody @Valid VolunterDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(controller.createVolunter(dto));
    }

    /**
     * Update a VOlunter
     * 
     * @param dto
     * @param id
     * @return {@code ResponseEntity<IVolunter>}
     */
    @PutMapping(value = "/updateVolunter/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<IVolunter> updateVolunter(
            @RequestBody @Valid VolunterDTO dto,
            @PathVariable("id") @NotBlank @NotNull String id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(controller.updateVolunter(dto, id));
    }



    /*********************************************************************************************************************/
    /**
     * Method to Create a Provider.
     * 
     * @param cuit
     * @param legalRpp
     * @param dto
     * @return {@code ResponseEntity<IProvider> }
     * @throws SDKVaultServiceIllegalArgumentException
     * @throws SDKVaultServiceException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    @PostMapping(value = "/createProvider",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<IProvider> createProvider(
            @RequestBody @Valid ProviderDTO dto)
            throws JsonMappingException, JsonProcessingException,
            SDKVaultServiceException, SDKVaultServiceIllegalArgumentException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(controller.createProvider(dto));
    }



    /**
     * Method to update a Provider
     * 
     * @param dto
     * @param id
     * @return {@code ResponseEntity<IProvider>}
     */
    @PutMapping(value = "/updateProvider/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<IProvider> updateProvider(
            @RequestBody @Valid ProviderDTO dto,
            @PathVariable("id") @NotBlank @NotNull String id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(controller.updateProvider(dto, id));
    }



    /*********************************************************************************************************************/
    /**
     * Method to Create a Beneficiary.
     * 
     * @param dto
     * @return {@code ResponseEntity<IBeneficiary>}
     */
    @PostMapping(value = "/createBeneficiary",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<IBeneficiary> createBeneficiary(
            @RequestBody @Valid BeneficiaryDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(controller.createBeneficiary(dto));
    }



    /**
     * Method to update a Beneficiary.
     * 
     * @param dto
     * @param id
     * @return {@code ResponseEntity<IBeneficiary>}
     */
    @PutMapping(value = "/updateBeneficiary/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<IBeneficiary> updateBeneficiary(
            @RequestBody @Valid BeneficiaryDTO dto,
            @PathVariable("id") @NotBlank @NotNull String id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(controller.updateBeneficiary(dto, id));
    }



}
