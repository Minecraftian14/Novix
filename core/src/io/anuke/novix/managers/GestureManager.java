package io.anuke.novix.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.anuke.novix.Novix;
import io.anuke.ucore.UCore;
import io.anuke.novix.modules.Core;
import io.anuke.novix.tools.Tool;

/* renamed from: io.anuke.novix.managers.GestureManager */
public class GestureManager implements GestureDetector.GestureListener {
    private final float flingvelocity = 1300.0f;
    private Core main;
    private final float swipevelocity = 1300.0f;
    private float touchy;
    private Vector2 vector = new Vector2();

    public GestureManager(Core main2) {
        this.main = main2;
    }

    public boolean touchDown(float x, float y, int pointer, int button) {
        this.touchy = ((float) Gdx.graphics.getHeight()) - y;
        return false;
    }

    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    public boolean longPress(float x, float y) {
        return false;
    }

    public boolean fling(float velocityX, float velocityY, int button) {
        if (Core.i.prefs.getBoolean("gestures", true) && Core.i.getCurrentDialog() == null && (Core.i.drawgrid.input.checkRange((int) this.touchy) || Core.i.toolmenu.getTool() != Tool.zoom || !Core.i.colorMenuCollapsed() || !Core.i.toolMenuCollapsed())) {
            float swipevelocity2 = 1300.0f * UCore.s;
            float flingvelocity2 = 1300.0f * UCore.s;
            float tooltop = this.main.toolmenu.localToStageCoordinates(this.vector.set(0.0f, 0.0f)).y + this.main.toolmenu.getHeight();
            float colortop = this.main.colormenu.localToStageCoordinates(this.vector.set(0.0f, 0.0f)).y;
            if (Math.abs(velocityX) > swipevelocity2 && Math.abs(velocityY) < swipevelocity2 && MathUtils.isEqual(this.touchy, (float) (Gdx.graphics.getHeight() / 2), 250.0f * UCore.s)) {
                if (!this.main.colorMenuCollapsed() && velocityX < (-swipevelocity2)) {
                    this.main.collapseColorMenu();
                }
                if (this.main.colorMenuCollapsed() && velocityX > swipevelocity2) {
                    if (!this.main.toolMenuCollapsed()) {
                        this.main.collapseToolMenu();
                    }
                    this.main.collapseColorMenu();
                }
            } else if (!this.main.colorMenuCollapsed() && this.touchy < (320.0f * UCore.s) + colortop && velocityY < (-flingvelocity2)) {
                this.main.collapseColorMenu();
            } else if (this.main.toolMenuCollapsed() && this.touchy < ((float) (Gdx.graphics.getHeight() / 3)) && velocityY < (-flingvelocity2)) {
                this.main.collapseToolMenu();
            }
            if (!this.main.toolMenuCollapsed() && this.touchy > tooltop - (60.0f * UCore.s) && velocityY > flingvelocity2) {
                this.main.collapseToolMenu();
            } else if (this.main.toolMenuCollapsed() && this.main.colorMenuCollapsed() && this.touchy > (((float) Gdx.graphics.getWidth()) / 3.0f) * 2.0f && velocityY > flingvelocity2) {
                Novix.log("collapsing other menu");
                this.main.collapseColorMenu();
            }
        }
        return false;
    }

    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    public void pinchStop() {
    }
}
