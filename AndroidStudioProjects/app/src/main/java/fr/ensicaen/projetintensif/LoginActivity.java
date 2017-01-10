package fr.ensicaen.projetintensif;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Amine on 10/01/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;

    private EditText _nickname;
    private EditText _password;
    private Button _login_button;
    private TextView _link_signup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _nickname = (EditText) findViewById(R.id.input_login_nickname);
        _password = (EditText) findViewById(R.id.input_login_password);
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
        // verification of login and password
        if (!validEntry()) {
            onLoginFailed();
            return;
        }

        _login_button.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_PopupOverlay);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Connexion en cours...");
        progressDialog.show();

        String nickname = _nickname.getText().toString();
        String password = _password.getText().toString();

        // process of authentification

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
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
