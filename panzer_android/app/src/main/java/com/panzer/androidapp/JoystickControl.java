package com.panzer.androidapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoystickControl extends AppCompatActivity {

    private Integer lockState;

    private ImageView background;
    private ImageView logo;
    private ImageView lock;

    JoystickView throttleJoystick;
    JoystickView steerJoystick;

    private EditText urlInput;

    private static PanzerAPI panzerAPI;

    private String throttleState;
    private String steerState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joystick_control);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        background = findViewById(R.id.background);
        logo = findViewById(R.id.logo);
        lock = findViewById(R.id.lock);

        urlInput = findViewById(R.id.urlInput);
        urlInput.setText(LocalStorage.getString(JoystickControl.this, "DEFAULT", "URL"));

        throttleJoystick = findViewById(R.id.throttleJoystick);
        steerJoystick = findViewById(R.id.steerJoystick);

        lockState = 7;

        if (urlInput.getText().toString().isEmpty())
            urlInput.setText("http://192.168.1.185/");

        panzerAPI = new PanzerAPI(urlInput.getText().toString());
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                panzerAPI.getConnectionState(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        updateUiConnected();
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.e(JoystickControl.class.toString(), t.getMessage());
                        updateUiDisconnected();
                    }
                });
            }
        }, 0, 5000);

        throttleState = "stop";
        steerState = "middle";

        throttleJoystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {

                String state = "backward";
                if (angle == 0)
                    state = "stop";
                else if (angle == 90)
                    state = "forward";

                if (!state.equals(throttleState)) {

                    Log.i(JoystickControl.class.toString(), state);
                    throttleState = state;
                    sendCommand(state);
                }
            }
        }, 17);

        steerJoystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {

                String state = "right";
                if (angle == 0 && strength == 0)
                    state = "middle";
                else if (angle == 180)
                    state = "left";

                if (!state.equals(steerState)) {

                    Log.i(JoystickControl.class.toString(), state);
                    steerState = state;
                    sendCommand(state);
                }
            }
        }, 17);
    }

    private void sendCommand(String command) {

        panzerAPI.controlPanzer(command, new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                Log.i(JoystickControl.class.toString(), String.valueOf(response.body()));
                updateUiConnected();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                Log.e(JoystickControl.class.toString(), t.getMessage());
                updateUiDisconnected();
            }
        });
    }

    private void updateUiDisconnected() {

        throttleJoystick.setEnabled(false);
        steerJoystick.setEnabled(false);

        logo.setImageResource(R.drawable.logo2);
        background.setImageResource(R.drawable.background2);
    }

    private void updateUiConnected() {

        throttleJoystick.setEnabled(true);
        steerJoystick.setEnabled(true);

        logo.setImageResource(R.drawable.logo);
        background.setImageResource(R.drawable.background);
    }

    public void editUrl(View view) {

        if (urlInput.getVisibility() == View.GONE) {
            urlInput.setVisibility(View.VISIBLE);
        } else {
            LocalStorage.setString(JoystickControl.this, "DEFAULT", "URL", urlInput.getText().toString());
            panzerAPI.setRetrofit(urlInput.getText().toString());
            urlInput.setVisibility(View.GONE);
        }
    }

    public void toggleLayout(View view) {

        if (lockState > 0) {
            lockState--;
            if (lockState >= 1)
                Toast.makeText(JoystickControl.this, "You're " + String.valueOf(lockState) + " steps away.", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(JoystickControl.this, "You are now a developer!", Toast.LENGTH_LONG).show();
        }

        if (lockState == 0) {

            throttleJoystick.setButtonDrawable(getDrawable(R.drawable.inner_circle_3));
            throttleJoystick.setBackground(getDrawable(R.drawable.circle_3));
            steerJoystick.setButtonDrawable(getDrawable(R.drawable.inner_circle_3));
            steerJoystick.setBackground(getDrawable(R.drawable.circle_3));
            lock.setImageDrawable(getDrawable(R.drawable.lock));

            lockState = -1;

        } else if (lockState == -1) {

            throttleJoystick.setButtonDrawable(getDrawable(R.drawable.inner_circle_1));
            throttleJoystick.setBackground(getDrawable(R.drawable.circle_1));
            steerJoystick.setButtonDrawable(getDrawable(R.drawable.inner_circle_2));
            steerJoystick.setBackground(getDrawable(R.drawable.circle_2));

            lock.setImageDrawable(getDrawable(R.drawable.unlock));
            lockState = 7;
        }
    }
}
