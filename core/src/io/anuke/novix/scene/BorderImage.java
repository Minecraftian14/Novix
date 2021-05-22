package io.anuke.novix.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.anuke.ucore.UCore;
import io.anuke.utools.MiscUtils;

/* renamed from: io.anuke.novix.scene.BorderImage */
public class BorderImage extends Actor {
    public BorderImage() {
        setColor(Color.CORAL);
    }

    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor());
        MiscUtils.setBatchAlpha(batch, alpha);
        MiscUtils.drawBorder(batch, getX(), getY(), getWidth(), getHeight(), (int) (UCore.s * 2.0f), (int) (UCore.s * 2.0f));
    }
}
