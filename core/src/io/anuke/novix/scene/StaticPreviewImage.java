package io.anuke.novix.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

/* renamed from: io.anuke.novix.scene.StaticPreviewImage */
public class StaticPreviewImage extends Group {
    private Stack stack;
    private Texture texture;

    public StaticPreviewImage(Texture texture2) {
        float scalex;
        float scaley;
        this.texture = texture2;
        Image image = new Image(texture2);
        BorderImage border = new BorderImage();
        border.setColor(Color.CORAL);
        float ratio = 1.0f / (((float) texture2.getWidth()) / ((float) texture2.getHeight()));
        if (texture2.getWidth() < texture2.getHeight()) {
            scalex = ((float) 16) / ratio;
            scaley = (float) 16;
        } else {
            scalex = (float) 16;
            scaley = ((float) 16) * ratio;
        }
        AlphaImage alpha = new AlphaImage(scalex, scaley);
        this.stack = new Stack();
        this.stack.add(alpha);
        this.stack.add(image);
        this.stack.add(border);
        addActor(this.stack);
    }

    public void act(float delta) {
        super.act(delta);
        float xscl = getWidth() / ((float) this.texture.getWidth());
        float yscl = getHeight() / ((float) this.texture.getHeight());
        if (MathUtils.isPowerOfTwo(this.texture.getWidth()) && MathUtils.isPowerOfTwo(this.texture.getHeight())) {
            setSize(((float) this.texture.getWidth()) * xscl, ((float) this.texture.getHeight()) * yscl);
        }
        this.stack.setBounds(0.0f, 0.0f, getWidth(), getHeight());
    }
}
