package control.controller;

import control.model.Manufacturer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManufacturerControllerTest {
    private ManufacturerController controller;

    Manufacturer manufacturer;

    @BeforeEach
    public void setUp() {
        controller = new ManufacturerController();
        manufacturer = new Manufacturer("Test Manufacturer", "Test Country");

    }

    @Test
    public void testAddManufacturer() {
        controller.addManufacturer(manufacturer);
        List<Manufacturer> manufacturers = controller.listManufacturers();
        assertTrue(manufacturers.contains(manufacturer));
    }

    @Test
    public void testRemoveManufacturer() {
        controller.addManufacturer(manufacturer);
        controller.removeManufacturer(manufacturer);
        List<Manufacturer> manufacturers = controller.listManufacturers();
        assertFalse(manufacturers.contains(manufacturer));
    }

    @Test
    public void testGetManufacturerByTitle() {
        controller.addManufacturer(manufacturer);
        Manufacturer retrievedManufacturer = controller.getManufacturerByTitle("Test Manufacturer");
        assertNotNull(retrievedManufacturer);
        assertEquals(manufacturer, retrievedManufacturer);
    }

    @Test
    public void testUpdateManufacturer() {
        controller.addManufacturer(manufacturer);
        Manufacturer updatedManufacturer = controller.updateManufacturer("Test Manufacturer", "Updated Manufacturer", "Updated Country");
        assertNotNull(updatedManufacturer);
        assertEquals("Updated Manufacturer", updatedManufacturer.title());
        assertEquals("Updated Country", updatedManufacturer.country());
    }

    @Test
    public void testGetManufacturersByCountry() {
        Manufacturer manufacturer1 = new Manufacturer("Manufacturer 1", "Country 1");
        Manufacturer manufacturer2 = new Manufacturer("Manufacturer 2", "Country 2");
        controller.addManufacturer(manufacturer1);
        controller.addManufacturer(manufacturer2);

        List<Manufacturer> manufacturers = controller.getManufacturersByCountry("Country 1");
        assertEquals(1, manufacturers.size());
        assertEquals(manufacturer1, manufacturers.get(0));
    }

    @Test
    public void testCheckManufacturerExists() {
        controller.addManufacturer(manufacturer);
        boolean exists = controller.checkManufacturerExists(manufacturer);
        assertTrue(exists);
    }
}
