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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.com.foodbank.address.dto.Address;
import co.com.foodbank.address.dto.AddressDTO;
import co.com.foodbank.country.dto.Country;
import co.com.foodbank.country.dto.CountryDTO;
import co.com.foodbank.user.dto.BeneficiaryDTO;
import co.com.foodbank.user.dto.ProviderDTO;
import co.com.foodbank.user.dto.VolunterDTO;
import co.com.foodbank.user.exception.ContributionNotFoundException;
import co.com.foodbank.user.model.IBeneficiary;
import co.com.foodbank.user.model.IProvider;
import co.com.foodbank.user.model.IUser;
import co.com.foodbank.user.model.IVolunter;
import co.com.foodbank.user.repository.BeneficiaryRepository;
import co.com.foodbank.user.repository.ProviderRepository;
import co.com.foodbank.user.repository.UserRepository;
import co.com.foodbank.user.repository.VolunterRepository;
import co.com.foodbank.user.v1.model.Beneficiary;
import co.com.foodbank.user.v1.model.Provider;
import co.com.foodbank.user.v1.model.User;
import co.com.foodbank.user.v1.model.Volunter;
import co.com.foodbank.vault.dto.IVault;
import co.com.foodbank.vault.dto.VaultDTO;
import co.com.foodbank.vault.sdk.exception.SDKVaultServiceException;
import co.com.foodbank.vault.sdk.exception.SDKVaultServiceIllegalArgumentException;
import co.com.foodbank.vault.sdk.model.VaultData;
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
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("sdkService")
    private SDKVaultService sdkService;



    /**
     * Method to list all users.
     * 
     * @return {@code Collection<IUser> }
     */
    public Collection<IUser> findAll() {
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
    public IUser findByCuit(String cuit) throws ContributionNotFoundException {

        User data = userRepository.finByCuit(Long.valueOf(cuit));
        if (Objects.isNull(data)) {
            throw new ContributionNotFoundException(cuit);
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
            throws ContributionNotFoundException {

        Collection<User> data = userRepository.findByEmail(email);

        if (data.isEmpty()) {
            throw new ContributionNotFoundException(email);
        }
        return data.stream().map(d -> modelMapper.map(d, IUser.class))
                .collect(Collectors.toList());
    }

    /**
     * Method to find Volunter By Dni.
     * 
     * @param dni
     * @return {@code IUser}
     * @throws ContributionNotFoundException
     */
    public IUser findByDni(String dni) throws ContributionNotFoundException {

        User data = userRepository.finByDni(Long.valueOf(dni));

        if (Objects.isNull(data)) {
            throw new ContributionNotFoundException(dni);
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
    public Volunter createVolunter(@Valid VolunterDTO dto) {
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
        Vehicule vehicule = serVehicule(dto);
        Address address = setAddress(dto.getAddress());

        Volunter volunter = modelMapper.map(dto, Volunter.class);
        volunter.setVehicule(vehicule);
        volunter.setAddress(address);
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
    private Vehicule serVehicule(VolunterDTO dto) {
        Vehicule dataVehicule = new Vehicule();
        if (!checkIsNullVehicule(dto.getVehicule())) {
            if (!checkIsNullAttVehicule(dto.getVehicule())) {
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

    private boolean checkIsNullAttVehicule(VehiculeDTO dto) {
        return Stream.of(dto).allMatch(Objects::isNull);
    }



    /**
     * Method to create a Provider.
     * 
     * @param dto
     * @return {@code Provider}
     * @throws SDKVaultServiceIllegalArgumentException
     * @throws SDKVaultServiceException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    public Provider createProvider(ProviderDTO dto)
            throws JsonMappingException, JsonProcessingException,
            SDKVaultServiceException, SDKVaultServiceIllegalArgumentException {
        return providerRepository.save(this.setProvider(dto));
    }


    /**
     * Method to create an Provider.
     * 
     * @param dto
     * @param cuit
     * @param legalRpp
     * @return {@code Provider}
     * @throws SDKVaultServiceIllegalArgumentException
     * @throws SDKVaultServiceException
     * @throws VaultServiceException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    private Provider setProvider(ProviderDTO providerDto)
            throws JsonMappingException, JsonProcessingException,
            SDKVaultServiceException, SDKVaultServiceIllegalArgumentException {


        Address address = setAddress(providerDto.getAddress());

        Provider provider = new Provider();
        provider.setAddress(address);
        provider.setCuil(Long.valueOf(providerDto.getCuil()));
        provider.setEmail(providerDto.getEmail());
        provider.setLegalRepresentation(providerDto.getLegalRepresentation());
        provider.setName(providerDto.getName());
        provider.setPassword(providerDto.getPassword());
        provider.setPhones(providerDto.getPhones());
        provider.setState(false);
        provider.setSucursal(createVault(providerDto));

        return provider;
    }


    /**
     * @param providerDto
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws SDKVaultServiceException
     * @throws SDKVaultServiceIllegalArgumentException
     */
    private Collection<IVault> createVault(ProviderDTO providerDto)
            throws JsonMappingException, JsonProcessingException,
            SDKVaultServiceException, SDKVaultServiceIllegalArgumentException {

        Collection<Vault> data = new ArrayList<Vault>();

        for (VaultDTO d : providerDto.getSucursal()) {
            VaultData vaultData = objectMapper.readValue(sdkService.create(d),
                    new TypeReference<VaultData>() {});
            data.add(modelMapper.map(vaultData, Vault.class));
        }

        return data.stream().map(d -> modelMapper.map(d, IVault.class))
                .collect(Collectors.toList());

    }



    /**
     * Method tocreate a Provider.
     * 
     * @param dto
     * @return {@code IBeneficiary}
     */
    public Beneficiary createBeneficiary(BeneficiaryDTO dto) {

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
     */
    public IBeneficiary updateBeneficiary(BeneficiaryDTO dto, String _id)
            throws NotFoundException {

        Beneficiary query = beneficiaryRepository.findById(_id)
                .orElseThrow(() -> new NotFoundException(_id));
        return beneficiaryRepository.save(buildBeneficiary(dto, query));

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



    public IProvider updateprovider(ProviderDTO dto, String _id)
            throws NotFoundException {

        Provider query = providerRepository.findById(_id)
                .orElseThrow(() -> new NotFoundException(_id));

        return providerRepository.save(buildProvider(dto, query));

    }


    /**
     * @param dto
     * @param query
     * @return
     */
    private Provider buildProvider(ProviderDTO dto, Provider query) {
        Provider provider = query;
        Address address = setAddress(dto.getAddress());
        provider.setAddress(address);
        provider.setEmail(dto.getEmail());
        provider.setName(dto.getName());
        provider.setPassword(dto.getPassword());
        provider.setPhones(dto.getPhones());
        provider.setCuil(Long.valueOf(dto.getCuil()));
        provider.setLegalRepresentation(dto.getLegalRepresentation());
        // provider.setSucursal(dto.getSucursal());

        return provider;

    }


    /**
     * Update a Volunter
     * 
     * @param dto
     * @param _id
     * @return
     * @throws NotFoundException
     */
    public IVolunter updateVolunter(VolunterDTO dto, String _id)
            throws NotFoundException {

        Volunter query = volunterRepository.findById(_id)
                .orElseThrow(() -> new NotFoundException(_id));

        return volunterRepository.save(buildVolunter(dto, query));
    }


    /**
     * Build Volunter
     * 
     * @param dto
     * @param query
     * @return {@code Volunter}
     */
    private Volunter buildVolunter(VolunterDTO dto, Volunter query) {
        Volunter volunter = query;
        Address address = setAddress(dto.getAddress());
        volunter.setAddress(address);
        volunter.setEmail(dto.getEmail());
        volunter.setName(dto.getName());
        volunter.setPassword(dto.getPassword());
        volunter.setPhones(dto.getPhones());
        volunter.setDni(Long.valueOf(dto.getDni()));
        return volunter;
    }



}
