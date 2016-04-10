package fiit.baranek.tomas.mtaa.Activities;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import fiit.baranek.tomas.mtaa.Enums.CategoryBrand;
import fiit.baranek.tomas.mtaa.MyAsyncTask;
import fiit.baranek.tomas.mtaa.R;
import fiit.baranek.tomas.mtaa.RequestParameters;
import fiit.baranek.tomas.mtaa.ResponseParameters;

public class EditScreenActivity extends AppCompatActivity implements AsyncResponse{
    @Override
    public void processFinish(ResponseParameters output) {

    }

    private MaterialBetterSpinner brand;
    private EditText model;
    private EditText location;
    private EditText year;
    private EditText mile;
    private MaterialBetterSpinner transsmition;
    private EditText color;
    private EditText engine;
    private EditText drive;
    private MaterialBetterSpinner fuel;
    private EditText phone;
    private EditText photo;
    private EditText price;
    private String CarID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_screen);
        Bundle bundle = getIntent().getExtras();

    //    collapsingToolbarLayout.setTitle(SelectCar.getC_categoryBrand() + " " + SelectCar.getC_model());
        String[] SPINNERLIST1 = new String[]{"Škoda", "Mercedes","BMW", "Audi", "Ford"};
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

       String[] SPINNERLIST2 = new String[]{"manuálna", "automatická"};
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST2);
        transsmition = (MaterialBetterSpinner)
                findViewById(R.id.spinner_transmission_edit_screen);
        transsmition.setText(SPINNERLIST2[bundle.getInt("transmission")]);
        transsmition.setAdapter(arrayAdapter2);

        color = (EditText) findViewById( R.id.textViewColorValue_edit_screen);
        color.setText(bundle.getString("color"));

        engine = (EditText) findViewById( R.id.textViewEngineValue_edit_screen);
        engine.setText(bundle.getString("engine"));

        drive = (EditText) findViewById( R.id.textViewDriveTypeValue_edit_screen);
        drive.setText(bundle.getString("drivetype"));

        String[] SPINNERLIST3 = new String[]{"nafta", "benzín", "LPG", "hybrid", "elektromotor"};
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

        Button button = (Button) findViewById(R.id.SaveButton_edit_screen);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

              // updateCar();

            }
        });

    }




    public void updateCar(){



        JSONObject car = new JSONObject();
        try {
            car.put("c_engine", engine.getText());
            car.put("c_phoneNumber", phone.getText());
            car.put("c_price", price.getText());
            car.put("c_location", location.getText());
            car.put("c_categoryBrand", brand.getText());
            car.put("c_yearOfProduction", year.getText());
            car.put("c_model", model.getText());
            car.put("c_mileAge", mile.getText());
            car.put("c_photo", photo.getText());
            car.put("c_categoryFuel", fuel.getText());
            car.put("c_categoryTransmission", transsmition.getText());
            car.put("c_driveType", drive.getText());
            car.put("c_interiorColor", color.getText());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }






        RequestParameters r = null;
        URL https = null;
        try {
            https = new URL("https://api.backendless.com/v1/data/Car/"+CarID);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        r = new RequestParameters(https, "PUT", 1, isOnline(), this, "",car);

        MyAsyncTask asyncTask =new MyAsyncTask(this);
        asyncTask.delegate = this;
        asyncTask.execute(r);

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
}
