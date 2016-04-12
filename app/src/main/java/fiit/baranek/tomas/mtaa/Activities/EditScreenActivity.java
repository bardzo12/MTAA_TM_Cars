package fiit.baranek.tomas.mtaa.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_screen);
        Bundle bundle = getIntent().getExtras();
        String[] SPINNERLIST1 = new String[]{"Škoda", "Mercedes","BMW", "Audi", "Ford"};
        String[] SPINNERLIST2 = new String[]{"automatická", "manuálna"};
        String[] SPINNERLIST3 = new String[]{"nafta", "benzín", "LPG", "hybrid", "elektromotor"};
        String[] SPINNERLIST4 = new String[]{"biela", "čierna", "hnedá"};

        Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar_edit_screen);
        toolbar.setTitle(SPINNERLIST1[bundle.getInt("brand")] + " " + bundle.getString("model"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST1);
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
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST2);
        transsmition = (MaterialBetterSpinner)
                findViewById(R.id.spinner_transmission_edit_screen);
        transsmition.setText(SPINNERLIST2[bundle.getInt("transmission")]);
        transsmition.setAdapter(arrayAdapter2);

        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST4);
        color = (MaterialBetterSpinner)
                findViewById(R.id.spinner_color_edit_screen);
        color.setText(bundle.getString("color"));

        color.setAdapter(arrayAdapter1);


        engine = (EditText) findViewById( R.id.textViewEngineValue_edit_screen);
        engine.setText(bundle.getString("engine"));

        drive = (EditText) findViewById( R.id.textViewDriveTypeValue_edit_screen);
        drive.setText(bundle.getString("drivetype"));


        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST3);
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

        CarID = bundle.getString("ID");
        db = new DatabaseHandler(this);

        Button button = (Button) findViewById(R.id.SaveButton_edit_screen);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

              updateCar();

            }
        });

    }




    public void updateCar(){

        Car updatedCar = new Car();

        JSONObject car = new JSONObject();
        try {
            car.put("c_engine", engine.getText());
            updatedCar.setC_engine(engine.getText().toString());
            car.put("c_phoneNumber", phone.getText());
            updatedCar.setC_phoneNumber(phone.getText().toString());
            car.put("c_price", price.getText());
            updatedCar.setC_price(Integer.parseInt(price.getText().toString()));
            car.put("c_location", location.getText());
            updatedCar.setC_location(location.getText().toString());
            CategoryBrand categoryBrand;
            categoryBrand = CategoryBrand.fromString(brand.getText().toString());
            car.put("c_categoryBrand", String.valueOf(categoryBrand.ordinal() + 1));
            updatedCar.setC_categoryBrand(categoryBrand.ordinal() + 1);
            car.put("c_yearOfProduction", year.getText());
            updatedCar.setC_yearOfProduction(Integer.parseInt(year.getText().toString()));
            car.put("c_model", model.getText());
            updatedCar.setC_model(model.getText().toString());
            car.put("c_mileAge", mile.getText());
            updatedCar.setC_mileAge(Integer.parseInt(mile.getText().toString()));
            car.put("c_photo", photo.getText());
            updatedCar.setC_photo(photo.getText().toString());
            CategoryFuel categoryFuel;
            categoryFuel = CategoryFuel.fromString(fuel.getText().toString());
            car.put("c_categoryFuel", String.valueOf(categoryFuel.ordinal() + 1));
            updatedCar.setC_categoryFuel(categoryFuel.ordinal() + 1);
            CategoryTransmission categoryTransmission;
            categoryTransmission = CategoryTransmission.fromString(transsmition.getText().toString());
            car.put("c_categoryTransmission", String.valueOf(categoryTransmission.ordinal() + 1));
            updatedCar.setC_categoryTransmission(categoryTransmission.ordinal() + 1);
            car.put("c_driveType", drive.getText());
            updatedCar.setC_driveType(drive.getText().toString());
            car.put("c_interiorColor", color.getText());
            updatedCar.setC_interiorColor(color.getText().toString());
            updatedCar.setObjectId(CarID);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        db.updateCar(updatedCar);

        if(isOnline()) {
            RequestParameters r = null;
            URL https = null;
            try {
                https = new URL("https://api.backendless.com/v1/data/Car/" + CarID);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            r = new RequestParameters(https, "PUT", 1, isOnline(), this, "", car);

            MyAsyncTask asyncTask = new MyAsyncTask(this);
            asyncTask.delegate = this;
            asyncTask.execute(r);
        } else{
            long unixTime = System.currentTimeMillis();
            System.out.println("System time unix: ");
            System.out.println(unixTime);
            updatedCar.setC_update(unixTime);
            db.addCarUpdated(updatedCar);
        }

    }

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
