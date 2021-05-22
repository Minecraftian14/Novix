package io.anuke.novix.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import io.anuke.novix.Novix;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.ShapeUtils;
import io.anuke.ucore.modules.Module;
import io.anuke.novix.ui.DialogClasses;
import io.anuke.novix.tools.TutorialStage;

/* renamed from: io.anuke.novix.modules.Tutorial */
public class Tutorial extends Module<Novix> {
    /* access modifiers changed from: private */
    public boolean active = false;
    private TutorialStage laststage = null;
    private float shadespeed = 0.05f;
    private TutorialStage stage = TutorialStage.values()[0];

    public Tutorial() {
        this.stage.trans = 0.0f;
    }

    public void update() {
        ShapeUtils.thickness = 4.0f;
        if (this.active) {
            for (Rectangle rect : TutorialStage.cliprects) {
                rect.set(0.0f, 0.0f, 0.0f, 0.0f);
            }
            Core.instance.stage.getBatch().begin();
            if (this.stage.trans < 1.0f) {
                if (this.laststage != null) {
                    this.laststage.trans -= this.shadespeed;
                    if (this.laststage.trans < 0.0f) {
                        this.laststage.trans = 0.0f;
                    }
                    this.laststage.draw(Core.instance.stage.getBatch());
                }
                this.stage.trans += this.shadespeed;
                if (this.stage.trans > 1.0f) {
                    this.stage.trans = 1.0f;
                }
                Gdx.graphics.requestRendering();
            }
            this.stage.draw(Core.instance.stage.getBatch());
            if (this.stage.next) {
                this.stage.end();
                this.laststage = this.stage;
                this.stage = TutorialStage.values()[this.stage.ordinal() + 1];
                this.stage.trans = 0.0f;
            }
            Core.instance.stage.getBatch().end();
        } else if (this.stage.trans > 0.0f) {
            Gdx.graphics.requestRendering();
            Core.instance.stage.getBatch().begin();
            this.stage.draw(Core.instance.stage.getBatch());
            Core.instance.stage.getBatch().end();
            this.stage.trans -= this.shadespeed;
        }
    }

    public void end() {
        this.active = false;
    }

    private void reset() {
        for (TutorialStage stage2 : TutorialStage.values()) {
            stage2.next = false;
            stage2.trans = 1.0f;
        }
        this.stage = TutorialStage.values()[0];
        this.laststage = null;
        this.active = false;
    }

    public void begin() {
        reset();
        this.active = true;
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        if (this.active) {
            this.stage.tap(x, Gdx.graphics.getHeight() - y);
            if (((float) x) > ((float) Gdx.graphics.getWidth()) - (50.0f * UCore.s) && ((float) (Gdx.graphics.getHeight() - y)) < 30.0f * UCore.s) {
                this.active = false;
                new DialogClasses.ConfirmDialog("Confirm", "Are you sure you want to\nexit the tutorial?") {
                    boolean confirming;

                    public void result() {
                        this.confirming = true;
                        close();
                    }

                    public void cancel() {
                        close();
                    }

                    public void close() {
                        super.close();
                        if (this.confirming) {
                            Tutorial.this.end();
                            if (Core.instance.projectmenu.getStage() != null) {
                                if (!Core.instance.colorMenuCollapsed()) {
                                    Core.instance.collapseColorMenu();
                                }
                                if (!Core.instance.toolMenuCollapsed()) {
                                    Core.instance.collapseToolMenu();
                                }
                                Core.instance.projectmenu.hide();
                                return;
                            }
                            return;
                        }
                        Tutorial.this.active = true;
                    }

                    public void hide() {
                    }
                }.show(Core.instance.stage);
            }
        }
        return inRect(x, y);
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return inRect(screenX, screenY);
    }

    public boolean inRect(int screenX, int screenY) {
        if (!this.active) {
            return false;
        }
        for (Rectangle rect : TutorialStage.cliprects) {
            if (rect.contains((float) screenX, (float) (Gdx.graphics.getHeight() - screenY))) {
                return true;
            }
        }
        return false;
    }
}
