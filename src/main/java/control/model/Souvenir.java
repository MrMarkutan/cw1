package control.model;

import java.time.LocalDate;

public record Souvenir(String title, Manufacturer manufacturer, LocalDate release, Double price) {


    public String toStringWithoutManufacturer() {
        return "Souvenir{" +
                "title='" + title + '\'' +
                ", release=" + release +
                ", price=" + price +
                '}';
    }

    public Souvenir updateTitle(String newTitle) {
        return new Souvenir(newTitle, manufacturer, release, price);
    }

    public Souvenir updateManufacturer(Manufacturer newManufacturer) {
        return new Souvenir(title, newManufacturer, release, price);
    }

    public Souvenir updateRelease(LocalDate updated) {
        return new Souvenir(title, manufacturer, updated, price);
    }

    public Souvenir updatePrice(Double newPrice) {
        return new Souvenir(title, manufacturer, release, newPrice);
    }
}
