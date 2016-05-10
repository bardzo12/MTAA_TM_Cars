package fiit.baranek.tomas.mtaa.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fiit.baranek.tomas.mtaa.Car;
import fiit.baranek.tomas.mtaa.R;
import fiit.baranek.tomas.mtaa.WebSocket.WebSocket;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        WebSocket socket = new WebSocket();
        Car newCar = new Car();
        newCar.setC_categoryBrand(4);
        newCar.setC_model("A1");
        newCar.setC_location("Čadečka");
        newCar.setC_yearOfProduction(2011);
        newCar.setC_mileAge(78439);
        newCar.setC_price(1000000);
        newCar.setC_categoryTransmission(1);
        newCar.setC_interiorColor("čierna");
        newCar.setC_engine("6 Cyl 3.0L");
        newCar.setC_categoryFuel(3);
        newCar.setC_phoneNumber("0918573333");
        socket.createNew(newCar);
    }
}
