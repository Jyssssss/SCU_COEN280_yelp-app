package jshih.model.daos;

import java.util.List;

public interface DAO<T> {
    void create(List<T> models);
}
