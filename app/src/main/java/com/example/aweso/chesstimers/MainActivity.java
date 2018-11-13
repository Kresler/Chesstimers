package com.example.aweso.chesstimers;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ProgressBar clock;
    private TextView showTime;
    private int timeValue;
    private int timerText;
    private CountDownTimer kev;
    private boolean kev2;
    private double temp;
    private boolean longClicked;
    private int lastInput =60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clock = findViewById(R.id.countRing);
        clock.setProgress(0);
        showTime = findViewById(R.id.countText);
        timeValue = 60;
        kev2 = false;
        Button timerButton = findViewById(R.id.timerButton);
        showTime.setEnabled(true);
        showTime.setCursorVisible(false);
        showTime.setFocusableInTouchMode(false);
        showTime.setShowSoftInputOnFocus(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideSystemUI();
                                }
                            }, 2000);
                        }
                    }
                });
        showTime.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    showTime.setCursorVisible(false);
                    showTime.setFocusableInTouchMode(false);
                    if(!showTime.getText().toString().matches(""))
                        timerText=Integer.parseInt(showTime.getText().toString());
                    else
                    {
                        timerText=60;
                    }
                    if(timerText==0){
                        timerText=60;
                        Toast toast = Toast.makeText(MainActivity.this, "Invalid Entry", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    timeValue=timerText;
                    lastInput =timerText;
                    longClicked=false;
                    showTime.clearFocus();
                    hideSystemUI();
                }
                return false;
            }
        });
        timerButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "Insert new length", Toast.LENGTH_SHORT);
                toast.show();
                showSystemUI();
                showTime.setCursorVisible(true);
                showTime.setFocusableInTouchMode(true);
                showTime.requestFocus();
                showSoftKeyboard(showTime);
                longClicked=true;
                return false;
            }
        });
        }

    public void showSoftKeyboard(View view){
        if(view.requestFocus()){
            InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }
    }



    public void countDown(View view) {
        if (kev2)
            kev.cancel();
        if (!longClicked) {
            timerText = timeValue + 1;
            temp = (double) 100 + (100 / timeValue);
            kev = new CountDownTimer(timeValue * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    temp -= 100 / (double) timeValue;
                    clock.setProgress(99 - (int) Math.floor(temp));
                    timerText--;
                    kev2 = true;
                    showTime.setText(Integer.toString(timerText));
                    if(showTime.isFocused()){
                        showTime.clearFocus();
                        showTime.requestFocus();
                    }
                }

                @Override
                public void onFinish() {
                    temp -= 100 / (double) timeValue;
                    clock.setProgress(100);
                    timerText=0;
                    showTime.setText(Integer.toString(timerText));
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                }
            };
            kev.start();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            kev.cancel();
            showTime.setCursorVisible(false);
            showTime.setFocusableInTouchMode(false);
            timerText= lastInput;
            timeValue= lastInput;
            longClicked=false;
            showTime.clearFocus();
            hideSystemUI();
            showTime.setText(Integer.toString(timerText));
            clock.setProgress(0);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}

