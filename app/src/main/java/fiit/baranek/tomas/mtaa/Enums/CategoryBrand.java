package fiit.baranek.tomas.mtaa.Enums;

/**
 * Created by TomasPC on 16.3.2016.
 */
public enum CategoryBrand {
    BMW("BMW"),
    MERCEDES("Mercedes"),
    AUDI("Audi"),
    SKODA("Škoda"),
    CITROEN("Citroen");

    private String stringValue;

    CategoryBrand(String toString){
        stringValue = toString;

    }

    @Override
    public String toString(){
        return stringValue;
    }
}
