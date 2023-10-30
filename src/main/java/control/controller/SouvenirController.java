package control.controller;

import control.exception.SouvenirNotFoundException;
import control.model.Manufacturer;
import control.model.Souvenir;
import control.service.SouvenirService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class SouvenirController {
    private final SouvenirService souvenirService;

    public SouvenirController() {
        this.souvenirService = SouvenirService.getInstance();
    }

    public List<Souvenir> getSouvenirs() {
        return souvenirService.listSouvenirs();
    }

    public void removeSouvenir(String title) {
        try {
            Souvenir souvenir = souvenirService.getSouvenirByTitle(title);
            souvenirService.removeSouvenir(souvenir);
        } catch (SouvenirNotFoundException souvenirNotFoundException) {
            System.out.println(souvenirNotFoundException.getMessage());
        }
    }

    public void addSouvenir(Souvenir souvenir) {
        souvenirService.addSouvenir(souvenir);
    }

    public Map<Manufacturer, List<Souvenir>> listManufacturerAndItsSouvenirs() {
        return souvenirService.listManufacturerAndItsSouvenirs();
    }

    public Souvenir updateSouvenirTitle(String title, String newSouvenirTitle) {
        return souvenirService.updateSouvenirTitle(title, newSouvenirTitle);
    }

    public Souvenir updateSouvenirManufacturer(String title, Manufacturer manufacturer) {
        return souvenirService.updateSouvenirManufacturer(title, manufacturer);
    }

    public Souvenir updateSouvenirReleaseDate(String title, String date) {
        LocalDate newDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return souvenirService.updateSouvenirReleaseDate(title, newDate);
    }

    public Souvenir updateSouvenirPrice(String title, Double newPrice) {
        return souvenirService.updateSouvenirPrice(title, newPrice);
    }

    public List<Souvenir> getSouvenirsByManufacturer(Manufacturer manufacturer) {
        return souvenirService.listSouvenirsByManufacturer(manufacturer);
    }

    public List<Manufacturer> listManufacturersForSouvenirInYear(String souvenirTitle, int year) {
        return souvenirService.listSouvenirs().stream()
                .filter(s -> souvenirTitle.equals(s.title()))
                .filter(s -> year == (s.release().getYear()))
                .map(Souvenir::manufacturer)
                .toList();
    }

    public void removeSouvenirsWhenManufacturerWasRemoved(String manufacturerTitle) {
        souvenirService.listSouvenirs().stream()
                .filter(s -> manufacturerTitle.equals(s.manufacturer().title()))
                .forEach(souvenirService::removeSouvenir);
    }

    public void replaceManufacturer(String manufacturerTitle, Manufacturer newManufacturer) {
        souvenirService.listSouvenirs().stream()
                .filter(s -> manufacturerTitle.equals(s.manufacturer().title()))
                .forEach(s -> s.updateManufacturer(newManufacturer));
    }

    public boolean checkSouvenirExists(Souvenir souvenir) {
        return souvenirService.listSouvenirs().stream()
                .anyMatch(souvenir::equals);
    }

    public void saveSouvenirs() {
        souvenirService.saveToFile();
    }
}
