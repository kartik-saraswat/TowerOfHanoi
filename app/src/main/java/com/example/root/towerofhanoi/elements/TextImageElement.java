package com.example.root.towerofhanoi.elements;

import android.graphics.Color;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.AlphaMapTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;

public class TextImageElement extends Plane{

    String name;
    int textImageId;
    float size;

    public TextImageElement(String name, int textImageId, float size) {
        super(size, size, 1, 1);
        this.name = name;
        this.textImageId = textImageId;
        this.size = size;

        Material material = new Material();
        AlphaMapTexture texture = new AlphaMapTexture("tex_"+this.name, textImageId);
        try {
            material.addTexture(texture);
            this.setColor(Color.TRANSPARENT);
            this.setDoubleSided(true);
            this.setMaterial(material);
            this.rotate(Vector3.Axis.Y, 180);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
    }

    public float getSize() {
        return size;
    }
}
