package io.anuke.novix.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/* renamed from: io.anuke.novix.scene.AnimatedImage */
public class AnimatedImage extends Actor {
    Drawable[] drawables;
    final float frametime = 10.0f;
    int index;
    float time;

    public AnimatedImage(Drawable... drawables2) {
        this.drawables = drawables2;
    }

    public void draw(Batch batch, float alpha) {
        batch.setColor(1.0f, 1.0f, 1.0f, alpha);
        this.drawables[this.index].draw(batch, getX(), getY(), getWidth(), getHeight());
    }

    public void act(float delta) {
        this.time += 60.0f * delta;
        if (this.time > 10.0f) {
            this.time = 0.0f;
            this.index++;
            if (this.index >= this.drawables.length) {
                this.index = 0;
            }
        }
    }
}
