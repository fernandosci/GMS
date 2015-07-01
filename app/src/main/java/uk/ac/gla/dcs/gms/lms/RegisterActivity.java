package uk.ac.gla.dcs.gms.lms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import uk.ac.gla.dcs.gms.Utils;
import uk.ac.gla.dcs.gms.api.APIHttpResponse;
import uk.ac.gla.dcs.gms.api.LMSRegisterRequest;
import uk.ac.gla.dcs.gms.api.validation.RegisterValidation;

@SuppressWarnings("deprecation")
public class RegisterActivity extends ActionBarActivity implements View.OnClickListener {

    public final static String KEY_EMAIL = "REGISTERACTIVITY.EMAIL";

    private EditText editTxtEmail;
    private EditText editTxtFirstName;
    private EditText editTxtLastName;
    private TextView tViewSecretQuestion;
    private EditText editTxtAnswer;
    private EditText editTxtPassword;
    private EditText editTxtConfirmPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        editTxtEmail = (EditText) findViewById(R.id.register_edittxt_email);
        editTxtFirstName = (EditText) findViewById(R.id.register_edittxt_first_name);
        editTxtLastName = (EditText) findViewById(R.id.register_edittxt_last_name);
        tViewSecretQuestion = (TextView) findViewById(R.id.register_tview_secret_question);
        editTxtAnswer = (EditText) findViewById(R.id.register_edittxt_answer);
        editTxtPassword = (EditText) findViewById(R.id.register_edittxt_password);
        editTxtConfirmPassword = (EditText) findViewById(R.id.register_edittxt_confirm_password);
        btnRegister = (Button) findViewById(R.id.register_btn);


        btnRegister.setOnClickListener(this);
        tViewSecretQuestion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    tViewSecretQuestion.performLongClick();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED, null);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_btn) {

            if (validate()) {
                //send details to server
                LMSRegisterRequest request = new LMSRegisterRequest(getApplicationContext()) {
                    @Override
                    protected void onPostExecute(APIHttpResponse apiResponse) {

                        Bundle bundle = new Bundle();
                        //bundle.putString(EMAIL, email);

                        Intent intent = new Intent();
                        intent.putExtras(bundle);

                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        Utils.shortToast(getApplicationContext(), apiResponse.getRawResponse());
                    }
                };
                 //(username + ":" + password + ":" + email + ":" + answer + ":" + question)
                request.execute(getString(R.string.username), getString(R.string.password), "email", "answer", "question");
            }

        } else if (v.getId() == R.id.register_tview_secret_question) {

            final String[] questions = getResources().getStringArray(R.array.register_secretQuestions);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.register_str_secret_question);
            builder.setItems(questions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tViewSecretQuestion.setText(questions[which]);
                    editTxtAnswer.requestFocus();
                }
            });
            builder.show();
        }
    }

    private void setViewValidationState(View v, RegisterValidation.ValidationResult validationResult) {

        if (validationResult.isValid()){

        }else{

        }
    }

    private boolean validatePasswordConfirmation() {
        RegisterValidation.ValidationResult validationResult = RegisterValidation.validateConfirmation(editTxtPassword.getText().toString(), editTxtConfirmPassword.getText().toString());
        setViewValidationState(editTxtPassword,validationResult);
        return validationResult.isValid();
    }

    private boolean validatePassword() {
        RegisterValidation.ValidationResult validationResult = RegisterValidation.validatePassword(editTxtPassword.getText().toString());
        setViewValidationState(editTxtPassword,validationResult);
        return validationResult.isValid();
    }

    private boolean validateSecretAnswer() {
        RegisterValidation.ValidationResult validationResult = RegisterValidation.validateAnswer(editTxtAnswer.getText().toString());
        setViewValidationState(editTxtAnswer,validationResult);
        return validationResult.isValid();
    }

    private boolean validateSecretQuestion() {
        RegisterValidation.ValidationResult validationResult = RegisterValidation.validateSecretQuestion(tViewSecretQuestion.getText().toString());
        setViewValidationState(tViewSecretQuestion,validationResult);
        return validationResult.isValid();
    }

    private boolean validateLastName() {
        RegisterValidation.ValidationResult validationResult = RegisterValidation.validateLastName(editTxtLastName.getText().toString());
        setViewValidationState(editTxtLastName,validationResult);
        return validationResult.isValid();
    }

    private boolean validateFirstName() {
        RegisterValidation.ValidationResult validationResult = RegisterValidation.validateFirstName(editTxtFirstName.getText().toString());
        setViewValidationState(editTxtFirstName,validationResult);
        return validationResult.isValid();
    }

    private boolean validateEmail() {
        RegisterValidation.ValidationResult validationResult = RegisterValidation.validateEmail(editTxtEmail.getText().toString());
        setViewValidationState(editTxtEmail,validationResult);
        return validationResult.isValid();
    }

    private boolean validate() {
        return  validateEmail() &&  validateFirstName() && validateLastName() && validateSecretQuestion() && validateSecretAnswer() && validatePassword() && validatePasswordConfirmation();
    }

    private class localFocusLostListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            boolean valid = true;
            if (!hasFocus) {
                if (v.equals(editTxtEmail)) {
                    validateEmail();
                } else if (v.equals(editTxtFirstName)) {
                    validateFirstName();
                } else if (v.equals(editTxtLastName)) {
                    validateLastName();
                } else if (v.equals(tViewSecretQuestion)) {
                    validateSecretQuestion();
                } else if (v.equals(editTxtAnswer)) {
                    validateSecretAnswer();
                } else if (v.equals(editTxtPassword)) {
                    validatePassword();
                } else if (v.equals(editTxtConfirmPassword)) {
                    validatePasswordConfirmation();
                }
            }
        }
    }


}
