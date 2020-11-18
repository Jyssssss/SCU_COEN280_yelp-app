package jshih.model.daos;

import jshih.model.models.YelpUser;

public interface YelpUserDAO extends DAO<YelpUser> {
    YelpUser getYelpUser(String userUid);
}
