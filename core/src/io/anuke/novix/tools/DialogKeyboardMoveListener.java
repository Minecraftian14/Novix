package io.anuke.novix.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.Focusable;
import com.kotcrab.vis.ui.widget.VisTextField;
import java.util.HashMap;
import java.util.Iterator;

//import io.anuke.novix.android.AndroidKeyboard;
//import io.anuke.novix.android.TextFieldDialogListener;
import io.anuke.utools.SceneUtils;

/* renamed from: io.anuke.novix.tools.DialogKeyboardMoveListener */
public class DialogKeyboardMoveListener /*implements AndroidKeyboard.AndroidKeyboardListener*/ {
    HashMap<Actor, Float> moved = new HashMap<>();

    /* access modifiers changed from: package-private */
    public void moveActor(final int height, boolean extra) {
        Focusable focus = FocusManager.getFocusedWidget();
        if (focus != null) {
            Actor actor = (Actor) focus;
            if (actor instanceof VisTextField) {
                VisTextField field = (VisTextField) actor;
                Iterator<EventListener> it = field.getListeners().iterator();
                while (it.hasNext()) {
//                    if (it.next() instanceof TextFieldDialogListener) return;
                    return;
                }
                Actor parent = SceneUtils.getTopParent(field);
                float actory = field.localToStageCoordinates(new Vector2(0.0f, 0.0f)).y;
                float keyheight = /*((float) AndroidKeyboard.getCurrentKeyboardHeight())*/ + 30.0f;
                if (height > 0) {
                    moveActorDown(parent);
                }
                if (actory < keyheight) {
                    moveActorUp(parent, keyheight - actory);
                }
            }
        } else if (extra) {
            Gdx.app.postRunnable(new Runnable() {
                public void run() {
                    DialogKeyboardMoveListener.this.moveActor(height, false);
                }
            });
        }
    }

    public void onSizeChange(int height) {
        moveActor(height, true);
    }

    /* access modifiers changed from: package-private */
    public void moveActorUp(Actor actor, float move) {
        actor.addAction(Actions.moveBy(0.0f, move, 0.1f));
        if (this.moved.containsKey(actor)) {
            this.moved.put(actor, Float.valueOf(this.moved.get(actor).floatValue() + move));
        } else {
            this.moved.put(actor, Float.valueOf(move));
        }
    }

    /* access modifiers changed from: package-private */
    public void moveActorDown(Actor actor) {
        if (this.moved.containsKey(actor)) {
            actor.addAction(Actions.moveBy(0.0f, -this.moved.get(actor).floatValue(), 0.1f));
            this.moved.remove(actor);
        }
    }
}
