package control.model;

public record Manufacturer(String title, String country) {
    public  Manufacturer updateTitle(String newTitle ){
        return new Manufacturer(newTitle, country);
    }
    public Manufacturer updateCountry(String newCountry) {
        return new Manufacturer(title, newCountry);
    }
}
