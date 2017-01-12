package fr.ensicaen.projetintensif;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;

public class ProfilConfigurationActivity extends AppCompatActivity {

    private EditText _name;
    private EditText _sir_name;
    private EditText _nickname;
    private EditText _new_password;
    private EditText _confirm_new_password;
    private EditText _old_password;
    private EditText _birth_date;
    private EditText _phone;
    private Button _edit;

    private String name;
    private String sir_name;
    private String nickname;
    private String birth_date;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_configuration);
        setTitle("Profile");

        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");

        _name = (EditText) findViewById(R.id.edit_name);
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

        _sir_name = (EditText) findViewById(R.id.edit_sir_name);
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

        _nickname = (EditText) findViewById(R.id.edit_nickname);
        _nickname.setText(nickname);
        _nickname.setEnabled(false);

        _new_password = (EditText) findViewById(R.id.edit_new_password);
        _new_password.setOnKeyListener(new View.OnKeyListener() {
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

        _confirm_new_password = (EditText) findViewById(R.id.edit_confirm_new_password);
        _confirm_new_password.setOnKeyListener(new View.OnKeyListener() {
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

        _old_password = (EditText) findViewById(R.id.edit_old_password);
        _old_password.setOnKeyListener(new View.OnKeyListener() {
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

        _birth_date = (EditText) findViewById(R.id.edit_birth_date);
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

        _phone = (EditText) findViewById(R.id.edit_phone);
        _phone.setEnabled(false);

        _edit = (Button) findViewById(R.id.button_edit);
        _edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
