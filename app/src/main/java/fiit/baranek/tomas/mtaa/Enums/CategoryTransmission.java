package fiit.baranek.tomas.mtaa.Enums;

/**
 * Created by TomasPC on 16.3.2016.
 */
public enum CategoryTransmission {
    MANUAL("manual"),
    AUTOMATIC("automatic");


    private String stringValue;

    CategoryTransmission(String toString){
        stringValue = toString;

    }

    @Override
    public String toString(){
        return stringValue;
    }
}
