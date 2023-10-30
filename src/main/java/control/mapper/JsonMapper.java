package control.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class JsonMapper<T> implements Mapper<T> {
    private final ObjectMapper objectMapper;
    private final String fileName;

    protected JsonMapper(String fileName) {
        this.fileName = fileName;
        this.objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<T> read() {
        List<T> items = null;
        try {
            items = objectMapper.readValue(new File(fileName), objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, getMappedClass()));
        } catch (IOException ioException) {
            ioException.getMessage();
        }
        return items;
    }

    @Override
    public void write(List<T> items) {
        try {
            objectMapper.writeValue(new File(fileName), items);
        } catch (IOException ioException) {
            ioException.getMessage();
        }
    }

    protected abstract Class<T> getMappedClass();
}
