package uk.ac.gla.dcs.gms.lms;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.gla.dcs.gms.api.APIHandler;
import uk.ac.gla.dcs.gms.api.APIResponse;
import uk.ac.gla.dcs.gms.api.LMSLoginRequest;

@SuppressWarnings("deprecation")
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    final static int REGISTER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ((ImageView)findViewById(R.id.login_iview_logo)).setImageResource(R.drawable.bonus_coin);

        findViewById(R.id.login_btn_register).setOnClickListener(this);
        findViewById(R.id.login_btn).setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REGISTER) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = intent.getExtras();
                String email = (String) bundle.get(RegisterActivity.EMAIL);

                ((EditText)findViewById(R.id.login_edittxt_email)).setText(email);
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Register Canceled", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn_register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, REGISTER);
        }
        else if (v.getId() == R.id.login_btn) {


            LMSLoginRequest request = new LMSLoginRequest(getApplicationContext()) {
                @Override
                protected void onPostExecute(APIResponse s) {
                    String dataField = getResources().getString(R.string.success_field_data);
                    String errorField = getResources().getString(R.string.error_field_errors);

                    //check if had an exception
                    if (s.isFailed()){
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_messages_genericNetworkFail), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        //check if server returned error message
                        if (s.getJsonObject().has(errorField)){
                            try {
                                Object errorMsg = s.getJsonObject().get(errorField);
                                //get the error message
                                if (errorMsg instanceof JSONArray)
                                {
                                    JSONArray jArray = (JSONArray)errorMsg;

                                    if (jArray.length() > 0) {
                                        Toast toast = Toast.makeText(getApplicationContext(), jArray.getString(0), Toast.LENGTH_SHORT);
                                        toast.show();
                                        return;
                                    }
                                }
                                // if dont have error message, display raw message
                                Toast toast = Toast.makeText(getApplicationContext(), s.getRawResponse(), Toast.LENGTH_SHORT);
                                toast.show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_messages_genericNetworkFail), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }else if (s.getJsonObject().has(dataField)){
                            //success
                            try {
                                Object dataMessage = s.getJsonObject().get(dataField);
                                //get the error message
                                if (dataMessage instanceof String)
                                {
                                    String token = (String)dataMessage;
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                                //failed to receive data
                                Log.e(TAG, "Failed to receive data");
                                Toast.makeText(getApplicationContext(), s.getRawResponse(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Log.e(TAG, "Exception error when receiving data response");
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),  s.getRawResponse(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            };
            APIHandler.login(request, getResources().getString(R.string.username), getResources().getString(R.string.password));
        }
    }
}

