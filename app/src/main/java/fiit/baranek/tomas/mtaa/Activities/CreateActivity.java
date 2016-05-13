package fiit.baranek.tomas.mtaa.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

import fiit.baranek.tomas.mtaa.AsyncResponse;
import fiit.baranek.tomas.mtaa.Car;
import fiit.baranek.tomas.mtaa.Database.DatabaseHandler;
import fiit.baranek.tomas.mtaa.Enums.CategoryBrand;
import fiit.baranek.tomas.mtaa.Enums.CategoryFuel;
import fiit.baranek.tomas.mtaa.Enums.CategoryTransmission;
import fiit.baranek.tomas.mtaa.MyAsyncTask;
import fiit.baranek.tomas.mtaa.R;
import fiit.baranek.tomas.mtaa.RequestParameters;
import fiit.baranek.tomas.mtaa.ResponseParameters;
import fiit.baranek.tomas.mtaa.WebSocket.WebSocket;

public class CreateActivity extends AppCompatActivity implements AsyncResponse {


    protected void vztvorenie() {

        setContentView(R.layout.activity_create);
        WebSocket socket = new WebSocket();
        Car newCar = new Car();
        newCar.setC_categoryBrand(4);
        newCar.setC_model("A7");
        newCar.setC_location("Čadečka");
        newCar.setC_yearOfProduction(2011);
        newCar.setC_mileAge(78439);
        newCar.setC_price(1000000);
        newCar.setC_categoryTransmission(1);
        newCar.setC_interiorColor("čierna");
        newCar.setC_engine("6 Cyl 3.0L");
        newCar.setC_categoryFuel(3);
        newCar.setC_phoneNumber("0918573333");
        newCar.setC_photo("https://a.tcimg.net/vehicle-images/inventory/21175/12/44/WAUFGAFB1BN064412/724MNTC6FPTEQGRJ6WTYAJRLBA-600.jpg?idx=0");
        socket.createNew(newCar);
    }

    private MaterialBetterSpinner brand;
    private EditText model;
    private EditText location;
    private EditText year;
    private EditText mile;
    private MaterialBetterSpinner transsmition;
    private MaterialBetterSpinner color;
    private EditText engine;
    private EditText drive;
    private MaterialBetterSpinner fuel;
    private EditText phone;
    private EditText photo;
    private EditText price;
    private String CarID;
    private String photoS;
    private DatabaseHandler db;
    private Toolbar toolbar;

    Car updatedCar = new Car();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        String[] SPINNERLIST1 = new String[]{"Škoda", "Mercedes","BMW", "Audi", "Ford"};
        String[] SPINNERLIST2 = new String[]{"automatická", "manuálna"};
        String[] SPINNERLIST3 = new String[]{"nafta", "benzín", "LPG", "hybrid", "elektromotor"};
        String[] SPINNERLIST4 = new String[]{"biela", "čierna", "hnedá"};

        toolbar = (Toolbar) findViewById(R.id.MyToolbar_create_screen);
        toolbar.setTitle("New car");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        updatedCar.setC_image(getIntent().getByteArrayExtra("image"));

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, SPINNERLIST1);
        brand = (MaterialBetterSpinner)
                findViewById(R.id.spinner_brand_create_screen);

        brand.setAdapter(arrayAdapter1);


        model = (EditText) findViewById( R.id.textViewModelValue_create_screen);


        location = (EditText) findViewById( R.id.textViewAddressValue_create_screen);


        year = (EditText) findViewById( R.id.textViewYearOfProductionValue_create_screen);


        mile = (EditText) findViewById( R.id.textViewMileAgeValue_create_screen);



        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, SPINNERLIST2);
        transsmition = (MaterialBetterSpinner)
                findViewById(R.id.spinner_transmission_create_screen);

        transsmition.setAdapter(arrayAdapter2);

        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, SPINNERLIST4);
        color = (MaterialBetterSpinner)
                findViewById(R.id.spinner_color_create_screen);


        color.setAdapter(arrayAdapter4);


        engine = (EditText) findViewById( R.id.textViewEngineValue_create_screen);


        drive = (EditText) findViewById( R.id.textViewDriveTypeValue_create_screen);



        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, SPINNERLIST3);
        fuel = (MaterialBetterSpinner)
                findViewById(R.id.spinner_fuel_create_screen);

        fuel.setAdapter(arrayAdapter3);

        phone = (EditText) findViewById( R.id.textViewPhoneValue_create_screen);


        price = (EditText) findViewById( R.id.textViewPriceValue_create_screen);


        photo = (EditText) findViewById( R.id.textViewPhotoValue_create_screen);

        db = new DatabaseHandler(this);

        Button button = (Button) findViewById(R.id.SaveButton_create_screen);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                saveCar();

            }
        });

    }


    /**
     * This method update car
     */
    public void saveCar(){

boolean valid= true;
            if(!engine.getText().toString().equals("")){
                updatedCar.setC_engine(engine.getText().toString());
            }else{
                valid=false;
                engine.setError("Zadaj typ motora");
            }




        if(!phone.getText().toString().equals("")){
            updatedCar.setC_phoneNumber(phone.getText().toString());
        }else{
            valid=false;
            phone.setError("Zadaj telefonne cislo");
        }

        if(!price.getText().toString().equals("")){
            try {
                int num = Integer.parseInt(price.getText().toString());
                updatedCar.setC_price(num);
                Log.i("", num + " is a number");
            } catch (NumberFormatException e) {
                valid=false;
                price.setError("Nespravny format");
            }
        }else{
            valid=false;
            price.setError("Zadaj cenu vozidla");
        }




        if(!location.getText().toString().equals("")){
            updatedCar.setC_location(location.getText().toString());
        }else{
            valid=false;
            location.setError("Zadaj miesto");
        }

        if(!brand.getText().toString().equals("")){
            CategoryBrand categoryBrand;
            categoryBrand = CategoryBrand.fromString(brand.getText().toString());

            updatedCar.setC_categoryBrand(categoryBrand.ordinal() + 1);
        }else{
            valid=false;
            brand.setError("Zadaj znacku");
        }






        if(!year.getText().toString().equals("")){
            try {
                int num = Integer.parseInt(year.getText().toString());
                updatedCar.setC_yearOfProduction(num);
                Log.i("", num + " is a number");
            } catch (NumberFormatException e) {
                valid=false;
                year.setError("Nespravny format");
            }
        }else{
            valid=false;
            year.setError("Zadaj rok vyroby");
        }

        if(!model.getText().toString().equals("")){
            updatedCar.setC_model(model.getText().toString());
        }else{
            valid=false;
            model.setError("Zadaj model");
        }




        if(!mile.getText().toString().equals("")){
            try {
                int num = Integer.parseInt(mile.getText().toString());
                updatedCar.setC_mileAge(num);
                Log.i("", num + " is a number");
            } catch (NumberFormatException e) {
                valid=false;
                mile.setError("Nespravny format");
            }
        }else{
            valid=false;
            mile.setError("Zadaj najazdene milometre");
        }

        if(!photo.getText().toString().equals("")){
            updatedCar.setC_photo(photo.getText().toString());

        }else{
            valid=false;
            photo.setError("Zadaj fotku");
        }

        if(!fuel.getText().toString().equals("")){
            CategoryFuel categoryFuel;
            categoryFuel = CategoryFuel.fromString(fuel.getText().toString());
            updatedCar.setC_categoryFuel(categoryFuel.ordinal() + 1);

        }else{
            valid=false;
            fuel.setError("Zadaj palivo");
        }
        if(!transsmition.getText().toString().equals("")){
            CategoryTransmission categoryTransmission;
            categoryTransmission = CategoryTransmission.fromString(transsmition.getText().toString());

            updatedCar.setC_categoryTransmission(categoryTransmission.ordinal() + 1);

        }else{
            valid=false;
            transsmition.setError("Zadaj prevodovku");
        }

        if(!drive.getText().toString().equals("")){
            updatedCar.setC_driveType(drive.getText().toString());

        }else{
            valid=false;
            drive.setError("Zadaj typ");
        }

        if(!color.getText().toString().equals("")){
            updatedCar.setC_interiorColor(color.getText().toString());

        }else{
            valid=false;
            color.setError("Zadaj farbu");
        }




if(valid){
    db.addCar(updatedCar);
    if(isOnline()) {
        //   db.updateCar(updatedCar);
        RequestParameters r = null;
        URL https = null;
        try {
            https = new URL("http://sandbox.touch4it.com:1341/?__sails_io_sdk_version=0.12.1");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        r = new RequestParameters(https, "POST", 1, isOnline(), this, db,"", updatedCar.getJSON());

        MyAsyncTask asyncTask = new MyAsyncTask(this);
        asyncTask.delegate = this;
        asyncTask.execute(r);
        Toast.makeText(this,"Car was saved", Toast.LENGTH_SHORT).show();
        toolbar.setTitle(brand.getText().toString() + " " + model.getText());
    } else{
        showDialog();
    }
}





    }
    public String createNew(Car car) {
        IO.Options opts = new IO.Options();
        opts.secure = false;
        opts.port = 1341;
        opts.reconnection = true;
        opts.forceNew = true;
        opts.timeout = 5000;
        Socket socket = null;
        try {
            socket = IO.socket("http://sandbox.touch4it.com:1341/?__sails_io_sdk_version=0.12.1", opts);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        JSONObject js = new JSONObject();
        try {
            UUID uid = UUID.fromString("f14f9190-c21b-446a-9b84-9c9ea2c1dc76");
            js.put("url", "/data/" + uid.toString());
            JSONObject obj = car.getJSON();
            js.put("data", new JSONObject().put("data", obj));
            //js.put("user",R.string.user_uuid);
            //js.put("id","f9df2caa-aaf3-4399-ad20-0a8519921647");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("post", js, new Ack() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject) args[0];
                System.out.println("Resposne create:" + response);
            }
        });
        return "";
    }
    /**
     * This method update car when device is offline
     */
    private void updateOfline(){
        long unixTime = System.currentTimeMillis();
        System.out.println(unixTime);
        updatedCar.setC_update(unixTime);
        db.addCarCreated(updatedCar);
        Toast.makeText(this,"Car was updated", Toast.LENGTH_SHORT).show();
        toolbar.setTitle(brand.getText().toString() + " " + model.getText());
    }


    private void showDialog() {
        new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("Uprava auta")
                .setMessage("Fotka nemôže byť zmenená bez pripojenia na internet. Chcete napriek tomu vykonať úpravu?")
                .setPositiveButton("ANO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateOfline();
                    }
                })
                .setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


    /**
     * Check internet connection
     * @return
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return false;
    }


    @Override
    public void processFinish(ResponseParameters output) {

    }
}
