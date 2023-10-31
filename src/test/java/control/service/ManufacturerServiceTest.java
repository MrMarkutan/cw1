package control.service;

import control.exception.ManufacturerNotFoundException;
import control.model.Manufacturer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManufacturerServiceTest {

    private ManufacturerService manufacturerService;
    Manufacturer testManufacturer;


    @BeforeEach
    public void setUp() {
        manufacturerService = ManufacturerService.getInstance();

        testManufacturer = new Manufacturer("Test Manufacturer", "Test Country");

    }

    @Test
    public void testAddManufacturer() {
        manufacturerService.addManufacturer(testManufacturer);
        assertTrue(manufacturerService.listManufacturers().contains(testManufacturer));
    }

    @Test
    public void testGetManufacturerByTitle() {
        manufacturerService.addManufacturer(testManufacturer);

        Manufacturer retrievedManufacturer = manufacturerService.getManufacturerByTitle("Test Manufacturer");
        assertEquals(testManufacturer, retrievedManufacturer);
    }

    @Test
    public void testGetManufacturerByTitleNotFound() {
        String title = "Non-Existent Manufacturer";
        try {
            manufacturerService.getManufacturerByTitle(title);
            fail("ManufacturerNotFoundException should be thrown in this test.");
        } catch (ManufacturerNotFoundException e) {
            assertEquals(title + " was not found.", e.getMessage());
        }
    }

    @Test
    public void testListManufacturersByCountry() {
        Manufacturer manufacturer1 = new Manufacturer("Manufacturer 1", "Country A");
        Manufacturer manufacturer2 = new Manufacturer("Manufacturer 2", "Country B");
        Manufacturer manufacturer3 = new Manufacturer("Manufacturer 3", "Country A");

        manufacturerService.addManufacturer(manufacturer1);
        manufacturerService.addManufacturer(manufacturer2);
        manufacturerService.addManufacturer(manufacturer3);

        var manufacturersByCountry = manufacturerService.listManufacturersByCountry("Country A");

        assertEquals(2, manufacturersByCountry.size());
        assertTrue(manufacturersByCountry.contains(manufacturer1));
        assertTrue(manufacturersByCountry.contains(manufacturer3));
        assertFalse(manufacturersByCountry.contains(manufacturer2));
    }

    @Test
    void testListManufacturers() {
        Manufacturer manufacturer1 = new Manufacturer("Manufacturer 1", "Country A");
        Manufacturer manufacturer2 = new Manufacturer("Manufacturer 2", "Country B");
        Manufacturer manufacturer3 = new Manufacturer("Manufacturer 3", "Country A");

        manufacturerService.addManufacturer(manufacturer1);
        manufacturerService.addManufacturer(manufacturer2);
        manufacturerService.addManufacturer(manufacturer3);

        var manufacturers = manufacturerService.listManufacturers();

        assertTrue(manufacturers.contains(manufacturer1));
        assertTrue(manufacturers.contains(manufacturer2));
        assertTrue(manufacturers.contains(manufacturer3));
    }

    @Test
    void testRemoveManufacturer() {
        manufacturerService.addManufacturer(testManufacturer);
        var manufacturers = manufacturerService.listManufacturers();
        assertTrue(manufacturers.contains(testManufacturer));
        manufacturerService.removeManufacturer(testManufacturer);
        manufacturers = manufacturerService.listManufacturers();
        assertFalse(manufacturers.contains(testManufacturer));
    }

    @Test
    public void testUpdateManufacturer() {
        manufacturerService.addManufacturer(testManufacturer);

        Manufacturer updatedManufacturer = manufacturerService.updateManufacturer(testManufacturer.title(), "New Title", "New Country");

        assertEquals("New Title", updatedManufacturer.title());
        assertEquals("New Country", updatedManufacturer.country());
    }
}
