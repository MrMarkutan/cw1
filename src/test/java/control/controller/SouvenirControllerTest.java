package control.controller;

import control.exception.SouvenirNotFoundException;
import control.model.Manufacturer;
import control.model.Souvenir;
import control.service.SouvenirService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SouvenirControllerTest {
    private SouvenirController controller;
    private SouvenirService souvenirService;

    String testTitle;
    Manufacturer testManufacturer;
    Souvenir testSouvenir;


    @BeforeEach
    public void setUp() {
        souvenirService = mock(SouvenirService.class);
        controller = new SouvenirController();
        controller.setSouvenirService(souvenirService);

        testTitle = "Test Souvenir";
        testManufacturer = new Manufacturer("New Manufacturer", "New Country");
        testSouvenir = new Souvenir(testTitle, testManufacturer, LocalDate.now(), 10.0);
    }

    @Test
    public void testAddSouvenir() {
        controller.addSouvenir(testSouvenir);
        verify(souvenirService, times(1)).addSouvenir(any());
    }

    @Test
    public void testRemoveSouvenir() {
        when(souvenirService.getSouvenirByTitle(anyString())).thenReturn(testSouvenir);
        try {
            controller.removeSouvenir(testTitle);
            verify(souvenirService, times(1)).getSouvenirByTitle(anyString());
            verify(souvenirService, times(1)).removeSouvenir(any(Souvenir.class));
        } catch (SouvenirNotFoundException e) {
            fail("SouvenirNotFoundException should not be thrown in this test.");
        }
    }

    @Test
    public void testRemoveSouvenirWhenNotExists() {
        doThrow(new SouvenirNotFoundException("Souvenir not found")).when(souvenirService).getSouvenirByTitle(testTitle);
        try {
            controller.removeSouvenir(testTitle);
        } catch (SouvenirNotFoundException e) {
            fail("SouvenirNotFoundException should not be thrown in this test.");
        }
        verify(souvenirService, times(1)).getSouvenirByTitle(anyString());
        verify(souvenirService, never()).removeSouvenir(any(Souvenir.class));
    }


    @Test
    public void testListManufacturerAndItsSouvenirs() {
        Map<Manufacturer, List<Souvenir>> manufacturerAndItsSouvenirs = controller.listManufacturerAndItsSouvenirs();
        assertNotNull(manufacturerAndItsSouvenirs);
    }

    @Test
    public void testUpdateSouvenirTitle() {
        String newTitle = "Updated Souvenir";
        testSouvenir = testSouvenir.updateTitle(newTitle);
        when(souvenirService.updateSouvenirTitle(anyString(), anyString())).thenReturn(testSouvenir);
        Souvenir updatedSouvenir = controller.updateSouvenirTitle(testTitle, newTitle);
        assertEquals(newTitle, updatedSouvenir.title());
    }

    @Test
    public void testUpdateSouvenirManufacturer() {
        when(souvenirService.updateSouvenirManufacturer(testTitle, testManufacturer)).thenReturn(testSouvenir);
        Souvenir updatedSouvenir = controller.updateSouvenirManufacturer(testTitle, testManufacturer);
        assertEquals(testManufacturer.title(), updatedSouvenir.manufacturer().title());
        assertEquals(testManufacturer.country(), updatedSouvenir.manufacturer().country());
    }

    @Test
    public void testUpdateSouvenirReleaseDate() {
        String title = "Test Souvenir";
        String date = "2023-10-31";
        LocalDate newDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        when(souvenirService.updateSouvenirReleaseDate(title, newDate)).thenReturn(testSouvenir);
        Souvenir updatedSouvenir = controller.updateSouvenirReleaseDate(title, date);
        assertEquals(newDate, updatedSouvenir.release());
    }

    @Test
    public void testUpdateSouvenirPrice() {
        Double newPrice = 20.0;
        testSouvenir = testSouvenir.updatePrice(newPrice);
        when(souvenirService.updateSouvenirPrice(testTitle, newPrice)).thenReturn(testSouvenir);
        Souvenir updatedSouvenir = controller.updateSouvenirPrice(testTitle, newPrice);
        assertEquals(newPrice, updatedSouvenir.price(), 0.001);
    }

    @Test
    public void testGetSouvenirsByManufacturer() {
        when(souvenirService.listSouvenirsByManufacturer(testManufacturer)).thenReturn(List.of(testSouvenir));
        List<Souvenir> souvenirs = controller.getSouvenirsByManufacturer(testManufacturer);
        assertEquals(1, souvenirs.size());
        assertEquals(testManufacturer, souvenirs.get(0).manufacturer());
    }

    @Test
    public void testListManufacturersForSouvenirInYear() {
        String souvenirTitle = "Test Souvenir";
        int year = LocalDate.now().getYear();
        when(souvenirService.listSouvenirs()).thenReturn(List.of(testSouvenir));
        List<Manufacturer> manufacturers = controller.listManufacturersForSouvenirInYear(souvenirTitle, year);
        assertEquals(1, manufacturers.size());
        assertEquals(testManufacturer, manufacturers.get(0));
    }

    @Test
    public void testRemoveSouvenirsWhenManufacturerWasRemoved() {
        List<Souvenir> souvenirs = List.of(
                new Souvenir("Souvenir 1", testManufacturer, LocalDate.now(), 10.0),
                new Souvenir("Souvenir 2", testManufacturer, LocalDate.now(), 15.0),
                new Souvenir("Other Souvenir", new Manufacturer("Other Manufacturer", "Other Country"), LocalDate.now(), 20.0)
        );

        when(souvenirService.listSouvenirs()).thenReturn(souvenirs);
        controller.removeSouvenirsWhenManufacturerWasRemoved(testManufacturer.title());
        verify(souvenirService, times(2)).removeSouvenir(any(Souvenir.class));
    }

    @Test
    public void testReplaceManufacturer() {
        String manufacturerTitle = "Test Manufacturer";
        Manufacturer newManufacturer = new Manufacturer("New Manufacturer", "Test Country");
        Manufacturer oldManufacturer = new Manufacturer(testTitle, "Test Country");
        when(souvenirService.listSouvenirs()).thenReturn(List.of(new Souvenir("Test Souvenir", oldManufacturer, LocalDate.now(), 10.0)));
        controller.replaceManufacturer(manufacturerTitle, newManufacturer);
        verify(souvenirService, times(1)).listSouvenirs();
    }

    @Test
    public void testCheckSouvenirExists() {
        when(souvenirService.listSouvenirs()).thenReturn(List.of(testSouvenir));
        boolean exists = controller.checkSouvenirExists(testSouvenir);
        assertTrue(exists);
    }
}
