package io.anuke.novix.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTextField;
import io.anuke.novix.Novix;
import io.anuke.novix.managers.GestureManager;
import io.anuke.novix.tools.Tool;
import io.anuke.novix.ui.DrawingGrid;
import io.anuke.ucore.modules.Module;
import io.anuke.utools.SceneUtils;

/* renamed from: io.anuke.novix.modules.Input */
public class Input extends Module<Novix> implements InputProcessor {
    /* access modifiers changed from: private */
    public Input input;
    private Vector2 vector = new Vector2();

    public void update() {
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(51)) {
            ((Core) getModule(Core.class)).drawgrid.offsety += 10.0f;
        }
        if (Gdx.input.isKeyPressed(29)) {
            ((Core) getModule(Core.class)).drawgrid.offsetx -= 10.0f;
        }
        if (Gdx.input.isKeyPressed(47)) {
            ((Core) getModule(Core.class)).drawgrid.offsety -= 10.0f;
        }
        if (Gdx.input.isKeyPressed(32)) {
            ((Core) getModule(Core.class)).drawgrid.offsetx += 10.0f;
        }
        ((Core) getModule(Core.class)).drawgrid.updateSize();
        ((Core) getModule(Core.class)).drawgrid.updateBounds();
    }

    public void init() {
        this.input = this;
        Gdx.input.setCatchBackKey(true);
        GestureDetector gesture = new GestureDetector(20.0f, 0.5f, 2.0f, 0.15f, new GestureDetectorListener());
        InputMultiplexer plex = new InputMultiplexer();
        plex.addProcessor((InputProcessor) getModule(Tutorial.class));
        plex.addProcessor(new GestureDetector(20.0f, 0.5f, 2.0f, 0.15f, new GestureManager(Core.i)));
        plex.addProcessor(this);
        plex.addProcessor(((Core) getModule(Core.class)).stage);
        plex.addProcessor(Core.i.drawgrid.input);
        plex.addProcessor(gesture);
        Gdx.input.setInputProcessor(plex);
    }

    public boolean keyDown(int keycode) {
        VisDialog dialog;
        if (keycode != 4 || (dialog = Core.i.getCurrentDialog()) == null) {
            return false;
        }
        dialog.hide();
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        if (character == 10 && FocusManager.getFocusedWidget() != null && (FocusManager.getFocusedWidget() instanceof VisTextField)) {
            Gdx.input.setOnscreenKeyboardVisible(false);
        }
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (Core.i.stage.getScrollFocus() != null) {
            Actor parent = SceneUtils.getTopParent(Core.i.stage.getScrollFocus());
            if (parent instanceof VisDialog) {
                VisDialog dialog = (VisDialog) parent;
                if (!SceneUtils.mouseOnActor(dialog, this.vector)) {
                    dialog.hide();
                }
            }
        }
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(int amount) {
        ((Core) getModule(Core.class)).drawgrid.setZoom(((Core) getModule(Core.class)).drawgrid.zoom - (((Core) getModule(Core.class)).drawgrid.zoom * (((float) amount) / 10.0f)));
        return false;
    }

    /* renamed from: io.anuke.novix.modules.Input$GestureDetectorListener */
    class GestureDetectorListener implements GestureDetector.GestureListener {
        float initzoom = 1.0f;
        Vector2 lastinitialpinch = new Vector2();
        Vector2 lastpinch;

        /* renamed from: to */
        Vector2 f321to = new Vector2();
        Vector2 toward = new Vector2();

        GestureDetectorListener() {
        }

        public boolean touchDown(float x, float y, int pointer, int button) {
            this.initzoom = ((Core) Input.this.input.getModule(Core.class)).drawgrid.zoom;
            return false;
        }

        public boolean tap(float x, float y, int count, int button) {
            return false;
        }

        public boolean longPress(float x, float y) {
            return false;
        }

        public boolean fling(float velocityX, float velocityY, int button) {
            return false;
        }

        public boolean pan(float x, float y, float deltaX, float deltaY) {
            if (!Core.i.menuOpen() && ((Core) Input.this.input.getModule(Core.class)).tool() == Tool.zoom) {
                Input.this.drawgrid().offsetx -= deltaX / Input.this.drawgrid().zoom;
                Input.this.drawgrid().offsety += deltaY / Input.this.drawgrid().zoom;
                Input.this.drawgrid().updateSize();
                Input.this.drawgrid().updateBounds();
            }
            return false;
        }

        public boolean zoom(float initialDistance, float distance) {
            if (Core.i.tool() == Tool.zoom && !Core.i.menuOpen()) {
                float newzoom = this.initzoom * (distance / initialDistance);
                if (newzoom < Input.this.drawgrid().maxZoom()) {
                    newzoom = Input.this.drawgrid().maxZoom();
                }
                Input.this.drawgrid().setZoom(newzoom);
            }
            return false;
        }

        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            if (Core.i.tool() == Tool.zoom && !Core.i.menuOpen()) {
                Vector2 afirst = initialPointer1.cpy().add(initialPointer2).scl(0.5f);
                Vector2 alast = pointer1.cpy().add(pointer2).scl(0.5f);
                if (!afirst.epsilonEquals(this.lastinitialpinch, 0.1f)) {
                    this.lastinitialpinch = afirst;
                    this.lastpinch = afirst.cpy();
                    this.toward.set(Input.this.drawgrid().offsetx, Input.this.drawgrid().offsety);
                    this.f321to.x = ((afirst.x - ((float) (Gdx.graphics.getWidth() / 2))) / Input.this.drawgrid().zoom) + Input.this.drawgrid().offsetx;
                    this.f321to.y = (((((float) Gdx.graphics.getHeight()) - afirst.y) - ((float) (Gdx.graphics.getHeight() / 2))) / Input.this.drawgrid().zoom) + Input.this.drawgrid().offsety;
                }
                this.lastpinch.sub(alast);
                Input.this.drawgrid().moveOffset(this.lastpinch.x / Input.this.drawgrid().zoom, (-this.lastpinch.y) / Input.this.drawgrid().zoom);
                this.lastpinch = alast;
            }
            return false;
        }

        public boolean panStop(float x, float y, int pointer, int button) {
            this.initzoom = Core.i.drawgrid.zoom;
            return false;
        }

        public void pinchStop() {
        }
    }

    public DrawingGrid drawgrid() {
        return ((Core) this.input.getModule(Core.class)).drawgrid;
    }
}
