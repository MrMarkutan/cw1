package control.mapper;

import control.model.Souvenir;

public class JsonSouvenirMapper extends JsonMapper<Souvenir> {
    private static final String SOUVENIRS_FILE = "souvenirs.json";

    private static JsonSouvenirMapper instance;

    private JsonSouvenirMapper() {
        super(SOUVENIRS_FILE);
    }

    public static JsonSouvenirMapper getInstance() {
        if (instance == null) {
            instance = new JsonSouvenirMapper();
        }
        return instance;
    }

    @Override
    protected Class<Souvenir> getMappedClass() {
        return Souvenir.class;
    }
}
