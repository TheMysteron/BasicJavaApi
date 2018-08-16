package api.business.model;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private List<Model> models = new ArrayList<>();

    public Response() {
    }

    public Response(List<Model> models) {
        this.models = models;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }
}
