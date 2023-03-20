package notebook.dao;

import java.util.List;

/**
 * Data Access Object (DAO) слой, с методами для работы с БД
 * @param <T> тип объекта для чтения/записи.
 */
public interface Operation<T> {
    List<T> readAll();
    void saveAll(List<T> data);
}
