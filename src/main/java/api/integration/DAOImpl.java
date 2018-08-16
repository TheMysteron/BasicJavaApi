package api.integration;

import api.business.model.Model;
import api.business.model.Response;
import api.common.exceptions.ResourceNotFoundException;
import api.common.exceptions.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DAOImpl implements DAO {

    private static Logger logger = LoggerFactory
            .getLogger(DAOImpl.class);

    /**
     * Get the current tax year information for an individual
     *
     * @param id - the id for the information requested
     * @return the current tax year information for an individual
     */
    @Override
    public Response getInfo(String id) {

        logger.debug("Call started for identifier {}", id);

        Response response = new Response();

        long startTime = System.currentTimeMillis();
        List<Model> list = new ArrayList<>();

        ArrayList<Model> models = new ArrayList<>();

        try {

            // Attempt to obtain data here
            // ...
            // ...

            // Temp create list of strings
            list.add(new Model("name","al1","al2","al3","al4","al5","pc"));

        } catch (Exception e) {
            throw new ServiceUnavailableException("Unable to retrieve data", e);
        }

        if (list.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("No data available for this id: %s", id));
        }

        for (Model m : list) {


            // Set the data items that are needed
            // ...
            // ...

            logger.debug("Got row");

            Model model = new Model();
            model.setName(m.getName());
            model.setAddress_line_1(m.getAddress_line_1());
            model.setAddress_line_2(m.getAddress_line_2());
            model.setAddress_line_3(m.getAddress_line_3());
            model.setAddress_line_4(m.getAddress_line_4());
            model.setAddress_line_5(m.getAddress_line_5());
            model.setPostcode(m.getPostcode());

            models.add(model);
        }
        logger.info("Completed data SCAN in milliSeconds:{}", (System.currentTimeMillis() - startTime));

        logger.debug("Data call ended for identifier {}", id);

        response.setModels(models);

        // Close any connections
        // ...
        // ...

        return response;
    }


}