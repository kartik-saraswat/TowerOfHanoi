package com.example.root.towerofhanoi.elements;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;

/**
 * Created by root on 24/3/16.
 */
public class Base extends SolidObject {

    public Base(String name, float length, float breadth, float height, int color) {
        super(name, breadth, height, length, color, SolidObjectType.CUBOID);
    }

    public void setTexture(int resourceId){

        Texture texture = new Texture("BASE_TEX", resourceId);
        Material material = new Material();
        material.setColor(0);
        try {
            material.addTexture(texture);
            getSolidObject().setMaterial(material);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
    }

    public float getLength(){
        return 2*getzSize();
    }

    public float getBreadth(){
        return 2*getxSize();
    }

    public float getHeight(){
        return 2*getySize();
    }
}
