package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import io.anuke.ucore.util.Mathf;

/* renamed from: io.anuke.ucore.graphics.Hue */
public class Hue {
    private static float[] hsv = new float[3];

    public static boolean approximate(Color a, Color b, float r) {
        return MathUtils.isEqual(a.r, b.r, r) && MathUtils.isEqual(a.g, b.g, r) && MathUtils.isEqual(a.b, b.b, r);
    }

    public static Color round(Color color, float amount) {
        color.r = ((float) ((int) (color.r / amount))) * amount;
        color.g = ((float) ((int) (color.g / amount))) * amount;
        color.b = ((float) ((int) (color.b / amount))) * amount;
        return color;
    }

    public static int closest(Color input, Color[] colors) {
        float bh = RGBtoHSB(input, hsv)[0];
        float bs = hsv[1];
        int index = 0;
        float cd = 360.0f;
        for (int i = 0; i < colors.length; i++) {
            RGBtoHSB(colors[i], hsv);
            float cr = Math.abs(bh - hsv[0]) + Math.abs(bs - hsv[1]);
            if (cr < cd) {
                index = i;
                cd = cr;
            }
        }
        return index;
    }

    public static float sum(Color color) {
        return color.r + color.g + color.b;
    }

    public static float diff(Color a, Color b) {
        return Math.abs(a.r - b.r) + Math.abs(a.g - b.g) + Math.abs(a.b - b.b);
    }

    public static void addu(Color a, Color b) {
        a.r += b.r;
        a.g += b.g;
        a.b += b.b;
        a.a += b.a;
    }

    public static Color fromHSB(float hue, float saturation, float brightness) {
        return fromHSB(hue, saturation, brightness, new Color());
    }

    public static Color fromHSB(float hue, float saturation, float brightness, Color color) {
        int r = 0;
        int g = 0;
        int b = 0;
        if (saturation != 0.0f) {
            float h = (hue - ((float) Math.floor((double) hue))) * 6.0f;
            float f = h - ((float) Math.floor((double) h));
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - (saturation * f));
            float t = brightness * (1.0f - ((1.0f - f) * saturation));
            switch ((int) h) {
                case 0:
                    r = (int) ((255.0f * brightness) + 0.5f);
                    g = (int) ((255.0f * t) + 0.5f);
                    b = (int) ((255.0f * p) + 0.5f);
                    break;
                case 1:
                    r = (int) ((255.0f * q) + 0.5f);
                    g = (int) ((255.0f * brightness) + 0.5f);
                    b = (int) ((255.0f * p) + 0.5f);
                    break;
                case 2:
                    r = (int) ((255.0f * p) + 0.5f);
                    g = (int) ((255.0f * brightness) + 0.5f);
                    b = (int) ((255.0f * t) + 0.5f);
                    break;
                case 3:
                    r = (int) ((255.0f * p) + 0.5f);
                    g = (int) ((255.0f * q) + 0.5f);
                    b = (int) ((255.0f * brightness) + 0.5f);
                    break;
                case 4:
                    r = (int) ((255.0f * t) + 0.5f);
                    g = (int) ((255.0f * p) + 0.5f);
                    b = (int) ((255.0f * brightness) + 0.5f);
                    break;
                case 5:
                    r = (int) ((255.0f * brightness) + 0.5f);
                    g = (int) ((255.0f * p) + 0.5f);
                    b = (int) ((255.0f * q) + 0.5f);
                    break;
            }
        } else {
            b = (int) ((255.0f * brightness) + 0.5f);
            g = b;
            r = b;
        }
        return color.set(((float) r) / 255.0f, ((float) g) / 255.0f, ((float) b) / 255.0f, 1.0f);
    }

    public static float[] RGBtoHSB(int r, int g, int b, float[] hsbvals) {
        int cmax;
        int cmin;
        float saturation;
        float hue;
        float hue2;
        if (hsbvals == null) {
            hsbvals = new float[3];
        }
        if (r > g) {
            cmax = r;
        } else {
            cmax = g;
        }
        if (b > cmax) {
            cmax = b;
        }
        if (r < g) {
            cmin = r;
        } else {
            cmin = g;
        }
        if (b < cmin) {
            cmin = b;
        }
        float brightness = ((float) cmax) / 255.0f;
        if (cmax != 0) {
            saturation = ((float) (cmax - cmin)) / ((float) cmax);
        } else {
            saturation = 0.0f;
        }
        if (saturation == 0.0f) {
            hue2 = 0.0f;
        } else {
            float redc = ((float) (cmax - r)) / ((float) (cmax - cmin));
            float greenc = ((float) (cmax - g)) / ((float) (cmax - cmin));
            float bluec = ((float) (cmax - b)) / ((float) (cmax - cmin));
            if (r == cmax) {
                hue = bluec - greenc;
            } else if (g == cmax) {
                hue = (2.0f + redc) - bluec;
            } else {
                hue = (4.0f + greenc) - redc;
            }
            hue2 = hue / 6.0f;
            if (hue2 < 0.0f) {
                hue2 += 1.0f;
            }
        }
        hsbvals[0] = hue2;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }

    public static float[] RGBtoHSB(Color color, float[] hsbvals) {
        return RGBtoHSB((int) (color.r * 255.0f), (int) (color.g * 255.0f), (int) (color.b * 255.0f), hsbvals);
    }

    public static Color mix(int r1, int g1, int b1, int r2, int g2, int b2, float s) {
        float i = 1.0f - s;
        return rgb((((float) r1) * i) + (((float) r2) * s), (((float) g1) * i) + (((float) g2) * s), (((float) b1) * i) + (((float) b2) * s));
    }

    public static Color mix(Color a, Color b, float s) {
        float i = 1.0f - s;
        return new Color((a.r * i) + (b.r * s), (a.g * i) + (b.g * s), (a.b * i) + (b.b * s), 1.0f);
    }

    public static Color mix(Color[] colors, Color out, float s) {
        int l = colors.length;
        Color a = colors[(int) (((float) (l - 1)) * s)];
        Color b = colors[Mathf.clamp((int) ((((float) (l - 1)) * s) + 1.0f), 0, l - 1)];
        float n = (((float) (l - 1)) * s) - ((float) ((int) (((float) (l - 1)) * s)));
        float i = 1.0f - n;
        return out.set((a.r * i) + (b.r * n), (a.g * i) + (b.g * n), (a.b * i) + (b.b * n), 1.0f);
    }

    public static Color mix(Color a, Color b, float s, Color to) {
        float i = 1.0f - s;
        return to.set((a.r * i) + (b.r * s), (a.g * i) + (b.g * s), (a.b * i) + (b.b * s), 1.0f);
    }

    public static Color blend2d(Color a, Color b, Color c, Color d, float x, float y) {
        return new Color((((b.r * y) + (a.r * (1.0f - y))) * x) + (((d.r * y) + (c.r * (1.0f - y))) * (1.0f - x)), (((b.g * y) + (a.g * (1.0f - y))) * x) + (((d.g * y) + (c.g * (1.0f - y))) * (1.0f - x)), (((b.b * y) + (a.b * (1.0f - y))) * x) + (((d.b * y) + (c.b * (1.0f - y))) * (1.0f - x)), 1.0f).clamp();
    }

    public static Color rgb(int r, int g, int b) {
        return new Color(((float) r) / 255.0f, ((float) g) / 255.0f, ((float) b) / 255.0f, 1.0f);
    }

    public static Color rgb(float r, float g, float b) {
        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, 1.0f);
    }

    public static Color rgb(int r, int g, int b, float brightness) {
        return new Color((((float) r) / 255.0f) + brightness, (((float) g) / 255.0f) + brightness, (((float) b) / 255.0f) + brightness, 1.0f);
    }

    public static Color lightness(float l) {
        return new Color(l, l, l, 1.0f);
    }
}
