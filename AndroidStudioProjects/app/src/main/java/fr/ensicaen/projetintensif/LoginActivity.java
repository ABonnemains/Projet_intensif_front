package fr.ensicaen.projetintensif;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;

/**
 * Created by Amine on 10/01/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 1;

    private EditText _nickname;
    private EditText _password;
    private Button _login_button;
    private TextView _link_signup;
    private Boolean _loginSuccessful;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _nickname = (EditText) findViewById(R.id.input_login_nickname);
        _nickname.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        _password = (EditText) findViewById(R.id.input_login_password);
        _password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        _login_button = (Button) findViewById(R.id.button_login);
        _login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });

        _link_signup = (TextView) findViewById(R.id.link_signup);
        _link_signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        _loginSuccessful = false;
        // verification of login and password
        if (!validEntry()) {
            onLoginFailed();
            return;
        }

        _login_button.setEnabled(false);

        /*final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_PopupOverlay);*/

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.LoadingTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Connexion en cours...");
        progressDialog.show();

        String nickname = _nickname.getText().toString();
        String password = _password.getText().toString();

        // process of authentification
        new LoginTask(this).execute(new Communication(nickname, password));

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(_loginSuccessful){
                           onLoginSuccess();
                        }
                        else{
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 10000);

        _login_button.setEnabled(true);
    }

    public void setLoginSuccess(String token)
    {
        _loginSuccessful = (token != null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // response of successfull signup, currently finish the activity and launch the main activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("nickname", _nickname.getText().toString());
                startActivity(intent);
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _login_button.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("nickname", _nickname.getText().toString());
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Echec de connexion !", Toast.LENGTH_LONG).show();
        _login_button.setEnabled(true);
    }

    public boolean validEntry() {
        boolean valid = true;

        String nickname = _nickname.getText().toString();
        String password = _password.getText().toString();

        if (nickname.isEmpty()) {
            _nickname.setError("Entrez un pseudo valide.");
            valid = false;
        }
        else {
            _nickname.setError(null);
        }

        // add password checks like length or special characters.
        if (password.isEmpty()) {
            _password.setError("Entrez un mot de passe valide.");
            valid = false;
        }
        else {
            _password.setError(null);
        }

        return valid;
    }
}
