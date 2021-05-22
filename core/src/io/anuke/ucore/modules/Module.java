package io.anuke.ucore.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;

/* renamed from: io.anuke.ucore.modules.Module */
public abstract class Module<T extends ModuleController<T>> extends InputAdapter {
    public ModuleController<T> main;

    public void update() {
    }

    public void init() {
    }

    public void preInit() {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
    }

    public void resize(int width, int height) {
    }

    public Module<T> getModule(Class<? extends Module<T>> c) {
        return this.main.getModule(c);
    }

    public void clearScreen() {
        clearScreen(Color.BLACK);
    }

    public void clearScreen(Color color) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, 1.0f);
        Gdx.gl.glClear(16640);
    }

    public void clearScreen(float r, float g, float b) {
        Gdx.gl.glClearColor(r, g, b, 1.0f);
        Gdx.gl.glClear(16640);
    }

    public void log(Object message) {
        System.out.println(message);
    }

    public int gwidth() {
        return Gdx.graphics.getWidth();
    }

    public int gheight() {
        return Gdx.graphics.getHeight();
    }

    public float delta() {
        return Gdx.graphics.getDeltaTime() * 60.0f;
    }
}
