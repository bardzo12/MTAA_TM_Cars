package fiit.baranek.tomas.mtaa.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import fiit.baranek.tomas.mtaa.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;




    // UI references.
    private TextView mEmailView;
    private TextView mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (TextView) findViewById(R.id.email);
        mPasswordView = (TextView) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {//inicializing action listener for login button
/*
                if(mEmailView.getText().toString().equals("")){
                    mEmailView.setError("Nebol zadaný prihlasovací E-mail");
                }else {
                    if (mPasswordView.getText().toString().equals("")) {
                        mPasswordView.setError("Nebolo zadané prihlasovacie heslo");
                    } else {
                        if (mEmailView.getText().toString().equals("mta@mta.sk") && mPasswordView.getText().toString().equals("mta")) {
                            if (!isEmailValid(mEmailView.getText().toString())) {
                                mEmailView.setError("Zle zadaný E-mail");
                            } else
                                login();
                        } else {
                            mPasswordView.setError("Zlé prihlasovacie údaje");
                        }
                    }
                }*/
                login();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mEmailView.setText("");
        mPasswordView.setText("");
    }

    public void login(){

        //if(isOnline()) {
        Intent scan = new Intent(this, ShowAllCarsActivity.class);

        startActivity(scan);
        /*}
        else{
            Intent intent = new Intent(this, ConnectionErrorActivity.class);
            intent.putExtra("ActivityID", 1);
            startActivity(intent);
        }*/
    }


    public void Edit(View v) {
        Intent intent = new Intent(this, EditScreenActivity.class);
        startActivity(intent);
    }



    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }


    /**
     * Checks whether the network is available.
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}

