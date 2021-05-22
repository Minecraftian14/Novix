package io.anuke.novix.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.GdxRuntimeException;

/* renamed from: io.anuke.novix.scene.SmoothCollapsibleWidget */
public class SmoothCollapsibleWidget extends WidgetGroup {
    /* access modifiers changed from: private */
    public boolean actionRunning;
    private CollapseAction collapseAction;
    /* access modifiers changed from: private */
    public boolean collapsed;
    /* access modifiers changed from: private */
    public float currentHeight;
    private float firsty;
    /* access modifiers changed from: private */
    public Table table;
    /* access modifiers changed from: private */

    /* renamed from: up */
    public boolean f324up;

    public SmoothCollapsibleWidget(Table table2) {
        this(table2, true);
    }

    public SmoothCollapsibleWidget(Table table2, boolean up) {
        this.collapseAction = new CollapseAction(this, (CollapseAction) null);
        this.firsty = 9998.0f;
        this.table = table2;
        this.f324up = up;
        if (up) {
            this.currentHeight = table2.getPrefHeight();
        }
        updateTouchable();
        addActor(table2);
    }

    public Table getTable() {
        return this.table;
    }

    public float getDone() {
        return this.currentHeight / this.table.getPrefHeight();
    }

    public void setCollapsed(boolean collapse, boolean withAnimation) {
        this.collapsed = collapse;
        updateTouchable();
        if (this.table != null) {
            this.actionRunning = true;
            if (withAnimation) {
                addAction(this.collapseAction);
                return;
            }
            if (collapse) {
                this.currentHeight = 0.0f;
                this.collapsed = true;
            } else {
                this.currentHeight = this.table.getPrefHeight();
                this.collapsed = false;
            }
            this.actionRunning = false;
            invalidateHierarchy();
        }
    }

    public void setCollapsed(boolean collapse) {
        setCollapsed(collapse, true);
    }

    public boolean isCollapsed() {
        return this.collapsed;
    }

    private void updateTouchable() {
        if (this.collapsed) {
            setTouchable(Touchable.disabled);
        } else {
            setTouchable(Touchable.enabled);
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        int i = -1;
        float y = getY();
        float f = (((float) (this.f324up ? -1 : 1)) * this.currentHeight) + y;
        if (!this.f324up) {
            i = 1;
        }
        setY(f - (((float) i) * this.table.getPrefHeight()));
        if (this.currentHeight > 1.0f) {
            batch.flush();
            super.draw(batch, parentAlpha);
            batch.flush();
        }
        setY(y);
    }

    public void layout() {
        if (this.table != null) {
            if (this.firsty >= 9997.0f) {
                this.firsty = getY();
            }
            this.table.setBounds(0.0f, 0.0f, this.table.getPrefWidth(), this.table.getPrefHeight());
            if (this.actionRunning) {
                return;
            }
            if (this.collapsed) {
                this.currentHeight = 0.0f;
            } else {
                this.currentHeight = this.table.getPrefHeight();
            }
        }
    }

    public float getPrefWidth() {
        if (this.table == null) {
            return 0.0f;
        }
        return this.table.getPrefWidth();
    }

    public float getPrefHeight() {
        if (this.table == null) {
            return 0.0f;
        }
        if (this.actionRunning) {
            return this.currentHeight;
        }
        if (!this.collapsed) {
            return this.currentHeight;
        }
        return 0.0f;
    }

    public void setTable(Table table2) {
        this.table = table2;
        clearChildren();
        addActor(table2);
    }

    public void resetY() {
        this.firsty = getY();
    }

    /* access modifiers changed from: protected */
    public void childrenChanged() {
        super.childrenChanged();
        if (getChildren().size > 1) {
            throw new GdxRuntimeException("Only one actor can be added to CollapsibleWidget");
        }
    }

    /* renamed from: io.anuke.novix.scene.SmoothCollapsibleWidget$CollapseAction */
    private class CollapseAction extends Action {
        private CollapseAction() {
        }

        /* synthetic */ CollapseAction(SmoothCollapsibleWidget smoothCollapsibleWidget, CollapseAction collapseAction) {
            this();
        }

        public boolean act(float delta) {
            if (SmoothCollapsibleWidget.this.collapsed) {
                SmoothCollapsibleWidget smoothCollapsibleWidget = SmoothCollapsibleWidget.this;
                Interpolation.BounceIn bounceIn = Interpolation.bounceIn;
                float access$1 = SmoothCollapsibleWidget.this.currentHeight;
                if (SmoothCollapsibleWidget.this.f324up) {
                }
                smoothCollapsibleWidget.currentHeight = bounceIn.apply(access$1, 0.0f, 0.2f);
                SmoothCollapsibleWidget smoothCollapsibleWidget2 = SmoothCollapsibleWidget.this;
                smoothCollapsibleWidget2.currentHeight = smoothCollapsibleWidget2.currentHeight - (delta * 400.0f);
                if (SmoothCollapsibleWidget.this.currentHeight <= 1.0f) {
                    SmoothCollapsibleWidget.this.currentHeight = 0.0f;
                    SmoothCollapsibleWidget.this.collapsed = true;
                    SmoothCollapsibleWidget.this.actionRunning = false;
                }
            } else {
                SmoothCollapsibleWidget.this.currentHeight = Interpolation.bounceIn.apply(SmoothCollapsibleWidget.this.currentHeight, SmoothCollapsibleWidget.this.table.getPrefHeight(), 0.4f);
                SmoothCollapsibleWidget smoothCollapsibleWidget3 = SmoothCollapsibleWidget.this;
                smoothCollapsibleWidget3.currentHeight = smoothCollapsibleWidget3.currentHeight + (delta * 400.0f);
                if (SmoothCollapsibleWidget.this.currentHeight > SmoothCollapsibleWidget.this.table.getPrefHeight() - 1.0f) {
                    SmoothCollapsibleWidget.this.currentHeight = SmoothCollapsibleWidget.this.table.getPrefHeight();
                    SmoothCollapsibleWidget.this.collapsed = false;
                    SmoothCollapsibleWidget.this.actionRunning = false;
                }
            }
            SmoothCollapsibleWidget.this.invalidateHierarchy();
            if (SmoothCollapsibleWidget.this.actionRunning) {
                return false;
            }
            return true;
        }
    }
}
