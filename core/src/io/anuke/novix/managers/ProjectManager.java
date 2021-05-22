package io.anuke.novix.managers;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import io.anuke.novix.Novix;
import io.anuke.novix.modules.Core;
import io.anuke.novix.ui.DialogClasses;
import io.anuke.novix.tools.PixelCanvas;
import io.anuke.novix.tools.Project;

/* renamed from: io.anuke.novix.managers.ProjectManager */
public class ProjectManager {
    /* access modifiers changed from: private */
    public boolean backedup;
    /* access modifiers changed from: private */
    public Project currentProject;
    private Json json = new Json();
    /* access modifiers changed from: private */
    public Core main;
    /* access modifiers changed from: private */
    public ObjectMap<Long, Project> projects = new ObjectMap<>();
    private Array<Project> projectsort = new Array<>();
    private boolean savingProject = false;

    public ProjectManager(Core main2) {
        this.main = main2;
    }

    public Iterable<Project> getProjects() {
        this.projectsort.clear();
        ObjectMap.Values<Project> it = this.projects.values().iterator();
        while (it.hasNext()) {
            this.projectsort.add((Project) it.next());
        }
        this.projectsort.sort();
        return this.projectsort;
    }

    public boolean isSavingProject() {
        return this.savingProject;
    }

    public Project getCurrentProject() {
        return this.currentProject;
    }

    public void newProject() {
        new DialogClasses.NamedSizeDialog("New Project") {
            public void result(String name, int width, int height) {
                ProjectManager.this.openProject(ProjectManager.this.createNewProject(name, width, height));
            }
        }.show(this.main.stage);
    }

    public Project createNewProject(String name, int width, int height) {
        long id = generateProjectID();
        PixmapIO.writePNG(getFile(id), new Pixmap(width, height, Pixmap.Format.RGBA8888));
        Project project = loadProject(name, id);
        Novix.log("Created new project with name " + name);
        return project;
    }

    public void openProject(Project project) {
        this.main.prefs.put("lastproject", project.id);
        project.lastloadtime = System.currentTimeMillis();
        this.currentProject = project;
        Novix.log("Opening project \"" + project.name + "\"...");
        PixelCanvas canvas = new PixelCanvas(project.getCachedPixmap());
        if (canvas.width() > 100 || canvas.height() > 100) {
            this.main.prefs.put("grid", false);
        }
        this.main.prefs.save();
        this.main.drawgrid.clearActionStack();
        this.main.drawgrid.setCanvas(canvas, false);
        this.main.updateToolColor();
        this.main.projectmenu.hide();
    }

    public void copyProject(Project project) {
        final Project project2 = project;
        new DialogClasses.InputDialog("Copy Project", project.name, "Copy Name: ") {
            public void result(String text) {
                try {
                    long id = ProjectManager.this.generateProjectID();
                    ProjectManager.this.getFile(project2.id).copyTo(ProjectManager.this.getFile(id));
                    Project newproject = new Project(text, id);
                    ProjectManager.this.projects.put(Long.valueOf(newproject.id), newproject);
                    ProjectManager.this.main.projectmenu.update(true);
                } catch (Exception e) {
                    DialogClasses.showError(ProjectManager.this.main.stage, "Error copying file!", e);
                    e.printStackTrace();
                }
            }
        }.show(this.main.stage);
    }

    public void renameProject(Project project) {
        final Project project2 = project;
        new DialogClasses.InputDialog("Rename Project", project.name, "Name: ") {
            public void result(String text) {
                project2.name = text;
                ProjectManager.this.main.projectmenu.update(true);
            }
        }.show(this.main.stage);
    }

    public void deleteProject(final Project project) {
        if (project == this.currentProject) {
            DialogClasses.showInfo(this.main.stage, "You cannot delete the canvas you are currently using!");
        } else {
            new DialogClasses.ConfirmDialog("Confirm", "Are you sure you want\nto delete this canvas?") {
                public void result() {
                    try {
                        project.getFile().delete();
                        if (ProjectManager.this.getBackupFile(project.id).exists()) {
                            ProjectManager.this.getBackupFile(project.id).delete();
                        }
                        project.dispose();
                        ProjectManager.this.projects.remove(Long.valueOf(project.id));
                        ProjectManager.this.main.projectmenu.update(true);
                    } catch (Exception e) {
                        DialogClasses.showError(ProjectManager.this.main.stage, "Error deleting file!", e);
                        e.printStackTrace();
                    }
                }
            }.show(this.main.stage);
        }
    }

    public void saveProject() {
        saveProjectsFile();
        Core.instance.prefs.put("lastproject", getCurrentProject().id);
        this.savingProject = true;
        Novix.log("Starting save..");
        PixmapIO.writePNG(this.currentProject.getFile(), this.main.drawgrid.canvas.pixmap);
        Novix.log("Saving project.");
        this.savingProject = false;
    }

    private void loadProjectFile() {
        try {
            ObjectMap<String, Project> map = (ObjectMap) this.json.fromJson(ObjectMap.class, Core.instance.projectFile);
            this.projects = new ObjectMap<>();
            ObjectMap.Keys<String> it = map.keys().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                this.projects.put(Long.valueOf(Long.parseLong(key)), map.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Novix.log("Project file nonexistant or corrupt.");
        }
    }

    private void saveProjectsFile() {
        Core.instance.projectFile.writeString(this.json.toJson(this.projects), false);
    }

    public void loadProjects() {
        loadProjectFile();
        long last = this.main.prefs.getLong("lastproject");
        this.currentProject = this.projects.get(Long.valueOf(last));
        ObjectMap.Values<Project> it = this.projects.values().iterator();
        while (it.hasNext()) {
            Project project = (Project) it.next();
            try {
                project.reloadTexture();
            } catch (Exception e) {
                e.printStackTrace();
                Novix.log("Error loading project \"" + project.name + "\": corrupt file?");
                if (project != this.currentProject) {
                    this.projects.remove(Long.valueOf(project.id));
                }
            }
        }
        saveProjectsFile();
        if (this.projects.get(Long.valueOf(last)) == null) {
            tryLoadAnotherProject();
            return;
        }
        this.backedup = false;
        try {
            this.currentProject = this.projects.get(Long.valueOf(last));
            this.currentProject.reloadTexture();
            getFile(this.currentProject.id).copyTo(getBackupFile(this.currentProject.id));
            Novix.log("Loaded and backed up current project.");
        } catch (Exception e2) {
            e2.printStackTrace();
            Novix.log("Project file corrupted?");
            this.projects.remove(Long.valueOf(this.currentProject.id));
            if (getBackupFile(this.currentProject.id).exists()) {
                try {
                    getBackupFile(this.currentProject.id).copyTo(getFile(this.currentProject.id));
                    this.currentProject.reloadTexture();
                    this.backedup = true;
                } catch (Exception e22) {
                    e22.printStackTrace();
                    Novix.log("Backup attempt failed.");
                    tryLoadAnotherProject();
                }
            } else {
                tryLoadAnotherProject();
            }
            Core.instance.stage.addAction(Actions.sequence(Actions.delay(0.01f), new Action() {
                public boolean act(float delta) {
                    new DialogClasses.InfoDialog("Info", ProjectManager.this.backedup ? "[ORANGE]Your project file has been either corrupted or deleted.\n\n[GREEN]Fortunately, a backup has been found and loaded." : "[RED]Your project file has been either corrupted or deleted.\n\n[ORANGE]A backup has not been found.\n\n[ROYAL]If you believe this is an error, try reporting the circumstances under which you last closed the app at the Google Play store listing. This could help the developer fix the problem.") {
                        public void result() {
                            ProjectManager.this.currentProject.reloadTexture();
                        }
                    }.show(Core.instance.stage);
                    return true;
                }
            }));
        }
    }

    /* access modifiers changed from: package-private */
    public void tryLoadAnotherProject() {
        if (this.projects.size == 0) {
            this.currentProject = createNewProject("Untitled", 16, 16);
        } else {
            this.currentProject = this.projects.values().next();
        }
    }

    public Project loadProject(String name, long id) {
        Project project = new Project(name, id);
        this.projects.put(Long.valueOf(project.id), project);
        return project;
    }

    public FileHandle getFile(long id) {
        return Core.instance.projectDirectory.child(String.valueOf(id) + ".png");
    }

    public FileHandle getBackupFile(long id) {
        return Core.instance.projectDirectory.child(String.valueOf(id) + "-backup.png");
    }

    public long generateProjectID() {
        return MathUtils.random(9223372036854775806L);
    }
}
