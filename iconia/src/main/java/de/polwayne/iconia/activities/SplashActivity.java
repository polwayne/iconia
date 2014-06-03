package de.polwayne.iconia.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.polwayne.iconia.R;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final String pin = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key_security_pin","");
        if(pin.isEmpty() || pin.equals("")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 1000);
            return;
        }
        else {
            final EditText et_pin = (EditText) findViewById(R.id.et_pin);
            et_pin.setVisibility(View.VISIBLE);
            et_pin.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN
                            && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (et_pin.getText().toString().equals(pin)) {
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                            return true;
                        } else{
                            Toast.makeText(SplashActivity.this,getString(R.string.wrong_pin),Toast.LENGTH_LONG).show();
                            return true;
                        }
                    }
                    return false;
                }
            });
        }


    }

}
