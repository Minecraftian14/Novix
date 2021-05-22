package io.anuke.novix.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import io.anuke.novix.modules.Core;
import io.anuke.novix.scene.AnimatedImage;
import io.anuke.novix.scene.BorderImage;
import io.anuke.novix.scene.StaticPreviewImage;
import io.anuke.novix.scene.TallMenuItem;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.Hue;
import io.anuke.utools.SceneUtils;
import io.anuke.novix.tools.Project;

/* renamed from: io.anuke.novix.ui.ProjectMenu */
public class ProjectMenu extends DialogClasses.BaseDialog {
    private boolean loading = false;
    /* access modifiers changed from: private */
    public Core main;
    private VisScrollPane pane;

    public ProjectMenu(Core mainref) {
        super("Projects");
        this.main = mainref;
        addTitleSeperator();
        padTop(getPadTop() + (10.0f * UCore.f331s));
        this.pane = new VisScrollPane(new VisTable()) {
            public float getPrefHeight() {
                return (float) Gdx.graphics.getHeight();
            }
        };
        this.pane.setName("projectpane");
        this.pane.setFadeScrollBars(false);
        this.pane.setOverscroll(false, false);
        VisTable newtable = new VisTable();
        final float newbuttonwidth = 190.0f * UCore.f331s;
        final VisTextButton newbutton = new VisTextButton("New Project");
        final PopupMenu popup = new PopupMenu() {
            public float getPrefWidth() {
                return newbuttonwidth;
            }
        };
        popup.addItem(new TallMenuItem("New...", new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                ProjectMenu.this.main.projectmanager.newProject();
            }
        }) {
            public float getPrefWidth() {
                return newbuttonwidth;
            }
        });
        popup.addItem(new TallMenuItem("From File..", new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                new DialogClasses.OpenProjectFileDialog().show(ProjectMenu.this.main.stage);
            }
        }) {
            public float getPrefWidth() {
                return newbuttonwidth;
            }
        });
        newbutton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                popup.showMenu(ProjectMenu.this.main.stage, newbutton);
            }
        });
        newbutton.setName("newproject");
        SceneUtils.addIconToButton(newbutton, new Image(VisUI.getSkin().getDrawable("icon-plus")), 32.0f * UCore.f331s);
        VisTextButton settingsbutton = new VisTextButton("Settings");
        settingsbutton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ProjectMenu.this.main.openSettingsMenu();
            }
        });
        settingsbutton.setName("settings");
        SceneUtils.addIconToButton(settingsbutton, new VisImage("icon-settings"), 32.0f * UCore.f331s);
        newtable.left().add(newbutton).padBottom(6.0f * UCore.f331s).size(newbuttonwidth, 60.0f * UCore.f331s);
        newtable.left().add().grow();
        newtable.left().add(settingsbutton).padBottom(6.0f * UCore.f331s).size(140.0f * UCore.f331s, 60.0f * UCore.f331s).align(18);
        getContentTable().add(newtable).grow().row();
        getContentTable().top().left().add(this.pane).align(10).grow();
        VisTextButton projectback = new VisTextButton("Back");
        projectback.add(new Image(VisUI.getSkin().getDrawable("icon-arrow-left"))).size(40.0f * UCore.f331s).center();
        projectback.getCells().reverse();
        projectback.getLabelCell().padRight(40.0f * UCore.f331s);
        getButtonsTable().add(projectback).width((((float) Gdx.graphics.getWidth()) - getPadLeft()) - getPadRight()).height(60.0f * UCore.f331s);
        setObject(projectback, false);
    }

    public void startLoading() {
        this.loading = true;
    }

    public void notifyLoaded() {
        this.loading = false;
    }

    public boolean isLoading() {
        return this.loading;
    }

    public ProjectTable update(boolean loaded) {
        boolean z;
        VisTable scrolltable = (VisTable) ((VisScrollPane) getContentTable().getCells().get(1).getActor()).getChildren().first();
        scrolltable.clearChildren();
        ProjectTable current = null;
        for (Project project : this.main.projectmanager.getProjects()) {
            if (project == this.main.getCurrentProject()) {
                z = loaded;
            } else {
                z = true;
            }
            ProjectTable table = new ProjectTable(project, z);
            scrolltable.top().left().add(table).padTop(8.0f * UCore.f331s).growX().padRight(10.0f * UCore.f331s).row();
            if (project == this.main.getCurrentProject()) {
                current = table;
            }
        }
        return current;
    }

    public ProjectTable getFirstTable() {
        return (ProjectTable) ((VisTable) ((VisScrollPane) getContentTable().getCells().get(1).getActor()).getChildren().first()).getCells().first().getActor();
    }

    public VisDialog show(Stage stage) {
        super.show(stage);
        stage.setScrollFocus(this.pane);
        int i = Gdx.app.getType() == Application.ApplicationType.Desktop ? 0 : 1;
        setSize(stage.getWidth(), stage.getHeight() - ((float) i));
        setY((float) i);
        return this;
    }

    /* renamed from: io.anuke.novix.ui.ProjectMenu$ProjectTable */
    public class ProjectTable extends VisTable {
        /* access modifiers changed from: private */
        public boolean created;
        /* access modifiers changed from: private */
        public Cell<?> imagecell;
        public boolean loaded;
        public final Project project;
        /* access modifiers changed from: private */
        public Label sizelabel = new VisLabel("Loading...");

        public ProjectTable(Project project2, boolean startloaded) {
            this.project = project2;
            this.loaded = startloaded;
            VisLabel namelabel = new VisLabel(project2.name);
            this.sizelabel.setColor(Color.GRAY);
            float imagesize = 42.0f * UCore.f331s;
            VisImageButton openbutton = new VisImageButton(VisUI.getSkin().getDrawable("icon-project-open"));
            openbutton.setGenerateDisabledImage(true);
            VisImageButton copybutton = new VisImageButton(VisUI.getSkin().getDrawable("icon-copy"));
            VisImageButton renamebutton = new VisImageButton(VisUI.getSkin().getDrawable("icon-rename"));
            VisImageButton deletebutton = new VisImageButton(VisUI.getSkin().getDrawable("icon-trash"));
            openbutton.setName(String.valueOf(project2.name) + "openbutton");
            copybutton.setName(String.valueOf(project2.name) + "copybutton");
            renamebutton.setName(String.valueOf(project2.name) + "renamebutton");
            deletebutton.setName(String.valueOf(project2.name) + "deletebutton");
            if (project2 == ProjectMenu.this.main.getCurrentProject()) {
                openbutton.setDisabled(true);
                openbutton.setColor(Hue.lightness(0.94f));
            }
            final Project project3 = project2;
            openbutton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if (project3 != ProjectMenu.this.main.getCurrentProject()) {
                        ProjectMenu.this.main.projectmanager.openProject(project3);
                    }
                }
            });
            final Project project4 = project2;
            copybutton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    ProjectMenu.this.main.projectmanager.copyProject(project4);
                }
            });
            final Project project5 = project2;
            renamebutton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    ProjectMenu.this.main.projectmanager.renameProject(project5);
                }
            });
            final Project project6 = project2;
            deletebutton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    ProjectMenu.this.main.projectmanager.deleteProject(project6);
                }
            });
            openbutton.getImageCell().size(imagesize);
            copybutton.getImageCell().size(imagesize);
            renamebutton.getImageCell().size(imagesize);
            deletebutton.getImageCell().size(imagesize);
            VisTable texttable = new VisTable();
            VisTable buttontable = new VisTable();
            float bheight = 50.0f * UCore.f331s;
            float space = 4.0f * UCore.f331s;
            float pad = 3.0f * UCore.f331s;
            buttontable.bottom().left().add(openbutton).align(12).height(bheight).growX().space(space).padBottom(pad);
            buttontable.add(copybutton).height(bheight).growX().space(space).padBottom(pad);
            buttontable.add(renamebutton).height(bheight).growX().space(space).padBottom(pad);
            buttontable.add(deletebutton).height(bheight).growX().space(space).padBottom(pad);
            top().left();
            background("button");
            setColor(Hue.lightness(0.85f));
            this.imagecell = stack(new AnimatedImage(VisUI.getSkin().getDrawable("icon-load-1"), VisUI.getSkin().getDrawable("icon-load-2"), VisUI.getSkin().getDrawable("icon-load-3")), new BorderImage());
            this.imagecell.padTop(this.imagecell.getPadTop() + 4.0f).padBottom(this.imagecell.getPadBottom() + 4.0f);
            SceneUtils.fitCell(this.imagecell, 128.0f * UCore.f331s, 1.0f);
            add(texttable).grow();
            texttable.top().left().add(namelabel).padLeft(8.0f * UCore.f331s).align(10);
            texttable.row();
            texttable.add(this.sizelabel).padLeft(8.0f * UCore.f331s).padTop(10.0f * UCore.f331s).align(10);
            texttable.row();
            texttable.add(buttontable).grow().padLeft(8.0f);
            setName("projecttable" + project2.name);
            final Project project7 = project2;
            addAction(new Action() {
                public boolean act(float delta) {
                    if (ProjectTable.this.created) {
                        return true;
                    }
                    if (!ProjectTable.this.loaded) {
                        return false;
                    }
                    if (project7 == ProjectMenu.this.main.getCurrentProject()) {
                        project7.reloadTexture();
                    }
                    Texture texture = project7.cachedTexture;
                    ProjectTable.this.sizelabel.setText("Size: " + texture.getWidth() + "x" + texture.getHeight());
                    ProjectTable.this.imagecell.setActor(new StaticPreviewImage(texture));
                    SceneUtils.fitCell(ProjectTable.this.imagecell, 128.0f * UCore.f331s, ((float) texture.getWidth()) / ((float) texture.getHeight()));
                    ProjectTable.this.imagecell.padTop(ProjectTable.this.imagecell.getPadTop() + 4.0f).padBottom(ProjectTable.this.imagecell.getPadBottom() + 4.0f);
                    ProjectTable.this.pack();
                    ProjectTable.this.created = true;
                    return true;
                }
            });
            addPadding(this);
        }
    }
}
