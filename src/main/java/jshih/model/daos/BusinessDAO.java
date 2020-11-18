package jshih.model.daos;

import jshih.model.models.Business;

public interface BusinessDAO extends DAO<Business> {
    Business getBusiness(String buisnessUid);
}
