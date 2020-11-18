package jshih.model.daos;

import jshih.model.models.Category;

public interface CategoryDAO extends DAO<Category> {
    Category getCategory(String name);
}
