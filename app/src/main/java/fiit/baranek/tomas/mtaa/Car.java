package fiit.baranek.tomas.mtaa;


public class Car {

    private String c_engine;
    private String created;
    private String c_phoneNumber;
    private int c_price;
    private String c_location;
    private int c_categoryBrand;
    private int c_yearOfProduction;
    private String c_model;
    private int c_mileAge;
    private String c_photo;
    private int c_categoryFuel;
    private int c_categoryTransmission;
    private String c_driveType;
    private String c_interiorColor;
    private String objectId;


    public String getC_engine() {
        return c_engine;
    }

    public void setC_engine(String c_engine) {
        this.c_engine = c_engine;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getC_phoneNumber() {
        return c_phoneNumber;
    }

    public void setC_phoneNumber(String c_phoneNumber) {
        this.c_phoneNumber = c_phoneNumber;
    }

    public int getC_price() {
        return c_price;
    }

    public void setC_price(int c_price) {
        this.c_price = c_price;
    }

    public String getC_location() {
        return c_location;
    }

    public void setC_location(String c_location) {
        this.c_location = c_location;
    }

    public int getC_categoryBrand() {
        return c_categoryBrand;
    }

    public void setC_categoryBrand(int c_categoryBrand) {
        this.c_categoryBrand = c_categoryBrand;
    }

    public int getC_yearOfProduction() {
        return c_yearOfProduction;
    }

    public void setC_yearOfProduction(int c_yearOfProduction) {
        this.c_yearOfProduction = c_yearOfProduction;
    }

    public String getC_model() {
        return c_model;
    }

    public void setC_model(String c_model) {
        this.c_model = c_model;
    }

    public int getC_mileAge() {
        return c_mileAge;
    }

    public void setC_mileAge(int c_mileAge) {
        this.c_mileAge = c_mileAge;
    }

    public String getC_photo() {
        return c_photo;
    }

    public void setC_photo(String c_photo) {
        this.c_photo = c_photo;
    }

    public int getC_categoryFuel() {
        return c_categoryFuel;
    }

    public void setC_categoryFuel(int c_categoryFuel) {
        this.c_categoryFuel = c_categoryFuel;
    }

    public int getC_categoryTransmission() {
        return c_categoryTransmission;
    }

    public void setC_categoryTransmission(int c_categoryTransmission) {
        this.c_categoryTransmission = c_categoryTransmission;
    }

    public String getC_driveType() {
        return c_driveType;
    }

    public void setC_driveType(String c_driveType) {
        this.c_driveType = c_driveType;
    }

    public String getC_interiorColor() {
        return c_interiorColor;
    }

    public void setC_interiorColor(String c_interiorColor) {
        this.c_interiorColor = c_interiorColor;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Car(){

    }

    /*
    Costructor for every Car elements
     */
    public Car (String c_engine, String created, String c_phoneNumber, int c_price, String c_location, int c_categoryBrand,
                int c_yearOfProduction, String c_model, int c_mileAge, String c_photo, int c_categoryFuel, int c_categoryTransmission,
                String c_driveType, String c_interiorColor, String objectId){

        this.c_engine = c_engine;
        this.created = created;
        this.c_phoneNumber = c_phoneNumber;
        this.c_price = c_price;
        this.c_location = c_location;
        this.c_categoryBrand = c_categoryBrand;
        this.c_yearOfProduction = c_yearOfProduction;
        this.c_model = c_model;
        this.c_mileAge = c_mileAge;
        this.c_photo = c_photo;
        this.c_categoryFuel = c_categoryFuel;
        this.c_categoryTransmission = c_categoryTransmission;
        this.c_driveType = c_driveType;
        this.c_interiorColor = c_interiorColor;
        this.objectId = objectId;
    }

}
