package control.mapper;

public class JsonMapperFactory {
    public static JsonManufacturerMapper createManufacturerMapper() {
        return JsonManufacturerMapper.getInstance();
    }

    public static JsonSouvenirMapper createSouvenirsMapper() {
        return JsonSouvenirMapper.getInstance();
    }
}
