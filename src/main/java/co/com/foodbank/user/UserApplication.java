package co.com.foodbank.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import co.com.foodbank.vault.sdk.config.EnableVaultSDK;


@SpringBootApplication
@EnableVaultSDK
public class UserApplication {


    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    /*
     * @Autowired VolunterRepository repo;
     * 
     * @Autowired BeneficiaryRepository repob;
     * 
     * @Autowired ModelMapper modelMapper;
     * 
     * @Override public void run(String... args) throws Exception {
     * 
     * System.out.println("Detele..repo volunter......"); repo.deleteAll();
     * 
     * // Genera el country// CountryDTO countryDto = new
     * CountryDTO("Colombia"); Country country = modelMapper.map(countryDto,
     * Country.class); modelMapper.map(country, ICountry.class);
     * 
     * // Genera el address// AddressDTO addressDto = new AddressDTO();
     * addressDto.setCountry(countryDto); addressDto.setDistrict("Bogota");
     * addressDto.setPostalCode("DC-96");
     * addressDto.setStreetName("Marcelo T alvear");
     * addressDto.setStreetNumber(65);
     * 
     * Address address2 = modelMapper.map(addressDto, Address.class);
     * address2.setId("999999999999");
     * address2.setCountry(modelMapper.map(country, ICountry.class));
     * 
     * // Genera vehiculo.// Vehicule car = new Vehicule();
     * car.setId("11111111111"); car.setBrand("Renaul"); car.setCapacity(50);
     * car.setCarPLate("REK487"); car.setVolume( modelMapper.map(new
     * Volume("12", "12", 6), IVolume.class));
     * 
     * 
     * 
     * // genera Volunter// Volunter d = new Volunter();
     * d.setEmail("test@gmail.com"); d.setName("Federido Pelaez");
     * d.setPassword("6546546"); d.setPhones("98798-696"); d.setAddress(null);
     * d.setDni(75094777); // d.setVehicule(car);
     * d.setAddress(modelMapper.map(address2, IAddress.class)); repo.save(d);
     * 
     * 
     * 
     * // Beneficiary// Beneficiary ben = new Beneficiary();
     * ben.setCategory("Escuela"); ben.setEmail("escuala@hmail.com");
     * ben.setName("Anya Ryvaloba"); ben.setPassword("13213");
     * ben.setPhones("987987-99999999"); ben.setSize(96);
     * ben.setSocialReason("ESCUAA DE PRUEBA");
     * ben.setAddress(modelMapper.map(address2, IAddress.class));
     * repob.save(ben);
     * 
     * 
     * 
     * for (User customer : repob.findAll()) {
     * System.out.println(customer.toString()); }
     * 
     * for (User customer : repo.findAll()) {
     * System.out.println(customer.toString()); }
     * 
     * 
     * }
     */

}
