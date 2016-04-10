package com.example.root.towerofhanoi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class DialogActivity extends AppCompatActivity {

    public static final int MIN_NO_OF_DISK = 3;

    int current_string = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        final TextSwitcher textSwitcher = (TextSwitcher)findViewById(R.id.textSwitcher);
        textSwitcher.setInAnimation(AnimationUtils.loadAnimation(DialogActivity.this, android.R.anim.slide_in_left));
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(DialogActivity.this);
                textView.setShadowLayer(1.0f, 0.1f, 0.1f, Color.BLACK);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(50);
                return textView;
            }
        });

        textSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] optionList = getResources().getStringArray(R.array.no_of_disk_list);
                current_string = (current_string+1)%(optionList.length);
                textSwitcher.setCurrentText(optionList[current_string]);
            }
        });

        textSwitcher.setText(getResources().getStringArray(R.array.no_of_disk_list)[current_string]);
        final ImageButton imageButton = (ImageButton)findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DialogActivity.this, MainActivity.class);
                intent.putExtra("NO_OF_STRING", current_string+MIN_NO_OF_DISK);
                startActivity(intent);
            }
        });

    }
}
