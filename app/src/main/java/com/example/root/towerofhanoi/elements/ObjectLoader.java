package com.example.root.towerofhanoi.elements;

import android.graphics.Color;

import static com.example.root.towerofhanoi.elements.Geometry.*;

import com.example.root.towerofhanoi.R;

import org.rajawali3d.Object3D;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.renderer.RajawaliRenderer;

/**
 * Created by root on 22/3/16.
 */
public class ObjectLoader {


    public static Object3D rootDisk;
    public static Object3D rootRod;
    public static Object3D rootCuboid;

    public static void init(RajawaliRenderer renderer){

        //Load Disk
        LoaderOBJ diskObjParser = new LoaderOBJ(renderer.getContext().getResources(),
                renderer.getTextureManager(), R.raw.disk_obj);
        try {
            diskObjParser.parse();
            rootDisk = diskObjParser.getParsedObject();

        } catch (ParsingException e) {
            e.printStackTrace();
        }

        //Load Rod
        LoaderOBJ rodObjParser = new LoaderOBJ(renderer.getContext().getResources(),
                renderer.getTextureManager(), R.raw.rod_obj);
        try {
            rodObjParser.parse();
            rootRod = rodObjParser.getParsedObject();

        } catch (ParsingException e) {
            e.printStackTrace();
        }

        //Load Cuboid
        rootCuboid = new Cube(2);
        /*LoaderOBJ cuboidObjParser = new LoaderOBJ(renderer.getContext().getResources(),
                renderer.getTextureManager(), R.raw.cube_obj);
        try {
            cuboidObjParser.parse();
            rootCuboid = cuboidObjParser.getParsedObject();

        } catch (ParsingException e) {
            e.printStackTrace();
        }*/
    }
}