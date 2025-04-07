package app.dao;

import java.util.List;

public interface IDAO<T, I> {

    T getById(I i);
    List<T> getAll();
    T create(T t);
    T update(I i, T t);
    void delete(I i);
}
