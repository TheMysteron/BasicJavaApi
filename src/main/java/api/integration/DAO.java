package api.integration;

import api.business.model.Response;

public interface DAO {
    Response getInfo(String id);
}

