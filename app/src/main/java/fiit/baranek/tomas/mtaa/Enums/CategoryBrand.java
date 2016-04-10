package fiit.baranek.tomas.mtaa.Enums;

/**
 * Created by TomasPC on 16.3.2016.
 */
public enum CategoryBrand {
    SKODA("Škoda"),
    MERCEDES("Mercedes"),
    BMW("BMW"),
    AUDI("Audi"),
    CITROEN("Ford");

    private String stringValue;

    public static CategoryBrand fromString(String text) {
        if (text != null) {
            for (CategoryBrand b : CategoryBrand.values()) {
                if (text.equalsIgnoreCase(b.stringValue)) {
                    return b;
                }
            }
        }
        return null;
    }
    CategoryBrand(String toString){
        stringValue = toString;

    }

    @Override
    public String toString(){
        return stringValue;
    }


}
