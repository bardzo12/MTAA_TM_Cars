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
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        getDetail(CarID);
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


                if (isOnline()) new DownloadImage().execute(SelectCar.getC_photo());

                Button button = (Button) findViewById(R.id.EditButton);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        startEdit( SelectCar.getC_model(), SelectCar.getC_location(), SelectCar.getC_yearOfProduction(),
                                SelectCar.getC_mileAge(), SelectCar.getC_interiorColor(), SelectCar.getC_engine(),
                                SelectCar.getC_driveType(), SelectCar.getC_phoneNumber(), SelectCar.getC_price(),
                                SelectCar.getC_photo(), CarID, CarBrandInt, CarFuelInt,CarTransmissionInt);

                    }
                });

                collapsingToolbarLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startDetailFoto(SelectCar.getC_photo());
                    }
                });



            }else if((responseParameters.getType().equals("DELETE"))){
                finish();
            }
        }else{
            System.out.println("Niečo je zle");
        }
    }

    public void startDetailFoto(String photo){
        Intent intent = new Intent(this, ActivityFotoDetail.class);
        intent.putExtra("photo", photo);
        startActivity(intent);
    }

    public void startEdit( String Model,String Location, int YearOfProduction, int MileAge,
                          String Color, String Engine, String DriveType, String phoneNumber, int price, String photo,
                          String ID,int brand, int Fuel, int Transmission){
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
            return downloadImage(arg0[0]);
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

            /*
             * THIS IS NOT NEEDED
             *
             * YOU TRY TO CREATE AN ACTUAL IMAGE HERE, BY WRITING
             * TO A NEW FILE
             * YOU ONLY NEED TO READ THE INPUTSTREAM
             * AND CONVERT THAT TO A BITMAP
            out = new BufferedOutputStream(new FileOutputStream("testImage.jpg"));
            int i;

             while ((i = in.read()) != -1) {
                 out.write(i);
             }
             out.close();
             in.close();
             */

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
                        .setTitle("Delete car")
                        .setMessage("Are you sure you want to delete this car?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCarById(SelectCar);
                                finish();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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
