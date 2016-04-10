package com.example.root.towerofhanoi;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.root.towerofhanoi.elements.ObjectLoader;

import org.rajawali3d.lights.DirectionalLight;

import org.rajawali3d.lights.SpotLight;
import org.rajawali3d.math.MathUtil;
import org.rajawali3d.math.vector.Vector2;
import org.rajawali3d.math.vector.Vector3;

import org.rajawali3d.renderer.RajawaliRenderer;

public class MyRenderer extends RajawaliRenderer{

    Context context;
    DirectionalLight directionalLight;
    MyArcballCamera arcball;
    GameState gameState;
    MyGestureListener myGestureListener;
    final int noOfDisks;

    public MyRenderer(Context context) {
        this(context, DialogActivity.MIN_NO_OF_DISK);
    }

    public MyRenderer(Context context, int noOfDisks) {
        super(context);
        this.context = context;
        setFrameRate(60);
        myGestureListener = new MyGestureListener();
        this.noOfDisks = noOfDisks;
    }

    @Override
    protected void initScene() {

        ObjectLoader.init(this);
        gameState = new GameState(this.noOfDisks, getCurrentScene());

        Vector3 cameraPosition = new Vector3(0, gameState.noOfDisks, gameState.noOfDisks*2.4);

        directionalLight = new DirectionalLight();
        directionalLight.setPower(1f);
        directionalLight.setPosition(cameraPosition);
        directionalLight.setLookAt(0, 0, 0);

        getCurrentScene().addLight(directionalLight);

        getCurrentScene().addChild(gameState.base.getSolidObject());
        getCurrentScene().addChild(gameState.scoreTextPlane);

        getCurrentScene().addChild(gameState.textA);
        getCurrentScene().addChild(gameState.textB);
        getCurrentScene().addChild(gameState.textC);

        for(int i = 0; i < gameState.noOfPegs; i++){
            getCurrentScene().addChild(gameState.rodList.get(i).getSolidObject());
        }

        for (int i = 0 ; i < gameState.noOfDisks; i++){
            getCurrentScene().addChild(gameState.diskList.get(i).getSolidObject());
        }

        getCurrentCamera().setPosition(cameraPosition);
        getCurrentCamera().setLookAt(0,0,0);
    }


    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
    }

    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        gameState.scoreTextPlane.render(getTextureManager());
        super.onRender(ellapsedRealtime, deltaTime);
    }

    private void mapToSphere(final double x, final double y, Vector3 out) {
        double lengthSquared = x * x + y * y;
        if (lengthSquared > 1) {
            out.setAll(x, y, 0);
            out.normalize();
        } else {
            out.setAll(x, y, Math.sqrt(1 - lengthSquared));
        }
    }

    private void mapToScreen(final float x, final float y, Vector2 out) {
        out.setX((2 * x - mCurrentViewportWidth) / mCurrentViewportWidth);
        out.setY(-(2 * y - mCurrentViewportHeight) / mCurrentViewportHeight);
    }

    public void handleAddDisk() {

    }

    public void handleRemoveDisk() {

    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        private Vector3 tempVector3 = new Vector3();
        private Vector2 tempVector2 = new Vector2();

        private final static int SWIPE_LEFT_RIGHT = 0;
        private final static int SWIPE_RIGHT_LEFT = 1;
        private final static int SWIPE_TOP_BOTTOM = 2;
        private final static int SWIPE_BOTTOM_TOP = 3;
        private final static int SWIPE_DIAGONAL = 4;
        private final static int SWIPE_THRESHOLD = 50;

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG, "onDown: " + event.toString());
            return true;
        }

        public int getSelectedRod(float x, float y){

            mapToScreen(x, y, tempVector2);
            mapToSphere(tempVector2.getX(), tempVector2.getY(), tempVector3);
            int selectedRod = -1;
            double min = Double.MAX_VALUE;
            for(int i = 0; i < gameState.noOfPegs; i++){
                double diff = Math.abs(gameState.rodList.get(i).getPosition().x - tempVector3.x*8);
                if( diff < min){
                    min = diff;
                    selectedRod = i;
                }
            }
            return selectedRod;
        }

        public int getSwipeType(float x1, float y1, float x2, float y2){
            int swipeType = SWIPE_DIAGONAL;
            float diffX = x2 - x1;
            float diffY = y1 - y2; // Android Screen Coordinates (0 to +Y) from top left corner

            if(Math.abs(Math.abs(diffX) - Math.abs(diffY)) > SWIPE_THRESHOLD) {

                if (Math.abs(diffY) > Math.abs(diffX)) {
                    //Vertical Swipe
                    if (diffY > 0) {
                        swipeType = SWIPE_BOTTOM_TOP;
                    } else {
                        swipeType = SWIPE_TOP_BOTTOM;
                    }
                } else {
                    // Horizontal Swipe
                    if (diffX > 0) {
                        swipeType = SWIPE_LEFT_RIGHT;

                    } else {
                        swipeType = SWIPE_RIGHT_LEFT;
                    }
                }
            }
            return swipeType;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            int swipeType = getSwipeType(e1.getX(), e1.getY(), e2.getX(), e2.getY());
            int selectedRod = getSelectedRod(e2.getX(), e2.getY());

            if(swipeType == SWIPE_BOTTOM_TOP && gameState.CURRENT_STATUS == GameState.State.FIXED){
                if(gameState.rodList.get(selectedRod).hasDisks()){
                    gameState.srcRod = gameState.rodList.get(selectedRod);
                    gameState.movingDisk = gameState.srcRod.topDisk();
                    gameState.CURRENT_STATUS = GameState.State.RISING;
                    gameState.removeDisk();
                    gameState.CURRENT_STATUS = GameState.State.UP;
                }
            } else if( swipeType == SWIPE_TOP_BOTTOM && gameState.CURRENT_STATUS == GameState.State.UP){

                gameState.destRod = gameState.rodList.get(selectedRod);
                if((!gameState.destRod.hasDisks()) || (gameState.destRod.topDisk().getRadius() > gameState.movingDisk.getRadius())){
                    //Safe to add disk
                    gameState.CURRENT_STATUS = GameState.State.FALLING;
                    gameState.addDisk();
                    gameState.CURRENT_STATUS = GameState.State.FIXED;

                    if(gameState.rodList.get(2).getDiskStack().size() == noOfDisks){
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                MediaPlayer player = MediaPlayer.create(context, R.raw.looney);
                                player.setLooping(false); // Set looping
                                player.setVolume(1.0f, 1.0f);
                                player.start();
                            }
                        });
                    }
                } else{
                    // cannot add to this peg
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                           Toast.makeText(context, "Oops!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    gameState.CURRENT_STATUS = GameState.State.FALLING;
                    gameState.bounceDisk();
                    gameState.CURRENT_STATUS = GameState.State.UP;
                }


            } else if( (swipeType == SWIPE_RIGHT_LEFT || swipeType == SWIPE_LEFT_RIGHT) && gameState.CURRENT_STATUS == GameState.State.UP){

                gameState.destRod = gameState.rodList.get(selectedRod);
                gameState.CURRENT_STATUS = GameState.State.FLOATING;
                gameState.moveDisk();
                gameState.CURRENT_STATUS = GameState.State.UP;
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            gameState.reset();
            super.onLongPress(e);
        }
    }
}
