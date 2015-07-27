package uk.ac.gla.dcs.gms.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import uk.ac.gla.dcs.gms.Utils;
import uk.ac.gla.dcs.gms.api.APIHandler;
import uk.ac.gla.dcs.gms.api.http.APIHttpJSONResponse;
import uk.ac.gla.dcs.gms.api.http.HTTPCustomException;
import uk.ac.gla.dcs.gms.api.lms.LMSAuthenticationRequest;
import uk.ac.gla.dcs.gms.api.lms.LMSImageRequest;
import uk.ac.gla.dcs.gms.api.lms.LMSImageRequestParamBuilder;
import uk.ac.gla.dcs.gms.api.lms.LMSUserRequest;
import uk.ac.gla.dcs.gms.api.Security;
import uk.ac.gla.dcs.gms.api.SecurityException;

@SuppressWarnings("deprecation")
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private final static int CODE_REGISTER = 0;

    private EditText editTxtEmail;
    private EditText editTxtPassword;
    private ImageView imgViewLogo;

    private Button btnRegister;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Security.initialize(getApplicationContext());

        } catch (uk.ac.gla.dcs.gms.api.SecurityException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.login);


        if (Security.hasToken(getApplicationContext())){
            login();
        }

        editTxtEmail = (EditText) findViewById(R.id.login_edittxt_email);
        editTxtPassword = (EditText) findViewById(R.id.login_edittxt_password);
        imgViewLogo = (ImageView) findViewById(R.id.login_iview_logo);
        btnRegister = (Button) findViewById(R.id.login_btn_register);
        btnLogin = (Button) findViewById(R.id.login_btn);

        imgViewLogo.setImageResource(R.drawable.bonus_coin);

        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == CODE_REGISTER) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = intent.getExtras();
                String email = bundle.getString(RegisterActivity.KEY_EMAIL);
                editTxtEmail.setText(email);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Register Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn_register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, CODE_REGISTER);
        } else if (v.getId() == R.id.login_btn) {

            if (editTxtEmail.getText().toString().length() == 0) {
                Utils.shortToast(getApplicationContext(), "Please input your e-mail.");
            } else if (editTxtPassword.getText().toString().length() == 0) {
                Utils.shortToast(getApplicationContext(), "Please input your password.");
            } else {

                LMSAuthenticationRequest request = new LMSAuthenticationRequest(getApplicationContext(), LMSAuthenticationRequest.LOGIN) {
                    @Override
                    protected void onPostExecute(APIHttpJSONResponse s) {
                        try {
                            String tokenFromResponse = getTokenFromResponse(s);
                            Security.setToken(getApplicationContext(), tokenFromResponse);
                            login();
                        } catch (HTTPCustomException e) {
                            Log.e(TAG, e.getMessage());
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }//login anyway
                        catch (SecurityException e) {
                            e.printStackTrace();
                            Utils.shortToast(getApplicationContext(), "Security failure.");
                        }
                        //startActivity(new Intent(LoginActivity.this, MainActivity.class)); //debug
                    }
                };
                APIHandler.login(request, editTxtEmail.getText().toString(), editTxtPassword.getText().toString());
//            APIHandler.login(request, getResources().getString(R.string.debug_username), getResources().getString(R.string.debug_password)); //debug
            }
        }
    }

    private void login(){
        LMSUserRequest request = new LMSUserRequest(this) {
            @Override
            protected void onPostExecute(APIHttpJSONResponse apiHttpResponse) {
                super.onPostExecute(apiHttpResponse);

                try {
                    saveUserInfo(apiHttpResponse);
                } catch (HTTPCustomException e) {
                    e.printStackTrace();
                    Log.e(TAG,"User info request failed. "+ e.getMessage()); //FIXME: handle error
                }
            }
        };
        request.execute();

        LMSImageRequest imgRequest = new LMSImageRequest(this) {
            @Override
            protected void onPostExecute(APIHttpJSONResponse apiHttpResponse) {
                try {
                    Object imageList = getImageList(apiHttpResponse);
                } catch (HTTPCustomException e) {
                    e.printStackTrace();
                }
            }
        };
        imgRequest.execute(new LMSImageRequestParamBuilder().setKeyMoments().setLimit(10).setPersonal(true).setSkip(0).toString());


        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

}

