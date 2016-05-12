package fiit.baranek.tomas.mtaa.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

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

public class EditScreenActivity extends AppCompatActivity implements AsyncResponse{
    @Override
    public void processFinish(ResponseParameters responseParameters) {

        if(responseParameters.getResponseCode() == 200) {// add list od Cars do ListView adapter
            if (responseParameters.getType().equals("PUT")) {


            } else if ((responseParameters.getType().equals("DELETE"))) {
                finish();
            }
        }


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
        setContentView(R.layout.activity_edit_screen);
        Bundle bundle = getIntent().getExtras();
        String[] SPINNERLIST1 = new String[]{"Škoda", "Mercedes","BMW", "Audi", "Ford"};
        String[] SPINNERLIST2 = new String[]{"automatická", "manuálna"};
        String[] SPINNERLIST3 = new String[]{"nafta", "benzín", "LPG", "hybrid", "elektromotor"};
        String[] SPINNERLIST4 = new String[]{"biela", "čierna", "hnedá"};

        toolbar = (Toolbar) findViewById(R.id.MyToolbar_edit_screen);
        toolbar.setTitle(SPINNERLIST1[bundle.getInt("brand")] + " " + bundle.getString("model"));
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        updatedCar.setC_image(getIntent().getByteArrayExtra("image"));

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, SPINNERLIST1);
        brand = (MaterialBetterSpinner)
                findViewById(R.id.spinner_brand_edit_screen);
        brand.setText(SPINNERLIST1[bundle.getInt("brand")]);
        brand.setAdapter(arrayAdapter1);


        model = (EditText) findViewById( R.id.textViewModelValue_edit_screen);
        model.setText(bundle.getString("model"));

        location = (EditText) findViewById( R.id.textViewAddressValue_edit_screen);
        location.setText(bundle.getString("location"));

        year = (EditText) findViewById( R.id.textViewYearOfProductionValue_edit_screen);
        year.setText(String.format(String.valueOf(bundle.getInt("year"))));

        mile = (EditText) findViewById( R.id.textViewMileAgeValue_edit_screen);
        mile.setText(String.format(String.valueOf(bundle.getInt("mileage"))));


        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, SPINNERLIST2);
        transsmition = (MaterialBetterSpinner)
                findViewById(R.id.spinner_transmission_edit_screen);
        transsmition.setText(SPINNERLIST2[bundle.getInt("transmission")]);
        transsmition.setAdapter(arrayAdapter2);

        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, SPINNERLIST4);
        color = (MaterialBetterSpinner)
                findViewById(R.id.spinner_color_edit_screen);
        color.setText(bundle.getString("color"));

        color.setAdapter(arrayAdapter4);


        engine = (EditText) findViewById( R.id.textViewEngineValue_edit_screen);
        engine.setText(bundle.getString("engine"));

        drive = (EditText) findViewById( R.id.textViewDriveTypeValue_edit_screen);
        drive.setText(bundle.getString("drivetype"));


        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, SPINNERLIST3);
        fuel = (MaterialBetterSpinner)
                findViewById(R.id.spinner_fuel_edit_screen);
        fuel.setText(SPINNERLIST3[bundle.getInt("fuel")]);
        fuel.setAdapter(arrayAdapter3);

        phone = (EditText) findViewById( R.id.textViewPhoneValue_edit_screen);
        phone.setText(bundle.getString("phonenumber"));

        price = (EditText) findViewById( R.id.textViewPriceValue_edit_screen);
        price.setText(String.format(String.valueOf(bundle.getInt("price"))));

        photo = (EditText) findViewById( R.id.textViewPhotoValue_edit_screen);
        photo.setText(bundle.getString("photo"));
        photoS = bundle.getString("photo");

        CarID = bundle.getString("ID");
        db = new DatabaseHandler(this);

        Button button = (Button) findViewById(R.id.SaveButton_edit_screen);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

              updateCar();

            }
        });

    }


    /**
     * This method update car
     */
    public void updateCar(){


        boolean valid= true;

            updatedCar.setC_engine(engine.getText().toString());

            updatedCar.setC_phoneNumber(phone.getText().toString());

        try {
            int num = Integer.parseInt(price.getText().toString());
            updatedCar.setC_price(num);
            Log.i("", num + " is a number");
        } catch (NumberFormatException e) {
            valid=false;
            price.setError("Nespravny format");
        }
            updatedCar.setC_location(location.getText().toString());
            CategoryBrand categoryBrand;
            categoryBrand = CategoryBrand.fromString(brand.getText().toString());

            updatedCar.setC_categoryBrand(categoryBrand.ordinal() + 1);

        try {
            int num = Integer.parseInt(year.getText().toString());
            updatedCar.setC_yearOfProduction(num);
            Log.i("", num + " is a number");
        } catch (NumberFormatException e) {
            valid=false;
            year.setError("Nespravny format");
        }
            updatedCar.setC_model(model.getText().toString());

        try {
            int num = Integer.parseInt(mile.getText().toString());
            updatedCar.setC_mileAge(num);
            Log.i("", num + " is a number");
        } catch (NumberFormatException e) {
            valid=false;
            mile.setError("Nespravny format");
        }


        updatedCar.setC_photo(photo.getText().toString());
            CategoryFuel categoryFuel;
            categoryFuel = CategoryFuel.fromString(fuel.getText().toString());

            updatedCar.setC_categoryFuel(categoryFuel.ordinal() + 1);
            CategoryTransmission categoryTransmission;
            categoryTransmission = CategoryTransmission.fromString(transsmition.getText().toString());

            updatedCar.setC_categoryTransmission(categoryTransmission.ordinal() + 1);

            updatedCar.setC_driveType(drive.getText().toString());

           updatedCar.setC_interiorColor(color.getText().toString());
            updatedCar.setObjectId(CarID);

if(valid){
    if(isOnline()) {

        db.updateCar(updatedCar);
        RequestParameters r = null;
        URL https = null;
        try {
            https = new URL("http://sandbox.touch4it.com:1341/?__sails_io_sdk_version=0.12.1");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        r = new RequestParameters(https, "PUT", 1, isOnline(), this, CarID, updatedCar.getJSON());

        MyAsyncTask asyncTask = new MyAsyncTask(this);
        asyncTask.delegate = this;
        asyncTask.execute(r);
        Toast.makeText(this,"Car was updated", Toast.LENGTH_SHORT).show();
        toolbar.setTitle(brand.getText().toString() + " " + model.getText());
    } else{
        showDialog();
    }
}





    }

    /**
     * This method update car when device is offline
     */
    private void updateOfline(){
        db.updateCar(updatedCar);
        long unixTime = System.currentTimeMillis();
        System.out.println(unixTime);
        updatedCar.setC_update(unixTime);
        db.addCarUpdated(updatedCar);
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





}
