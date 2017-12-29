package com.austinabell8.shopifyapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * SplashActivity - Activity that shows while MainActivity is inflated
 * @author  Austin Abell
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Activity does not inflate a view, simply shows while MainActivity is inflated
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
