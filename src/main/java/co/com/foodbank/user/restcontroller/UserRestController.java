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
import org.webjars.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import co.com.foodbank.contribution.state.ContributionData;
import co.com.foodbank.user.dto.BeneficiaryDTO;
import co.com.foodbank.user.dto.ProviderDTO;
import co.com.foodbank.user.dto.VolunterDTO;
import co.com.foodbank.user.exception.UserErrorException;
import co.com.foodbank.user.exception.UserNotFoundException;
import co.com.foodbank.user.interfaces.IBeneficiary;
import co.com.foodbank.user.interfaces.IProvider;
import co.com.foodbank.user.interfaces.IUser;
import co.com.foodbank.user.interfaces.IVolunter;
import co.com.foodbank.user.v1.controller.UserController;
import co.com.foodbank.user.v1.model.Beneficiary;
import co.com.foodbank.user.v1.model.Provider;
import co.com.foodbank.user.v1.model.Volunter;
import co.com.foodbank.validaton.ValidateEmail;
import co.com.foodbank.vault.dto.VaultDTO;
import co.com.foodbank.vault.sdk.exception.SDKVaultServiceException;
import co.com.foodbank.vault.sdk.exception.SDKVaultServiceIllegalArgumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * Class to handle all operations in User.
 * 
 * @author mauricio.londono@gmail.com co.com.foodbank.user.restcontroller
 *         14/05/2021
 */

@RestController
@RequestMapping(value = "/user")
@Tag(name = "User", description = "the User API")
@Validated
public class UserRestController {



    @Autowired
    public UserController controller;



    /**
     * Method to find Provider by sucursal.
     * 
     * @return {@code ResponseEntity<IUser>}
     */
    @Operation(summary = "Find Provider by Sucursal.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "User found.",
                            content = {
                                    @Content(mediaType = "application/json")}),
                    @ApiResponse(responseCode = "500",
                            description = "Service not available.",
                            content = @Content),
                    @ApiResponse(responseCode = "400",
                            description = "Bad request.", content = @Content)})
    @GetMapping(value = "/findBySucursal/{id-vault}")
    public ResponseEntity<IProvider> findBySucursal(
            @PathVariable("id-vault") @NotBlank @NotNull String id)
            throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(controller.findBySucursal(id));
    }



    /**
     * Method to findAll users.
     * 
     * @return {@code ResponseEntity<IUser>}
     */
    @Operation(summary = "Find all users.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "User found.",
                            content = {
                                    @Content(mediaType = "application/json")}),
                    @ApiResponse(responseCode = "500",
                            description = "Service not available.",
                            content = @Content),
                    @ApiResponse(responseCode = "400",
                            description = "Bad request.", content = @Content)})
    @GetMapping(value = "/findAll")
    public ResponseEntity<Collection<IUser>> findAllUsers()
            throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(controller.findAll());
    }


    /**
     * Method to find Volunter by Dni.
     * 
     * @return {@code Collection<IUser>}
     * @throws NotFoundException
     * @throws NumberFormatException
     */

    @Operation(summary = "Find user by dni.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "User found.",
                            content = {
                                    @Content(mediaType = "application/json")}),
                    @ApiResponse(responseCode = "500",
                            description = "Service not available.",
                            content = @Content),
                    @ApiResponse(responseCode = "400",
                            description = "Bad request.", content = @Content)})
    @GetMapping(value = "/findByDni/{dni}")
    public ResponseEntity<IUser> findByDni(@PathVariable("dni") @Pattern(
            regexp = "^[0-9]{8,8}$") @NotBlank @NotNull @Size(min = 8,
                    max = 8) String dni)
            throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(controller.findByDni(dni));
    }

    //

    /**
     * Method to find Provider by Cuit.
     * 
     * @return {@code Collection<IUser>}
     * @throws NotFoundException
     * @throws NumberFormatException
     */

    @Operation(summary = "Find user by cuit.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "User found.",
                            content = {
                                    @Content(mediaType = "application/json")}),
                    @ApiResponse(responseCode = "500",
                            description = "Service not available.",
                            content = @Content),
                    @ApiResponse(responseCode = "400",
                            description = "Bad request.", content = @Content)})
    @GetMapping(value = "/findByCuit/{cuit}")
    public ResponseEntity<IUser> findByCuit(
            @PathVariable("cuit") @Pattern(
                    regexp = "^[0-9]{12,12}$") @NotBlank @NotNull @Size(
                            min = 12, max = 12) String cuit)
            throws UserNotFoundException {

        return ResponseEntity.status(HttpStatus.OK)
                .body(controller.findByCuit(cuit));

    }



    /**
     * Method to find an user by email.
     * 
     * @param email
     * @return {@code ResponseEntity<IUser> }
     * @throws NotFoundException
     */
    @Operation(summary = "Find user by email.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "User found.",
                            content = {
                                    @Content(mediaType = "application/json")}),
                    @ApiResponse(responseCode = "500",
                            description = "Service not available.",
                            content = @Content),
                    @ApiResponse(responseCode = "400",
                            description = "Bad request.", content = @Content)})
    @GetMapping(value = "/findByEmail/{email}")
    public ResponseEntity<Collection<IUser>> findByEmail(
            @PathVariable("email") @Email @ValidateEmail @NotBlank @NotNull String email)
            throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(controller.findByEmail(email));
    }


    /*********************************************************************************************************************/
    /**
     * Method to create an Volunter.
     * 
     * @param user
     * @return {@code ResponseEntity<IVolunter>}
     */
    @Operation(summary = "Create  a Volunter", description = "",
            tags = {"Volunter"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201",
                            description = "Volunter created",
                            content = @Content(schema = @Schema(
                                    implementation = Volunter.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid input"),
                    @ApiResponse(responseCode = "409",
                            description = "Volunter already exists")})
    @PostMapping(value = "/createVolunter",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<IVolunter> createVolunter(
            @RequestBody @Valid VolunterDTO dto) throws UserNotFoundException {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(controller.createVolunter(dto));
    }

    /**
     * Update a VOlunter
     * 
     * @param dto
     * @param id
     * @return {@code ResponseEntity<IVolunter>}
     * @throws UserErrorException
     * @throws NotFoundException
     */
    @Operation(summary = "Update a Volunter base information", description = "",
            tags = {"Volunter"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201",
                            description = "Volunter updated",
                            content = @Content(schema = @Schema(
                                    implementation = Volunter.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid input"),
                    @ApiResponse(responseCode = "409",
                            description = "Volunter already exists")})
    @PutMapping(value = "/updateVolunter/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<IVolunter> updateVolunter(
            @RequestBody @Valid VolunterDTO dto,
            @PathVariable("id") @NotBlank @NotNull String id)
            throws UserNotFoundException, NotFoundException,
            UserErrorException {

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
     */
    @Operation(summary = "Create  a Provider", description = "",
            tags = {"Provider"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201",
                            description = "Provider created",
                            content = @Content(schema = @Schema(
                                    implementation = Provider.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid input"),
                    @ApiResponse(responseCode = "409",
                            description = "Provider already exists")})
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
     * @throws UserErrorException
     * @throws NotFoundException
     * @throws SDKVaultServiceIllegalArgumentException
     * @throws SDKVaultServiceException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    @Operation(summary = "Update a Provider base information ",
            description = "", tags = {"Provider"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201",
                            description = "Provider updated",
                            content = @Content(schema = @Schema(
                                    implementation = Provider.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid input"),
                    @ApiResponse(responseCode = "409",
                            description = "Provider already exists")})
    @PutMapping(value = "/updateProvider/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<IProvider> updateProvider(
            @RequestBody @Valid ProviderDTO dto,
            @PathVariable("id") @NotBlank @NotNull String id)
            throws UserNotFoundException, NotFoundException, UserErrorException,
            JsonMappingException, JsonProcessingException,
            SDKVaultServiceException, SDKVaultServiceIllegalArgumentException {

        return ResponseEntity.status(HttpStatus.OK)
                .body(controller.updateProvider(dto, id));
    }



    /**
     * Method to update Vault in a Provider Restricted Operation, only the API
     * REST Vault can update Vault in Provider.
     * 
     * @param dto
     * @param id
     * @return {@code ResponseEntity<IProvider>}
     * @throws UserErrorException
     * @throws NotFoundException
     * @throws SDKVaultServiceIllegalArgumentException
     * @throws SDKVaultServiceException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    @Operation(
            summary = "Update Vault in Provider, Restricted by spring security, only used by rest vault.",
            description = "Restricted by spring security, only used by rest vault.",
            tags = {"Provider"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201",
                            description = "Provider updated",
                            content = @Content(schema = @Schema(
                                    implementation = Provider.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid input"),
                    @ApiResponse(responseCode = "409",
                            description = "Provider already exists")})
    @PutMapping(value = "/updateVaultInProvider/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<IProvider> updateVaultProvider(
            @RequestBody @Valid VaultDTO dto,
            @PathVariable("id") @NotBlank @NotNull String id)
            throws UserNotFoundException, NotFoundException, UserErrorException,
            JsonMappingException, JsonProcessingException,
            SDKVaultServiceException, SDKVaultServiceIllegalArgumentException {

        return ResponseEntity.status(HttpStatus.OK)
                .body(controller.updateVaultProvider(dto, id));
    }



    /**
     * Method to ADD Vault in Provider
     * 
     * @param dto
     * @param id
     * @return {@code ResponseEntity<IProvider>}
     * @throws UserErrorException
     * @throws NotFoundException
     * @throws SDKVaultServiceIllegalArgumentException
     * @throws SDKVaultServiceException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    @Operation(summary = "Add Vault in Provider ",
            description = "add new Vault.", tags = {"Provider"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201",
                            description = "Provider updated",
                            content = @Content(schema = @Schema(
                                    implementation = Provider.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid input"),
                    @ApiResponse(responseCode = "409",
                            description = "Provider already exists")})
    @PostMapping(value = "/createVaultInProvider/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<IProvider> addVaultInProvider(
            @RequestBody @Valid VaultDTO vaultDto,
            @PathVariable("id") @NotBlank @NotNull String idProvider)
            throws UserNotFoundException, NotFoundException, UserErrorException,
            JsonMappingException, JsonProcessingException,
            SDKVaultServiceException, SDKVaultServiceIllegalArgumentException {

        return ResponseEntity.status(HttpStatus.OK)
                .body(controller.addVaultInProvider(vaultDto, idProvider));
    }



    /*********************************************************************************************************************/
    /**
     * Method to Create a Beneficiary.
     * 
     * @param dto
     * @return {@code ResponseEntity<IBeneficiary>}
     */
    @Operation(summary = "Create  a Beneficiary", description = "",
            tags = {"Beneficiary"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Beneficiary created",
                    content = @Content(schema = @Schema(
                            implementation = Beneficiary.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409",
                    description = "Beneficiary already exists")})
    @PostMapping(value = "/createBeneficiary",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<IBeneficiary> createBeneficiary(
            @RequestBody @Valid BeneficiaryDTO dto)
            throws UserNotFoundException {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(controller.createBeneficiary(dto));
    }



    /**
     * Method to update a Beneficiary.
     * 
     * @param dto
     * @param id
     * @return {@code ResponseEntity<IBeneficiary>}
     * @throws UserErrorException
     * @throws NotFoundException
     */
    @Operation(summary = "Update  a Beneficiary base information",
            description = "", tags = {"Beneficiary"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Beneficiary updated",
                    content = @Content(schema = @Schema(
                            implementation = Beneficiary.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409",
                    description = "Beneficiary already exists")})

    @PutMapping(value = "/updateBeneficiary/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<IBeneficiary> updateBeneficiary(
            @RequestBody @Valid BeneficiaryDTO dto,
            @PathVariable("id") @NotBlank @NotNull String id)
            throws UserNotFoundException, NotFoundException,
            UserErrorException {

        return ResponseEntity.status(HttpStatus.OK)
                .body(controller.updateBeneficiary(dto, id));
    }


    /*********************************************************************/
    /**
     * Method to find users by id.
     * 
     * @param _id
     * @return {@code ResponseEntity<IUser>}
     * @throws UserNotFoundException
     */
    @Operation(summary = "Find user by Id.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "User found.",
                            content = {
                                    @Content(mediaType = "application/json")}),
                    @ApiResponse(responseCode = "500",
                            description = "Service not available.",
                            content = @Content),
                    @ApiResponse(responseCode = "400",
                            description = "Bad request.", content = @Content)})
    @GetMapping(value = "/findById/{id}")
    public ResponseEntity<IUser> findById(
            @PathVariable("id") @NotBlank @NotNull String _id)
            throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(controller.findById(_id));
    }



    /*****************************************************************************************/
    /**
     * Method to update contribution in provider vault
     * 
     * @param data
     * @param idVault
     * @return ResponseEntity<IProvider>
     * @throws UserNotFoundException
     */
    @Operation(
            summary = "Update Contributions in Beneficiary, Restricted by spring security, only used by rest vault. ",
            description = "Restricted by spring security, only used by rest vault.",
            tags = {"Beneficiary"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Beneficiary created",
                    content = @Content(schema = @Schema(
                            implementation = Beneficiary.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409",
                    description = "Beneficiary already exists")})
    @PutMapping(value = "/updateContribution/{idVault}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<IProvider> updateContribution(
            @RequestBody @Valid ContributionData data,
            @PathVariable("idVault") @NotBlank @NotNull String idVault)
            throws UserNotFoundException {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(controller.updateContribution(data, idVault));
    }


}
