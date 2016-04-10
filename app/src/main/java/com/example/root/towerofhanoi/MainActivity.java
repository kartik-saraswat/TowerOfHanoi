package com.example.root.towerofhanoi;

import android.app.ActionBar;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

public class MainActivity extends AppCompatActivity {

    MyRenderer renderer;
    private GestureDetectorCompat mDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int noOfStrings = getIntent().getIntExtra("NO_OF_STRING", DialogActivity.MIN_NO_OF_DISK);

        setContentView(R.layout.activity_main);
        final RajawaliSurfaceView surface = new RajawaliSurfaceView(this);
        surface.setFrameRate(60.0);
        surface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);
        renderer = new MyRenderer(this, noOfStrings);
        surface.setSurfaceRenderer(renderer);

        mDetector = new GestureDetectorCompat(this, renderer.myGestureListener);

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.main_layout);
        relativeLayout.addView(surface);

        TextView textView = (TextView)relativeLayout.findViewById(R.id.moves_text_view);
        textView.bringToFront();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
