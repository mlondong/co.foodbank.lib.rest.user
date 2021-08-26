package co.com.foodbank.user.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import co.com.foodbank.address.dto.Address;
import co.com.foodbank.address.dto.AddressDTO;
import co.com.foodbank.contribution.dto.interfaces.IContribution;
import co.com.foodbank.contribution.dto.response.DetailContributionData;
import co.com.foodbank.contribution.dto.response.GeneralContributionData;
import co.com.foodbank.contribution.state.ContributionData;
import co.com.foodbank.contribution.state.Pending;
import co.com.foodbank.country.dto.Country;
import co.com.foodbank.country.dto.CountryDTO;
import co.com.foodbank.user.dto.BeneficiaryDTO;
import co.com.foodbank.user.dto.ProviderDTO;
import co.com.foodbank.user.dto.VolunterDTO;
import co.com.foodbank.user.dto.interfaces.IBeneficiary;
import co.com.foodbank.user.dto.interfaces.IProvider;
import co.com.foodbank.user.dto.interfaces.IUser;
import co.com.foodbank.user.dto.interfaces.IVolunter;
import co.com.foodbank.user.dto.request.RequestBeneficiaryData;
import co.com.foodbank.user.dto.request.RequestUserData;
import co.com.foodbank.user.dto.request.RequestVolunterData;
import co.com.foodbank.user.exception.UserErrorException;
import co.com.foodbank.user.exception.UserNotFoundException;
import co.com.foodbank.user.repository.BeneficiaryRepository;
import co.com.foodbank.user.repository.ProviderRepository;
import co.com.foodbank.user.repository.UserRepository;
import co.com.foodbank.user.repository.VolunterRepository;
import co.com.foodbank.user.util.ParametersUser;
import co.com.foodbank.user.v1.model.Beneficiary;
import co.com.foodbank.user.v1.model.Provider;
import co.com.foodbank.user.v1.model.User;
import co.com.foodbank.user.v1.model.Volunter;
import co.com.foodbank.vault.dto.VaultDTO;
import co.com.foodbank.vault.dto.interfaces.IVault;
import co.com.foodbank.vault.sdk.exception.SDKVaultServiceException;
import co.com.foodbank.vault.sdk.exception.SDKVaultServiceIllegalArgumentException;
import co.com.foodbank.vault.sdk.model.ResponseVaultData;
import co.com.foodbank.vault.sdk.service.SDKVaultService;
import co.com.foodbank.vault.v1.model.Vault;
import co.com.foodbank.vehicule.dto.Vehicule;
import co.com.foodbank.vehicule.dto.VehiculeDTO;
import co.com.foodbank.vehicule.dto.Volume;
import co.com.foodbank.vehicule.dto.VolumeDTO;

/**
 * @author mauricio.londono@gmail.com co.com.foodbank.user.service 14/05/2021
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private VolunterRepository volunterRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    @Qualifier("sdkVaultService")
    private SDKVaultService sdkVaultService;



    /**
     * Method to list all users.
     * 
     * @return {@code Collection<IUser> }
     */
    public Collection<IUser> findAll() throws UserNotFoundException {

        return userRepository.findAll().stream()
                .map(d -> modelMapper.map(d, IUser.class))
                .collect(Collectors.toList());

    }


    /**
     * Method to find Providers by cuit.
     * 
     * @param cuit
     * @return {@code Collection<IProvider>}
     * @throws org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
     * @throws org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
     * @throws org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
     * @throws NumberFormatException
     */
    public IUser findByCuit(String cuit) throws UserNotFoundException {

        User data = userRepository.finByCuit(Long.valueOf(cuit));
        if (Objects.isNull(data)) {
            throw new UserNotFoundException(cuit);
        }
        return modelMapper.map(data, IUser.class);
    }

    /**
     * Method to find Users by email.
     * 
     * @param email
     * @return {@code Collection<IUser>}
     * @throws org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
     */
    public Collection<IUser> findByEmail(String email)
            throws UserNotFoundException {

        Collection<User> data = userRepository.findByEmail(email);

        if (data.isEmpty()) {
            throw new UserNotFoundException(email);
        }
        return data.stream().map(d -> modelMapper.map(d, IUser.class))
                .collect(Collectors.toList());
    }

    /**
     * Method to find Volunter By Dni.
     * 
     * @param dni
     * @return {@code IUser}
     * @throws UserNotFoundException
     */
    public IUser findByDni(String dni) throws UserNotFoundException {

        User data = userRepository.finByDni(Long.valueOf(dni));

        if (Objects.isNull(data)) {
            throw new UserNotFoundException(dni);
        }
        return modelMapper.map(data, IUser.class);
    }


    /**
     * Method to create a Beneficiary.
     * 
     * @param user
     * @return {@code Beneficiary}
     */
    public Beneficiary create(IBeneficiary user) {
        return beneficiaryRepository
                .save(modelMapper.map(user, Beneficiary.class));
    }


    /**
     * @param dto
     * @return {@code Volunter}
     */
    public Volunter createVolunter(@Valid VolunterDTO dto)
            throws UserNotFoundException {
        return volunterRepository.save(this.setVolunter(dto));
    }


    /**
     * Method to prepare the object Volunter.
     * 
     * @param dto
     * @param dni
     * @return {@code Volunter}
     */
    private Volunter setVolunter(@Valid VolunterDTO dto) {

        Vehicule vehicule = setVehicule(dto);
        Address address = setAddress(dto.getAddress());

        Volunter volunter = modelMapper.map(dto, Volunter.class);
        volunter.setVehicule(vehicule);
        volunter.setAddress(address);
        volunter.setState(true);
        return volunter;
    }

    /**
     * Build Volunter
     * 
     * @param dto
     * @param query
     * @return {@code Volunter}
     */
    private Volunter buildVolunter(VolunterDTO dto, Volunter query) {
        Address address = setAddress(dto.getAddress());

        Volunter volunter = query;
        volunter.setAddress(address);
        volunter.setEmail(dto.getEmail());
        volunter.setName(dto.getName());
        volunter.setPassword(dto.getPassword());
        volunter.setPhones(dto.getPhones());
        volunter.setDni(Long.valueOf(dto.getDni()));
        return volunter;
    }


    private Address setAddress(AddressDTO dtoAddress) {
        Address dataDaddress = new Address();

        if (!checkIsNullAddress(dtoAddress)) {
            if (!checkIsNullAttAddress(dtoAddress)) {
                dataDaddress = modelMapper.map(dtoAddress, Address.class);
                if (!checkIsNullCountry(dtoAddress.getCountry())) {
                    dataDaddress.setCountry(modelMapper
                            .map(dtoAddress.getCountry(), Country.class));
                }
            }
        }
        return dataDaddress;
    }



    private boolean checkIsNullCountry(CountryDTO country) {
        return Objects.isNull(country);
    }

    private boolean checkIsNullAddress(AddressDTO dto) {
        return Objects.isNull(dto);
    }

    private boolean checkIsNullAttAddress(AddressDTO dto) {
        return Stream.of(dto).allMatch(Objects::isNull);
    }


    /**
     * Method to evaluate aVehicule for Volunter.
     * 
     * @param dto
     * @return {@code Vehicule}
     */
    private Vehicule setVehicule(VolunterDTO dto) {
        Vehicule dataVehicule = new Vehicule();
        if (!checkIsNullVehicule(dto.getVehicule())) {
            if (!checkIsNullAttrbInVehicule(dto.getVehicule())) {
                dataVehicule =
                        modelMapper.map(dto.getVehicule(), Vehicule.class);
                if (!checkIsNullVolume(dto.getVehicule().getVolume())) {
                    dataVehicule.setVolume(modelMapper
                            .map(dto.getVehicule().getVolume(), Volume.class));
                }
            }
        }
        return dataVehicule;
    }

    private boolean checkIsNullVolume(VolumeDTO volume) {
        return Objects.isNull(volume);
    }

    private boolean checkIsNullVehicule(VehiculeDTO dto) {
        return Objects.isNull(dto);
    }

    private boolean checkIsNullAttrbInVehicule(VehiculeDTO dto) {
        return Stream.of(dto).allMatch(Objects::isNull);
    }


    /****************************************************************************************************************/

    /**
     * Method to create a Provider.
     * 
     * @param dto
     * @return {@code Provider}
     */
    public Provider createProvider(ProviderDTO dto)
            throws JsonMappingException, JsonProcessingException,
            SDKVaultServiceException, SDKVaultServiceIllegalArgumentException {
        return providerRepository.save(this.setProvider(dto));
    }


    /**
     * Method to build a Provider.
     * 
     * @param dto
     * @param cuit
     * @param legalRpp
     * @return {@code Provider}
     */
    private Provider setProvider(ProviderDTO providerDto)
            throws JsonMappingException, JsonProcessingException,
            SDKVaultServiceException, SDKVaultServiceIllegalArgumentException {

        Provider provider = new Provider();
        provider = initProvider(providerDto, provider);
        provider.setState(true);
        provider.setSucursal(new ArrayList<IVault>());
        return provider;
    }


    private Provider initProvider(ProviderDTO providerDto, Provider provider) {
        Address address = setAddress(providerDto.getAddress());
        provider.setAddress(address);
        provider.setCuil(Long.valueOf(providerDto.getCuil()));
        provider.setEmail(providerDto.getEmail());
        provider.setLegalRepresentation(providerDto.getLegalRepresentation());
        provider.setName(providerDto.getName());
        provider.setPassword(providerDto.getPassword());
        provider.setPhones(providerDto.getPhones());
        return provider;
    }


    /****************************************************************************************************************/


    /**
     * Method to create a Beneficiary.
     * 
     * @param dto
     * @return {@code IBeneficiary}
     */
    public Beneficiary createBeneficiary(BeneficiaryDTO dto)
            throws UserNotFoundException {
        return beneficiaryRepository.save(setBeneficiary(dto));
    }


    /**
     * Set all values in Beneficiary.
     * 
     * @param dto
     * @param sreason
     * @param category
     * @param size
     * @return {@code Beneficiary}
     */
    private Beneficiary setBeneficiary(BeneficiaryDTO dto) {

        Beneficiary beneficiary = modelMapper.map(dto, Beneficiary.class);
        Address address = setAddress(dto.getAddress());
        beneficiary.setAddress(address);
        beneficiary.setState(true);

        return beneficiary;
    }


    /**
     * Method to update a beneficiary
     * 
     * @param dto
     * @param sreason
     * @param category
     * @param size
     * @return {@code IBeneficiary}
     * @throws UserErrorException
     */
    public IBeneficiary updateBeneficiary(BeneficiaryDTO dto, String _id)
            throws NotFoundException, UserNotFoundException,
            UserErrorException {

        User dataDB = findById(_id);
        if (!checkInstansOfBeneficiary(dataDB)) {
            String err = _id + ParametersUser.MSG_ERROR
                    + ParametersUser.MSG_BENEFICIARY;
            throw new UserErrorException(err);
        }
        return beneficiaryRepository
                .save(buildBeneficiary(dto, (Beneficiary) dataDB));

    }


    private boolean checkInstansOfBeneficiary(User dataDB) {
        return (dataDB instanceof Beneficiary) ? true : false;
    }


    /**
     * @param dto
     * @param query
     * @return {@code Beneficiary}
     */
    private Beneficiary buildBeneficiary(BeneficiaryDTO dto,
            Beneficiary query) {
        Beneficiary beneficiary = query;
        Address address = setAddress(dto.getAddress());
        beneficiary.setAddress(address);
        beneficiary.setCategory(dto.getCategory());
        beneficiary.setEmail(dto.getEmail());
        beneficiary.setName(dto.getName());
        beneficiary.setPassword(dto.getPassword());
        beneficiary.setPhones(dto.getPhones());
        beneficiary.setSize(Integer.valueOf(dto.getSize()));
        beneficiary.setSocialReason(dto.getSocialReason());
        return beneficiary;
    }


    /*******************************************************************************************************************/
    /**
     * Method to update provider adding vault
     * 
     * @param dto
     * @param _id
     * @return {@code IProvider}
     * @throws NotFoundException
     * @throws UserErrorException
     * @throws SDKVaultServiceIllegalArgumentException
     * @throws SDKVaultServiceException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    public IProvider updateprovider(ProviderDTO dto, String _id)
            throws NotFoundException, UserErrorException, JsonMappingException,
            JsonProcessingException, SDKVaultServiceException,
            SDKVaultServiceIllegalArgumentException {

        User dataDB = findById(_id);
        if (!checkInstansOfProvider(dataDB)) {
            String err = _id + ParametersUser.MSG_ERROR
                    + ParametersUser.MSG_PROVIDER;
            throw new UserErrorException(err);
        }

        return providerRepository.save(buildProvider(dto, (Provider) dataDB));

    }


    private boolean checkInstansOfProvider(User dataDB) {
        return (dataDB instanceof Provider) ? true : false;
    }


    /**
     * Method to build Provider and add new Vault.
     * 
     * @param data
     * @param query
     * @return {@code Provider}
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws SDKVaultServiceException
     * @throws SDKVaultServiceIllegalArgumentException
     */
    private Provider buildProvider(ProviderDTO dto, Provider query)
            throws JsonMappingException, JsonProcessingException,
            SDKVaultServiceException, SDKVaultServiceIllegalArgumentException {

        Provider provider = query;
        provider = initProvider(dto, provider);
        // provider.getSucursal().addAll(createVault(dto));
        return provider;

    }

    /*******************************************************************************************************************/



    /**
     * Update a Volunter
     * 
     * @param dto
     * @param _id
     * @return
     * @throws NotFoundException
     * @throws UserErrorException
     */
    public IVolunter updateVolunter(VolunterDTO dto, String _id)
            throws NotFoundException, UserErrorException {

        User dataDB = findById(_id);
        if (!checkInstansOfVolunter(dataDB)) {
            String err = _id + ParametersUser.MSG_ERROR
                    + ParametersUser.MSG_VOLUNTER;
            throw new UserErrorException(err);
        }
        return volunterRepository.save(buildVolunter(dto, (Volunter) dataDB));
    }


    private boolean checkInstansOfVolunter(User dataDB) {
        return (dataDB instanceof Volunter) ? true : false;
    }



    /**
     * Find User by Id.
     * 
     * @param _id
     * @return {@code IUser}
     */
    public User findById(String _id) throws UserNotFoundException {
        return userRepository.findById(_id)
                .orElseThrow(() -> new UserNotFoundException(_id));
    }


    /**
     * Method to add vault in provider.
     * 
     * @param vaultDto
     * @param id
     * @return {@code IProvider}
     * @throws SDKVaultServiceIllegalArgumentException
     * @throws SDKVaultServiceException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws UserErrorException
     */
    public IProvider addVaultInProvider(VaultDTO vaultDto, String idProvider)
            throws JsonMappingException, JsonProcessingException,
            SDKVaultServiceException, SDKVaultServiceIllegalArgumentException,
            UserErrorException {

        User responseP = this.findById(idProvider);

        if (!checkInstansOfProvider(responseP)) {
            String err = idProvider + ParametersUser.MSG_ERROR
                    + ParametersUser.MSG_PROVIDER;
            throw new UserErrorException(err);
        }

        ResponseVaultData responseV = sdkVaultService.create(vaultDto);

        Provider data = modelMapper.map(responseP, Provider.class);
        data.getSucursal().add(modelMapper.map(responseV, Vault.class));

        return modelMapper.map(providerRepository.save(data), IProvider.class);
    }



    /**
     * Method to find provider by sucursal.
     * 
     * @param id
     * @return {@code IProvider}
     */
    public IProvider findBySucursal(String id) throws UserNotFoundException {

        Provider result = providerRepository.findBySucursal(id);

        if (Objects.isNull(result)) {
            throw new UserNotFoundException(id);
        }

        return modelMapper.map(result, IProvider.class);
    }


    /**
     * Method to update vault in provider.
     * 
     * @param sucursal
     * @param id
     * @return {@code IProvider}
     * @throws UserErrorException
     * @throws SDKVaultServiceIllegalArgumentException
     * @throws SDKVaultServiceException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    public IProvider updateVaultProvider(VaultDTO dto, String _id)
            throws UserErrorException, NotFoundException, JsonMappingException,
            JsonProcessingException, SDKVaultServiceException,
            SDKVaultServiceIllegalArgumentException {

        String err = error(_id);

        IProvider result = findBySucursal(_id);

        IVault vault = findVaultInProvider(_id, err, result);

        Vault updated = modelMapper.map(vault, Vault.class);
        updated.setAddress(modelMapper.map(dto.getAddress(), Address.class));
        updated.setContact(dto.getContact());
        updated.setPhones(dto.getPhones());

        return providerRepository.save((Provider) result);

    }


    private String error(String id) {
        return id + ParametersUser.MSG_NOT_FOUND + ParametersUser.VAULT;
    }


    /**
     * Method to update contribution in provider. Restricted Method Only the API
     * Rest Vault can update Contribution in User througth operation
     * http://localhost:8081/vault/add-GeneralContribution/vault-id/
     * http://localhost:8081/vault/add-DetailContribution/vault-id/
     * 
     * @param data
     * @param idVault
     * @return {@code IProvider}
     */
    public IProvider updateContribution(ContributionData data, String idVault,
            String idContribution) {

        String err = error(idVault);

        /** CHECK VAULT IN PROVIDER */
        IProvider resultProvider = findBySucursal(idVault);

        /** FIND VAULT IN USER */
        IVault resultVault = findVaultInProvider(idVault, err, resultProvider);

        /** SET THE STATE PENDING */
        Pending pending = new Pending();
        pending.pending(data);

        /** ADD CONTRIBUTION IN VAULT PROVIDER */
        resultVault.getContribution()
                .add(checkTypeOfContribution(data, idVault, idContribution));

        return providerRepository.save((Provider) resultProvider);
    }


    /**
     * Method to identify the Contribution type.
     * 
     * @param data
     * @param idVault
     * @return {@code IContribution}
     */
    private IContribution checkTypeOfContribution(ContributionData data,
            String idVault, String idContribution) {


        /** CONVERTO TO ICONTRIBUTION */
        GeneralContributionData general =
                modelMapper.map(data, GeneralContributionData.class);
        general.setId(idContribution);

        DetailContributionData detail =
                modelMapper.map(data, DetailContributionData.class);
        detail.setId(idContribution);

        return validate(general, detail);
    }



    /**
     * Check the type of Contribution.
     * 
     * @param general
     * @param detail
     * @return {@code IContribution }
     */
    private IContribution validate(GeneralContributionData general,
            DetailContributionData detail) {

        return (Objects.isNull(general.getVolume())) ? detail : general;
    }


    /**
     * Method to find vault in provider.
     * 
     * @param idVault
     * @param err
     * @param resultProvider
     * @return {@code IVault}
     */
    private IVault findVaultInProvider(String idVault, String err,
            IProvider resultProvider) {
        return resultProvider.getSucursal().stream()
                .filter(d -> d.getId().equals(idVault)).findFirst()
                .orElseThrow(() -> new NotFoundException(err));
    }


    /**
     * Method to find by User.
     * 
     * @param user
     * @return {@code IUser}
     */
    public IUser findByUser(RequestUserData user) throws UserNotFoundException {

        User result = userRepository.findByUser(user.getName(), user.getEmail(),
                user.getPhones());

        if (Objects.isNull(result)) {
            throw new UserNotFoundException(user.toString());
        }
        return modelMapper.map(result, IUser.class);
    }

    /**
     * Method to find Beneficiary.
     * 
     * @param dto
     * @return {@code IBeneficiary}
     */

    public IBeneficiary findBeneficiary(RequestBeneficiaryData dto)
            throws UserNotFoundException {

        Beneficiary result = beneficiaryRepository.findBeneficiary(dto.getId(),
                dto.getSocialReason());

        if (Objects.isNull(result)) {
            throw new UserNotFoundException(dto.toString());
        }
        return modelMapper.map(result, IBeneficiary.class);
    }


    /**
     * Method to find Volunteer.
     * 
     * @param dto
     * @return {@code IVolunter}
     */
    public IVolunter findVolunteer(RequestVolunterData data) {

        Volunter result = volunterRepository.findVolunteer(data.getId(),
                Long.valueOf(data.getDni()));

        if (Objects.isNull(result)) {
            throw new UserNotFoundException(data.toString());
        }
        return modelMapper.map(result, IVolunter.class);
    }



}
