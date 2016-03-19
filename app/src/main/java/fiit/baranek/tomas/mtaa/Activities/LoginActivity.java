package fiit.baranek.tomas.mtaa.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
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
                // if(mEmailView.getText().equals("ProjektMTAA") && mPasswordView.getText().equals("heslo123")){

                login();
                //  }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void login(){
        Intent scan = new Intent(this,ShowAllCarsActivity.class);
        startActivity(scan);
    }


    public void Nova(View v){
        Intent intent = new Intent(this, DetailikActivity.class);
        startActivity(intent);
    }

}

