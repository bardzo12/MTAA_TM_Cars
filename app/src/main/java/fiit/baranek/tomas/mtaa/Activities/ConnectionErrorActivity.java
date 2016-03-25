package fiit.baranek.tomas.mtaa.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fiit.baranek.tomas.mtaa.R;

public class ConnectionErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_error);
        Button ButtonRefresh = (Button) findViewById(R.id.refreshButton);
        ButtonRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               login();
            }
        });
    }


    public void login(){
        if(isOnline()) {
            Intent scan = new Intent(this, ShowAllCarsActivity.class);
            finish();
            startActivity(scan);
        }
        else{
            Intent intent = new Intent(this, ConnectionErrorActivity.class);
            finish();
            startActivity(intent);
        }
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
}
