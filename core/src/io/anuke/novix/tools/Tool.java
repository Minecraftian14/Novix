package io.anuke.novix.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.IntSet;
import com.kotcrab.vis.ui.widget.VisImageButton;
import java.util.Stack;

import io.anuke.novix.modules.Core;
import io.anuke.novix.ui.DrawingGrid;
import io.anuke.ucore.graphics.Hue;
import io.anuke.utools.MiscUtils;

/* renamed from: io.anuke.novix.tools.Tool */
public enum Tool {
    pencil {
        public void clicked(Color color, PixelCanvas canvas, int x, int y) {
            canvas.drawRadius(x, y, Core.i.drawgrid.brushSize);
        }

        public boolean scalable() {
            return true;
        }
    },
    eraser {
        public void clicked(Color color, PixelCanvas canvas, int x, int y) {
            canvas.eraseRadius(x, y, Core.i.drawgrid.brushSize);
        }

        public void onColorChange(Color color, PixelCanvas canvas) {
            canvas.setColor(Color.CLEAR.cpy(), true);
        }

        public boolean scalable() {
            return true;
        }
    },
    fill(true, false) {
        public void clicked(Color color, PixelCanvas canvas, int x, int y) {
            canvas.texture.bind();
            int dest = canvas.getIntColor(x, y);
            int width = canvas.width();
            IntSet set = new IntSet();
            Stack<GridPoint2> points = new Stack<>();
            points.add(new GridPoint2(x, y));
            canvas.setColor(color.cpy());
            while (!points.isEmpty()) {
                GridPoint2 pos = points.pop();
                set.add(MiscUtils.asInt(pos.x, pos.y, width));
                if (colorEquals(canvas.getIntColor(pos.x, pos.y), dest)) {
                    canvas.drawPixel(pos.x, pos.y, false);
                    if (pos.x > 0 && !set.contains(MiscUtils.asInt(pos.x - 1, pos.y, width))) {
                        points.add(new GridPoint2(pos).cpy().add(-1, 0));
                    }
                    if (pos.y > 0 && !set.contains(MiscUtils.asInt(pos.x, pos.y - 1, width))) {
                        points.add(new GridPoint2(pos).cpy().add(0, -1));
                    }
                    if (pos.x < canvas.width() - 1 && !set.contains(MiscUtils.asInt(pos.x + 1, pos.y, width))) {
                        points.add(new GridPoint2(pos).cpy().add(1, 0));
                    }
                    if (pos.y < canvas.height() - 1 && !set.contains(MiscUtils.asInt(pos.x, pos.y + 1, width))) {
                        points.add(new GridPoint2(pos).cpy().add(0, 1));
                    }
                }
            }
            canvas.updateTexture();
        }

        /* access modifiers changed from: package-private */
        public boolean colorEquals(int a, int b) {
            return a == b;
        }
    },
    pick(false) {
        public void clicked(Color color, PixelCanvas canvas, int x, int y) {
            Color selected = canvas.getColor(x, y);
            for (int i = 0; i < Core.i.getCurrentPalette().size(); i++) {
                if (Hue.approximate(selected, Core.i.getCurrentPalette().colors[i], 0.001f)) {
                    Core.i.colormenu.setSelectedColor(i);
                    return;
                }
            }
            selected.a = 1.0f;
            Core.i.colormenu.setSelectedColor(selected);
            Core.i.colormenu.addRecentColor(selected);
        }

        public boolean symmetric() {
            return false;
        }
    },
    zoom(false, false) {
        public void clicked(Color color, PixelCanvas canvas, int x, int y) {
        }

        public void update(DrawingGrid grid) {
            grid.setCursor(((float) (Gdx.graphics.getWidth() / 2)) - grid.getX(), ((float) (Gdx.graphics.getHeight() / 2)) - grid.getY());
        }

        public boolean moveCursor() {
            return false;
        }

        public boolean drawCursor() {
            return false;
        }

        public boolean symmetric() {
            return false;
        }
    },
    undo(false, false) {
        public void onSelected() {
            Core.i.drawgrid.actions.undo(Core.i.canvas());
        }

        public boolean selectable() {
            return false;
        }
    },
    redo(false, false) {
        public void onSelected() {
            Core.i.drawgrid.actions.redo(Core.i.canvas());
        }

        public boolean selectable() {
            return false;
        }
    };
    
    public VisImageButton button;
    public String cursor;
    public final boolean drawOnMove;
    public final boolean push;

    private Tool() {
        this.cursor = "cursor";
        this.push = true; // TODO: is false a better value
        this.drawOnMove = true; // TODO: is false a better value
    }

    private Tool(boolean pushOnUp) {
        this.cursor = "cursor";
        this.push = pushOnUp;
        this.drawOnMove = true;
    }

    private Tool(boolean pushOnUp, boolean drawOnMove2) {
        this.cursor = "cursor";
        this.push = pushOnUp;
        this.drawOnMove = drawOnMove2;
    }

    public boolean moveCursor() {
        return true;
    }

    public boolean drawCursor() {
        return true;
    }

    public boolean selectable() {
        return true;
    }

    public boolean scalable() {
        return false;
    }

    public boolean symmetric() {
        return true;
    }

    public void update(DrawingGrid grid) {
    }

    public void onSelected() {
    }

    public void onColorChange(Color color, PixelCanvas canvas) {
        canvas.setColor(color);
    }

    public String toString() {
        return String.valueOf(name().substring(0, 1).toUpperCase()) + name().substring(1);
    }

    public void clicked(Color color, PixelCanvas canvas, int x, int y) {
    }
}
