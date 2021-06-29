package co.com.foodbank.user.service;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import co.com.foodbank.address.dto.Address;
import co.com.foodbank.address.dto.AddressDTO;
import co.com.foodbank.country.dto.Country;
import co.com.foodbank.country.dto.CountryDTO;
import co.com.foodbank.user.dto.BeneficiaryDTO;
import co.com.foodbank.user.dto.ProviderDTO;
import co.com.foodbank.user.dto.VolunterDTO;
import co.com.foodbank.user.exception.UserErrorException;
import co.com.foodbank.user.exception.UserNotFoundException;
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



    private static final String MSG_ERROR = "is not a";
    private static final String MSG_BENEFICIARY = " Beneficiary";
    private static final String MSG_VOLUNTER = " Volunter";
    private static final String MSG_PROVIDER = " Provider";


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



    /**
     * Method to create a Provider.
     * 
     * @param dto
     * @return {@code Provider}
     */
    public Provider createProvider(ProviderDTO dto) {
        return providerRepository.save(this.setProvider(dto));
    }


    /**
     * Method to create an Provider.
     * 
     * @param dto
     * @param cuit
     * @param legalRpp
     * @return {@code Provider}
     */
    private Provider setProvider(ProviderDTO providerDto) {

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

        return provider;
    }



    /**
     * Method tocreate a Provider.
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
            String err = _id + MSG_ERROR + MSG_BENEFICIARY;
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



    public IProvider updateprovider(ProviderDTO dto, String _id)
            throws NotFoundException, UserErrorException {

        User dataDB = findById(_id);
        if (!checkInstansOfProvider(dataDB)) {
            String err = _id + MSG_ERROR + MSG_PROVIDER;
            throw new UserErrorException(err);
        }
        return providerRepository.save(buildProvider(dto, (Provider) dataDB));
    }


    private boolean checkInstansOfProvider(User dataDB) {
        return (dataDB instanceof Provider) ? true : false;
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
     * @throws UserErrorException
     */
    public IVolunter updateVolunter(VolunterDTO dto, String _id)
            throws NotFoundException, UserErrorException {

        User dataDB = findById(_id);
        if (!checkInstansOfVolunter(dataDB)) {
            String err = _id + MSG_ERROR + MSG_VOLUNTER;
            throw new UserErrorException(err);
        }
        return volunterRepository.save(buildVolunter(dto, (Volunter) dataDB));
    }


    private boolean checkInstansOfVolunter(User dataDB) {
        return (dataDB instanceof Volunter) ? true : false;
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



}
