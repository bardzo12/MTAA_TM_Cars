package fiit.baranek.tomas.mtaa.Enums;

/**
 * Created by TomasPC on 16.3.2016.
 */
public enum CategoryFuel {
    NAFTA("nafta"),
    BENZIN("benzín"),
    LPG("LPG"),
    HYBRID("hybrid"),
    ELEKTROMOTOR("elektromotor");

    private String stringValue;

    CategoryFuel(String toString){
        stringValue = toString;

    }
    public static CategoryFuel fromString(String text) {
        if (text != null) {
            for (CategoryFuel b : CategoryFuel.values()) {
                if (text.equalsIgnoreCase(b.stringValue)) {
                    return b;
                }
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return stringValue;
    }
}
