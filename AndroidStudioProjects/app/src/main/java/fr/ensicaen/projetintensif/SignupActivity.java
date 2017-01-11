package fr.ensicaen.projetintensif;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;

/**
 * Created by Amine on 10/01/2017.
 */

public class SignupActivity extends AppCompatActivity {

    private int REQUEST_LOGIN = 0;

    private EditText _name;
    private EditText _sir_name;
    private EditText _nickname;
    private EditText _password;
    private EditText _confirm_password;
    private EditText _birth_date;
    private EditText _phone;
    private CheckBox _terms_and_conditions_checkbox;
    private Button _validate;
    private TextView _link_login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        _name = (EditText) findViewById(R.id.input_name);
        _name.setOnKeyListener(new View.OnKeyListener() {
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

        _sir_name = (EditText) findViewById(R.id.input_sir_name);
        _sir_name.setOnKeyListener(new View.OnKeyListener() {
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

        _nickname = (EditText) findViewById(R.id.input_signup_nickname);
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

        _password = (EditText) findViewById(R.id.input_signup_password);
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

        _confirm_password = (EditText) findViewById(R.id.input_signup_confirm_password);
        _confirm_password.setOnKeyListener(new View.OnKeyListener() {
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

        _birth_date = (EditText) findViewById(R.id.input_birth_date);
        _birth_date.setOnKeyListener(new View.OnKeyListener() {
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

        _phone = (EditText) findViewById(R.id.input_phone);
        _phone.setOnKeyListener(new View.OnKeyListener() {
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

        _validate = (Button) findViewById(R.id.button_validate);
        _validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _link_login = (TextView) findViewById(R.id.link_login);
        _link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        _terms_and_conditions_checkbox = (CheckBox) findViewById(R.id.checkbox_terms_and_conditions);

        TextView terms_and_conditions = (TextView) findViewById(R.id.terms_and_conditions);
        terms_and_conditions.setText(Html.fromHtml("J'ai lu et j'accepte les " +
                "<a href=''>conditions d'utilisation</a>" + "."));
        terms_and_conditions.setClickable(true);
        terms_and_conditions.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void signup() {
        if (!validEntry()) {
            onSignupFailed();
            return;
        }

        _validate.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_PopupOverlay);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Création du compte en cours...");
        progressDialog.show();

        String name = _name.getText().toString();
        String sir_name = _sir_name.getText().toString();
        String nickname = _nickname.getText().toString();
        String password = _password.getText().toString();
        String birth_date = _birth_date.getText().toString();
        String phone = _phone.getText().toString();

        // process of signup

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onSignupSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _validate.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Echec de l'enregistrement.", Toast.LENGTH_LONG).show();
        _validate.setEnabled(true);
    }

    public boolean validEntry() {
        boolean valid = true;

        String name = _name.getText().toString();
        String sir_name = _sir_name.getText().toString();
        String nickname = _nickname.getText().toString();
        String password = _password.getText().toString();
        String confirm_password = _confirm_password.getText().toString();
        String birth_date = _birth_date.getText().toString();
        String phone = _phone.getText().toString();
        boolean terms_and_conditions_accepted = _terms_and_conditions_checkbox.isChecked();

        // add other checks
        if (name.isEmpty()) {
            _name.setError("Champ obligatoire.");
            valid = false;
        } else {
            _name.setError(null);
        }

        // add other checks
        if (sir_name.isEmpty()) {
            _sir_name.setError("Champ obligatoire.");
            valid = false;
        } else {
            _sir_name.setError(null);
        }

        // add other checks
        if (nickname.isEmpty()) {
            _nickname.setError("Champ obligatoire.");
            valid = false;
        } else {
            _nickname.setError(null);
        }

        // add other checks
        if (password.isEmpty()) {
            _password.setError("Champ obligatoire.");
            valid = false;
        } else {
            _password.setError(null);
        }

        // add other checks
        if (confirm_password.isEmpty()) {
            _confirm_password.setError("Champ obligatoire.");
            valid = false;
        } else {
            if (!confirm_password.equals(password)) {
                _confirm_password.setError("Mot de passe différent.");
            }
            else {
                _confirm_password.setError(null);
            }
        }

        // add other checks
        if (birth_date.isEmpty()) {
            _birth_date.setError("Champ obligatoire.");
            valid = false;
        } else {
            _birth_date.setError(null);
        }

        // add other checks
        if (phone.isEmpty()) {
            _phone.setError("Champ obligatoire.");
            valid = false;
        } else {
            _phone.setError(null);
        }

        // add other checks
        if (terms_and_conditions_accepted) {
            _terms_and_conditions_checkbox.setError("Champ obligatoire.");
            valid = false;
        } else {
            _terms_and_conditions_checkbox.setError(null);
        }

        return valid;
    }
}
