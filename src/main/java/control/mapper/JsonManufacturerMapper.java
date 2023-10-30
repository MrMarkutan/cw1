package control.mapper;

import control.model.Manufacturer;

public class JsonManufacturerMapper extends JsonMapper<Manufacturer> {
    private static final String MANUFACTURERS_FILE = "manufacturers.json";

    private static JsonManufacturerMapper instance;

    private JsonManufacturerMapper() {
        super(MANUFACTURERS_FILE);
    }

    public static JsonManufacturerMapper getInstance() {
        if (instance == null) {
            instance = new JsonManufacturerMapper();
        }
        return instance;
    }

    @Override
    protected Class<Manufacturer> getMappedClass() {
        return Manufacturer.class;
    }
}
