package io.anuke.novix.tools;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.ui.widget.VisImageButton;
import java.util.Iterator;
import io.anuke.novix.modules.Core;
import io.anuke.ucore.graphics.PixmapUtils;

/* renamed from: io.anuke.novix.tools.ActionStack */
public class ActionStack implements Disposable {
    private int index = 0;
    private Array<DrawAction> stack = new Array<>();

    public ActionStack() {
        this.stack.add(new DrawAction());
        update();
    }

    public void clear() {
        this.stack.clear();
        this.index = 0;
        update();
    }

    public void add(DrawAction action) {
        this.stack.truncate(this.stack.size + this.index);
        this.index = 0;
        this.stack.add(action);
        update();
    }

    public boolean canUndo() {
        return (this.stack.size + -1) + this.index >= 1;
    }

    public boolean canRedo() {
        return this.index <= -1 && (this.stack.size + -1) + this.index >= 0;
    }

    public void undo(PixelCanvas canvas) {
        if (canUndo()) {
            this.stack.get((this.stack.size - 1) + this.index).apply(canvas, false);
            this.index--;
            update();
        }
    }

    public void redo(PixelCanvas canvas) {
        if (canRedo()) {
            this.index++;
            this.stack.get((this.stack.size - 1) + this.index).apply(canvas, true);
            update();
        }
    }

    private void update() {
        boolean z;
        boolean z2 = false;
        VisImageButton visImageButton = Tool.undo.button;
        if (canUndo()) {
            z = false;
        } else {
            z = true;
        }
        visImageButton.setDisabled(z);
        VisImageButton visImageButton2 = Tool.redo.button;
        if (!canRedo()) {
            z2 = true;
        }
        visImageButton2.setDisabled(z2);
    }

    /* access modifiers changed from: package-private */
    public void print() {
        System.out.println("\n\n\n\n\n\n\n\n\n");
        System.out.println("index: " + this.index);
        int i = 0;
        Iterator<DrawAction> it = this.stack.iterator();
        while (it.hasNext()) {
            DrawAction action = it.next();
            boolean sel = (this.stack.size + this.index) + -1 == i;
            System.out.println("<" + i + "> " + (sel ? "[" : "") + action.positions.size + "S" + (sel ? "]" : ""));
            i++;
        }
    }

    public void dispose() {
        Iterator<DrawAction> it = this.stack.iterator();
        while (it.hasNext()) {
            DrawAction action = it.next();
            if (!(action.toCanvas == null || action.toCanvas == Core.i.canvas())) {
                action.toCanvas.dispose();
            }
            if (!(action.fromCanvas == null || action.fromCanvas == Core.i.canvas() || PixmapUtils.isDisposed(action.fromCanvas.pixmap))) {
                action.fromCanvas.dispose();
            }
        }
    }
}
