package com.example.root.towerofhanoi.elements;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.AlphaMapTexture;
import org.rajawali3d.materials.textures.TextureManager;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;

public class TextPlane extends Plane {

    String name;
    String currentString="";
    float size;

    private AlphaMapTexture mTimeTexture;
    private Bitmap mTimeBitmap;
    private Canvas mTimeCanvas;
    private Paint mTextPaint;

    private int color;

    private boolean isDirty;

    public TextPlane(String name, String currentString,float size){
        this(name, currentString, size, Color.WHITE);
    }

    public TextPlane( String name, String currentString, float size, int color) {
        super(size, size, 1, 1);
        this.name = name;
        this.currentString = currentString;
        this.size = size;
        this.color = color;

        Material textMaterial = new Material();
        mTimeBitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        mTimeTexture = new AlphaMapTexture("Texture_"+name, mTimeBitmap);
        createTexture();
        mTimeTexture.setBitmap(mTimeBitmap);
        try {
            textMaterial.addTexture(mTimeTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
        textMaterial.setColorInfluence(1);
        this.setMaterial(textMaterial);
        this.setDoubleSided(true);
        this.rotate(Vector3.Axis.Y, 180);
        this.setColor(this.color);
    }


    void createTexture(){
        if (mTimeCanvas == null) {
            mTimeCanvas = new Canvas(mTimeBitmap);
            mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setColor(Color.WHITE);
            mTextPaint.setTextSize(35);
        }

        mTimeCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        mTimeCanvas.drawText(currentString, 75,
                128, mTextPaint);

        isDirty = true;
    }

    public void updateText(final String currentString){
        this.currentString = currentString;

        new Thread(new Runnable() {
            @Override
            public void run() {
                createTexture();
            }
        }).start();
    }

    public void render(TextureManager textureManager){
        if(isDirty){
            mTimeTexture.setBitmap(mTimeBitmap);
            textureManager.replaceTexture(mTimeTexture);
            isDirty = false;
        }
    }
}
