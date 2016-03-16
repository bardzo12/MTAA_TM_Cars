package fiit.baranek.tomas.mtaa.Enums;

/**
 * Created by TomasPC on 16.3.2016.
 */
public enum CategoryFuel {
    NAFTA("nafta"),
    BENZIN("benzin"),
    LPG("LPG");

    private String stringValue;

    CategoryFuel(String toString){
        stringValue = toString;

    }

    @Override
    public String toString(){
        return stringValue;
    }
}
