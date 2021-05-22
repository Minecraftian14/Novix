package io.anuke.ucore;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import java.lang.reflect.Field;

/* renamed from: io.anuke.ucore.UCore */
public class UCore {

    /* renamed from: s */
    public static final float s = (Gdx.app == null ? 0.0f : Gdx.app.getType() == Application.ApplicationType.Desktop ? 1.0f : Gdx.graphics.getDensity() / 1.5f);

    public static String parseException(Exception e) {
        StringBuilder build = new StringBuilder();
        build.append(String.valueOf(e.getClass().getName()) + ": " + e.getMessage());
        StackTraceElement[] stackTrace = e.getStackTrace();
        int length = stackTrace.length;
        for (int i = 0; i < length; i++) {
            build.append("\n" + stackTrace[i].toString());
        }
        return build.toString();
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
}
