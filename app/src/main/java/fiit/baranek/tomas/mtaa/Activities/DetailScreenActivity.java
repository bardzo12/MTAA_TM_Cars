package fiit.baranek.tomas.mtaa.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import fiit.baranek.tomas.mtaa.MyAsyncTask;
import fiit.baranek.tomas.mtaa.R;
import fiit.baranek.tomas.mtaa.RequestParameters;
import fiit.baranek.tomas.mtaa.ResponseParameters;

public class DetailScreenActivity extends AppCompatActivity implements AsyncResponse {

    ImageView mImageView;
    Car SelectCar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        mImageView = (ImageView) findViewById(R.id.bgheader);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        Context context = this;
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(context, R.color.colorAccent));

        Bundle bundle = getIntent().getExtras();
        String CarID = bundle.getString("CarID");
        getDetail(CarID);

    }

    @Override
    public void processFinish(ResponseParameters responseParameters) {
        if(responseParameters.getResponseCode() == 200) {// add list od Cars do ListView adapter
            if(responseParameters.getType().equals("GET")) {
             SelectCar = responseParameters.getCar();


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
                price.setText(String.format(String.valueOf(SelectCar.getC_price())));

                new DownloadImage().execute(SelectCar.getC_photo());



            }
        }else{
            System.out.println("Niečo je zle");
        }
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
        r = new RequestParameters(https, "GET",2);

        MyAsyncTask asyncTask =new MyAsyncTask(this);
        asyncTask.delegate = this;
        asyncTask.execute(r);
    }
}
