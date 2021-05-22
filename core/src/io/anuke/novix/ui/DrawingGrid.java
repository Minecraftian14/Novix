package io.anuke.novix.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.VisUI;
import io.anuke.novix.Novix;
import io.anuke.novix.modules.Core;
import io.anuke.novix.scene.AlphaImage;
import io.anuke.novix.scene.GridImage;
import io.anuke.novix.tools.ActionStack;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.PixmapUtils;
import io.anuke.ucore.graphics.ShapeUtils;
import io.anuke.ucore.graphics.Textures;
import io.anuke.ucore.util.Mathf;
import io.anuke.utools.MiscUtils;
import io.anuke.novix.tools.DrawAction;
import io.anuke.novix.tools.PixelCanvas;
import io.anuke.novix.tools.Tool;

/* renamed from: io.anuke.novix.ui.DrawingGrid */
public class DrawingGrid extends Actor {
    public ActionStack actions = new ActionStack();
    private AlphaImage alphaimage;
    public float baseCursorSpeed = 1.03f;
    private Vector2[][] brushPolygons = new Vector2[10][];
    public int brushSize;
    public PixelCanvas canvas;
    private final boolean clip = true;
    /* access modifiers changed from: private */
    public Core core;
    /* access modifiers changed from: private */
    public float cursorx;
    /* access modifiers changed from: private */
    public float cursory;
    private GridImage gridimage;
    public boolean hSymmetry = false;
    public GridInput input = new GridInput();
    /* access modifiers changed from: private */
    public boolean moving;
    public float offsetx = 0.0f;
    public float offsety = 0.0f;
    /* access modifiers changed from: private */
    public GridPoint2 selected = new GridPoint2();
    private Color tempcolor = new Color();
    /* access modifiers changed from: private */
    public int touches = 0;
    /* access modifiers changed from: private */
    public int tpointer;
    public boolean vSymmetry = false;
    public float zoom = 1.0f;

    /* renamed from: io.anuke.novix.ui.DrawingGrid$GridInput */
    public class GridInput extends InputAdapter {
        public GridInput() {
        }

        public boolean touchDown(int x, int y, int pointer, int button) {
            if (!Core.i.tool().moveCursor() || !Core.i.colorMenuCollapsed() || !Core.i.toolMenuCollapsed() || checkRange(y)) {
                return false;
            }
            DrawingGrid drawingGrid = DrawingGrid.this;
            drawingGrid.touches = drawingGrid.touches + 1;
            if (!DrawingGrid.this.cursormode()) {
                DrawingGrid.this.cursorx = ((float) x) - DrawingGrid.this.getX();
                DrawingGrid.this.cursory = ((float) (Gdx.graphics.getHeight() - y)) - DrawingGrid.this.getY();
                DrawingGrid.this.updateCursorSelection();
                DrawingGrid.this.processToolTap(DrawingGrid.this.selected.x, DrawingGrid.this.selected.y);
                return true;
            } else if (DrawingGrid.this.moving) {
                DrawingGrid.this.processToolTap(DrawingGrid.this.selected.x, DrawingGrid.this.selected.y);
                return true;
            } else {
                DrawingGrid.this.tpointer = pointer;
                DrawingGrid.this.moving = true;
                return true;
            }
        }

        public boolean touchUp(int x, int y, int pointer, int button) {
            if (DrawingGrid.this.touches == 0) {
                return false;
            }
            if (DrawingGrid.this.cursormode()) {
                if (pointer == DrawingGrid.this.tpointer) {
                    DrawingGrid.this.moving = false;
                } else if (Core.i.tool().push) {
                    DrawingGrid.this.canvas.pushActions();
                }
            } else if (Core.i.tool().push) {
                DrawingGrid.this.canvas.pushActions();
            }
            DrawingGrid drawingGrid = DrawingGrid.this;
            drawingGrid.touches = drawingGrid.touches - 1;
            return true;
        }

        public boolean keyUp(int keycode) {
            if (keycode != 33) {
                return false;
            }
            if (Core.i.tool().push) {
                DrawingGrid.this.canvas.pushActions();
            }
            return true;
        }

        public boolean keyDown(int keycode) {
            if (keycode != 33) {
                return false;
            }
            DrawingGrid.this.processToolTap(DrawingGrid.this.selected.x, DrawingGrid.this.selected.y);
            return true;
        }

        public boolean touchDragged(int x, int y, int pointer) {
            float movex;
            float movey;
            if ((pointer != 0 && Gdx.app.getType() != Application.ApplicationType.Desktop) || DrawingGrid.this.touches == 0 || !Core.i.tool().moveCursor()) {
                return false;
            }
            float cursorSpeed = DrawingGrid.this.baseCursorSpeed * DrawingGrid.this.core.prefs.getFloat("cursorspeed", 1.0f);
            float deltax = ((float) Gdx.input.getDeltaX(pointer)) * cursorSpeed;
            float deltay = ((float) (-Gdx.input.getDeltaY(pointer))) * cursorSpeed;
            float movex2 = deltax;
            float movey2 = deltay;
            int move = Math.round(Math.max(Math.abs(movex2), Math.abs(movey2)));
            if (Math.abs(movex2) > Math.abs(movey2)) {
                movey = movey2 / Math.abs(movex2);
                movex = movex2 / Math.abs(movex2);
            } else {
                movex = movex2 / Math.abs(movey2);
                movey = movey2 / Math.abs(movey2);
            }
            float currentx = 0.0f;
            float currenty = 0.0f;
            for (int i = 0; i < move; i++) {
                currentx += movex;
                currenty += movey;
                int newx = (int) ((DrawingGrid.this.cursorx + currentx) / (DrawingGrid.this.canvasScale() * DrawingGrid.this.zoom));
                int newy = (int) ((DrawingGrid.this.cursory + currenty) / (DrawingGrid.this.canvasScale() * DrawingGrid.this.zoom));
                if (DrawingGrid.this.cursormode()) {
                    if (!(DrawingGrid.this.selected.x == newx && DrawingGrid.this.selected.y == newy) && ((DrawingGrid.this.touches > 1 || Gdx.input.isKeyPressed(33)) && Core.i.tool().drawOnMove)) {
                        DrawingGrid.this.processToolTap(newx, newy);
                    }
                } else if (!(DrawingGrid.this.selected.x == newx && DrawingGrid.this.selected.y == newy) && Core.i.tool().drawOnMove) {
                    DrawingGrid.this.processToolTap(newx, newy);
                }
                DrawingGrid.this.selected.set(newx, newy);
            }
            if (!DrawingGrid.this.cursormode()) {
                DrawingGrid.this.cursorx = ((float) x) - DrawingGrid.this.getX();
                DrawingGrid.this.cursory = ((float) (Gdx.graphics.getHeight() - y)) - DrawingGrid.this.getY();
            } else if (pointer != DrawingGrid.this.tpointer || !Core.i.tool().moveCursor()) {
                return false;
            } else {
                DrawingGrid drawingGrid = DrawingGrid.this;
                drawingGrid.cursorx = drawingGrid.cursorx + deltax;
                DrawingGrid drawingGrid2 = DrawingGrid.this;
                drawingGrid2.cursory = drawingGrid2.cursory + deltay;
                DrawingGrid.this.cursorx = Mathf.clamp(DrawingGrid.this.cursorx, 0.0f, DrawingGrid.this.getWidth() - 1.0f);
                DrawingGrid.this.cursory = Mathf.clamp(DrawingGrid.this.cursory, 0.0f, DrawingGrid.this.getHeight() - 1.0f);
            }
            return true;
        }

        public boolean checkRange(int y) {
            return ((float) y) <= ((float) (Gdx.graphics.getHeight() / 2)) - (DrawingGrid.this.min() / 2.0f) || ((float) y) >= ((float) (Gdx.graphics.getHeight() / 2)) + (DrawingGrid.this.min() / 2.0f);
        }
    }

    public DrawingGrid(Core core2) {
        this.core = core2;
        this.gridimage = new GridImage(1, 1);
        this.alphaimage = new AlphaImage(1.0f, 1.0f);
        generatePolygons();
    }

    /* access modifiers changed from: private */
    public void processToolTap(int x, int y) {
        Core.i.tool().clicked(Core.i.selectedColor(), this.canvas, x, y);
        if (Core.i.tool().symmetric()) {
            if (this.vSymmetry) {
                Core.i.tool().clicked(Core.i.selectedColor(), this.canvas, (this.canvas.width() - 1) - x, y);
            }
            if (this.hSymmetry) {
                Core.i.tool().clicked(Core.i.selectedColor(), this.canvas, x, (this.canvas.height() - 1) - y);
                if (this.vSymmetry) {
                    Core.i.tool().clicked(Core.i.selectedColor(), this.canvas, (this.canvas.width() - 1) - x, (this.canvas.height() - 1) - y);
                }
            }
        }
    }

    private void generatePolygons() {
        for (int i = 1; i < 11; i++) {
            final int index = i;
            this.brushPolygons[i - 1] = MiscUtils.getOutline(new MiscUtils.GridChecker() {
                public int getWidth() {
                    return index * 2;
                }

                public int getHeight() {
                    return index * 2;
                }

                public boolean exists(int x, int y) {
                    return Vector2.dst((float) (x - index), (float) (y - index), 0.0f, 0.0f) < ((float) index) - 0.5f;
                }
            });
        }
    }

    public void moveOffset(float x, float y) {
        this.offsetx += x;
        this.offsety += y;
    }

    public void moveCursor(float x, float y) {
        this.cursorx += x;
        this.cursory += y;
    }

    public void setZoom(float newzoom) {
        float max = (float) (Math.max(this.canvas.width(), this.canvas.height()) / 5);
        if (newzoom < maxZoom()) {
            newzoom = maxZoom();
        }
        if (newzoom > max) {
            newzoom = max;
        }
        this.cursorx *= newzoom / this.zoom;
        this.cursory *= newzoom / this.zoom;
        this.zoom = newzoom;
        updateSize();
        updateBounds();
    }

    public void setCursor(float x, float y) {
        this.cursorx = x;
        this.cursory = y;
        this.cursorx = Mathf.clamp(this.cursorx, 0.0f, getWidth() - 1.0f);
        this.cursory = Mathf.clamp(this.cursory, 0.0f, getHeight() - 1.0f);
        this.selected.set((int) (this.cursorx / (canvasScale() * this.zoom)), (int) (this.cursory / (canvasScale() * this.zoom)));
    }

    public void clearActionStack() {
        this.actions.dispose();
        this.actions = new ActionStack();
    }

    public void setCanvas(PixelCanvas canvas2, boolean saveOp) {
        Novix.log("Drawgrid: setting new canvas.");
        if (this.canvas != null) {
            Novix.log("this.Pixmap disposed at start?" + PixmapUtils.isDisposed(this.canvas.pixmap));
            if (saveOp) {
                Novix.log("Drawgrid: performing switch operation: " + this.canvas.name);
                DrawAction action = new DrawAction();
                action.fromCanvas = this.canvas;
                action.toCanvas = canvas2;
                this.actions.add(action);
            } else if (!Core.i.loadingProject()) {
                Novix.log("Drawgrid: disposing old canvas: " + this.canvas.name);
                this.canvas.dispose();
            } else {
                Novix.log("Drawgrid: NOT disposing old canvas \"" + this.canvas.name + "\" due to core still loading it.");
                Novix.log("TODO dispose it later, callbacks?");
            }
        }
        resetCanvas(canvas2);
        Novix.log("Pixmap disposed at end? " + PixmapUtils.isDisposed(canvas2.pixmap));
    }

    public void actionSetCanvas(PixelCanvas canvas2) {
        Novix.log("Drawgrid: undoing operation.");
        resetCanvas(canvas2);
    }

    private void resetCanvas(PixelCanvas canvas2) {
        this.canvas = canvas2;
        Novix.log("Drawgrid: new canvas \"" + canvas2.name + "\" set.");
        updateSize();
        updateBounds();
        this.zoom = maxZoom();
        this.cursorx = getWidth() / 2.0f;
        this.cursory = getHeight() / 2.0f;
        this.selected.set((int) (this.cursorx / canvasScale()), (int) (this.cursory / canvasScale()));
        this.offsetx = getWidth() / 2.0f;
        this.offsety = getHeight() / 2.0f;
        this.gridimage.setImageSize(canvas2.width(), canvas2.height());
        this.alphaimage.setImageSize(canvas2.width(), canvas2.height());
        Core.i.projectmanager.saveProject();
    }

    public void updateCursorSelection() {
        this.selected.set((int) (this.cursorx / (canvasScale() * this.zoom)), (int) (this.cursory / (canvasScale() * this.zoom)));
    }

    public void draw(Batch batch, float parentAlpha) {
        this.canvas.update();
        setPosition((float) (Gdx.graphics.getWidth() / 2), (float) (Gdx.graphics.getHeight() / 2), 1);
        setZIndex(0);
        updateSize();
        updateBounds();
        updateCursor();
        batch.flush();
        clipBegin(((float) (Gdx.graphics.getWidth() / 2)) - (min() / 2.0f), ((float) (Gdx.graphics.getHeight() / 2)) - (min() / 2.0f), min(), min());
        float cscl = canvasScale() * this.zoom;
        batch.setColor(Color.WHITE);
        int u = ((int) (((getWidth() / UCore.s) / ((float) 20)) / ((float) this.canvas.width()))) * this.canvas.width();
        if (u == 0) {
            int u2 = (int) (getWidth() / ((float) 20));
            if (u2 == 0) {
                u2 = 1;
            }
            if (u2 > this.canvas.width()) {
                u = this.canvas.width();
            } else {
                u = this.canvas.width() / (this.canvas.width() / u2);
            }
        }
        batch.draw(Textures.get("alpha"), getX(), getY(), getWidth(), getHeight(), (float) u, ((float) u) / (((float) this.canvas.width()) / ((float) this.canvas.height())), 0.0f, 0.0f);
        this.alphaimage.setImageSize(this.canvas.width(), this.canvas.height());
        this.alphaimage.setBounds(getX(), getY(), getWidth(), getHeight());
        batch.draw(this.canvas.texture, getX(), getY(), getWidth(), getHeight());
        if (this.core.prefs.getBoolean("grid", true)) {
            this.gridimage.setBounds(getX(), getY(), getWidth(), getHeight());
            this.gridimage.draw(batch, parentAlpha);
        }
        if (this.vSymmetry) {
            batch.setColor(Color.CYAN);
            batch.draw((TextureRegion) VisUI.getSkin().getAtlas().findRegion("white"), (float) ((int) ((getX() + (getWidth() / 2.0f)) - 2.0f)), getY(), 4.0f, getHeight());
        }
        if (this.hSymmetry) {
            batch.setColor(Color.PURPLE);
            batch.draw((TextureRegion) VisUI.getSkin().getAtlas().findRegion("white"), getX(), (float) ((int) ((getY() + (getHeight() / 2.0f)) - 2.0f)), getWidth(), 4.0f);
        }
        int xt = (int) (4.0f * (10.0f / ((float) this.canvas.width())) * this.zoom);
        if ((cursormode() || (this.touches > 0 && Core.i.tool().moveCursor())) && Core.i.tool().drawCursor()) {
            this.tempcolor.set(this.canvas.getIntColor(this.selected.x, this.selected.y));
            if (this.tempcolor.r + this.tempcolor.g + this.tempcolor.b < 1.5f || this.tempcolor.a < 0.01f || (this.core.tool().scalable() && this.core.prefs.getInteger("brushsize") > 1)) {
                this.tempcolor.set(Color.CORAL);
            } else {
                this.tempcolor.set(((float) 32) / 255.0f, ((float) 33) / 255.0f, ((float) 54) / 255.0f, 1.0f);
            }
            this.tempcolor.a = 1.0f;
            batch.setColor(this.tempcolor);
            drawSelection(batch, this.selected.x, this.selected.y, cscl, (float) xt);
            if (this.vSymmetry) {
                drawSelection(batch, (this.canvas.width() - 1) - this.selected.x, this.selected.y, cscl, (float) xt);
            }
            if (this.hSymmetry) {
                drawSelection(batch, this.selected.x, (this.canvas.height() - 1) - this.selected.y, cscl, (float) xt);
            }
            if (this.vSymmetry && this.hSymmetry) {
                drawSelection(batch, (this.canvas.width() - 1) - this.selected.x, (this.canvas.height() - 1) - this.selected.y, cscl, (float) xt);
            }
        }
        batch.setColor(Color.GRAY);
        MiscUtils.drawBorder(batch, ((float) (Gdx.graphics.getWidth() / 2)) - (min() / 2.0f), ((float) (Gdx.graphics.getHeight() / 2)) - (min() / 2.0f), min(), min(), 2);
        batch.setColor(Color.CORAL);
        MiscUtils.drawBorder(batch, (float) ((int) getX()), (float) ((int) getY()), (float) ((int) getWidth()), (float) ((int) getHeight()), (int) (2.0f * UCore.s), aspectRatio() < 1.0f ? 1 : 0, aspectRatio() > 1.0f ? 1 : 0);
        if (cursormode() || ((this.touches > 0 && Core.i.tool().moveCursor()) || !Core.i.tool().moveCursor())) {
            batch.setColor(Color.PURPLE);
            float csize = 32.0f * this.core.prefs.getFloat("cursorsize", 1.0f) * UCore.s;
            batch.draw(Textures.get(Core.i.tool().cursor), (getX() + this.cursorx) - (csize / 2.0f), (getY() + this.cursory) - (csize / 2.0f), csize, csize);
            batch.setColor(Color.CORAL);
            if (!(Core.i.tool() == Tool.pencil || Core.i.tool() == Tool.zoom)) {
                batch.draw(VisUI.getSkin().getRegion("icon-" + Core.i.tool().name()), this.cursorx + getX(), this.cursory + getY(), csize, csize);
            }
        }
        batch.draw(Textures.get("alpha"), -999.0f, -999.0f, 30.0f, 30.0f);
        clipEnd();
        batch.flush();
        batch.setColor(Color.WHITE);
    }

    private void drawSelection(Batch batch, int x, int y, float cscl, float xt) {
        ShapeUtils.thickness = (float) ((int) (4.0f * UCore.s));
        ShapeUtils.polygon(batch, !Core.i.tool().scalable() ? this.brushPolygons[0] : this.brushPolygons[this.brushSize - 1], (float) ((int) (getX() + (((float) x) * cscl))), (float) ((int) (getY() + (((float) y) * cscl))), cscl);
    }

    public void updateCursor() {
        if (this.cursorx > ((float) Gdx.graphics.getWidth()) - getX()) {
            this.cursorx = ((float) Gdx.graphics.getWidth()) - getX();
        }
        if (this.cursory > ((float) ((Gdx.graphics.getHeight() / 2) + (Gdx.graphics.getWidth() / 2))) - getY()) {
            this.cursory = ((float) ((Gdx.graphics.getHeight() / 2) + (Gdx.graphics.getWidth() / 2))) - getY();
        }
        if (this.cursorx < (-getX())) {
            this.cursorx = -getX();
        }
        if (this.cursory < ((float) ((Gdx.graphics.getHeight() / 2) - (Gdx.graphics.getWidth() / 2))) - getY()) {
            this.cursory = ((float) ((Gdx.graphics.getHeight() / 2) - (Gdx.graphics.getWidth() / 2))) - getY();
        }
        this.cursorx = Mathf.clamp(this.cursorx, 0.0f, getWidth() - 1.0f);
        this.cursory = Mathf.clamp(this.cursory, 0.0f, getHeight() - 1.0f);
        updateCursorSelection();
    }

    public void updateBounds() {
        int toolheight = (Gdx.graphics.getHeight() - Gdx.graphics.getWidth()) / 2;
        int colorheight = toolheight;
        if (aspectRatio() >= 1.0f) {
            if (getX() + getWidth() < ((float) Gdx.graphics.getWidth())) {
                this.offsetx = (-(((float) (Gdx.graphics.getWidth() / 2)) - getWidth())) / this.zoom;
            }
            if (getX() > 0.0f) {
                this.offsetx = ((float) (Gdx.graphics.getWidth() / 2)) / this.zoom;
            }
            if (getHeight() > ((float) Gdx.graphics.getWidth())) {
                if (getY() + getHeight() < ((float) (Gdx.graphics.getHeight() - colorheight))) {
                    this.offsety = (-((((float) (Gdx.graphics.getHeight() / 2)) - getHeight()) - ((float) colorheight))) / this.zoom;
                }
                if (getY() > ((float) toolheight)) {
                    this.offsety = ((float) ((Gdx.graphics.getHeight() / 2) - toolheight)) / this.zoom;
                }
            } else {
                this.offsety = (getHeight() / 2.0f) / this.zoom;
            }
        }
        if (aspectRatio() <= 1.0f) {
            if (getY() + getHeight() < ((float) (Gdx.graphics.getHeight() - colorheight))) {
                this.offsety = (-((((float) (Gdx.graphics.getHeight() / 2)) - getHeight()) - ((float) colorheight))) / this.zoom;
            }
            if (getY() > ((float) toolheight)) {
                this.offsety = ((float) ((Gdx.graphics.getHeight() / 2) - toolheight)) / this.zoom;
            }
            if (getWidth() > ((float) Gdx.graphics.getWidth())) {
                if (getX() + getWidth() < ((float) Gdx.graphics.getWidth())) {
                    this.offsetx = (-(((float) (Gdx.graphics.getWidth() / 2)) - getWidth())) / this.zoom;
                }
                if (getX() > 0.0f) {
                    this.offsetx = ((float) (Gdx.graphics.getWidth() / 2)) / this.zoom;
                    return;
                }
                return;
            }
            this.offsetx = (getWidth() / 2.0f) / this.zoom;
        }
    }

    public void updateSize() {
        setWidth(min() * this.zoom);
        setHeight((min() / ((float) this.canvas.width())) * ((float) this.canvas.height()) * this.zoom);
        setX(((float) (Gdx.graphics.getWidth() / 2)) - (this.offsetx * this.zoom));
        setY(((float) (Gdx.graphics.getHeight() / 2)) - (this.offsety * this.zoom));
    }

    public float maxZoom() {
        return Math.min(getWidth() / getHeight(), 1.0f);
    }

    public float aspectRatio() {
        return getWidth() / getHeight();
    }

    public float canvasScale() {
        return min() / ((float) this.canvas.width());
    }

    public float min() {
        return (float) Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /* access modifiers changed from: package-private */
    public boolean cursormode() {
        return this.core.prefs.getBoolean("cursormode");
    }
}
