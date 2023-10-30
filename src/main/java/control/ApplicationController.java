package control;

import control.controller.ManufacturerController;
import control.controller.SouvenirController;
import control.exception.ManufacturerNotFoundException;
import control.model.Manufacturer;
import control.model.Souvenir;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationController {

    private final ManufacturerController manufacturerController;

    private final SouvenirController souvenirController;

    public ApplicationController() {
        souvenirController = new SouvenirController();
        manufacturerController = new ManufacturerController();

    }

    public void addSouvenir(Souvenir souvenir) {
        Manufacturer manufacturer = souvenir.manufacturer();
        if (!manufacturerController.checkManufacturerExists(manufacturer)) {
            addManufacturer(manufacturer);
        }
        souvenirController.addSouvenir(souvenir);

    }

    public void addSouvenir(String title,
                            String manufacturer,
                            String manufacturerCountry,
                            String releaseDate,
                            Double price) {
        addSouvenir(
                new Souvenir(title,
                        new Manufacturer(manufacturer, manufacturerCountry),
                        LocalDate.parse(releaseDate),
                        price));
        System.out.println(title + " was added.");
    }

    public void addManufacturer(Manufacturer manufacturer) {
        manufacturerController.addManufacturer(manufacturer);
    }

    public void addManufacturer(String title, String country) {
        manufacturerController.addManufacturer(title, country);
        System.out.printf("Manufacturer %s, %s was added.\n", title, country);
    }

    public void removeSouvenir(String souvenirTitle) {
        souvenirController.removeSouvenir(souvenirTitle);
    }

    //Видалити заданого виробника та його сувеніри.
    public void removeManufacturer(String manufacturerTitle) {
        manufacturerController.removeManufacturer(manufacturerTitle);
        souvenirController.removeSouvenirsWhenManufacturerWasRemoved(manufacturerTitle);
        System.out.printf("Manufacturer %s and all its souvenirs were removed.\n", manufacturerTitle);
    }

    public void updateSouvenirTitle(String souvenirTitle, String updatedTitle) {
        System.out.println(souvenirController.updateSouvenirTitle(souvenirTitle, updatedTitle));
    }

    public void updateSouvenirReleaseDate(String title, String date) {
        System.out.println(souvenirController.updateSouvenirReleaseDate(title, date));
    }

    public void updateSouvenirPrice(String title, Double newPrice) {
        System.out.println(souvenirController.updateSouvenirPrice(title, newPrice));
    }

    public void updateSouvenirManufacturer(String title, String updatedManufacturer) {
        try {
            Manufacturer manufacturer = manufacturerController.getManufacturerByTitle(updatedManufacturer);
            System.out.println(souvenirController.updateSouvenirManufacturer(title, manufacturer));
        } catch (ManufacturerNotFoundException manufacturerNotFoundException) {
            System.out.println(manufacturerNotFoundException.getMessage());
        }
    }

    public void updateManufacturer(String oldTitle, String newTitle, String newCountry) {
        try {
            Manufacturer updated = manufacturerController.updateManufacturer(oldTitle, newTitle, newCountry);
            souvenirController.replaceManufacturer(oldTitle, updated);
        } catch (ManufacturerNotFoundException manufacturerNotFoundException) {
            System.out.println(manufacturerNotFoundException.getMessage());
        }

    }

    public void listAllSouvenirs() {
        souvenirController.getSouvenirs().stream()
                .sorted(Comparator.comparing(Souvenir::title))
                .forEach(System.out::println);
    }

    public void listAllManufacturers() {
        manufacturerController.listManufacturers().stream()
                .sorted(Comparator.comparing(Manufacturer::title))
                .forEach(System.out::println);
    }

    //Вивести інформацію про сувеніри заданого виробника.
    public void listSouvenirsByManufacturerTitle(String manufactorerTitle) {
        try {
            Manufacturer manufacturer = manufacturerController.getManufacturerByTitle(manufactorerTitle);
            souvenirController.getSouvenirsByManufacturer(manufacturer).stream()
                    .filter(s -> manufactorerTitle.equals(s.manufacturer().title()))
                    .map(Souvenir::toString)
                    .forEach(System.out::println);
        } catch (ManufacturerNotFoundException manufacturerNotFoundException) {
            System.out.println(manufacturerNotFoundException.getMessage());
        }
    }

    //Вивести інформацію про сувеніри, виготовлені в заданій країні.
    public void listSouvenirsByCountry(String country) {
        List<Manufacturer> manufacturers = manufacturerController.getManufacturersByCountry(country);

        manufacturers.stream()
                .flatMap(manufacturer -> souvenirController.getSouvenirsByManufacturer(manufacturer).stream()
                        .filter(souvenir -> country.equals(souvenir.manufacturer().country()))
                        .map(Souvenir::toString))
                .forEach(System.out::println);
    }

    //Вивести інформацію про виробників, чиї ціни на сувеніри менше заданої.
    public void listManufacturersWhichPricesLowerThan(double price) {
        souvenirController.getSouvenirs().stream()
                .filter(souvenir -> souvenir.price() < price)
                .map(Souvenir::manufacturer)
                .forEach(System.out::println);
    }

    //Вивести інформацію по всіх виробниках та, для кожного виробника вивести інформацію
    //про всі сувеніри, які він виробляє.
    public void listAllManufacturersAndTheirsSouvenirs() {
        souvenirController.listManufacturerAndItsSouvenirs()
                .entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().title()))
                .map(e -> {
                    String manufacturerInfo = e.getKey().toString();
                    List<Souvenir> souvenirs = e.getValue();
                    String souvenirInfo = souvenirs.stream()
                            .sorted(Comparator.comparing(Souvenir::title))
                            .map(Souvenir::toStringWithoutManufacturer)
                            .collect(Collectors.joining("\n"));
                    return manufacturerInfo + "\n" + souvenirInfo + "\n";
                })
                .forEach(System.out::println);
    }

    //Вивести інформацію про виробників заданого сувеніру, виробленого у заданому року.

    public void listManufacturersForSouvenirInYear(String souvenirTitle, int year) {
        souvenirController.listManufacturersForSouvenirInYear(souvenirTitle, year)
                .forEach(System.out::println);
    }
    //Для кожного року вивести список сувенірів, зроблених цього року.

    public void listAllSouvenirsByYears() {
        souvenirController.getSouvenirs().stream()
                .collect(Collectors.groupingBy(s -> s.release().getYear(), Collectors.toList()))
                .forEach((year, souvenirs) -> {
                    System.out.println("Year " + year);
                    souvenirs.forEach(souvenir -> System.out.println("\t" + souvenir));
                });
    }

    private void saveManufacturersToFile() {
        manufacturerController.saveManufacturers();
    }

    private void saveSouvenirsToFile() {
        souvenirController.saveSouvenirs();
    }

    public void saveData() {
        saveManufacturersToFile();
        saveSouvenirsToFile();
    }

    public void updateSouvenir(String souvenirTitle,
                               String newTitle,
                               String manufacturerTitle,
                               String manufacturerCountry,
                               String releaseDate,
                               String price) {
        if (souvenirTitle.isEmpty() || souvenirTitle.isBlank()) return;
        if (!newTitle.isEmpty() && !newTitle.isBlank()) updateSouvenirTitle(souvenirTitle, newTitle);
        if (!manufacturerTitle.isEmpty() && !manufacturerTitle.isBlank())
            updateSouvenirManufacturer(souvenirTitle, manufacturerTitle);
        if (!releaseDate.isEmpty() && !releaseDate.isBlank()) updateSouvenirReleaseDate(souvenirTitle, releaseDate);
        if (!price.isEmpty() && !price.isBlank()) updateSouvenirPrice(souvenirTitle, Double.parseDouble(price));
    }
}