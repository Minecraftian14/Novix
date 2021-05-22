package io.anuke.ucore.util;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import java.util.Iterator;

/* renamed from: io.anuke.ucore.util.Timers */
public class Timers {
    private static Array<DelayRun> remove = new Array<>();
    private static Array<DelayRun> runs = new Array<>();
    private static float time;
    private static ObjectMap<String, Float> timers = new ObjectMap<>();

    public static void run(float delay, Runnable r) {
        DelayRun run = new DelayRun();
        run.run = r;
        run.delay = delay;
        runs.add(run);
    }

    public static boolean get(String name, float frames) {
        if (timers.containsKey(name)) {
            if (time - timers.get(name).floatValue() <= frames) {
                return false;
            }
            timers.put(name, Float.valueOf(time));
            return true;
        }
        timers.put(name, Float.valueOf(time));
        return true;
    }

    public static float time() {
        return time;
    }

    public static void update(float delta) {
        time += delta;
        remove.clear();
        Iterator<DelayRun> it = runs.iterator();
        while (it.hasNext()) {
            DelayRun run = it.next();
            run.delay -= delta;
            if (run.delay <= 0.0f) {
                run.run.run();
                remove.add(run);
            }
        }
        runs.removeAll(remove, true);
    }

    /* renamed from: io.anuke.ucore.util.Timers$DelayRun */
    static class DelayRun {
        float delay;
        Runnable run;

        DelayRun() {
        }
    }
}
