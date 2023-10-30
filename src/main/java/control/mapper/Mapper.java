package control.mapper;

import java.util.List;

public interface Mapper<T> {
    List<T> read();

    void write(List<T> list);
}
