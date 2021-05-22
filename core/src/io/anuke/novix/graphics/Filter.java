package io.anuke.novix.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.kotcrab.vis.ui.util.ColorUtils;
import io.anuke.ucore.graphics.Hue;

/* renamed from: io.anuke.novix.graphics.Filter */
public enum Filter {
    flip {
        public void applyTo(Pixmap input, Pixmap pixmap, Object... args) {
            boolean vertical = (boolean) args[0];
            for (int x = 0; x < pixmap.getWidth(); x++) {
                for (int y = 0; y < pixmap.getHeight(); y++) {
                    if (vertical) {
                        pixmap.drawPixel(x, y, input.getPixel((input.getWidth() - 1) - x, y));
                    } else {
                        pixmap.drawPixel(x, y, input.getPixel(x, (input.getHeight() - 1) - y));
                    }
                }
            }
        }
    },
    rotate {
        public void applyTo(Pixmap input, Pixmap pixmap, Object... args) {
            float angle = (float) args[0];
            Vector2 vector = new Vector2();
            for (int x = 0; x < input.getWidth(); x++) {
                for (int y = 0; y < input.getHeight(); y++) {
                    vector.set((((float) x) - (((float) input.getWidth()) / 2.0f)) + 0.5f, (((float) y) - (((float) input.getHeight()) / 2.0f)) + 0.5f);
                    vector.rotate(angle);
                    pixmap.drawPixel(x, y, input.getPixel((int) (vector.x + (((float) input.getWidth()) / 2.0f)), (int) (vector.y + (((float) input.getHeight()) / 2.0f))));
                }
            }
        }
    },
    colorize {
        float[] hsb;

        /* access modifiers changed from: protected */
        public void applyPixel(Pixmap input, Pixmap pixmap, int x, int y, Color color, Object... args) {
            float h = (float) args[0];
            float s = (float) args[1];
            float b = (float) args[2];
            Hue.RGBtoHSB((int) (color.r * 255.0f), (int) (color.g * 255.0f), (int) (color.b * 255.0f), this.hsb);
            float[] fArr = this.hsb;
            fArr[0] = fArr[0] + (h - 0.5f);
            if (this.hsb[0] < 0.0f) {
                float[] fArr2 = this.hsb;
                fArr2[0] = fArr2[0] + 1.0f;
            }
            if (this.hsb[0] > 1.0f) {
                float[] fArr3 = this.hsb;
                fArr3[0] = fArr3[0] - 1.0f;
            }
            float[] fArr4 = this.hsb;
            fArr4[1] = fArr4[1] * 2.0f * s;
            float[] fArr5 = this.hsb;
            fArr5[2] = fArr5[2] * 2.0f * b;
            ColorUtils.HSVtoRGB(this.hsb[0] * 360.0f, this.hsb[1] * 100.0f, this.hsb[2] * 100.0f, color);
            pixmap.setColor(color);
            pixmap.drawPixel(x, y);
        }
    },
    invert {
        /* access modifiers changed from: protected */
        public void applyPixel(Pixmap input, Pixmap pixmap, int x, int y, Color color, Object... args) {
            color.set(1.0f - color.r, 1.0f - color.g, 1.0f - color.b, color.a);
            pixmap.setColor(color);
            pixmap.drawPixel(x, y);
        }
    },
    replace {
        /* access modifiers changed from: protected */
        public void applyPixel(Pixmap input, Pixmap pixmap, int x, int y, Color color, Object... args) {
            Color to = (Color) args[1];
            if (Hue.approximate((Color) args[0], color, 0.01f)) {
                pixmap.setColor(to);
                pixmap.drawPixel(x, y);
                return;
            }
            pixmap.drawPixel(x, y, input.getPixel(x, y));
        }
    },
    colorToAlpha {
        /* access modifiers changed from: protected */
        public void applyPixel(Pixmap input, Pixmap pixmap, int x, int y, Color color, Object... args) {
            if (color.equals(args[0])) {
                pixmap.setColor(Color.CLEAR);
                pixmap.drawPixel(x, y);
                return;
            }
            pixmap.drawPixel(x, y, input.getPixel(x, y));
        }
    },
    outline {
        /* access modifiers changed from: protected */
        public void applyPixel(Pixmap input, Pixmap pixmap, int x, int y, Color color, Object... args) {
            Color from = (Color) args[0];
            if (color.a >= 0.001f) {
                return;
            }
            if (!empty(input.getPixel(x + 1, y)) || !empty(input.getPixel(x - 1, y)) || !empty(input.getPixel(x, y + 1)) || !empty(input.getPixel(x, y - 1))) {
                color.set(from);
                pixmap.setColor(color);
                pixmap.drawPixel(x, y);
            }
        }

        public boolean empty(int value) {
            return alpha(value) == 0;
        }

        public int alpha(int value) {
            return value & 255;
        }
    },
    contrast {
        /* access modifiers changed from: protected */
        public void applyPixel(Pixmap input, Pixmap pixmap, int x, int y, Color color, Object... args) {
            float contrast = (float) args[0];
            float factor = ((contrast + 1.0f) * 1.0156863f) / ((1.0156863f - contrast) * 1.0f);
            pixmap.setColor(color.set(((color.r - 0.44f) * factor) + 0.44f, ((color.g - 0.44f) * factor) + 0.44f, ((color.b - 0.44f) * factor) + 0.44f, color.a));
            pixmap.drawPixel(x, y);
        }
    };
    
    protected static Color color;

    static {
        color = new Color();
    }

    /* access modifiers changed from: protected */
    public void applyTo(Pixmap input, Pixmap pixmap, Object... args) {
        for (int x = 0; x < input.getWidth(); x++) {
            for (int y = 0; y < input.getHeight(); y++) {
                applyPixel(input, pixmap, x, y, color.set(input.getPixel(x, y)), args);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void applyPixel(Pixmap input, Pixmap pixmap, int x, int y, Color color2, Object... args) {
    }

    public void apply(Pixmap input, Pixmap pixmap, Object... args) {
        pixmap.setBlending(Pixmap.Blending.None);
        applyTo(input, pixmap, args);
        pixmap.setBlending(Pixmap.Blending.SourceOver);
    }
}
