package com.example.root.towerofhanoi.elements;

import android.graphics.Color;

import org.rajawali3d.materials.Material;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Line3D;

import java.util.Stack;

/**
 * Created by root on 20/3/16.
 */
public class Geometry {

    public final static int POINT_ORIGIN = 0;

    public final static Vector3 originVector = new Vector3(0,0,0);

    public final static Vector3 tmpVector = new Vector3(0,0,0);

    public final static Vector3 VECTOR_NX_NY = new Vector3(-1, -1, 0);
    public final static Vector3 VECTOR_NX_PY = new Vector3(-1, 1, 0);
    public final static Vector3 VECTOR_PX_NY = new Vector3(1, -1, 0);
    public final static Vector3 VECTOR_PX_PY = new Vector3(1, 1, 0);

    public final static int OCTANT_NX_NY_NZ = 1;
    public final static int OCTANT_NX_NY_PZ = 2;
    public final static int OCTANT_NX_PY_NZ = 3;
    public final static int OCTANT_NX_PY_PZ = 4;
    public final static int OCTANT_PX_NY_NZ = 5;
    public final static int OCTANT_PX_NY_PZ = 6;
    public final static int OCTANT_PX_PY_NZ = 7;
    public final static int OCTANT_PX_PY_PZ = 8;

    public final static int QUADRANT_NX_NY = 9;
    public final static int QUADRANT_NX_PY = 10;
    public final static int QUADRANT_PX_NY = 11;
    public final static int QUADRANT_PX_PY = 12;

    public final static Vector3 VECTOR_NX_NY_NZ = new Vector3(-1, -1, -1);
    public final static Vector3 VECTOR_NX_NY_PZ = new Vector3(-1, -1, 1);
    public final static Vector3 VECTOR_NX_PY_NZ = new Vector3(-1, 1, -1);
    public final static Vector3 VECTOR_NX_PY_PZ = new Vector3(-1, 1, 1);
    public final static Vector3 VECTOR_PX_NY_NZ = new Vector3(1, -1, -1);
    public final static Vector3 VECTOR_PX_NY_PZ = new Vector3(1, -1, 1);
    public final static Vector3 VECTOR_PX_PY_NZ = new Vector3(1, 1, -1);
    public final static Vector3 VECTOR_PX_PY_PZ = new Vector3(1, 1, 1);

    public final static Vector3 VECTOR_NEG_X = new Vector3(-6, 0, 0);
    public final static Vector3 VECTOR_POS_X = new Vector3( 6, 0, 0);

    public final static Vector3 VECTOR_NEG_Y = new Vector3( 0,-6, 0);
    public final static Vector3 VECTOR_POS_Y = new Vector3( 0, 6, 0);

    public final static Vector3 VECTOR_NEG_Z = new Vector3( 0, 0,-6);
    public final static Vector3 VECTOR_POS_Z = new Vector3( 0, 0, 6);

    public static Stack<Vector3> xAxisPoints;
    public static Stack<Vector3> yAxisPoints;
    public static Stack<Vector3> zAxisPoints;

    public static Line3D xAxisLine;
    public static Line3D yAxisLine;
    public static Line3D zAxisLine;


    static {
        initialize();
    }

    public static void initialize() {

        xAxisPoints = new Stack<Vector3>();
        xAxisPoints.add(VECTOR_NEG_X);
        xAxisPoints.add(Vector3.ZERO);
        xAxisPoints.add(VECTOR_POS_X);


        yAxisPoints = new Stack<Vector3>();
        yAxisPoints.add(VECTOR_NEG_Y);
        yAxisPoints.add(Vector3.ZERO);
        yAxisPoints.add(VECTOR_POS_Y);

        zAxisPoints = new Stack<Vector3>();
        zAxisPoints.add(VECTOR_NEG_Z);
        zAxisPoints.add(Vector3.ZERO);
        zAxisPoints.add(VECTOR_POS_Z);

        xAxisLine = new Line3D(Geometry.xAxisPoints, 2, Color.GREEN);
        yAxisLine = new Line3D(Geometry.yAxisPoints, 2, Color.BLUE);
        zAxisLine = new Line3D(Geometry.zAxisPoints, 2, Color.YELLOW);

        Material lineMaterial = new Material();
        lineMaterial.setColor(Color.WHITE);

        xAxisLine.setMaterial(lineMaterial);
        yAxisLine.setMaterial(lineMaterial);
        zAxisLine.setMaterial(lineMaterial);
    }

    public static int getOctant(Vector3 point) {
        return getOctant(point.x, point.y, point.z);
    }

    public static int getOctant(double x, double y, double z) {
        if (x == 0 && y == 0 && z == 0) {
            return POINT_ORIGIN;
        }

        int sum = 0;
        if (x > 0) {
            sum += 1;
        }

        if (y > 0) {
            sum += 2;
        }

        if (z > 0) {
            sum += 4;
        }

        return sum;
    }

    public static int getQuadrant(double x, double y) {
        if (x == 0 && y == 0) {
            return POINT_ORIGIN;
        }

        int sum = 0;
        if (x > 0) {
            sum += 1;
        }

        if (y > 0) {
            sum += 2;
        }

        return sum;
    }

    public static Vector3 getUnitVector(int octant) {
        Vector3 vector3;
        switch (octant) {
            case OCTANT_NX_NY_NZ:
                vector3 = VECTOR_NX_NY_NZ;
                break;
            case OCTANT_NX_NY_PZ:
                vector3 = VECTOR_NX_NY_PZ;
                break;
            case OCTANT_NX_PY_NZ:
                vector3 = VECTOR_NX_PY_NZ;
                break;
            case OCTANT_NX_PY_PZ:
                vector3 = VECTOR_NX_NY_PZ;
                break;
            case OCTANT_PX_NY_NZ:
                vector3 = VECTOR_PX_NY_NZ;
                break;
            case OCTANT_PX_NY_PZ:
                vector3 = VECTOR_PX_NY_PZ;
                break;
            case OCTANT_PX_PY_NZ:
                vector3 = VECTOR_PX_PY_NZ;
                break;
            case OCTANT_PX_PY_PZ:
                vector3 = VECTOR_PX_PY_PZ;
                break;

            case QUADRANT_NX_NY:
                vector3 = VECTOR_NX_NY;
                break;
            case QUADRANT_NX_PY:
                vector3 = VECTOR_NX_PY;
                break;
            case QUADRANT_PX_NY:
                vector3 = VECTOR_PX_NY;
                break;
            case QUADRANT_PX_PY:
                vector3 = VECTOR_PX_PY;
                break;

            default:
                vector3 = Vector3.Z;
                break;
        }
        return vector3;
    }
}
