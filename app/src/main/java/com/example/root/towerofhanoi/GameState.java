package com.example.root.towerofhanoi;

import android.graphics.Color;
import android.util.Log;

import com.example.root.towerofhanoi.elements.Base;
import com.example.root.towerofhanoi.elements.Disk;
import com.example.root.towerofhanoi.elements.Rod;
import com.example.root.towerofhanoi.elements.TextImageElement;
import com.example.root.towerofhanoi.elements.TextPlane;

import org.rajawali3d.animation.ColorAnimation3D;
import org.rajawali3d.animation.TranslateAnimation3D;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.scene.RajawaliScene;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class GameState {

    int noOfPegs;
    int noOfDisks;

    public final static float MIN_RADIUS = 0.2f;
    public final static float MIN_HEIGHT = 0.2f;

    public final static String SCORE_STRING = " ";

    Base base;
    ArrayList<Rod> rodList;
    ArrayList<Disk> diskList;
    TextPlane textA, textB, textC;

    TextPlane scoreTextPlane;

    Vector3 tempFrom = new Vector3();
    Vector3 tempTo =  new Vector3();

    static int[] colorPool = new int[]{
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.CYAN,
            Color.MAGENTA,
            Color.rgb(0,128,128)
            };

    public enum State{
        FIXED, UP ,RISING, FALLING, FLOATING
    }

    RajawaliScene scene;

    State CURRENT_STATUS = State.FIXED;
    Rod srcRod = null;
    Rod destRod = null;
    Disk movingDisk = null;

    int noOfMoves = 0;
    boolean isReset = true;

    public GameState(int noOfDisks, RajawaliScene scene) {

        this.noOfPegs = 3;
        this.noOfDisks = noOfDisks;
        this.rodList = new ArrayList<>();
        this.diskList = new ArrayList<>();
        this.scene = scene;
        createBase();
        createPegs();
        createDisks();
        createScoreTextPlane();
        createTextElements();
        reset();
    }

    public void reset(){

        for (int i = 0; i < noOfPegs; i++){
            rodList.get(i).clear();
        }

        Rod firstRod = rodList.get(0);
        for(int i = noOfDisks-1; i >= 0; i--){
            Disk disk = diskList.get(i);

            double yPos = firstRod.getPeekY();
            yPos += disk.getLength()/2;
            disk.setPosition(firstRod.getX(), yPos, firstRod. getZ());

            firstRod.pushDisk(disk);
        }

        CURRENT_STATUS = State.FIXED;
        srcRod = null;
        destRod = null;
        movingDisk = null;
        isReset = true;
        noOfMoves = 0;
        scoreTextPlane.updateText( SCORE_STRING + noOfMoves);
    }

    public void createBase(){
        float baseWidth = (noOfPegs + 1)*(noOfDisks + 1)*MIN_RADIUS;
        float baseHeight = (noOfDisks + 1)*MIN_HEIGHT;
        base = new Base("BASE_CUBE", baseWidth/2, baseWidth, baseHeight, Color.LTGRAY);
        base.setPosition(0, -(base.getHeight()/2), 0);
    }

    public void createScoreTextPlane(){
        scoreTextPlane = new TextPlane("Score","0", base.getLength()*2);
        scoreTextPlane.setPosition(base.getLeftX() + base.getBreadth() + 1.5f, rodList.get(0).getTopY() + 0.5f , base.getZ());
    }

    public void createTextElements(){
        Vector3 temp = base.getPosition();
        float baseWidth = base.getBreadth()/5;
        float leftX = (float)base.getLeftX() + baseWidth*1.5f;

        textA = new TextPlane("a","A",baseWidth*4, Color.parseColor("#734d26"));
        textB = new TextPlane("b","B",baseWidth*4, Color.parseColor("#734d26"));
        textC = new TextPlane("c","C",baseWidth*4, Color.parseColor("#734d26"));

        textA.setPosition(leftX, base.getY(), base.getZ()+base.getLength()/2+0.5f);
        leftX = leftX + baseWidth*1.5f;
        textB.setPosition(leftX, base.getY(), base.getZ()+base.getLength()/2+0.5f);
        leftX = leftX + baseWidth*1.5f;
        textC.setPosition(leftX, base.getY(), base.getZ()+base.getLength()/2+0.5f);

        textA.updateText("A");
        textB.updateText("B");
        textC.updateText("C");
    }

    public void createPegs(){
        float rodLength = (noOfDisks+1)*MIN_HEIGHT;
        float rodRadius = MIN_RADIUS/2;

        float pegGap = (base.getBreadth()/(noOfPegs));

        float x = (float)base.getLeftX() + pegGap/2;
        float y = (float)base.getTopY();
        float z = (float)base.getZ();

        for (int i = 0 ; i < noOfPegs; i++){
            Rod rod = new Rod("ROD_"+i,rodLength, rodRadius,Color.GRAY);
            rod.setPosition(x + i*pegGap,y + rod.getLength()/2, z);
            Log.d("ROD", rod.getPosition().toString());
            rodList.add(rod);
        }
    }

    public void createDisks(){
        float diskHeight = MIN_HEIGHT;
        float diskRadius = MIN_RADIUS;
        diskList.clear();
        for(int i = 0; i < noOfDisks; i++ ){
            int color = colorPool[ i % colorPool.length];
            Disk disk = new Disk("DISK_"+i, diskHeight, diskRadius*(i+1), color);
            diskList.add(disk);
        }
    }

    public void removeDisk() {
        if (CURRENT_STATUS == State.RISING && srcRod != null && movingDisk != null) {
            if (srcRod.hasDisks()) {
                srcRod.popDisk();

                tempFrom.setAll(movingDisk.getPosition());
                tempTo.setAll(srcRod.getX(), srcRod.getTopY() + movingDisk.getLength() / 2, srcRod.getZ());

                scene.clearAnimations();
                final TranslateAnimation3D anim = new TranslateAnimation3D(tempFrom,
                        tempTo);
                anim.setDurationMilliseconds(500);
                anim.setTransformable3D(movingDisk.getSolidObject());
                scene.registerAnimation(anim);
                anim.play();

            }
        }
    }

    public void moveDisk(){

        if(CURRENT_STATUS == State.FLOATING && movingDisk != null && srcRod != null && destRod != null){

            tempFrom.setAll(srcRod.getX(), srcRod.getTopY() + movingDisk.getLength() / 2, srcRod.getZ());
            tempTo.setAll(destRod.getX(), destRod.getTopY() + movingDisk.getLength() / 2, destRod.getZ());

            scene.clearAnimations();
            final TranslateAnimation3D anim = new TranslateAnimation3D(tempFrom,
                    tempTo);
            anim.setDurationMilliseconds(1000);
            anim.setTransformable3D(movingDisk.getSolidObject());
            scene.registerAnimation(anim);
            anim.play();
        }
    }

    public void addDisk(){
        if(CURRENT_STATUS == State.FALLING && movingDisk != null && destRod != null) {

            tempFrom.setAll(destRod.getX(), destRod.getTopY() + movingDisk.getLength() / 2, destRod.getZ());
            tempTo.setAll(destRod.getX(), destRod.getPeekY() + movingDisk.getLength() / 2, destRod.getZ());

            Log.d("XMEN_ADD_DISK_PEEKY", "Y:" + destRod.getPeekY());
            Log.d("XMEN_ADD_DISK_TO", tempTo.toString() );

            scene.clearAnimations();
            final TranslateAnimation3D anim = new TranslateAnimation3D(tempFrom,
                    tempTo);
            anim.setDurationMilliseconds(500);
            anim.setTransformable3D(movingDisk.getSolidObject());
            scene.registerAnimation(anim);
            anim.play();

            destRod.pushDisk(movingDisk);
            noOfMoves++;
            scoreTextPlane.updateText( SCORE_STRING + noOfMoves);
        }
    }

    public void bounceDisk(){
        if(CURRENT_STATUS == State.FALLING && movingDisk != null && destRod != null) {

            scene.clearAnimations();
            final ColorAnimation3D anim = new ColorAnimation3D( Color.WHITE - movingDisk.getColor(),
                    movingDisk.getColor());
            anim.setDurationMilliseconds(1000);
            anim.setTransformable3D(movingDisk.getSolidObject());
            scene.registerAnimation(anim);
            anim.play();

        }
    }

}
