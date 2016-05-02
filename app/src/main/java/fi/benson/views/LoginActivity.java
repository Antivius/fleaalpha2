package fi.benson.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fi.benson.R;

public class LoginActivity extends AppCompatActivity {

    private static final String APP_ID = "C9435C02-8205-26CC-FF63-3067244CAF00";
    private static final String SECRET_KEY="D1A3A4A0-BD38-37CB-FF5E-D582B1E47600";

    private  EditText et_login_email, et_login_passwd;
    private  EditText et_sign_name,   et_sign_email,  et_sign_passwd;
    private  Button   btn_login,      btn_signup;

    private  FABRevealLayout fabRevealLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String appVersion = "v1";
        Backendless.initApp(this, APP_ID, SECRET_KEY, appVersion);

        initUi();




    }


    public void initUi(){

        et_login_email      = (EditText) findViewById(R.id.et_login_email);
        et_login_passwd     = (EditText) findViewById(R.id.et_login_passwd);
        et_sign_name        = (EditText) findViewById(R.id.et_sign_username);
        et_sign_email       = (EditText) findViewById(R.id.et_sign_email);
        et_sign_passwd      = (EditText) findViewById(R.id.et_sign_passwd);
        btn_login           = (Button) findViewById(R.id.btn_login);
        btn_signup          = (Button) findViewById(R.id.btn_signup);


        fabRevealLayout = (FABRevealLayout) findViewById(R.id.fab_reveal_layout);
        configureFABReveal(fabRevealLayout);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String _email  = et_login_email.getText().toString();
                final String _passwd = et_login_passwd.getText().toString();
                //signIn(_email,_passwd);


                    LoadingCallback<BackendlessUser> loginCallback = createLoginCallback();

                    loginCallback.showLoading();
                    loginUser(_email, _passwd, loginCallback);

            }
        });



        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });



    }

    public LoadingCallback<BackendlessUser> createLoginCallback()
    {
        return new LoadingCallback<BackendlessUser>( this, getString( R.string.loading_login ) )
        {
            @Override
            public void handleResponse( BackendlessUser loggedInUser )
            {
                super.handleResponse(loggedInUser);
                Toast.makeText( LoginActivity.this, String.format( getString( R.string.info_logged_in ), loggedInUser.getObjectId() ), Toast.LENGTH_LONG ).show();

                Intent userActionIntent = new Intent( LoginActivity.this, MainActivity.class );
                startActivity( userActionIntent );
                finish();
            }
        };
    }

    public void loginUser( String email, String password, AsyncCallback<BackendlessUser> loginCallback )
    {
        Backendless.UserService.login(email, password, loginCallback);
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void configureFABReveal(FABRevealLayout fabRevealLayout) {
        fabRevealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabRevealLayout, View mainView) {}

            @Override
            public void onSecondaryViewAppeared(final FABRevealLayout fabRevealLayout, View secondaryView) {

            }
        });
    }

    private void prepareBackTransition(final FABRevealLayout fabRevealLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabRevealLayout.revealMainView();
            }
        }, 10000);
    }





    public void showDialog(){
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Signing You Up");
        pDialog.setCancelable(false);
        pDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {pDialog.dismiss();
                    }
                }, 3000);
    }


    public void signUp(){

        final String name   = et_sign_name.getText().toString();
        final String passwd = et_sign_passwd.getText().toString();
        final String email     = et_sign_email.getText().toString();

        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(passwd);
        user.setEmail(email);


        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    signIn(name, passwd);
                } else {
                    Toast.makeText(getApplicationContext(), "Baad", Toast.LENGTH_SHORT).show();
                }
            }
        });
        showDialog();
    }

    public void signIn(String name, String passwd) {
        ParseUser.logInInBackground(name, passwd, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {

                    Toast.makeText(getApplicationContext(), "Good", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Not signed in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
