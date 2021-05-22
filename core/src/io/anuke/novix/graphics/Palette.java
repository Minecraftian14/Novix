package io.anuke.novix.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

/* renamed from: io.anuke.novix.graphics.Palette */
public class Palette implements Comparable<Palette> {
    public Color[] colors;

    /* renamed from: id */
    public String id;
    public String name;
    public long time;

    public Palette(String name2, String id, int size, boolean auto) {
        this.name = name2;
        this.id = id;
        this.colors = new Color[size];
        this.time = TimeUtils.millis();
        if (auto) {
            genColors();
            return;
        }
        for (int i = 0; i < size; i++) {
            this.colors[i] = Color.BLACK.cpy();
        }
    }

    public Palette() {
    }

    public int size() {
        return this.colors.length;
    }

    private void genColors() {
        int clen = MathUtils.random(1, 4);
        int ci = 0;
        Color current = null;
        for (int i = 0; i < this.colors.length; i++) {
            if (current == null) {
                current = new Color(MathUtils.random(0.5f), MathUtils.random(0.5f), MathUtils.random(0.5f), 1.0f);
            } else {
                float b = MathUtils.random(0.1f);
                current = new Color(current).add(b, b, b, 0.0f).add(MathUtils.random(0.2f), MathUtils.random(0.2f), MathUtils.random(0.2f), 0.0f);
                current.clamp();
            }
            this.colors[i] = current;
            ci++;
            if (ci > clen) {
                current = null;
                ci = 0;
            }
        }
    }

    public int compareTo(Palette other) {
        if (other.time == this.time) {
            return 0;
        }
        return other.time > this.time ? -1 : 1;
    }
}
