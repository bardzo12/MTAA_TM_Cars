package fiit.baranek.tomas.mtaa.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import fiit.baranek.tomas.mtaa.AsyncResponse;
import fiit.baranek.tomas.mtaa.Car;
import fiit.baranek.tomas.mtaa.Database.DatabaseHandler;
import fiit.baranek.tomas.mtaa.MyAsyncTask;
import fiit.baranek.tomas.mtaa.R;
import fiit.baranek.tomas.mtaa.RequestParameters;
import fiit.baranek.tomas.mtaa.ResponseParameters;

public class DetailScreenActivity extends AppCompatActivity implements AsyncResponse {

    ImageView mImageView;
    Car SelectCar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private String CarID;
    private int CarBrandInt;
    private int CarFuelInt;
    private int CarTransmissionInt;
    private DatabaseHandler db;
    private byte[] fotecka;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        mImageView = (ImageView) findViewById(R.id.bgheader);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        Context context = this;
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(context, R.color.colorPrimary));

        Bundle bundle = getIntent().getExtras();
        CarID = bundle.getString("CarID");


        db = new DatabaseHandler(this);



    }

    @Override
    protected void onResume(){
        super.onResume();
        getDetail(CarID);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public void processFinish(ResponseParameters responseParameters) {




        Log.i("Code",""+responseParameters.getResponseCode());
        if(responseParameters.getResponseCode() == 200) {// add list od Cars do ListView adapter
            if(responseParameters.getType().equals("GET")) {
             SelectCar = responseParameters.getCar();


                CarBrandInt = SelectCar.getC_categoryBrandInt();
                CarFuelInt = SelectCar.getC_categoryFuelInt();
                CarTransmissionInt = SelectCar.getC_categoryTransmissionInt();


                collapsingToolbarLayout.setTitle(SelectCar.getC_categoryBrand() + " " + SelectCar.getC_model());

                TextView location = (TextView) findViewById( R.id.textViewAddressValue);
                location.setText(SelectCar.getC_location());

                TextView year = (TextView) findViewById( R.id.textViewYearOfProductionValue);
                year.setText(String.format(String.valueOf(SelectCar.getC_yearOfProduction())));

                TextView mile = (TextView) findViewById( R.id.textViewMileAgeValue);
                mile.setText(String.format(String.valueOf(SelectCar.getC_mileAge())));

                TextView transsmition = (TextView) findViewById( R.id.textViewTransmissionValue);
                transsmition.setText(SelectCar.getC_categoryTransmission());

                TextView color = (TextView) findViewById( R.id.textViewColorValue);
                color.setText(SelectCar.getC_interiorColor());

                TextView engine = (TextView) findViewById( R.id.textViewEngineValue);
                engine.setText(SelectCar.getC_engine());

                TextView drive = (TextView) findViewById( R.id.textViewDriveTypeValue);
                drive.setText(SelectCar.getC_driveType());

                TextView fuel = (TextView) findViewById( R.id.textViewFuelValue);
                fuel.setText(SelectCar.getC_categoryFuel());

                TextView phone = (TextView) findViewById( R.id.textViewPhoneValue);
                phone.setText(SelectCar.getC_phoneNumber());

                TextView price = (TextView) findViewById( R.id.textViewPriceValue);
                price.setText(String.format(String.valueOf(SelectCar.getC_price())) + "€");

                fotecka = SelectCar.getC_image();

                new DownloadImage().execute(SelectCar.getC_photo());

                Button button = (Button) findViewById(R.id.EditButton);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        startEdit( SelectCar.getC_model(), SelectCar.getC_location(), SelectCar.getC_yearOfProduction(),
                                SelectCar.getC_mileAge(), SelectCar.getC_interiorColor(), SelectCar.getC_engine(),
                                SelectCar.getC_driveType(), SelectCar.getC_phoneNumber(), SelectCar.getC_price(),
                                SelectCar.getC_photo(), CarID, CarBrandInt, CarFuelInt,CarTransmissionInt, SelectCar.getC_image());

                    }
                });

                collapsingToolbarLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startDetailFoto(SelectCar.getC_photo(),SelectCar.getC_image());
                    }
                });



            }else if((responseParameters.getType().equals("DELETE"))){
                finish();
            }
              }else if(responseParameters.getResponseCode() == 400){

        Toast.makeText(DetailScreenActivity.this, "BAD REQUEST ON DATABASE!", Toast.LENGTH_SHORT).show();

    }else if(responseParameters.getResponseCode() == 404){

            Toast.makeText(DetailScreenActivity.this, "Toto auto uz bolo vymazane!", Toast.LENGTH_SHORT).show();
        finish();
    }else if(500<=responseParameters.getResponseCode() && responseParameters.getResponseCode()<=510){

        Toast.makeText(DetailScreenActivity.this, "Chyba serverovej časti aplikácie, dáta nie sú dostupné!", Toast.LENGTH_SHORT).show();
    }
    }

    public void startDetailFoto(String photo, byte[] byteArray){
        Intent intent = new Intent(this, ActivityFotoDetail.class);
        intent.putExtra("photo", photo);
        intent.putExtra("image",byteArray);
        startActivity(intent);
    }

    public void startEdit( String Model,String Location, int YearOfProduction, int MileAge,
                          String Color, String Engine, String DriveType, String phoneNumber, int price, String photo,
                          String ID,int brand, int Fuel, int Transmission, byte[] byteArray){
        Intent intent = new Intent(this, EditScreenActivity.class);
        intent.putExtra("brand", brand);
        intent.putExtra("model", Model);
        intent.putExtra("location", Location);
        intent.putExtra("year", YearOfProduction);
        intent.putExtra("mileage", MileAge);
        intent.putExtra("transmission", Transmission);
        intent.putExtra("color", Color);
        intent.putExtra("engine", Engine);
        intent.putExtra("drivetype", DriveType);
        intent.putExtra("fuel", Fuel);
        intent.putExtra("phonenumber", phoneNumber);
        intent.putExtra("price", price);
        intent.putExtra("photo", photo);
        intent.putExtra("ID", ID);
        intent.putExtra("image",byteArray);
        startActivity(intent);
    }


    private void setImage(Drawable drawable)
    {
        mImageView.setBackgroundDrawable(drawable);
    }

    public class DownloadImage extends AsyncTask<String, Integer, Drawable> {

        @Override
        protected Drawable doInBackground(String... arg0) {
            // This is done in a background thread
            if(isOnline())
                return downloadImage(arg0[0]);
            else
                return new BitmapDrawable(BitmapFactory.decodeByteArray(fotecka, 0, fotecka.length));
        }

        /**
         * Called after the image has been downloaded
         * -> this calls a function on the main thread again
         */
        protected void onPostExecute(Drawable image)
        {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            findViewById(R.id.bgheader).setVisibility(View.VISIBLE);
            setImage(image);
        }


        /**
         * Actually download the Image from the _url
         * @param _url
         * @return
         */
        private Drawable downloadImage(String _url)
        {
            //Prepare to download image
            URL url;
            BufferedOutputStream out;
            InputStream in;
            BufferedInputStream buf;

            //BufferedInputStream buf;
            try {
                url = new URL(_url);
                in = url.openStream();

                // Read the inputstream
                buf = new BufferedInputStream(in);

                // Convert the BufferedInputStream to a Bitmap
                Bitmap bMap = BitmapFactory.decodeStream(buf);
                if (in != null) {
                    in.close();
                }
                if (buf != null) {
                    buf.close();
                }

                return new BitmapDrawable(bMap);

            } catch (Exception e) {
                Log.e("Error reading file", e.toString());
            }

            return null;
        }

    }

    /**
     * get detail car with Id
     * @param Id
     */

    public void getDetail(String Id){

        RequestParameters r = null;
        URL https = null;
        try {
            https = new URL("https://api.backendless.com/v1/data/Car/" + Id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        r = new RequestParameters(https, "GET",2, isOnline(),this, Id);

        MyAsyncTask asyncTask =new MyAsyncTask(this);
        asyncTask.delegate = this;
        asyncTask.execute(r);
    }

    /**
     * Checks whether the network is available.
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:
                new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                        .setTitle("Vymazanie auta")
                        .setMessage("Naozaj chcete vymazat toto auto?")
                        .setPositiveButton("ANO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCarById(SelectCar);
                                finish();
                            }
                        })
                        .setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Delete car by ID
     */
    public boolean deleteCarById(Car CarID){
        if(isOnline()) {
            RequestParameters r = null;
            URL https = null;
            try {
                https = new URL("https://api.backendless.com/v1/data/Car/" + CarID.getObjectId());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            r = new RequestParameters(https, "DELETE", 1, isOnline(), this, CarID.getObjectId());

            MyAsyncTask asyncTask = new MyAsyncTask(this);
            asyncTask.delegate = this;
            asyncTask.execute(r);

            return true;
        }else{
            db.addCarDeleted(CarID);
            return false;
        }
    }
}
