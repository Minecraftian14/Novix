package io.anuke.novix.tools;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.ObjectMap;
import io.anuke.novix.modules.Core;
import io.anuke.novix.ui.DrawingGrid;
import io.anuke.utools.MiscUtils;

/* renamed from: io.anuke.novix.tools.DrawAction */
public class DrawAction {
    public PixelCanvas fromCanvas;
    public ObjectMap<Integer, ColorPair> positions = new ObjectMap<>();
    public PixelCanvas toCanvas;

    public void push(int x, int y, int from, int to) {
        if (from != to) {
            int key = MiscUtils.asInt(x, y, Core.i.drawgrid.canvas.width());
            if (this.positions.containsKey(Integer.valueOf(key))) {
                this.positions.get(Integer.valueOf(key)).tocolor = to;
            } else {
                this.positions.put(Integer.valueOf(key), new ColorPair(from, to));
            }
        }
    }

    public void clear() {
        this.positions.clear();
    }

    public void apply(PixelCanvas canvas, boolean reapply) {
        PixelCanvas pixelCanvas;
        if (this.fromCanvas != null) {
            DrawingGrid drawingGrid = Core.i.drawgrid;
            if (reapply) {
                pixelCanvas = this.toCanvas;
            } else {
                pixelCanvas = this.fromCanvas;
            }
            drawingGrid.actionSetCanvas(pixelCanvas);
            return;
        }
        ObjectMap.Keys<Integer> it = this.positions.keys().iterator();
        while (it.hasNext()) {
            Integer i = (Integer) it.next();
            ColorPair pos = this.positions.get(i);
            int x = i.intValue() % Core.i.drawgrid.canvas.width();
            int y = i.intValue() / Core.i.drawgrid.canvas.width();
            canvas.pixmap.setBlending(Pixmap.Blending.None);
            canvas.drawPixelActionless(x, y, reapply ? pos.tocolor : pos.fromcolor);
            canvas.pixmap.setBlending(Pixmap.Blending.SourceOver);
        }
        canvas.updatePixmapColor();
        canvas.updateTexture();
    }

    /* renamed from: io.anuke.novix.tools.DrawAction$ColorPair */
    class ColorPair {
        int fromcolor;
        int tocolor;

        public ColorPair(int fromcolor2, int tocolor2) {
            this.fromcolor = fromcolor2;
            this.tocolor = tocolor2;
        }
    }

    public String toString() {
        return "DrawAction: " + this.positions.size + "x";
    }
}
