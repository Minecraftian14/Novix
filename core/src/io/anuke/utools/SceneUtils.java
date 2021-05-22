package io.anuke.utools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Pools;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTextField;

/* renamed from: io.anuke.utools.SceneUtils */
public class SceneUtils {
    public static void moveTextToSide(VisTextField field) {
        GlyphLayout layout = (GlyphLayout) Pools.obtain(GlyphLayout.class);
        layout.setText(VisUI.getSkin().getFont("default-font"), field.getText());
        if (layout.width < field.getWidth()) {
            field.setCursorPosition(0);
        } else {
            field.setCursorPosition(field.getText().length());
        }
    }

    public static Actor getTopParent(Actor actor) {
        Actor parent = actor.getParent();
        if (parent.getParent() == null) {
            return actor;
        }
        while (parent != null && ((!(parent.getParent() instanceof Group) || parent.getParent().getParent() != null) && parent.getParent() != null)) {
            parent = parent.getParent();
        }
        return parent;
    }

    public static boolean mouseOnActor(Actor actor, Vector2 temp) {
        float x = (float) Gdx.input.getX();
        float y = (float) (Gdx.graphics.getHeight() - Gdx.input.getY());
        actor.localToStageCoordinates(temp.set(0.0f, 0.0f));
        return x >= temp.x && y >= temp.y && x <= temp.x + actor.getWidth() && y <= temp.y + actor.getHeight();
    }

    public static Cell<?> fitCell(Cell<?> cell, float size, float ratio) {
        float width = size;
        float height = size / ratio;
        if (height > width) {
            height = size;
            width = size * ratio;
        }
        float sidePad = (size - width) / 2.0f;
        float topPad = (size - height) / 2.0f;
        return cell.size(width, height).padTop(3.0f + topPad).padBottom(topPad).padLeft(sidePad + 2.0f).padRight(sidePad + 2.0f);
    }

    public static void addIconToButton(Button button, Image image, float size) {
        button.add(image).size(size).center();
        button.getCells().reverse();
    }

    public static void addHideButton(final VisDialog dialog) {
        Label titleLabel = dialog.getTitleLabel();
        Table titleTable = dialog.getTitleTable();
        VisImageButton closeButton = new VisImageButton("close-window");
        titleTable.add(closeButton).padRight((-dialog.getPadRight()) + 0.7f);
        closeButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                dialog.hide();
            }
        });
        closeButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.cancel();
                return true;
            }
        });
        if (titleLabel.getLabelAlign() == 1 && titleTable.getChildren().size == 2) {
            titleTable.getCell(titleLabel).padLeft(closeButton.getWidth() * 2.0f);
        }
    }

    /* renamed from: io.anuke.utools.SceneUtils$TextFieldEmptyListener */
    public static class TextFieldEmptyListener extends ChangeListener {
        final Button button;
        final VisTextField[] fields;

        public TextFieldEmptyListener(Button button2, VisTextField... fields2) {
            this.button = button2;
            this.fields = fields2;
            for (VisTextField field : fields2) {
                field.addListener(this);
            }
            changed((ChangeListener.ChangeEvent) null, (Actor) null);
        }

        public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            boolean disabled = false;
            for (VisTextField field : this.fields) {
                disabled = disabled || field.isEmpty();
            }
            this.button.setDisabled(disabled);
        }
    }
}
