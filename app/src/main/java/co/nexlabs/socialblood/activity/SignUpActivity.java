package co.nexlabs.socialblood.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apkfuns.logutils.utils.SystemUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.nexlabs.socialblood.R;
import co.nexlabs.socialblood.model.Message;
import co.nexlabs.socialblood.model.User;
import co.nexlabs.socialblood.rest.RestClient;
import co.nexlabs.socialblood.util.SystemUtils;
import pl.tajchert.nammu.Nammu;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by myozawoo on 3/14/16.
 */
public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.ed_name) EditText name;
    @BindView(R.id.ed_email) EditText email;
    @BindView(R.id.ed_password) EditText password;
    @BindView(R.id.ed_confirm_password) EditText confirmPassword;
    @BindView(R.id.btn_signup) Button btnSignup;

    @BindView(R.id.ed_name_layout) TextInputLayout nameLayout;
    @BindView(R.id.ed_email_layout) TextInputLayout emailLayout;
    @BindView(R.id.ed_password_layout) TextInputLayout passwordLayout;
    @BindView(R.id.ed_confirm_password_layout) TextInputLayout confirmPasswordLayout;

    @BindView(R.id.toolbar) Toolbar toolbar;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        toolbar.setTitle(getString(R.string.sign_up));
        toolbar.setTitleTextColor(Color.parseColor("#5A47A0"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading...");
    }

    @OnClick(R.id.btn_signup) void signUp() {


        if (SystemUtils.isNetworkConnected(this) && validateEmail() && validatePassword() &&
                validateConfirmPassword() && validateName()  && isMatchPassword()) {
            String EMAIL = email.getText().toString().trim();
            String PASSWORD = password.getText().toString().trim();
            String NAME = name.getText().toString().trim();
            Intent intent = new Intent(getBaseContext(), FinalRegistrationActivity.class);
            intent.putExtra("name", NAME);
            intent.putExtra("email", EMAIL);
            intent.putExtra("password", PASSWORD);
            intent.putExtra("status", "signup");
            startActivity(intent);

        } else {
            if (!SystemUtils.isNetworkConnected(this)) {
                Toast.makeText(this, "Internet connection isn't available!", Toast.LENGTH_SHORT).show();
            }
        }

    }




    private boolean validateEmail() {
        String mEmail = email.getText().toString().trim();

        if (mEmail.isEmpty() || !isValidEmail(mEmail)) {
            emailLayout.setError(getString(R.string.err_msg_email));
            requestFocus(email);
            return false;
        } else {
            emailLayout.setErrorEnabled(true);
        }

        return true;
    }

    private boolean isMatchPassword() {

        if (validatePassword() && validateConfirmPassword()) {
            if (!password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
                passwordLayout.setError(getString(R.string.err_matching_two_password));
                confirmPasswordLayout.setError(getString(R.string.err_matching_two_password));
                requestFocus(password);
                return false;
            }
        }

        else {
            passwordLayout.setErrorEnabled(true);
            confirmPasswordLayout.setErrorEnabled(true);
        }

        return true;
    }

    private boolean validateName() {
        if (name.getText().toString().trim().isEmpty()) {
            nameLayout.setError(getString(R.string.err_msg_name));
            requestFocus(name);
            return false;
        } else {
            nameLayout.setErrorEnabled(true);
        }
        return true;
    }


    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty() || password.getText().length() != 8) {
            passwordLayout.setError(getString(R.string.err_msg_password));
            requestFocus(password);
            return false;
        } else {
            passwordLayout.setErrorEnabled(true);
        }
        return true;
    }

    private boolean validateConfirmPassword() {
        if (confirmPassword.getText().toString().trim().isEmpty() || confirmPassword.getText().length() != 8) {
            confirmPasswordLayout.setError(getString(R.string.err_msg_password));
            requestFocus(confirmPassword);
            return false;
        } else {
            confirmPasswordLayout.setErrorEnabled(true);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !android.text.TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    void showErrorDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}
