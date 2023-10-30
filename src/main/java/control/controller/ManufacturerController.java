package control.controller;

import control.model.Manufacturer;
import control.service.ManufacturerService;

import java.util.List;

public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    public ManufacturerController() {
        this.manufacturerService = ManufacturerService.getInstance();
    }

    public Manufacturer getManufacturerByTitle(String title) {
        return manufacturerService.getManufacturerByTitle(title);
    }

    public List<Manufacturer> getManufacturersByCountry(String country) {
        return manufacturerService.listManufacturersByCountry(country);
    }

    public void addManufacturer(Manufacturer manufacturer) {
        manufacturerService.addManufacturer(manufacturer);
    }

    public void addManufacturer(String title, String country) {
        manufacturerService.addManufacturer(title, country);
    }

    public void removeManufacturer(Manufacturer manufacturer) {
        manufacturerService.removeManufacturer(manufacturer);
    }

    public void removeManufacturer(String title) {
        manufacturerService.removeManufacturer(title);
    }

    public List<Manufacturer> listManufacturers() {
        return manufacturerService.listManufacturers();
    }

    public Manufacturer updateManufacturer(String oldTitle, String newTitle, String newCountry) {
        return manufacturerService.updateManufacturer(oldTitle, newTitle, newCountry);
    }

    public boolean checkManufacturerExists(Manufacturer manufacturer) {
        return manufacturerService.listManufacturers().stream()
                .anyMatch(manufacturer::equals);
    }

    public void saveManufacturers() {
        manufacturerService.saveToFile();
    }
}
