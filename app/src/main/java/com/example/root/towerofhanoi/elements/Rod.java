package com.example.root.towerofhanoi.elements;

import android.util.Log;

import org.rajawali3d.math.vector.Vector3;

import java.util.Stack;

/**
 * Created by root on 24/3/16.
 */
public class Rod extends SolidObject {

    Stack<Disk> diskStack;

    public Rod(String name, float length, float radius, int color) {
        super(name, radius, length, radius, color, SolidObjectType.ROD);
        diskStack = new Stack<>();
    }

    public float getRadius(){
        return 2*getxSize();
    }

    public float getLength(){
        return 2*getySize();
    }

    public Stack<Disk> getDiskStack() {
        return diskStack;
    }

    public boolean hasDisks(){
        return !diskStack.empty();
    }

    public Disk topDisk(){
        if (hasDisks()) {
            return diskStack.peek();
        } else {
            return null;
        }
    }

    public void pushDisk(Disk disk){
        diskStack.push(disk);
    }

    public void popDisk(){
        if (hasDisks()) {
            diskStack.pop();
        }
    }

    public double getPeekY(){
        double yPos = this.getBottomY();
        if(hasDisks()){
            yPos = diskStack.peek().getTopY();
        }

        return  yPos;
    }

    public void clear(){
        diskStack.clear();
    }

}

