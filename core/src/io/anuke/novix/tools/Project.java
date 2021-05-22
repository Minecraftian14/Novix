package io.anuke.novix.tools;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import io.anuke.novix.modules.Core;
import io.anuke.utools.MiscUtils;

/* renamed from: io.anuke.novix.tools.Project */
public class Project implements Disposable, Comparable<Project> {
    private transient Pixmap cachedPixmap;
    public transient Texture cachedTexture;

    /* renamed from: id */
    public long id;
    public long lastloadtime;
    public String name;

    public Project(String name2, long id) {
        this.name = name2;
        this.id = id;
        reloadTexture();
    }

    public Project() {
    }

    public Pixmap getCachedPixmap() {
        if (this.cachedPixmap == null || ((Boolean) MiscUtils.getPrivate(this.cachedPixmap, "disposed")).booleanValue()) {
            reloadTexture();
        }
        return this.cachedPixmap;
    }

    public void reloadTexture() {
        if (this.cachedTexture != null) {
            this.cachedTexture.dispose();
        }
        this.cachedTexture = new Texture(getFile());
        this.cachedTexture.getTextureData().prepare();
        this.cachedPixmap = this.cachedTexture.getTextureData().consumePixmap();
    }

    public FileHandle getFile() {
        return Core.i.projectmanager.getFile(this.id);
    }

    public void dispose() {
        this.cachedTexture.dispose();
        this.cachedPixmap.dispose();
    }

    public int compareTo(Project other) {
        if (other.lastloadtime == this.lastloadtime) {
            return 0;
        }
        return other.lastloadtime > this.lastloadtime ? 1 : -1;
    }
}
