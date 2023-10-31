package control.service;

import control.exception.SouvenirNotFoundException;
import control.model.Manufacturer;
import control.model.Souvenir;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SouvenirServiceTest {
    private SouvenirService souvenirService;
    Manufacturer testManufacturer;
    Souvenir testSouvenir;


    @BeforeEach
    public void setUp() {
        souvenirService = SouvenirService.getInstance();

        testManufacturer = new Manufacturer("Test Manufacturer", "Test Country");
        testSouvenir = new Souvenir("Test Souvenir", testManufacturer, LocalDate.now(), 10.0);

        souvenirService.addSouvenir(testSouvenir);
    }

    @Test
    public void testAddSouvenir() {
        Souvenir souvenir = new Souvenir("Added Sounvenir", testManufacturer, LocalDate.now(), 200.0);

        souvenirService.addSouvenir(souvenir);

        assertTrue(souvenirService.listSouvenirs().contains(souvenir));
    }

    @Test
    public void testRemoveSouvenirByTitle() {
        assertTrue(souvenirService.listSouvenirs().contains(testSouvenir));

        souvenirService.removeSouvenir(testSouvenir.title());
        assertFalse(souvenirService.listSouvenirs().contains(testSouvenir));
    }

    @Test
    public void testRemoveSouvenir() {
        assertTrue(souvenirService.listSouvenirs().contains(testSouvenir));

        souvenirService.removeSouvenir(testSouvenir);
        assertFalse(souvenirService.listSouvenirs().contains(testSouvenir));
    }

    @Test
    public void testGetSouvenirByTitle() {
        Souvenir retrievedSouvenir = souvenirService.getSouvenirByTitle(testSouvenir.title());
        assertEquals(testSouvenir, retrievedSouvenir);
    }

    @Test
    public void testGetSouvenirByTitleNotFound() {
        String title = "Non-Existent Souvenir";
        try {
            souvenirService.getSouvenirByTitle(title);
        } catch (SouvenirNotFoundException e) {
            assertEquals(title + " was not found.", e.getMessage());
        }
    }

    @Test
    public void testListSouvenirsByManufacturer() {
        Manufacturer manufacturer1 = new Manufacturer("Manufacturer 1", "Country A");

        Souvenir souvenir1 = new Souvenir("Souvenir 1", manufacturer1, LocalDate.now(), 10.0);
        Souvenir souvenir2 = new Souvenir("Souvenir 2", manufacturer1, LocalDate.now(), 20.0);

        souvenirService.addSouvenir(souvenir1);
        souvenirService.addSouvenir(souvenir2);

        List<Souvenir> souvenirsByManufacturer = souvenirService.listSouvenirsByManufacturer(manufacturer1);

        assertEquals(2, souvenirsByManufacturer.size());
        assertEquals(souvenir2, souvenirsByManufacturer.get(0));
        assertEquals(souvenir1, souvenirsByManufacturer.get(1));
    }

    @Test
    public void testUpdateSouvenirTitle() {
        Souvenir updatedSouvenir = souvenirService.updateSouvenirTitle(testSouvenir.title(), "Updated Title");
        assertEquals("Updated Title", updatedSouvenir.title());
    }

    @Test
    public void testUpdateSouvenirManufacturer() {
        Manufacturer manufacturer1 = new Manufacturer("Manufacturer 1", "Country A");

        Souvenir updatedSouvenir = souvenirService.updateSouvenirManufacturer(testSouvenir.title(), manufacturer1);
        assertEquals(manufacturer1, updatedSouvenir.manufacturer());
    }

    @Test
    public void testUpdateSouvenirReleaseDate() {
        LocalDate newDate = LocalDate.of(2023, 1, 1);

        Souvenir updatedSouvenir = souvenirService.updateSouvenirReleaseDate(testSouvenir.title(), newDate);

        assertEquals(newDate, updatedSouvenir.release());
    }

    @Test
    public void testUpdateSouvenirPrice() {
        double newPrice = 20.0;
        Souvenir updatedSouvenir = souvenirService.updateSouvenirPrice(testSouvenir.title(), newPrice);
        assertEquals(newPrice, updatedSouvenir.price(), 0.01);
    }


    @Test
    public void testListManufacturerAndItsSouvenirs() {
        Manufacturer manufacturer1 = new Manufacturer("Manufacturer 1", "Country A");
        Manufacturer manufacturer2 = new Manufacturer("Manufacturer 2", "Country B");
        Souvenir souvenir1 = new Souvenir("Souvenir 1", manufacturer1, LocalDate.now(), 10.0);
        Souvenir souvenir2 = new Souvenir("Souvenir 2", manufacturer1, LocalDate.now(), 20.0);
        Souvenir souvenir3 = new Souvenir("Souvenir 3", manufacturer2, LocalDate.now(), 30.0);

        souvenirService.addSouvenir(souvenir1);
        souvenirService.addSouvenir(souvenir2);
        souvenirService.addSouvenir(souvenir3);

        Map<Manufacturer, List<Souvenir>> manufacturerAndItsSouvenirs = souvenirService.listManufacturerAndItsSouvenirs();

        assertTrue(manufacturerAndItsSouvenirs.containsKey(manufacturer1));
        assertEquals(2, manufacturerAndItsSouvenirs.get(manufacturer1).size());

        assertTrue(manufacturerAndItsSouvenirs.containsKey(manufacturer2));
        assertEquals(1, manufacturerAndItsSouvenirs.get(manufacturer2).size());
    }

    @Test
    public void testRemoveSouvenirsByManufacturer() {
        Manufacturer manufacturer = new Manufacturer("Manufacturer 1", "Country A");
        Souvenir souvenir1 = new Souvenir("Souvenir 1", manufacturer, LocalDate.now(), 10.0);
        Souvenir souvenir2 = new Souvenir("Souvenir 2", manufacturer, LocalDate.now(), 20.0);
        Souvenir souvenir3 = new Souvenir("Souvenir 3", new Manufacturer("Manufacturer 2", "Country B"), LocalDate.now(), 30.0);

        souvenirService.addSouvenir(souvenir1);
        souvenirService.addSouvenir(souvenir2);
        souvenirService.addSouvenir(souvenir3);

        souvenirService.removeSouvenirsByManufacturer(manufacturer);

        List<Souvenir> remainingSouvenirs = souvenirService.listSouvenirs();
        assertFalse(remainingSouvenirs.contains(souvenir1));
        assertFalse(remainingSouvenirs.contains(souvenir2));
        assertTrue(remainingSouvenirs.contains(souvenir3));
    }
}
