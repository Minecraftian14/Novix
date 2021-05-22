package io.anuke.ucore.util;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.FloatArray;
import java.awt.Polygon;
import java.awt.geom.PathIterator;

/* renamed from: io.anuke.ucore.util.Geometry */
public class Geometry {

    /* renamed from: e */
    private static final Vector2 f332e = new Vector2();
    private static final Vector2 ep1 = new Vector2();
    private static final Vector2 ep2 = new Vector2();
    private static final FloatArray floatArray = new FloatArray();
    private static final FloatArray floatArray2 = new FloatArray();

    /* renamed from: ip */
    private static final Vector2 f333ip = new Vector2();

    /* renamed from: s */
    private static final Vector2 f334s = new Vector2();

    /* renamed from: io.anuke.ucore.util.Geometry$PositionConsumer */
    public interface PositionConsumer {
        void consume(float f, float f2);
    }

    /* renamed from: io.anuke.ucore.util.Geometry$SegmentConsumer */
    public interface SegmentConsumer {
        void consume(float f, float f2, float f3, float f4);
    }

    public static float[] regPoly(int amount, float size) {
        float[] v = new float[(amount * 2)];
        Vector2 vec = new Vector2(1.0f, 1.0f);
        vec.setLength(size);
        for (int i = 0; i < amount; i++) {
            vec.setAngle(((360.0f / ((float) amount)) * ((float) i)) + 90.0f);
            v[i * 2] = vec.x;
            v[(i * 2) + 1] = vec.y;
        }
        return v;
    }

    public static Polygon polygon(float[] vertices) {
        Polygon poly = new Polygon();
        for (int i = 0; i < vertices.length / 2; i++) {
            poly.addPoint((int) vertices[i * 2], (int) vertices[(i * 2) + 1]);
        }
        return poly;
    }

    public static boolean intersectPolygons(float[] p1, float[] p2) {
        floatArray2.clear();
        floatArray.clear();
        floatArray2.addAll(p1);
        if (p1.length == 0 || p2.length == 0) {
            return false;
        }
        for (int i = 0; i < p2.length; i += 2) {
            ep1.set(p2[i], p2[i + 1]);
            if (i < p2.length - 2) {
                ep2.set(p2[i + 2], p2[i + 3]);
            } else {
                ep2.set(p2[0], p2[1]);
            }
            if (floatArray2.size == 0) {
                return false;
            }
            f334s.set(floatArray2.get(floatArray2.size - 2), floatArray2.get(floatArray2.size - 1));
            for (int j = 0; j < floatArray2.size; j += 2) {
                f332e.set(floatArray2.get(j), floatArray2.get(j + 1));
                if (Intersector.pointLineSide(ep2, ep1, f332e) > 0) {
                    if (Intersector.pointLineSide(ep2, ep1, f334s) <= 0) {
                        Intersector.intersectLines(f334s, f332e, ep1, ep2, f333ip);
                        if (!(floatArray.size >= 2 && floatArray.get(floatArray.size - 2) == f333ip.x && floatArray.get(floatArray.size - 1) == f333ip.y)) {
                            floatArray.add(f333ip.x);
                            floatArray.add(f333ip.y);
                        }
                    }
                    floatArray.add(f332e.x);
                    floatArray.add(f332e.y);
                } else if (Intersector.pointLineSide(ep2, ep1, f334s) > 0) {
                    Intersector.intersectLines(f334s, f332e, ep1, ep2, f333ip);
                    floatArray.add(f333ip.x);
                    floatArray.add(f333ip.y);
                }
                f334s.set(f332e.x, f332e.y);
            }
            floatArray2.clear();
            floatArray2.addAll(floatArray);
            floatArray.clear();
        }
        if (floatArray2.size != 0) {
            return true;
        }
        return false;
    }

    public static float iterateLine(float start, float x1, float y1, float x2, float y2, float segment, PositionConsumer pos) {
        float len = Vector2.dst(x1, y1, x2, y2);
        int steps = (int) (len / segment);
        float step = 1.0f / ((float) steps);
        float offset = len;
        ep2.set(x2, y2);
        for (int i = 0; i < steps; i++) {
            ep1.set(x1, y1);
            ep1.lerp(ep2, step * ((float) i));
            pos.consume(ep1.x, ep1.y);
            offset -= step;
        }
        return offset;
    }

    public static void iteratePolySegments(float[] vertices, SegmentConsumer it) {
        float x2;
        float y2;
        for (int i = 0; i < vertices.length; i += 2) {
            float x = vertices[i];
            float y = vertices[i + 1];
            if (i == vertices.length - 2) {
                x2 = vertices[0];
                y2 = vertices[1];
            } else {
                x2 = vertices[i + 2];
                y2 = vertices[i + 3];
            }
            it.consume(x, y, x2, y2);
        }
    }

    public static void iteratePolygon(PositionConsumer path, float[] vertices) {
        for (int i = 0; i < vertices.length; i += 2) {
            path.consume(vertices[i], vertices[i + 1]);
        }
    }

    public static void iterate(PathIterator path, PositionConsumer c) {
        float[] floats = new float[2];
        while (!path.isDone()) {
            path.currentSegment(floats);
            c.consume(floats[0], floats[1]);
            path.next();
        }
    }

    public static float[] array(PathIterator path) {
        final FloatArray array = new FloatArray();
        iterate(path, new PositionConsumer() {
            public void consume(float x, float y) {
                array.add(x);
                array.add(y);
            }
        });
        return array.toArray();
    }
}
