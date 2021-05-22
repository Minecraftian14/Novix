package io.anuke.ucore.modules;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import java.util.Iterator;

/* renamed from: io.anuke.ucore.modules.ModuleController */
public abstract class ModuleController<T extends ModuleController<T>> extends ApplicationAdapter {
    private static ModuleController<?> instance;
    protected Array<Module<T>> modulearray = new Array<>();
    protected ObjectMap<Class<? extends Module<T>>, Module<T>> modules = new ObjectMap<>();

    public abstract void init();

    public ModuleController() {
        instance = this;
    }

    public void addModule(Class<? extends Module<T>> c) {
        try {
            Module<T> m = ClassReflection.newInstance(c);
            m.main = this;
            this.modules.put(c, m);
            this.modulearray.add(m);
        } catch (Exception e2) {
            e2.printStackTrace();
            Gdx.app.exit();
        }
    }

    /*
    public <N extends Module<T>> void addModule(N t) {
        try {
            this.modules.put(t.getClass(), t);
            t.main = this;
            this.modulearray.add(t);
        } catch (RuntimeException e) {
            throw e;
        }
    }
    */

    public Module<T> getModule(Class<? extends Module<T>> c) {
        return this.modules.get(c);
    }

    /*
    public static <N> N module(Class<N> c) {
        return instance.getModule(c);
    }
    */

    public void update() {
    }

    public void resize(int width, int height) {
        Iterator<Module<T>> it = this.modulearray.iterator();
        while (it.hasNext()) {
            it.next().resize(width, height);
        }
    }

    public final void create() {
        init();
        Iterator<Module<T>> it = this.modulearray.iterator();
        while (it.hasNext()) {
            Module<T> module = it.next();
            module.preInit();
            module.init();
        }
    }

    public void render() {
        Iterator<Module<T>> it = this.modulearray.iterator();
        while (it.hasNext()) {
            it.next().update();
        }
        update();
    }

    public void pause() {
        Iterator<Module<T>> it = this.modulearray.iterator();
        while (it.hasNext()) {
            it.next().pause();
        }
    }

    public void resume() {
        Iterator<Module<T>> it = this.modulearray.iterator();
        while (it.hasNext()) {
            it.next().resume();
        }
    }

    public void dispose() {
        Iterator<Module<T>> it = this.modulearray.iterator();
        while (it.hasNext()) {
            it.next().dispose();
        }
    }
}
