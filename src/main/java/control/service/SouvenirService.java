package control.service;

import control.exception.SouvenirNotFoundException;
import control.mapper.JsonMapperFactory;
import control.mapper.Mapper;
import control.model.Manufacturer;
import control.model.Souvenir;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SouvenirService {
    private final Set<Souvenir> souvenirs;
    private static SouvenirService instance;
    private static Mapper<Souvenir> mapper;


    private SouvenirService() {
        mapper = JsonMapperFactory.createSouvenirsMapper();
        this.souvenirs = loadSouvenirsFromFile() != null ?
                new HashSet<>(loadSouvenirsFromFile()) : new HashSet<>();
    }

    private List<Souvenir> loadSouvenirsFromFile() {
        return mapper.read();
    }

    public void saveToFile() {
        mapper.write(souvenirs.stream().toList());
    }

    public static SouvenirService getInstance() {
        if (instance == null) {
            instance = new SouvenirService();
        }
        return instance;
    }

    public void addSouvenir(String title, Manufacturer manufacturer, LocalDate release, double price) {
        addSouvenir(new Souvenir(title, manufacturer, release, price));
    }

    public void addSouvenir(Souvenir souvenir) {
        souvenirs.add(souvenir);
    }

    public List<Souvenir> listSouvenirs() {
        return souvenirs.stream().toList();
    }

    public void removeSouvenir(String title) {
        Souvenir souvenirByTitle = getSouvenirByTitle(title);
        removeSouvenir(souvenirByTitle);

    }

    public void removeSouvenir(Souvenir souvenir) {
        souvenirs.remove(souvenir);
    }

    public Souvenir getSouvenirByTitle(String title) {
        Optional<Souvenir> optional = souvenirs.stream()
                .filter(s -> title.equals(s.title()))
                .findFirst();
        return optional.orElseThrow(() -> new SouvenirNotFoundException(title + " souvenir was not found."));
    }

    public List<Souvenir> listSouvenirsByManufacturer(Manufacturer manufacturer) {
        return souvenirs.stream()
                .filter(s -> manufacturer.equals(s.manufacturer()))
                .toList();
    }

    public Map<Manufacturer, List<Souvenir>> listManufacturerAndItsSouvenirs() {
        return souvenirs.stream()
                .collect(Collectors.groupingBy(Souvenir::manufacturer));
    }

    public void removeSouvenirsByManufacturer(Manufacturer manufacturer) {
        souvenirs.removeIf(s -> s.manufacturer().equals(manufacturer));
    }

    public Souvenir updateSouvenirTitle(String title, String newSouvenirTitle) {
        return updateSouvenir(title,
                Optional.of(newSouvenirTitle),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }

    public Souvenir updateSouvenirManufacturer(String title, Manufacturer manufacturer) {
        return updateSouvenir(title,
                Optional.empty(),
                Optional.of(manufacturer),
                Optional.empty(),
                Optional.empty());
    }

    public Souvenir updateSouvenirReleaseDate(String title, LocalDate newDate) {
        return updateSouvenir(title,
                Optional.empty(),
                Optional.empty(),
                Optional.of(newDate),
                Optional.empty());
    }

    public Souvenir updateSouvenirPrice(String title, Double newPrice) {
        return updateSouvenir(title,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(newPrice));
    }

    public Souvenir updateSouvenir(String title,
                                   Optional<String> newTitle,
                                   Optional<Manufacturer> newManufacturer,
                                   Optional<LocalDate> newReleaseDate,
                                   Optional<Double> newPrice) {
        Souvenir souvenir = getSouvenirByTitle(title);
        souvenirs.remove(souvenir);
        if (newTitle.isPresent()) {
            souvenir = souvenir.updateTitle(newTitle.get());
        }
        if (newManufacturer.isPresent()) {
            souvenir = souvenir.updateManufacturer(newManufacturer.get());
        }
        if (newReleaseDate.isPresent()) {
            souvenir = souvenir.updateRelease(newReleaseDate.get());
        }
        if (newPrice.isPresent()) {
            souvenir = souvenir.updatePrice(newPrice.get());
        }
        souvenirs.add(souvenir);
        return souvenir;
    }
}
