package control.service;

import control.exception.ManufacturerNotFoundException;
import control.mapper.JsonMapperFactory;
import control.mapper.Mapper;
import control.model.Manufacturer;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ManufacturerService {
    private final Set<Manufacturer> manufacturers;
    private static ManufacturerService instance;
    private static Mapper<Manufacturer> mapper;

    private ManufacturerService() {
        mapper = JsonMapperFactory.createManufacturerMapper();

        this.manufacturers = loadManufacturersFromFile() != null ?
                new HashSet<>(loadManufacturersFromFile()) : new HashSet<>();

    }
    private List<Manufacturer> loadManufacturersFromFile() {
        return mapper.read();
    }

    public void saveToFile() {
        mapper.write(manufacturers.stream().toList());
    }


    public void setMapper(Mapper mapper){
        ManufacturerService.mapper = mapper;
    }
    public static ManufacturerService getInstance() {
        if (instance == null) {
            instance = new ManufacturerService();
        }
        return instance;
    }

    public void addManufacturer(String title, String country) {
        addManufacturer(new Manufacturer(title, country));
    }

    public void addManufacturer(Manufacturer manufacturer) {
        manufacturers.add(manufacturer);
    }

    public Manufacturer getManufacturerByTitle(String title) {
        Optional<Manufacturer> optional = manufacturers.stream()
                .filter(m -> title.equals(m.title()))
                .findFirst();
        return optional.orElseThrow(() -> new ManufacturerNotFoundException(title + " was not found."));
    }

    public List<Manufacturer> listManufacturersByCountry(String country) {
        return manufacturers.stream()
                .filter(m -> country.equals(m.country()))
                .toList();
    }

    public List<Manufacturer> listManufacturers() {
        return manufacturers.stream().toList();
    }

    public void removeManufacturer(String title) {
        removeManufacturer(getManufacturerByTitle(title));
    }

    public void removeManufacturer(Manufacturer manufacturer) {
        manufacturers.remove(manufacturer);
    }

    public Manufacturer updateManufacturer(String title, String newTitle, String newCountry) {
        Manufacturer manufacturer = getManufacturerByTitle(title);
        manufacturers.remove(manufacturer);
        if (!newTitle.isBlank() && !newTitle.isEmpty()) {
            manufacturer = manufacturer.updateTitle(newTitle);
        }
        if (!newCountry.isBlank() && !newCountry.isEmpty()) {
            manufacturer = manufacturer.updateCountry(newCountry);
        }
        manufacturers.add(manufacturer);
        return manufacturer;
    }
}
