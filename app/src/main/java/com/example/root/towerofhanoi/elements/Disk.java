package com.example.root.towerofhanoi.elements;

/**
 * Created by root on 24/3/16.
 */
public class Disk extends SolidObject {

    public Disk(String name, float length, float radius, int color) {
        super(name, radius, length, radius, color, SolidObjectType.DISK);
    }

    public float getRadius(){
        return 2*getxSize();
    }

    public float getLength(){
        return 2*getySize();
    }
}
