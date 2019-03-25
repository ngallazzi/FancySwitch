package it.ngallazzi.fancyswitchsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import it.ngallazzi.fancyswitch.FancySwitch;
import org.jetbrains.annotations.NotNull;

/**
 * FancySwitch
 * Created by Nicola on 3/25/2019.
 * Copyright © 2019 Zehus. All rights reserved.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FancySwitch fancySwitch = findViewById(R.id.fancySwitch);
        fancySwitch.setSwitchStateChangedListener(new FancySwitch.SwitchStateChangedListener() {
            @Override
            public void onChanged(@NotNull FancySwitch.State newState) {
                Toast.makeText(MainActivity.this,
                        "New switch state: " + newState.name(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
