package com.example.root.towerofhanoi.elements;

import android.graphics.Color;

import com.example.root.towerofhanoi.R;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.EllipticalOrbitAnimation3D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;

import static com.example.root.towerofhanoi.elements.Geometry.*;
import static com.example.root.towerofhanoi.elements.ObjectLoader.*;

/**
 * Created by root on 23/3/16.
 */
public class SolidObject {

    public enum  SolidObjectType{
        CUBOID, DISK, ROD
    }

    private String name;
    private float xSize, ySize, zSize;
    private int color;
    private SolidObjectType type;
    private Vector3 initialPosition;
    private Vector3 position;

    private Object3D solidObject;
    private Material material;

    public SolidObject(String name, float xSize, float ySize, float zSize, int color, SolidObjectType type) {
        this(name, xSize, ySize, zSize, color, type, new Vector3(0,0,0));
    }

    public SolidObject(String name, float xSize, float ySize, float zSize, int color, SolidObjectType type, Vector3 position) {
        this.name = name;
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        this.color = color;
        this.type = type;
        this.position = new Vector3(position);

        switch (this.type){
            case CUBOID : this.solidObject = rootCuboid.clone(false);break;
            case DISK : this.solidObject = rootDisk.clone(false);break;
            case ROD : this.solidObject = rootRod.clone(false);break;
        }
        this.material = new Material();
        this.material.enableLighting(true);
        this.material.setColor(0);
        this.material.setDiffuseMethod(new DiffuseMethod.Lambert());

        solidObject.setName(this.name);
        if(this.type != SolidObjectType.CUBOID) {
            solidObject.setColor(this.color);
        } else{
            Texture texture = new Texture("BASE_TEX", R.drawable.wood_texture);
            try {
                material.addTexture(texture);
            } catch (ATexture.TextureException e) {
                e.printStackTrace();
            }
        }
        solidObject.setMaterial(this.material);

        solidObject.setScale(this.xSize, this.ySize, this.zSize);
        solidObject.setPosition(this.position);
    }

    public String getName() {
        return name;
    }

    public float getxSize() {
        return xSize;
    }

    public float getySize() {
        return ySize;
    }

    public float getzSize() {
        return zSize;
    }

    public int getColor() {
        return color;
    }

    public SolidObjectType getType() {
        return type;
    }

    public Vector3 getPosition() {
        this.position.setAll(solidObject.getPosition());
        return this.position;
    }

    public Object3D getSolidObject() {
        return solidObject;
    }

    public void setPosition(double x, double y, double z) {
        if(initialPosition == null){
            initialPosition = new Vector3(x,y,z);
        }
        this.position.setAll(x,y,z);
        solidObject.setPosition(position);
    }

    public Vector3 getInitialPosition() {
        return initialPosition;
    }

    public void setPosition(Vector3 position) {
        setPosition(position.x, position.y, position.z);
    }

    public double getX() { return getPosition().x; }

    public double getY(){
        return getPosition().y;
    }

    public double getZ(){
        return getPosition().z;
    }

    public void setX(double x){
        position.x = x;
        solidObject.setPosition(position);
    }

    public void setY(double y){
        position.y = y;
        solidObject.setPosition(position);
    }

    public void setZ(double z){
        position.z = z;
        solidObject.setPosition(position);
    }

    public void translate(float x, float y, float z){
        this.setPosition(this.position.x + x,
                this.position.y + y,
                this.position.z + z);
    }

    public double getTopY(){
        return this.getY() + this.getySize();
    }

    public double getBottomY(){
        return this.getY() - this.getySize();
    }

    public double getLeftX(){
        return this.getX() - this.getxSize();
    }
}
