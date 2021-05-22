package io.anuke.novix;

import com.badlogic.gdx.Gdx;
import io.anuke.novix.modules.Core;
import io.anuke.novix.modules.Input;
import io.anuke.novix.modules.Tutorial;
import io.anuke.ucore.modules.ModuleController;

/* renamed from: io.anuke.novix.Novix */
public class Novix extends ModuleController<Novix> {
    public void init() {
        addModule(Input.class);
        addModule(Core.class);
        addModule(Tutorial.class);
    }

    public static void log(Object o) {
        StackTraceElement e = Thread.currentThread().getStackTrace()[2];
        Gdx.app.log("[" + e.getFileName().replace(".java", "") + "::" + e.getMethodName() + "]", new StringBuilder().append(o).toString());
    }
}
