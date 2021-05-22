package io.anuke.novix.scene;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import io.anuke.ucore.UCore;

/* renamed from: io.anuke.novix.scene.CollapseButton */
public class CollapseButton extends VisImageButton {
    Drawable down = VisUI.getSkin().getDrawable("icon-down");

    /* renamed from: up */
    Drawable f323up = VisUI.getSkin().getDrawable("icon-up");

    public CollapseButton() {
        super("default");
        setStyle(new VisImageButton.VisImageButtonStyle(getStyle()));
        getImageCell().size(48.0f * UCore.s);
        getStyle().up = VisUI.getSkin().getDrawable("button");
        set(this.f323up);
    }

    public void flip() {
        if (getStyle().imageUp == this.f323up) {
            set(this.down);
        } else {
            set(this.f323up);
        }
    }

    private void set(Drawable d) {
        getStyle().imageUp = d;
    }
}
