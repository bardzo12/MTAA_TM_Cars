package fiit.baranek.tomas.mtaa;

import java.util.List;

/**
 * Created by matus on 18. 3. 2016.
 */
public class ResponseParameters {
    private List<Car> cars;
    private String type;

    public void setListOfCars(List<Car> cars){
        this.cars = cars;
    }

    public void setType(String type){
        this.type = type;
    }

    public List<Car> getListOfCars(){
        return this.cars;
    }

    public String getType(){
        return this.type;
    }

}
