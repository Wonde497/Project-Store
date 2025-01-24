package com.verifone.demo.emv;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class splash_screen extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIMEOUT = 4000;
    private static final long LETTER_DELAY = 200; // Delay between each letter in milliseconds

    private TextView splashTextView, splashText2, splashText3;
    private String fullText = "GLOBAL BANK";
    private String fullText2 = "ETHIOPIA";
    private String fullText3 = "OUR SHARED SUCCESS";

    private StringBuilder currentText = new StringBuilder();
    private StringBuilder currentText2 = new StringBuilder();
    private StringBuilder currentText3 = new StringBuilder();

    private int currentIndex = 0;
    private int currentIndex2 = 0;
    private int currentIndex3 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashTextView = findViewById(R.id.splashText);
        splashText2 = findViewById(R.id.splashText2);
        splashText3 = findViewById(R.id.splashText3);

        startTyping(fullText, splashTextView);
    }

    private void startTyping(final String text, final TextView textView) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                typeNextLetter(text, textView);
            }
        }, LETTER_DELAY);
    }

    private void typeNextLetter(final String text, final TextView textView) {
        if (text.equals(fullText)) {
            if (currentIndex < text.length()) {
                currentText.append(text.charAt(currentIndex));
                textView.setText(currentText.toString());
                currentIndex++;
                // Repeat typing the next letter with a delay
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        typeNextLetter(text, textView);
                    }
                }, LETTER_DELAY);
            } else {
                currentIndex = 0; // Reset index for the next text
                startTyping(fullText2, splashText2); // Start typing next text
            }
        } else if (text.equals(fullText2)) {
            if (currentIndex2 < text.length()) {
                currentText2.append(text.charAt(currentIndex2));
                textView.setText(currentText2.toString());
                currentIndex2++;
                // Repeat typing the next letter with a delay
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        typeNextLetter(text, textView);
                    }
                }, LETTER_DELAY);
            } else {
                currentIndex2 = 0; // Reset index for the next text
                startTyping(fullText3, splashText3); // Start typing next text
            }
        } else if (text.equals(fullText3)) {
            if (currentIndex3 < text.length()) {
                currentText3.append(text.charAt(currentIndex3));
                textView.setText(currentText3.toString());
                currentIndex3++;
                // Repeat typing the next letter with a delay
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        typeNextLetter(text, textView);
                    }
                }, LETTER_DELAY);
            } else {
                // If all texts are displayed, start the next activity
                startActivity(new Intent(splash_screen.this, Base_Screen.class));
                finish();
            }
        }
    }
}