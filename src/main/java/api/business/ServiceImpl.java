package api.business;

import api.business.model.Response;
import api.integration.DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    private static final Logger logger = LoggerFactory
            .getLogger(ServiceImpl.class);

    @Autowired
    private DAO dao;

    @Override
    public Response getInfo(String id) {
        logger.debug("Entered Service Impl");
        return dao.getInfo(id);
    }
}
