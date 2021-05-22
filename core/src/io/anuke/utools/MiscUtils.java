package io.anuke.utools;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import gwt.awt.Rectangle;
import gwt.awt.geom.AffineTransform;
import gwt.awt.geom.Area;
import gwt.awt.geom.PathIterator;
import java.lang.reflect.Field;

/* renamed from: io.anuke.utools.MiscUtils */
public class MiscUtils {
    private static final char[] ILLEGAL_CHARACTERS;

    /* renamed from: io.anuke.utools.MiscUtils$GridChecker */
    public interface GridChecker {
        boolean exists(int i, int i2);

        int getHeight();

        int getWidth();
    }

    static {
        char[] cArr = new char[15];
        cArr[0] = '/';
        cArr[1] = 10;
        cArr[2] = 13;
        cArr[3] = 9;
        cArr[5] = 12;
        cArr[6] = '`';
        cArr[7] = '?';
        cArr[8] = '*';
        cArr[9] = '\\';
        cArr[10] = '<';
        cArr[11] = '>';
        cArr[12] = '|';
        cArr[13] = '\"';
        cArr[14] = ':';
        ILLEGAL_CHARACTERS = cArr;
    }

    public static Object getPrivate(Object object, String name) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int asInt(int x, int y, int gridwidth) {
        return (y * gridwidth) + x;
    }

    public static boolean inRange(float x, float y, float x2, float y2, float width, float height) {
        return MathUtils.isEqual(x, x2, width) && MathUtils.isEqual(y, y2, height);
    }

    public static float densityScale() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            return 1.0f;
        }
        return Gdx.graphics.getDensity() / 1.5f;
    }

    public static float densityScale(float mul) {
        return densityScale() * mul;
    }

    public static int densityScale(int mul) {
        return (int) (((float) mul) * densityScale());
    }

    public static String capitalize(String string) {
        return String.valueOf(string.substring(0, 1).toUpperCase()) + string.substring(1);
    }

    public static String limit(String string, int len) {
        return string.substring(0, Math.min(string.length(), len));
    }

    public static void drawBorder(Batch batch, float x, float y, float width, float height, int thickness) {
        drawBorder(batch, x, y, width, height, thickness, 0);
    }

    public static void drawBorder(Batch batch, float x, float y, float width, float height, int thickness, int space) {
        drawBorder(batch, x, y, width, height, thickness, space, space);
    }

    public static void drawBorder(Batch batch, float x, float y, float width, float height, int thickness, int xspace, int yspace) {
        float x2 = x - ((float) xspace);
        float y2 = y - ((float) yspace);
        float width2 = width + ((float) (xspace * 2));
        float height2 = height + ((float) (yspace * 2));
        batch.draw((TextureRegion) VisUI.getSkin().getAtlas().findRegion("white"), x2, y2, width2, (float) thickness);
        Batch batch2 = batch;
        float f = x2;
        batch2.draw((TextureRegion) VisUI.getSkin().getAtlas().findRegion("white"), f, y2 + height2, width2, (float) (-thickness));
        Batch batch3 = batch;
        batch3.draw((TextureRegion) VisUI.getSkin().getAtlas().findRegion("white"), x2 + width2, y2, (float) (-thickness), height2);
        batch.draw((TextureRegion) VisUI.getSkin().getAtlas().findRegion("white"), x2, y2, (float) thickness, height2);
    }

    public static void setBatchAlpha(Batch batch, float alpha) {
        Color color = batch.getColor();
        color.a *= alpha;
        batch.setColor(color);
    }

    public static void drawMasked(Batch batch, TextureRegion region, float fx, float fy, float fwidth, float fheight, float fmx, float fmy, float fmwidth, float fmheight) {
        int x = (int) fx;
        int y = (int) fy;
        int width = (int) fwidth;
        int height = (int) fheight;
        int mx = (int) fmx;
        int my = (int) fmy;
        int mwidth = (int) fmwidth;
        int mheight = (int) fmheight;
        if (mwidth < 0) {
            mx += mwidth;
            mwidth = -mwidth;
        }
        if (mheight < 0) {
            my += mheight;
            mheight = -mheight;
        }
        Batch batch2 = batch;
        TextureRegion textureRegion = region;
        batch2.draw(textureRegion, (float) x, (float) y, (float) width, (float) (my - y));
        Batch batch3 = batch;
        TextureRegion textureRegion2 = region;
        batch3.draw(textureRegion2, (float) x, (float) (my + mheight), (float) width, (float) ((y + height) - (my + mheight)));
        batch.draw(region, (float) x, (float) my, (float) (mx - x), (float) mheight);
        batch.draw(region, (float) (mx + mwidth), (float) my, (float) ((x + width) - (mx + mwidth)), (float) mheight);
    }

    public static String displayFloat(float f) {
        String string = new StringBuilder(String.valueOf(f)).toString();
        if (string.endsWith(".0")) {
            return string.substring(0, string.length() - 2);
        }
        return string;
    }

    public static boolean isFileNameValid(String file) {
        for (char c : ILLEGAL_CHARACTERS) {
            if (file.indexOf(c) != -1) {
                return false;
            }
        }
        if (file.isEmpty() || file.replace(" ", "").isEmpty()) {
            return false;
        }
        return true;
    }

    public static Vector2[] getOutline(GridChecker checker) {
        Array<Vector2> vertices = new Array<>();
        Area area = new Area();
        for (int x = 0; x < checker.getWidth(); x++) {
            for (int y = 0; y < checker.getHeight(); y++) {
                if (checker.exists(x, y)) {
                    area.add(new Area(new Rectangle(x - (checker.getWidth() / 2), y - (checker.getHeight() / 2), 1, 1)));
                }
            }
        }
        PathIterator path = area.getPathIterator((AffineTransform) null);
        float[] points = new float[2];
        Array<Vector2> removal = new Array<>();
        Vector2 last = null;
        Vector2 markedremoval = null;
        while (!path.isDone()) {
            path.currentSegment(points);
            Vector2 vector = new Vector2(points[0], points[1]);
            if (markedremoval != null) {
                if (MathUtils.isEqual(vector.x, markedremoval.x)) {
                    removal.add(markedremoval);
                }
                markedremoval = null;
            }
            if (last != null && MathUtils.isEqual(vector.x, last.x)) {
                markedremoval = vector;
            }
            last = vector;
            vertices.add(vector);
            path.next();
        }
        vertices.removeAll(removal, true);
        return (Vector2[]) vertices.toArray(Vector2.class);
    }

    public static String getLineNumber() {
        StackTraceElement e = Thread.currentThread().getStackTrace()[2];
        return String.valueOf(e.getFileName()) + ": " + e.getMethodName() + "()\nLine: " + e.getLineNumber();
    }
}
