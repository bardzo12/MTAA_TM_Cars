package fiit.baranek.tomas.mtaa.Enums;

/**
 * Created by TomasPC on 16.3.2016.
 */
public enum CategoryTransmission {
    AUTOMATIC("automatická"),
    MANUAL("manuálna");


    private String stringValue;

    CategoryTransmission(String toString){
        stringValue = toString;

    }

    public static CategoryTransmission fromString(String text) {
        if (text != null) {
            for (CategoryTransmission b : CategoryTransmission.values()) {
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
