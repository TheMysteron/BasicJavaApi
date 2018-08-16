package api.business.model;

public class Model {

    private String name;
    private String address_line_1;
    private String address_line_2;
    private String address_line_3;
    private String address_line_4;
    private String address_line_5;
    private String postcode;

    public Model() {
    }

    public Model(String name, String address_line_1, String address_line_2, String address_line_3, String address_line_4, String address_line_5, String postcode) {
        this.name = name;
        this.address_line_1 = address_line_1;
        this.address_line_2 = address_line_2;
        this.address_line_3 = address_line_3;
        this.address_line_4 = address_line_4;
        this.address_line_5 = address_line_5;
        this.postcode = postcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getAddress_line_3() {
        return address_line_3;
    }

    public void setAddress_line_3(String address_line_3) {
        this.address_line_3 = address_line_3;
    }

    public String getAddress_line_4() {
        return address_line_4;
    }

    public void setAddress_line_4(String address_line_4) {
        this.address_line_4 = address_line_4;
    }

    public String getAddress_line_5() {
        return address_line_5;
    }

    public void setAddress_line_5(String address_line_5) {
        this.address_line_5 = address_line_5;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}