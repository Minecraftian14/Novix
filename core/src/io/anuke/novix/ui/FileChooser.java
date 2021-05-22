package io.anuke.novix.ui;

//import android.os.Environment;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

//import io.anuke.novix.android.TextFieldDialogListener;
import io.anuke.utools.MiscUtils;

/* renamed from: io.anuke.novix.ui.FileChooser */
public class FileChooser extends DialogClasses.BaseDialog {
    public static FileHandleFilter defaultFilter = new FileHandleFilter() {
        public boolean accept(FileHandle file) {
            return true;
        }
    };
    public static FileHandleFilter jpegFilter = new FileHandleFilter() {
        public boolean accept(FileHandle file) {
            return file.extension().equalsIgnoreCase("png") || file.extension().equalsIgnoreCase("jpg") || file.extension().equalsIgnoreCase("jpeg");
        }
    };
    public static FileHandleFilter pngFilter = new FileHandleFilter() {
        public boolean accept(FileHandle file) {
            return file.extension().equalsIgnoreCase("png");
        }
    };

    /* renamed from: s */
    private static float f329s = MiscUtils.densityScale();
    /* access modifiers changed from: private */
    public FileHandle directory;
    /* access modifiers changed from: private */
    public VisTextField filefield;
    private Table files;
    private FileHandleFilter filter;
    /* access modifiers changed from: private */
    public FileHandle homeDirectory;
    private VisTextField navigation;
    /* access modifiers changed from: private */

    /* renamed from: ok */
    public VisTextButton f330ok;
    private boolean open;
    private VisScrollPane pane;
    /* access modifiers changed from: private */
    public FileHistory stack;

    /* renamed from: io.anuke.novix.ui.FileChooser$FileHandleFilter */
    public interface FileHandleFilter {
        boolean accept(FileHandle fileHandle);
    }

    public FileChooser(boolean open2) {
        this(defaultFilter, open2);
    }

    public FileChooser(FileHandleFilter filter2, boolean open2) {
        super("Choose File");
//        FileHandle absolute = Gdx.app.getType() == Application.ApplicationType.Desktop ? Gdx.files.absolute(System.getProperty("user.home")) : Gdx.files.absolute(Environment.getExternalStorageDirectory().getAbsolutePath());
        FileHandle absolute = Gdx.files.absolute(System.getProperty("user.home"));
        this.directory = absolute;
        this.homeDirectory = absolute;
        this.directory = this.homeDirectory;
        this.stack = new FileHistory();
        this.open = open2;
        this.filter = filter2;
        setMovable(false);
        addCloseButton();
        setupWidgets();
    }

    private void setupWidgets() {
        this.filefield = new VisTextField();
        /*if (!this.open) {
            TextFieldDialogListener.add(this.filefield);
        }*/
        this.filefield.setDisabled(this.open);
        this.f330ok = new VisTextButton(this.open ? "Open" : "Save");
        this.f330ok.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (!FileChooser.this.f330ok.isDisabled()) {
                    FileChooser.this.fileSelected(FileChooser.this.directory.child(FileChooser.this.filefield.getText()));
                    FileChooser.this.close();
                }
            }
        });
        this.filefield.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                FileChooser.this.f330ok.setDisabled(FileChooser.this.filefield.getText().replace(" ", "").isEmpty());
            }
        });
        this.filefield.fire(new ChangeListener.ChangeEvent());
        VisTextButton cancel = new VisTextButton("Cancel");
        cancel.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                FileChooser.this.close();
            }
        });
        this.navigation = new VisTextField("");
        this.navigation.setTouchable(Touchable.disabled);
        this.files = new VisTable();
        this.pane = new VisScrollPane(this.files);
        this.pane.setOverscroll(false, false);
        this.pane.setFadeScrollBars(false);
        updateFiles(true);
        VisTable icontable = new VisTable();
        VisImageButton up = new VisImageButton(VisUI.getSkin().getDrawable("icon-folder-parent"));
        up.getImageCell().size(f329s * 42.0f);
        up.addListener(new UpListener(this, (UpListener) null));
        VisImageButton back = new VisImageButton(VisUI.getSkin().getDrawable("icon-arrow-left"));
        back.getImageCell().size(f329s * 42.0f);
        VisImageButton forward = new VisImageButton(VisUI.getSkin().getDrawable("icon-arrow-right"));
        forward.getImageCell().size(f329s * 42.0f);
        forward.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                FileChooser.this.stack.forward();
            }
        });
        back.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                FileChooser.this.stack.back();
            }
        });
        VisImageButton home = new VisImageButton(VisUI.getSkin().getDrawable("icon-home"));
        home.getImageCell().size(f329s * 42.0f);
        home.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                FileChooser.this.directory = FileChooser.this.homeDirectory;
                FileChooser.this.updateFiles(true);
            }
        });
        Table table = getContentTable();
        table.top().left();
        table.add(icontable).expandX().fillX();
        icontable.add(up).expandX().fillX().height(f329s * 60.0f).padBottom(f329s * 10.0f).uniform();
        icontable.add(back).expandX().fillX().height(f329s * 60.0f).padBottom(f329s * 10.0f).uniform();
        icontable.add(forward).expandX().fillX().height(f329s * 60.0f).padBottom(f329s * 10.0f).uniform();
        icontable.add(home).expandX().fillX().height(f329s * 60.0f).padBottom(f329s * 10.0f).uniform();
        table.row();
        table.add(this.navigation).colspan(3).left().padBottom(f329s * 10.0f).expandX().fillX().height(40.0f * f329s);
        table.row();
        table.center().add(this.pane).colspan(3).expand().fill();
        table.row();
        Table fieldtable = new VisTable();
        table.bottom().left().add(fieldtable).colspan(3).expand().fill();
        fieldtable.bottom().left().add(new VisLabel("File Name:")).padTop(20.0f);
        fieldtable.add(this.filefield).padTop(20.0f * f329s).height(f329s * 50.0f).fillX().expandX().padLeft(f329s * 10.0f).padRight(f329s * 10.0f);
        table.row();
        getButtonsTable().add(cancel).size(((float) Gdx.graphics.getWidth()) / 2.0f, f329s * 50.0f).expandX().fillX().padBottom(f329s * 10.0f).padTop(f329s * 10.0f);
        getButtonsTable().add(this.f330ok).expandX().fillX().size(((float) Gdx.graphics.getWidth()) / 2.0f, f329s * 50.0f).padBottom(f329s * 10.0f).padTop(f329s * 10.0f);
    }

    /* access modifiers changed from: private */
    public void updateFileFieldStatus() {
        if (!this.open) {
            this.f330ok.setDisabled(this.filefield.getText().replace(" ", "").isEmpty());
        } else {
            this.f330ok.setDisabled(!this.directory.child(this.filefield.getText()).exists() || this.directory.child(this.filefield.getText()).isDirectory());
        }
    }

    /* renamed from: io.anuke.novix.ui.FileChooser$UpListener */
    private class UpListener extends ClickListener {
        private UpListener() {
        }

        /* synthetic */ UpListener(FileChooser fileChooser, UpListener upListener) {
            this();
        }

        public void clicked(InputEvent event, float x, float y) {
            FileChooser.this.directory = FileChooser.this.directory.parent();
            FileChooser.this.updateFiles(true);
        }
    }

    private FileHandle[] getFileNames() {
        FileHandle[] handles = this.directory.list((FileFilter) new FileFilter() {
            public boolean accept(File file) {
                return !file.getName().startsWith(".");
            }
        });
        Arrays.sort(handles, new Comparator<FileHandle>() {
            public int compare(FileHandle a, FileHandle b) {
                if (a.isDirectory() && !b.isDirectory()) {
                    return -1;
                }
                if (a.isDirectory() || !b.isDirectory()) {
                    return a.name().compareTo(b.name());
                }
                return 1;
            }
        });
        return handles;
    }

    /* access modifiers changed from: private */
    public void updateFiles(boolean push) {
        if (push) {
            this.stack.push(this.directory);
        }
        this.navigation.setText(this.directory.toString());
        GlyphLayout layout = (GlyphLayout) Pools.obtain(GlyphLayout.class);
        layout.setText(VisUI.getSkin().getFont("default-font"), this.navigation.getText());
        if (layout.width < this.navigation.getWidth()) {
            this.navigation.setCursorPosition(0);
        } else {
            this.navigation.setCursorPosition(this.navigation.getText().length());
        }
        Pools.free(layout);
        this.files.clearChildren();
        FileHandle[] names = getFileNames();
        Image upimage = new Image(VisUI.getSkin().getDrawable("icon-folder-parent"));
        VisTextButton upbutton = new VisTextButton("...");
        upbutton.addListener(new UpListener(this, (UpListener) null));
        upbutton.left().add(upimage).padRight(4.0f * f329s).size(42.0f * f329s);
        upbutton.getCells().reverse();
        this.files.top().left().add(upbutton).align(10).fillX().expandX().height(50.0f * f329s).pad(2.0f).colspan(2);
        upbutton.getLabel().setAlignment(8);
        this.files.row();
        ButtonGroup<VisTextButton> group = new ButtonGroup<>();
        group.setMinCheckCount(0);
        for (final FileHandle file : names) {
            if (file.isDirectory() || this.filter.accept(file)) {
                final String filename = file.name();
                final VisTextButton button = new VisTextButton(shorten(filename), "toggle");
                group.add(button);
                button.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        if (!file.isDirectory()) {
                            FileChooser.this.filefield.setText(filename);
                            FileChooser.this.updateFileFieldStatus();
                            return;
                        }
                        FileChooser.this.directory = FileChooser.this.directory.child(filename);
                        FileChooser.this.updateFiles(true);
                    }
                });
                this.filefield.addListener(new ChangeListener() {
                    public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                        button.setChecked(filename.equals(FileChooser.this.filefield.getText()));
                    }
                });
                button.add(new Image(VisUI.getSkin().getDrawable(file.isDirectory() ? "icon-folder" : "icon-file-text"))).padRight(4.0f * f329s).size(42.0f * f329s);
                button.getCells().reverse();
                this.files.top().left().add(button).align(10).fillX().expandX().height(50.0f * f329s).pad(2.0f * f329s).colspan(2);
                button.getLabel().setAlignment(8);
                this.files.row();
            }
        }
        this.pane.setScrollY(0.0f);
        updateFileFieldStatus();
        if (this.open) {
            this.filefield.clearText();
        }
    }

    private String shorten(String string) {
        return string.length() <= 30 ? string : string.substring(0, 27).concat("...");
    }

    public VisDialog show(Stage stage) {
        super.show(stage);
        stage.setScrollFocus(this.pane);
        return this;
    }

    public void fileSelected(FileHandle file) {
    }

    public float getPrefWidth() {
        return (float) Gdx.graphics.getWidth();
    }

    public float getPrefHeight() {
        return (float) (Gdx.graphics.getHeight() - 1);
    }

    /* renamed from: io.anuke.novix.ui.FileChooser$FileHistory */
    public class FileHistory {
        private Array<FileHandle> history = new Array<>();
        private int index;

        public FileHistory() {
        }

        public void push(FileHandle file) {
            if (this.index != this.history.size) {
                this.history.truncate(this.index);
            }
            this.history.add(file);
            this.index++;
        }

        public void back() {
            if (canBack()) {
                this.index--;
                FileChooser.this.directory = this.history.get(this.index - 1);
                FileChooser.this.updateFiles(false);
            }
        }

        public void forward() {
            if (canForward()) {
                FileChooser.this.directory = this.history.get(this.index);
                this.index++;
                FileChooser.this.updateFiles(false);
            }
        }

        public boolean canForward() {
            return this.index < this.history.size;
        }

        public boolean canBack() {
            return this.index != 1 && this.index > 0;
        }

        /* access modifiers changed from: package-private */
        public void print() {
            System.out.println("\n\n\n\n\n\n");
            int i = 0;
            Iterator<FileHandle> it = this.history.iterator();
            while (it.hasNext()) {
                FileHandle file = it.next();
                i++;
                if (this.index == i) {
                    System.out.println("[[" + file.toString() + "]]");
                } else {
                    System.out.println("--" + file.toString() + "--");
                }
            }
        }
    }
}
