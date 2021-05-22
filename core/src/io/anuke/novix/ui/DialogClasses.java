package io.anuke.novix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import io.anuke.novix.graphics.Filter;
import io.anuke.novix.modules.Core;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.PixmapUtils;
import io.anuke.ucore.util.Mathf;
import io.anuke.utools.MiscUtils;
import io.anuke.utools.SceneUtils;
import io.anuke.novix.scene.AlphaImage;
import io.anuke.novix.scene.ColorBox;
import io.anuke.novix.scene.ColorWidget;
import io.anuke.novix.scene.GridImage;
import io.anuke.novix.scene.ImagePreview;
import io.anuke.novix.scene.ShiftedImage;
import io.anuke.novix.tools.PixelCanvas;

/* renamed from: io.anuke.novix.ui.DialogClasses */
public class DialogClasses {

    /* renamed from: io.anuke.novix.ui.DialogClasses$SizeDialog */
    public static class SizeDialog extends MenuDialog {
        VisTextField heightfield = new VisTextField();
        VisTextField widthfield = new VisTextField();

        public SizeDialog(String title) {
            super(title);
            this.widthfield.setTextFieldFilter(new VisTextField.TextFieldFilter.DigitsOnlyFilter());
            this.heightfield.setTextFieldFilter(new VisTextField.TextFieldFilter.DigitsOnlyFilter());
            this.widthfield.setText(new StringBuilder(String.valueOf(Core.i.drawgrid.canvas.width())).toString());
            this.heightfield.setText(new StringBuilder(String.valueOf(Core.i.drawgrid.canvas.height())).toString());
            float twidth = 160.0f * UCore.s;
            float theight = 40.0f * UCore.s;
            getContentTable().add(new VisLabel("Width: ")).padLeft(UCore.s * 50.0f).padTop(UCore.s * 40.0f);
            getContentTable().add(this.widthfield).size(twidth, theight).padRight(UCore.s * 50.0f).padTop(UCore.s * 40.0f);
            getContentTable().row();
            getContentTable().add(new VisLabel("Height: ")).padLeft(UCore.s * 50.0f).padTop(UCore.s * 40.0f).padBottom(UCore.s * 40.0f);
            getContentTable().add(this.heightfield).size(twidth, theight).padRight(UCore.s * 50.0f).padTop(UCore.s * 40.0f).padBottom(UCore.s * 40.0f);
            getContentTable().row();
            ChangeListener listener = new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    SizeDialog.this.f326ok.setDisabled(SizeDialog.this.widthfield.getText().replace("0", "").isEmpty() || SizeDialog.this.heightfield.getText().replace("0", "").isEmpty());
                }
            };
            this.widthfield.addListener(listener);
            this.heightfield.addListener(listener);
        }

        public void result() {
            try {
                result(Integer.parseInt(this.widthfield.getText()), Integer.parseInt(this.heightfield.getText()));
            } catch (Exception e) {
                e.printStackTrace();
                DialogClasses.showError(getStage(), "Image error!", e);
            }
        }

        public void result(int width, int height) {
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$NamedSizeDialog */
    public static class NamedSizeDialog extends MenuDialog {
        VisTextField heightfield = new VisTextField();
        VisTextField namefield = new VisTextField();
        VisTextField widthfield = new VisTextField();

        public NamedSizeDialog(String title) {
            super(title);
            this.widthfield.setText(new StringBuilder(String.valueOf(Core.i.drawgrid.canvas.width())).toString());
            this.heightfield.setText(new StringBuilder(String.valueOf(Core.i.drawgrid.canvas.height())).toString());
            float twidth = 160.0f * UCore.s;
            float theight = 40.0f * UCore.s;
            getContentTable().add(new VisLabel("Name: ")).padLeft(UCore.s * 50.0f).padTop(UCore.s * 40.0f);
            getContentTable().add(this.namefield).size(twidth, theight).padRight(UCore.s * 50.0f).padTop(UCore.s * 40.0f);
            getContentTable().row();
            getContentTable().add(new VisLabel("Width: ")).padLeft(UCore.s * 50.0f).padTop(UCore.s * 40.0f);
            getContentTable().add(this.widthfield).size(twidth, theight).padRight(UCore.s * 50.0f).padTop(UCore.s * 40.0f);
            getContentTable().row();
            getContentTable().add(new VisLabel("Height: ")).padLeft(UCore.s * 50.0f).padTop(UCore.s * 40.0f).padBottom(UCore.s * 40.0f);
            getContentTable().add(this.heightfield).size(twidth, theight).padRight(UCore.s * 50.0f).padTop(UCore.s * 40.0f).padBottom(UCore.s * 40.0f);
            getContentTable().row();
            ChangeListener oklistener = new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    NamedSizeDialog.this.f326ok.setDisabled(NamedSizeDialog.this.widthfield.getText().isEmpty() || NamedSizeDialog.this.heightfield.getText().isEmpty() || NamedSizeDialog.this.namefield.getText().replace(" ", "").isEmpty());
                }
            };
            this.widthfield.addListener(oklistener);
            this.heightfield.addListener(oklistener);
            this.namefield.addListener(oklistener);
            this.widthfield.fire(new ChangeListener.ChangeEvent());
        }

        public void result() {
            try {
                result(this.namefield.getText(), Integer.parseInt(this.widthfield.getText()), Integer.parseInt(this.heightfield.getText()));
            } catch (Exception e) {
                e.printStackTrace();
                DialogClasses.showError(getStage(), "Image error!", e);
            }
        }

        public void result(String name, int width, int height) {
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$ContrastDialog */
    public static class ContrastDialog extends FilterDialog {
        VisSlider slider = new VisSlider(-50.0f, 50.0f, 1.0f, false);

        public ContrastDialog() {
            super(Filter.contrast, "Change Image Contrast");
            final VisLabel label = new VisLabel("Contrast: 0");
            this.slider.setValue(0.0f);
            this.slider.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    label.setText("Contrast: " + ContrastDialog.this.slider.getValue());
                }
            });
            addSliderChangeListener(this.slider);
            getContentTable().add(label).expand().align(12).row();
            getContentTable().add(this.slider).expand().growX().align(2).padTop(15.0f * UCore.s).padBottom(30.0f * UCore.s);
            updatePreview();
        }

        /* access modifiers changed from: package-private */
        public Object[] getArgs() {
            return new Object[]{Float.valueOf(this.slider.getValue() / 50.0f)};
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$ColorChooseDialog */
    public static class ColorChooseDialog extends BaseDialog {
        final ColorWidget picker;

        public ColorChooseDialog(final FilterDialog filter) {
            this((Runnable) new Runnable() {
                public void run() {
                    filter.updatePreview();
                }
            });
        }

        public ColorChooseDialog(final Runnable changed) {
            super("Choose Color");
            this.picker = new ColorWidget(false) {
                public void onColorChanged() {
                    ColorChooseDialog.this.colorChanged();
                }
            };
            this.picker.setRecentColors(Core.i.colormenu.getRecentColors());
            addCloseButton();
            getContentTable().add(this.picker).expand().fill();
            VisTextButton button = new VisTextButton("OK");
            button.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    changed.run();
                }
            });
            getButtonsTable().add(button).size(320.0f * UCore.s, 70.0f * UCore.s).pad(5.0f * UCore.s);
            setObject(button, true);
        }

        public float getPrefWidth() {
            return Math.min((float) Gdx.graphics.getWidth(), super.getPrefWidth());
        }

        public void colorChanged() {
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$ReplaceDialog */
    public static class ReplaceDialog extends FilterDialog {
        ColorBox from = new ColorBox(Core.i.selectedColor());
        ColorBox selected;

        /* renamed from: to */
        ColorBox f327to = new ColorBox();

        public ReplaceDialog() {
            super(Filter.replace, "Replace Colors");
            final ColorChooseDialog dialog = new ColorChooseDialog(this) {
                public void colorChanged() {
                    ReplaceDialog.this.selected.setColor(this.picker.getSelectedColor());
                }
            };
            ClickListener listener = new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    ReplaceDialog.this.selected = (ColorBox) event.getTarget();
                    dialog.picker.setSelectedColor(event.getTarget().getColor());
                    dialog.show(Core.i.stage);
                }
            };
            this.from.addSelectListener();
            this.f327to.addSelectListener();
            this.from.addListener(listener);
            this.f327to.addListener(listener);
            Table table = new VisTable();
            getContentTable().add(table).expand().fill();
            VisImageButton pickfrom = new VisImageButton(VisUI.getSkin().getDrawable("icon-pick"));
            VisImageButton pickto = new VisImageButton(VisUI.getSkin().getDrawable("icon-pick"));
            pickfrom.getImageCell().size(UCore.s * 60.0f);
            pickto.getImageCell().size(UCore.s * 60.0f);
            table.add(this.from).size(UCore.s * 70.0f).pad(UCore.s * 10.0f);
            table.add(new Image(VisUI.getSkin().getDrawable("icon-arrow-right"))).size(UCore.s * 60.0f).pad(5.0f * UCore.s);
            table.add(this.f327to).size(UCore.s * 70.0f).pad(UCore.s * 10.0f);
            updatePreview();
        }

        /* access modifiers changed from: package-private */
        public Object[] getArgs() {
            return new Object[]{this.from.getColor(), this.f327to.getColor()};
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$ColorAlphaDialog */
    public static class ColorAlphaDialog extends FilterDialog {
        ColorBox selected;

        public ColorAlphaDialog(Filter filter, String name, String colorname) {
            super(filter, name);
            setup(colorname);
        }

        public ColorAlphaDialog() {
            this(Filter.colorToAlpha, "Color to Alpha", "Color:");
        }

        private void setup(String colorname) {
            this.selected = new ColorBox(Core.i.selectedColor());
            this.selected.addSelectListener();
            final ColorChooseDialog dialog = new ColorChooseDialog(this) {
                public void colorChanged() {
                    ColorAlphaDialog.this.selected.setColor(this.picker.getSelectedColor());
                }
            };
            this.selected.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    ColorAlphaDialog.this.selected = (ColorBox) event.getTarget();
                    dialog.picker.setSelectedColor(event.getTarget().getColor());
                    dialog.show(Core.i.stage);
                }
            });
            Table table = new VisTable();
            getContentTable().add((CharSequence) colorname).padTop(15.0f * UCore.s).row();
            getContentTable().add(table).expand().fill();
            table.top().add(this.selected).size(70.0f * UCore.s).pad(5.0f * UCore.s);
            updatePreview();
        }

        /* access modifiers changed from: package-private */
        public Object[] getArgs() {
            return new Object[]{this.selected.getColor()};
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$OutlineDialog */
    public static class OutlineDialog extends ColorAlphaDialog {
        public OutlineDialog() {
            super(Filter.outline, "Add Outline", "Outline Color:");
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$ColorizeDialog */
    public static class ColorizeDialog extends FilterDialog {
        VisSlider bslider = new VisSlider(0.0f, 100.0f, 1.0f, false);
        VisSlider hslider = new VisSlider(0.0f, 360.0f, 1.0f, false);
        VisSlider sslider = new VisSlider(0.0f, 100.0f, 1.0f, false);

        public ColorizeDialog() {
            super(Filter.colorize, "Colorize Image");
            final VisLabel hlabel = new VisLabel("Hue:");
            final VisLabel slabel = new VisLabel("Saturation:");
            final VisLabel blabel = new VisLabel("Brightness:");
            ChangeListener listener = new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    hlabel.setText("Hue: " + ColorizeDialog.this.hslider.getValue());
                    slabel.setText("Saturation: " + ColorizeDialog.this.sslider.getValue());
                    blabel.setText("Brightness: " + ColorizeDialog.this.bslider.getValue());
                }
            };
            this.hslider.addListener(listener);
            this.sslider.addListener(listener);
            this.bslider.addListener(listener);
            addSliderChangeListener(this.hslider, this.sslider, this.bslider);
            this.hslider.setValue(180.0f);
            this.sslider.setValue(50.0f);
            this.bslider.setValue(50.0f);
            getContentTable().add(hlabel).align(8).padTop(10.0f * UCore.s).row();
            getContentTable().add(this.hslider).expand().fill().row();
            getContentTable().add(slabel).align(8).padTop(UCore.s * 5.0f).row();
            getContentTable().add(this.sslider).expand().fill().row();
            getContentTable().add(blabel).align(8).padTop(UCore.s * 5.0f).row();
            getContentTable().add(this.bslider).expand().fill().padBottom(30.0f * UCore.s);
            updatePreview();
        }

        /* access modifiers changed from: package-private */
        public Object[] getArgs() {
            return new Object[]{Float.valueOf(this.hslider.getValue() / 360.0f), Float.valueOf(this.sslider.getValue() / 100.0f), Float.valueOf(this.bslider.getValue() / 100.0f)};
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$InvertDialog */
    public static class InvertDialog extends FilterDialog {
        public InvertDialog() {
            super(Filter.invert, "Invert Image");
            updatePreview();
        }

        /* access modifiers changed from: package-private */
        public Object[] getArgs() {
            return new Object[0];
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$FlipDialog */
    public static class FlipDialog extends FilterDialog {
        VisCheckBox hbox = new VisCheckBox("Flip Horizontally");
        VisCheckBox vbox = new VisCheckBox("Flip Vertically");

        public FlipDialog() {
            super(Filter.flip, "Flip Image");
            new ButtonGroup(this.hbox, this.vbox);
            this.vbox.setChecked(true);
            ChangeListener listener = new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    FlipDialog.this.updatePreview();
                }
            };
            this.hbox.addListener(listener);
            this.vbox.addListener(listener);
            this.hbox.getImageStackCell().size(UCore.s * 40.0f);
            this.vbox.getImageStackCell().size(UCore.s * 40.0f);
            Table table = getContentTable();
            table.add(this.vbox).align(8).padTop(UCore.s * 25.0f).padLeft(UCore.s * 40.0f).row();
            table.add(this.hbox).align(8).padTop(UCore.s * 25.0f).padLeft(UCore.s * 40.0f).padBottom(UCore.s * 25.0f);
            updatePreview();
        }

        /* access modifiers changed from: package-private */
        public Object[] getArgs() {
            return new Object[]{Boolean.valueOf(this.vbox.isChecked())};
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$RotateDialog */
    public static class RotateDialog extends FilterDialog {
        VisSlider slider = new VisSlider(0.0f, 360.0f, 5.0f, false);

        public RotateDialog() {
            super(Filter.rotate, "Rotate Image");
            final VisLabel label = new VisLabel("Rotation: 0.0");
            this.slider.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    label.setText("Rotation: " + MiscUtils.limit(new StringBuilder(String.valueOf(RotateDialog.this.slider.getValue())).toString(), 5));
                }
            });
            addSliderChangeListener(this.slider);
            getContentTable().add(label).align(8).padTop(20.0f * UCore.s).row();
            getContentTable().add(this.slider).expand().fill().padBottom(30.0f * UCore.s).padTop(UCore.s * 5.0f);
            updatePreview();
        }

        /* access modifiers changed from: package-private */
        public Object[] getArgs() {
            return new Object[]{Float.valueOf(this.slider.getValue())};
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$FilterDialog */
    public static abstract class FilterDialog extends MenuDialog {
        private Filter filter;
        ImagePreview preview = new ImagePreview(PixmapUtils.copy(sourcePixmap()));

        /* access modifiers changed from: package-private */
        public abstract Object[] getArgs();

        public FilterDialog(Filter filter2, String title) {
            super(title);
            setFillParent(true);
            this.filter = filter2;
            DialogClasses.resizeImageCell(getContentTable().add(this.preview));
            getContentTable().row();
            VisLabel label = new VisLabel("Preview");
            label.setColor(Color.GRAY);
            getContentTable().add(label);
            getContentTable().row();
        }

        public void updatePreview() {
            this.filter.apply(sourcePixmap(), pixmap(), getArgs());
            this.preview.image.updateTexture();
        }

        public final void result() {
            this.filter.apply(sourcePixmap(), pixmap(), getArgs());
            Core.i.drawgrid.canvas.drawPixmap(pixmap());
            pixmap().dispose();
        }

        public Pixmap sourcePixmap() {
            return Core.i.drawgrid.canvas.pixmap;
        }

        public Pixmap pixmap() {
            return this.preview.image.pixmap;
        }

        /* access modifiers changed from: package-private */
        public void addSliderChangeListener(VisSlider... sliders) {
            EventListener listener;
            if (Core.i.isImageLarge()) {
                listener = new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }

                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        FilterDialog.this.updatePreview();
                    }
                };
            } else {
                listener = new ChangeListener() {
                    public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                        FilterDialog.this.updatePreview();
                    }
                };
            }
            for (VisSlider slider : sliders) {
                slider.addListener(listener);
            }
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$SymmetryDialog */
    public static class SymmetryDialog extends MenuDialog {
        VisCheckBox hbox = new VisCheckBox("Horizontal Symmetry", Core.i.drawgrid.hSymmetry);
        VisCheckBox vbox = new VisCheckBox("Vertical Symmetry", Core.i.drawgrid.vSymmetry);

        public SymmetryDialog() {
            super("Edit Symmetry");
            this.hbox.getImageStackCell().size(UCore.s * 40.0f);
            this.vbox.getImageStackCell().size(UCore.s * 40.0f);
            Table table = getContentTable();
            table.add(this.vbox).align(8).row();
            table.add(this.hbox).align(8).padTop(UCore.s * 10.0f).padBottom(UCore.s * 10.0f);
        }

        public void result() {
            Core.i.drawgrid.hSymmetry = this.hbox.isChecked();
            Core.i.drawgrid.vSymmetry = this.vbox.isChecked();
        }

        /* access modifiers changed from: package-private */
        public Object[] getArgs() {
            return null;
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$ExportDialog */
    public static class ExportDialog extends MenuDialog {
        VisTextButton button;
        FileHandle file;
        VisValidatableTextField heightfield = new VisValidatableTextField(new StringBuilder(String.valueOf(Core.i.canvas().height())).toString());
        VisValidatableTextField scalefield;
        VisValidatableTextField widthfield = new VisValidatableTextField(new StringBuilder(String.valueOf(Core.i.canvas().width())).toString());

        public ExportDialog(FileHandle file2) {
            super("Export Image");
            this.file = file2;
            this.widthfield.setTextFieldFilter(new VisTextField.TextFieldFilter.DigitsOnlyFilter());
            this.heightfield.setTextFieldFilter(new VisTextField.TextFieldFilter.DigitsOnlyFilter());
            this.scalefield = new VisValidatableTextField("1");
            this.scalefield.setTextFieldFilter(new FloatFilter());
            this.button = new VisTextButton(String.valueOf(file2.name()) + ".png");
            this.scalefield.setProgrammaticChangeEvents(false);
            this.widthfield.setProgrammaticChangeEvents(false);
            this.heightfield.setProgrammaticChangeEvents(false);
            final ChangeListener oklistener = new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    ExportDialog.this.f326ok.setDisabled(!ExportDialog.this.scalefield.isInputValid() || ExportDialog.this.scalefield.getText().isEmpty() || ExportDialog.this.scalefield.getText().replace("0", "").replace(".", "").isEmpty());
                }
            };
            this.button.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    FileChooser.FileHandleFilter fileHandleFilter = FileChooser.pngFilter;
                    final ChangeListener changeListener = oklistener;
                    new FileChooser(fileHandleFilter, false) {
                        public void fileSelected(FileHandle afile) {
                            ExportDialog.this.file = afile;
                            ExportDialog.this.button.setText(String.valueOf(afile.file().getName()) + ".png");
                            changeListener.changed((ChangeListener.ChangeEvent) null, (Actor) null);
                        }
                    }.show(ExportDialog.this.getStage());
                }
            });
            InputValidator sizeValid = new InputValidator() {
                public boolean validateInput(String input) {
                    try {
                        if (Integer.parseInt(input) >= 0) {
                            return true;
                        }
                        return false;
                    } catch (Exception e) {
                        return false;
                    }
                }
            };
            this.widthfield.addValidator(sizeValid);
            this.heightfield.addValidator(sizeValid);
            this.scalefield.addValidator(new InputValidator() {
                public boolean validateInput(String input) {
                    try {
                        return Float.parseFloat(input) <= 100.0f;
                    } catch (Exception e) {
                        return false;
                    }
                }
            });
            this.scalefield.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    try {
                        float f = Float.parseFloat(ExportDialog.this.scalefield.getText());
                        int w = (int) (((float) Core.i.canvas().width()) * f);
                        int h = (int) (((float) Core.i.canvas().height()) * f);
                        ExportDialog.this.widthfield.setText(new StringBuilder(String.valueOf(w)).toString());
                        ExportDialog.this.heightfield.setText(new StringBuilder(String.valueOf(h)).toString());
                    } catch (Exception e) {
                    }
                }
            });
            this.widthfield.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    try {
                        float f = ((float) Integer.parseInt(ExportDialog.this.widthfield.getText())) / ((float) Core.i.canvas().width());
                        ExportDialog.this.heightfield.setText(new StringBuilder(String.valueOf((int) (((float) Core.i.canvas().height()) * f))).toString());
                        ExportDialog.this.scalefield.setText(new StringBuilder(String.valueOf(f)).toString());
                    } catch (Exception e) {
                    }
                }
            });
            this.heightfield.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    try {
                        float f = ((float) Integer.parseInt(ExportDialog.this.heightfield.getText())) / ((float) Core.i.canvas().height());
                        ExportDialog.this.widthfield.setText(new StringBuilder(String.valueOf((int) (((float) Core.i.canvas().width()) * f))).toString());
                        ExportDialog.this.scalefield.setText(new StringBuilder(String.valueOf(f)).toString());
                    } catch (Exception e) {
                    }
                }
            });
            this.scalefield.addListener(oklistener);
            this.scalefield.fire(new ChangeListener.ChangeEvent());
            float sidepad = 20.0f * UCore.s;
            float height = 45.0f * UCore.s;
            float width = 90.0f * UCore.s;
            float isize = 35.0f * UCore.s;
            Table content = getContentTable();
            content.add(new VisImage("icon-file")).size(isize).padTop(8.0f);
            content.add(new VisLabel("File:")).padTop(UCore.s * 15.0f).left();
            content.add(this.button).fill().padTop(UCore.s * 15.0f).height(height).colspan(3).padRight(sidepad);
            content.row();
            content.add(new VisImage("icon-scale")).size(isize);
            content.add(new VisLabel("Scale:")).padTop(UCore.s * 15.0f).left().padBottom(UCore.s * 10.0f);
            content.add(this.scalefield).grow().height(height).padTop(UCore.s * 15.0f).colspan(3).padRight(sidepad).padBottom(UCore.s * 10.0f);
            content.row();
            content.add(new VisImage("icon-resize")).size(isize);
            content.add(new VisLabel("Size:")).left();
            content.add(this.widthfield).height(height).width(width);
            content.add(new VisLabel("x"));
            content.add(this.heightfield).height(height).width(width).padRight(sidepad);
            content.row();
            content.add().height(30.0f * UCore.s);
        }

        public void result() {
            DialogClasses.exportPixmap(PixmapUtils.scale(Core.i.drawgrid.canvas.pixmap, Float.parseFloat(this.scalefield.getText())), this.file);
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$OpenProjectFileDialog */
    public static class OpenProjectFileDialog extends MenuDialog {
        VisTextField directory;
        VisTextField namefield = new VisTextField("");

        public OpenProjectFileDialog() {
            super("Open Project File");
            VisTextButton button = new VisTextButton("...");
            this.directory = new VisTextField("");
            this.directory.setTouchable(Touchable.disabled);
            button.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    new FileChooser(FileChooser.jpegFilter, true) {
                        public void fileSelected(FileHandle file) {
                            OpenProjectFileDialog.this.directory.setText(file.file().getAbsolutePath());
                            SceneUtils.moveTextToSide(OpenProjectFileDialog.this.directory);
                            OpenProjectFileDialog.this.directory.fire(new ChangeListener.ChangeEvent());
                        }
                    }.show(OpenProjectFileDialog.this.getStage());
                }
            });
            ChangeListener oklistener = new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    OpenProjectFileDialog.this.f326ok.setDisabled(OpenProjectFileDialog.this.directory.getText().isEmpty() || OpenProjectFileDialog.this.namefield.getText().isEmpty());
                }
            };
            this.namefield.addListener(oklistener);
            this.directory.addListener(oklistener);
            this.namefield.fire(new ChangeListener.ChangeEvent());
            this.directory.fire(new ChangeListener.ChangeEvent());
            float sidepad = 20.0f * UCore.s;
            float height = 45.0f * UCore.s;
            getContentTable().add(new VisLabel("File:")).padTop(UCore.s * 15.0f).padLeft(sidepad);
            getContentTable().add(this.directory).size(150.0f * UCore.s, UCore.s * 50.0f).padTop(UCore.s * 15.0f);
            getContentTable().add(button).size(UCore.s * 50.0f).padTop(UCore.s * 15.0f).padRight(sidepad);
            getContentTable().row();
            getContentTable().add(new VisLabel("Name:")).padTop(UCore.s * 15.0f).padBottom(UCore.s * 30.0f).padLeft(sidepad);
            getContentTable().add(this.namefield).grow().height(height).padTop(UCore.s * 15.0f).padBottom(UCore.s * 30.0f).colspan(2).padRight(sidepad);
        }

        public void result() {
            long id = Core.i.projectmanager.generateProjectID();
            Gdx.files.absolute(this.directory.getText()).copyTo(Core.i.projectmanager.getFile(id));
            Core.i.projectmanager.openProject(Core.i.projectmanager.loadProject(this.namefield.getText(), id));
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$ScaleDialog */
    public static class ScaleDialog extends MenuDialog {
        VisTextField heightfield = new VisTextField(new StringBuilder(String.valueOf(Core.i.drawgrid.canvas.height())).toString());
        VisTextField widthfield = new VisTextField(new StringBuilder(String.valueOf(Core.i.drawgrid.canvas.width())).toString());
        VisTextField xscalefield;
        VisTextField yscalefield;

        public ScaleDialog() {
            super("Scale Image");
            final float aspectRatio = ((float) Core.i.drawgrid.canvas.width()) / ((float) Core.i.drawgrid.canvas.height());
            this.widthfield.setTextFieldFilter(new VisTextField.TextFieldFilter.DigitsOnlyFilter());
            this.heightfield.setTextFieldFilter(new VisTextField.TextFieldFilter.DigitsOnlyFilter());
            this.xscalefield = new VisTextField("1");
            this.yscalefield = new VisTextField("1");
            this.xscalefield.setTextFieldFilter(new FloatFilter());
            this.yscalefield.setTextFieldFilter(new FloatFilter());
            final VisCheckBox box = new VisCheckBox("Keep Aspect Ratio", true);
            box.getImageStackCell().size(40.0f * UCore.s);
            box.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    ScaleDialog.this.heightfield.setDisabled(box.isChecked());
                    ScaleDialog.this.yscalefield.setDisabled(box.isChecked());
                }
            });
            box.fire(new ChangeListener.ChangeEvent());
            ChangeListener sizeClickListener = new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    VisTextField field = (VisTextField) actor;
                    ScaleDialog.this.checkOkStatus(ScaleDialog.this.widthfield, ScaleDialog.this.heightfield, ScaleDialog.this.xscalefield, ScaleDialog.this.yscalefield);
                    if (!field.getText().isEmpty() && !field.getText().equals(".")) {
                        int value = Integer.parseInt(field.getText());
                        if (box.isChecked()) {
                            if (field == ScaleDialog.this.widthfield) {
                                ScaleDialog.this.heightfield.setText(new StringBuilder(String.valueOf((int) (((float) value) / aspectRatio))).toString());
                            } else {
                                ScaleDialog.this.widthfield.setText(new StringBuilder(String.valueOf((int) (((float) value) * aspectRatio))).toString());
                            }
                        }
                        float xscl = ((float) Integer.parseInt(ScaleDialog.this.widthfield.getText())) / ((float) Core.i.drawgrid.canvas.width());
                        float yscl = ((float) Integer.parseInt(ScaleDialog.this.heightfield.getText())) / ((float) Core.i.drawgrid.canvas.height());
                        ScaleDialog.this.xscalefield.setText(MiscUtils.displayFloat(xscl));
                        ScaleDialog.this.yscalefield.setText(MiscUtils.displayFloat(yscl));
                        ScaleDialog.this.checkOkStatus(ScaleDialog.this.widthfield, ScaleDialog.this.heightfield, ScaleDialog.this.xscalefield, ScaleDialog.this.yscalefield);
                    }
                }
            };
            ChangeListener scaleClickListener = new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    VisTextField field = (VisTextField) actor;
                    ScaleDialog.this.checkOkStatus(ScaleDialog.this.widthfield, ScaleDialog.this.heightfield, ScaleDialog.this.xscalefield, ScaleDialog.this.yscalefield);
                    if (!field.getText().isEmpty() && !field.getText().equals(".")) {
                        float value = Float.parseFloat(field.getText());
                        if (box.isChecked()) {
                            if (field == ScaleDialog.this.xscalefield) {
                                ScaleDialog.this.yscalefield.setText(MiscUtils.displayFloat(value));
                            } else {
                                ScaleDialog.this.xscalefield.setText(MiscUtils.displayFloat(value));
                            }
                        }
                        int width = (int) (Float.parseFloat(ScaleDialog.this.xscalefield.getText()) * ((float) Core.i.drawgrid.canvas.width()));
                        int height = (int) (Float.parseFloat(ScaleDialog.this.yscalefield.getText()) * ((float) Core.i.drawgrid.canvas.height()));
                        ScaleDialog.this.widthfield.setText(new StringBuilder(String.valueOf(width)).toString());
                        ScaleDialog.this.heightfield.setText(new StringBuilder(String.valueOf(height)).toString());
                        ScaleDialog.this.checkOkStatus(ScaleDialog.this.widthfield, ScaleDialog.this.heightfield, ScaleDialog.this.xscalefield, ScaleDialog.this.yscalefield);
                    }
                }
            };
            this.widthfield.addListener(sizeClickListener);
            this.heightfield.addListener(sizeClickListener);
            this.yscalefield.addListener(scaleClickListener);
            this.xscalefield.addListener(scaleClickListener);
            Table table = getContentTable();
            float width = 135.0f * UCore.s;
            float height = 55.0f * UCore.s;
            float pad = 30.0f * UCore.s;
            float right = 80.0f * UCore.s;
            table.add().height(UCore.s * 30.0f);
            table.row();
            table.add(new VisLabel("Size: "));
            table.add(this.widthfield).size(width, height);
            table.add(new VisLabel("x"));
            table.add(this.heightfield).size(width, height).padRight(right);
            table.row();
            table.add(new VisLabel("Scale: ")).padTop(pad);
            table.add(this.xscalefield).size(width, height).padTop(pad);
            table.add(new VisLabel("x")).padTop(pad);
            table.add(this.yscalefield).size(width, height).padTop(pad).padRight(right);
            table.row();
            table.add(box).colspan(4).padTop(15.0f * UCore.s);
            table.row();
            table.add().height(UCore.s * 30.0f);
        }

        public void result() {
            try {
                Core.i.drawgrid.setCanvas(new PixelCanvas(PixmapUtils.scale(Core.i.drawgrid.canvas.pixmap, Float.parseFloat(this.xscalefield.getText()), Float.parseFloat(this.yscalefield.getText()))), true);
                Core.i.checkGridResize();
                Core.i.updateToolColor();
            } catch (Exception e) {
                e.printStackTrace();
                DialogClasses.showError(getStage(), e);
            }
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$ShiftDialog */
    public static class ShiftDialog extends MenuDialog {
        ShiftImagePreview preview = new ShiftImagePreview();

        public ShiftDialog() {
            super("Shift Image");
            VisTable table = new VisTable();
            table.setClip(true);
            table.add(this.preview).grow().pad(2.0f);
            DialogClasses.resizeImageCell(getContentTable().add(table));
            getContentTable().row();
        }

        /* renamed from: io.anuke.novix.ui.DialogClasses$ShiftDialog$ShiftController */
        static class ShiftController extends Group {
            VisImage down = new VisImage(VisUI.getSkin().getDrawable("icon-arrow-down"));
            VisImage left = new VisImage(VisUI.getSkin().getDrawable("icon-arrow-left"));
            VisImage right = new VisImage(VisUI.getSkin().getDrawable("icon-arrow-right"));

            /* renamed from: up */
            VisImage f328up = new VisImage(VisUI.getSkin().getDrawable("icon-arrow-up"));

            public ShiftController() {
                final Color color = Color.PURPLE;
                this.f328up.setColor(color);
                this.down.setColor(color);
                this.left.setColor(color);
                this.right.setColor(color);
                float size = 70.0f * UCore.s;
                this.left.setSize(size, size);
                this.right.setSize(size, size);
                this.f328up.setSize(size, size);
                this.down.setSize(size, size);
                InputListener colorlistener = new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        event.getTarget().setColor(Color.CORAL);
                        return true;
                    }

                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        event.getTarget().setColor(color);
                    }
                };
                this.f328up.addListener(colorlistener);
                this.down.addListener(colorlistener);
                this.left.addListener(colorlistener);
                this.right.addListener(colorlistener);
                this.f328up.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        ShiftController.this.shifted(0, 1);
                    }
                });
                this.down.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        ShiftController.this.shifted(0, -1);
                    }
                });
                this.left.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        ShiftController.this.shifted(-1, 0);
                    }
                });
                this.right.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        ShiftController.this.shifted(1, 0);
                    }
                });
                addActor(this.left);
                addActor(this.right);
                addActor(this.f328up);
                addActor(this.down);
            }

            public void draw(Batch batch, float alpha) {
                super.draw(batch, alpha);
                float centerx = getX() + (getWidth() / 2.0f);
                float centery = getY() + (getHeight() / 2.0f);
                float hwidth = getWidth() / 2.5f;
                float hheight = getHeight() / 2.5f;
                this.f328up.setPosition(centerx, centery + hheight, 1);
                this.down.setPosition(centerx, centery - hheight, 1);
                this.left.setPosition(centerx - hwidth, centery, 1);
                this.right.setPosition(centerx + hwidth, centery, 1);
            }

            public void shifted(int x, int y) {
            }
        }

        /* renamed from: io.anuke.novix.ui.DialogClasses$ShiftDialog$ShiftImagePreview */
        class ShiftImagePreview extends Group {
            ShiftController controller;
            ShiftedImage image;
            Stack stack = new Stack();

            public ShiftImagePreview() {
                AlphaImage alpha = new AlphaImage((float) Core.i.drawgrid.canvas.width(), (float) Core.i.drawgrid.canvas.height());
                GridImage grid = new GridImage(Core.i.drawgrid.canvas.width(), Core.i.drawgrid.canvas.height());
                this.image = new ShiftedImage(Core.i.drawgrid.canvas.texture);
                this.controller = new ShiftController() {
                    public void shifted(int x, int y) {
                        ShiftImagePreview.this.image.offsetx += x;
                        ShiftImagePreview.this.image.offsety += y;
                    }
                };
                this.stack.add(alpha);
                this.stack.add(this.image);
                if (Core.i.prefs.getBoolean("grid")) {
                    this.stack.add(grid);
                }
                this.stack.add(this.controller);
                addActor(this.stack);
            }

            public void draw(Batch batch, float alpha) {
                super.draw(batch, alpha);
                this.stack.setBounds(0.0f, 0.0f, getWidth(), getHeight());
                Color color = Color.CORAL.cpy();
                color.a = alpha;
                batch.setColor(color);
                MiscUtils.drawBorder(batch, getX(), getY(), getWidth(), getHeight(), 2, 2);
                batch.setColor(Color.WHITE);
            }
        }

        public void result() {
            PixelCanvas canvas = Core.i.drawgrid.canvas;
            Pixmap temp = PixmapUtils.copy(canvas.pixmap);
            int offsetx = this.preview.image.offsetx;
            int offsety = this.preview.image.offsety;
            temp.setBlending(Pixmap.Blending.None);
            for (int x = 0; x < canvas.width(); x++) {
                for (int y = 0; y < canvas.height(); y++) {
                    if (x >= canvas.width() + offsetx || y >= canvas.height() + offsety || x < offsetx || y < offsety) {
                        canvas.drawPixelBlendless(x, y, 0);
                    }
                    canvas.drawPixelBlendless(x + offsetx, y + offsety, temp.getPixel(x, (temp.getHeight() - 1) - y));
                }
            }
            temp.setBlending(Pixmap.Blending.SourceOver);
            canvas.updateAndPush();
            this.preview.image.offsetx = 0;
            this.preview.image.offsety = 0;
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$CropDialog */
    public static class CropDialog extends MenuDialog {
        CropImagePreview preview;

        public CropDialog() {
            super("Crop Image");
            Table contentTable = getContentTable();
            CropImagePreview cropImagePreview = new CropImagePreview();
            this.preview = cropImagePreview;
            DialogClasses.resizeImageCell(contentTable.add(cropImagePreview));
            getContentTable().row();
        }

        public void result() {
            int x = Math.min(this.preview.controller.selx1, this.preview.controller.selx2);
            int x2 = Math.max(this.preview.controller.selx1, this.preview.controller.selx2);
            int y = Math.min(this.preview.controller.sely1, this.preview.controller.sely2);
            Core.i.drawgrid.setCanvas(new PixelCanvas(PixmapUtils.crop(Core.i.drawgrid.canvas.pixmap, x, y, x2 - x, Math.max(this.preview.controller.sely1, this.preview.controller.sely2) - y)), true);
            Core.i.updateToolColor();
        }

        /* renamed from: io.anuke.novix.ui.DialogClasses$CropDialog$CropImagePreview */
        static class CropImagePreview extends ImagePreview {
            CropController controller;

            public CropImagePreview() {
                super(Core.i.drawgrid.canvas.pixmap);
                Stack stack = this.stack;
                CropController cropController = new CropController(this);
                this.controller = cropController;
                stack.add(cropController);
            }
        }

        /* renamed from: io.anuke.novix.ui.DialogClasses$CropDialog$CropController */
        static class CropController extends Actor {
            CropPoint[] croppoints = new CropPoint[10];
            Vector2[] points = {new Vector2(), new Vector2(), new Vector2(), new Vector2(), new Vector2(), new Vector2(), new Vector2(), new Vector2()};
            CropImagePreview preview;
            int selx1;
            int selx2;
            int sely1;
            int sely2;
            /* access modifiers changed from: private */
            public float xscale;
            /* access modifiers changed from: private */
            public float yscale;

            /* renamed from: io.anuke.novix.ui.DialogClasses$CropDialog$CropController$CropPoint */
            class CropPoint {
                int point = -1;
                int pointer;

                public CropPoint(int pointer2) {
                    this.pointer = pointer2;
                }
            }

            public CropController(CropImagePreview preview2) {
                this.preview = preview2;
                for (int i = 0; i < 10; i++) {
                    this.croppoints[i] = new CropPoint(i);
                }
                int pwidth = preview2.image.pixmap.getWidth();
                int pheight = preview2.image.pixmap.getHeight();
                this.selx1 = (pwidth / 2) - (pwidth / 3);
                this.sely1 = (pheight / 2) - (pheight / 3);
                this.selx2 = (pwidth / 2) + (pwidth / 3);
                this.sely2 = (pheight / 2) + (pheight / 3);
                addListener(new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        for (int i = 0; i < 8; i++) {
                            if (CropController.this.points[i].dst(x, y) < 30.0f) {
                                CropController.this.croppoints[pointer].point = i;
                                touch(x, y, pointer);
                                return true;
                            }
                        }
                        return false;
                    }

                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        CropController.this.croppoints[pointer].point = -1;
                    }

                    public void touchDragged(InputEvent event, float x, float y, int pointer) {
                        touch(x, y, pointer);
                    }

                    /* access modifiers changed from: package-private */
                    public void touch(float x, float y, int pointer) {
                        CropPoint point = CropController.this.croppoints[pointer];
                        CropController.this.points[point.point].set(x, y);
                        Vector2 vector = CropController.this.points[point.point];
                        vector.x = Mathf.clamp(vector.x, CropController.this.getX(), CropController.this.getX() + CropController.this.getWidth());
                        vector.y = Mathf.clamp(vector.y, CropController.this.getY(), CropController.this.getY() + CropController.this.getHeight());
                        if (point.point == 4) {
                            CropController.this.points[0].x = vector.x;
                            CropController.this.points[2].x = vector.x;
                        } else if (point.point == 5) {
                            CropController.this.points[0].y = vector.y;
                            CropController.this.points[1].y = vector.y;
                        } else if (point.point == 6) {
                            CropController.this.points[1].x = vector.x;
                            CropController.this.points[3].x = vector.x;
                        } else if (point.point == 7) {
                            CropController.this.points[3].y = vector.y;
                            CropController.this.points[2].y = vector.y;
                        }
                        if (point.point == 1) {
                            CropController.this.points[0].y = vector.y;
                            CropController.this.points[3].x = vector.x;
                        } else if (point.point == 2) {
                            CropController.this.points[0].x = vector.x;
                            CropController.this.points[3].y = vector.y;
                        }
                        CropController.this.selx1 = (int) (((CropController.this.points[0].x - CropController.this.getX()) / CropController.this.xscale) + 0.5f);
                        CropController.this.sely1 = (int) (((CropController.this.points[0].y - CropController.this.getY()) / CropController.this.yscale) + 0.5f);
                        CropController.this.selx2 = (int) (((CropController.this.points[3].x - CropController.this.getX()) / CropController.this.xscale) + 0.5f);
                        CropController.this.sely2 = (int) (((CropController.this.points[3].y - CropController.this.getY()) / CropController.this.yscale) + 0.5f);
                    }
                });
            }

            public void draw(Batch batch, float alpha) {
                Color color;
                this.xscale = this.preview.getWidth() / ((float) this.preview.image.pixmap.getWidth());
                this.yscale = this.preview.getHeight() / ((float) this.preview.image.pixmap.getHeight());
                updatePoints();
                TextureRegion region = VisUI.getSkin().getAtlas().findRegion("white");
                int s = 1;
                if (this.selx1 > this.selx2 && this.sely1 > this.sely2) {
                    s = -1;
                }
                batch.setColor(0.0f, 0.0f, 0.0f, 0.5f);
                MiscUtils.setBatchAlpha(batch, alpha);
                MiscUtils.drawMasked(batch, region, getX(), getY(), getWidth(), getHeight(), (((float) this.selx1) * this.xscale) + getX(), (((float) this.sely1) * this.yscale) + getY(), this.xscale * ((float) (this.selx2 - this.selx1)), this.yscale * ((float) (this.sely2 - this.sely1)));
                batch.setColor(Color.PURPLE);
                MiscUtils.setBatchAlpha(batch, alpha);
                MiscUtils.drawBorder(batch, getX() + (((float) this.selx1) * this.xscale), getY() + (((float) this.sely1) * this.yscale), ((float) (this.selx2 - this.selx1)) * this.xscale, ((float) (this.sely2 - this.sely1)) * this.yscale, s * 4, s * 2);
                Color color2 = Color.CORAL;
                Color select = Color.PURPLE;
                for (int i = 0; i < 8; i++) {
                    boolean selected = false;
                    CropPoint[] cropPointArr = this.croppoints;
                    int length = cropPointArr.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (cropPointArr[i2].point == i) {
                            selected = true;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (selected) {
                        color = select;
                    } else {
                        color = color2;
                    }
                    batch.setColor(color);
                    MiscUtils.setBatchAlpha(batch, alpha);
                    batch.draw(region, this.points[i].x - ((float) 20), this.points[i].y - ((float) 20), (float) 40, (float) 40);
                }
            }

            /* access modifiers changed from: package-private */
            public void updatePoints() {
                float width = ((float) (this.selx2 - this.selx1)) * this.xscale;
                float height = ((float) (this.sely2 - this.sely1)) * this.yscale;
                this.points[0].set(getX() + (((float) this.selx1) * this.xscale), getY() + (((float) this.sely1) * this.yscale));
                this.points[1].set(getX() + (((float) this.selx1) * this.xscale) + width, getY() + (((float) this.sely1) * this.yscale));
                this.points[2].set(getX() + (((float) this.selx1) * this.xscale), getY() + (((float) this.sely1) * this.yscale) + height);
                this.points[3].set(getX() + (((float) this.selx1) * this.xscale) + width, getY() + (((float) this.sely1) * this.yscale) + height);
                this.points[4].set(this.points[0].cpy().add(this.points[2]).scl(0.5f));
                this.points[5].set(this.points[0].cpy().add(this.points[1]).scl(0.5f));
                this.points[6].set(this.points[1].cpy().add(this.points[3]).scl(0.5f));
                this.points[7].set(this.points[3].cpy().add(this.points[2]).scl(0.5f));
            }
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$ClearDialog */
    public static class ClearDialog extends MenuDialog {
        public ClearDialog() {
            super("Confirm Clear Image");
            VisLabel label = new VisLabel("Are you sure you want\nto clear the image?");
            label.setAlignment(1);
            getContentTable().center().add(label).pad(40.0f * UCore.s).align(1);
        }

        public void result() {
            PixelCanvas canvas = Core.i.drawgrid.canvas;
            float alpha = canvas.getAlpha();
            canvas.setAlpha(1.0f);
            for (int x = 0; x < canvas.width(); x++) {
                for (int y = 0; y < canvas.height(); y++) {
                    canvas.erasePixelFullAlpha(x, y);
                }
            }
            canvas.pushActions();
            canvas.updateTexture();
            canvas.setAlpha(alpha);
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$ColorFillDialog */
    public static class ColorFillDialog extends MenuDialog {
        ColorBox selected = new ColorBox(Core.i.selectedColor());

        public ColorFillDialog() {
            super("Fill Color");
            this.selected.addSelectListener();
            final ColorChooseDialog dialog = new ColorChooseDialog(new Runnable() {
                public void run() {
                }
            }) {
                public void colorChanged() {
                    ColorFillDialog.this.selected.setColor(this.picker.getSelectedColor());
                }
            };
            this.selected.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    ColorFillDialog.this.selected = (ColorBox) event.getTarget();
                    dialog.picker.setSelectedColor(event.getTarget().getColor());
                    dialog.show(Core.i.stage);
                }
            });
            VisLabel label = new VisLabel("Fill Color:");
            label.setAlignment(1);
            getContentTable().center().add(label).pad(UCore.s * 20.0f).padBottom(0.0f).align(1).row();
            getContentTable().add(this.selected).size(60.0f * UCore.s).padBottom(20.0f);
        }

        public void result() {
            PixelCanvas canvas = Core.i.drawgrid.canvas;
            float alpha = canvas.getAlpha();
            int color = Color.rgba8888(this.selected.getColor());
            canvas.setAlpha(1.0f);
            for (int x = 0; x < canvas.width(); x++) {
                for (int y = 0; y < canvas.height(); y++) {
                    canvas.drawPixelBlendless(x, y, color);
                }
            }
            canvas.pushActions();
            canvas.updateTexture();
            canvas.setAlpha(alpha);
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$InputDialog */
    public static class InputDialog extends MenuDialog {
        protected VisTextField textfield;

        public InputDialog(String title, String fieldtext, String text) {
            super(title);
            this.textfield = new VisTextField(fieldtext);
            getContentTable().center().add(new VisLabel(text));
            getContentTable().center().add(this.textfield).pad(20.0f * UCore.s).padLeft(0.0f).size(160.0f * UCore.s, 40.0f * UCore.s);
        }

        public final void result() {
            result(this.textfield.getText());
        }

        public void result(String string) {
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$NumberInputDialog */
    public static class NumberInputDialog extends MenuDialog {
        protected VisTextField numberfield;

        public NumberInputDialog(String title, String fieldtext, String text) {
            super(title);
            this.numberfield = new VisTextField(fieldtext);
            this.numberfield.setTextFieldFilter(new VisTextField.TextFieldFilter.DigitsOnlyFilter());
            getContentTable().center().add(new VisLabel(text));
            getContentTable().center().add(this.numberfield).pad(20.0f * UCore.s).padLeft(0.0f).size(160.0f * UCore.s, 40.0f * UCore.s);
        }

        public final void result() {
            if (!this.numberfield.getText().isEmpty()) {
                result(Integer.parseInt(this.numberfield.getText()));
            }
        }

        public void result(int i) {
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$InfoDialog */
    public static class InfoDialog extends MenuDialog {
        public InfoDialog(String title, String info) {
            super(title);
            getButtonsTable().clearChildren();
            getButtonsTable().add(this.f326ok).size(130.0f * UCore.s, 60.0f * UCore.s).padBottom(3.0f * UCore.s);
            this.f326ok.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    InfoDialog.this.hide();
                }
            });
            VisLabel label = new VisLabel(info);
            label.setAlignment(1);
            label.setWrap(true);
            getContentTable().add(label).width(400.0f * UCore.s).padTop(UCore.s * 20.0f).padBottom(UCore.s * 20.0f);
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$ErrorDialog */
    public static class ErrorDialog extends MenuDialog {
        public ErrorDialog(String info, String extra) {
            super("Error");
            getButtonsTable().clear();
            getButtonsTable().add(this.f326ok).size(130.0f * UCore.s, 60.0f * UCore.s).padBottom(3.0f * UCore.s);
            this.f326ok.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    ErrorDialog.this.hide();
                }
            });
            VisLabel label = new VisLabel(info);
            label.setAlignment(1);
            label.setWrap(true);
            VisLabel extralabel = new VisLabel(extra);
            extralabel.setColor(Color.RED);
            extralabel.setAlignment(1);
            extralabel.setWrap(true);
            getContentTable().add(label).width(400.0f).padTop(UCore.s * 20.0f).padBottom(UCore.s * 5.0f).row();
            getContentTable().add(extralabel).width(400.0f).padTop(UCore.s * 5.0f).padBottom(UCore.s * 20.0f);
        }
    }

    public static void showInfo(Stage stage, String info) {
        new InfoDialog("Info", info).show(stage);
    }

    public static void showError(Stage stage, String info, String details) {
        new ErrorDialog(info, details).show(stage);
    }

    public static void showError(Stage stage, String info) {
        new ErrorDialog(info, "").show(stage);
    }

    public static void showError(Stage stage, Exception e) {
        showError(stage, "Failed to write image!", e);
    }

    public static void showError(Stage stage, String title, Exception e) {
        showError(stage, title, convertToString(e));
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$ConfirmDialog */
    public static class ConfirmDialog extends MenuDialog {
        public ConfirmDialog(String title, String text) {
            super(title);
            getContentTable().center().add(new VisLabel(text)).pad(20.0f * UCore.s);
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$MenuDialog */
    public static abstract class MenuDialog extends BaseDialog {
        protected VisTextButton cancel;

        /* renamed from: ok */
        protected VisTextButton f326ok;

        public MenuDialog(String title) {
            super(title);
            addCloseButton();
            addButtons();
        }

        /* access modifiers changed from: package-private */
        public void addButtons() {
            this.cancel = new VisTextButton("Cancel");
            this.f326ok = new VisTextButton("OK");
            setObject(this.f326ok, true);
            setObject(this.cancel, false);
            getButtonsTable().add(this.cancel).size(UCore.s * 130.0f, UCore.s * 60.0f);
            getButtonsTable().add(this.f326ok).size(UCore.s * 130.0f, UCore.s * 60.0f);
        }

        /* access modifiers changed from: protected */
        public void result(Object object) {
            if (((Boolean) object).booleanValue()) {
                result();
            } else {
                cancel();
            }
        }

        public void cancel() {
        }

        public void result() {
        }

        public void checkOkStatus(VisTextField... fields) {
            for (VisTextField field : fields) {
                if (field.getText().replace("0", "").isEmpty()) {
                    this.f326ok.setDisabled(true);
                    return;
                }
            }
            this.f326ok.setDisabled(false);
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$BaseDialog */
    public static abstract class BaseDialog extends VisDialog {
        public BaseDialog(String title) {
            super(title, "dialog");
            setMovable(false);
            addPadding(this);
        }

        public static void addPadding(Table table) {
            float v = (UCore.s - 1.0f) * 10.0f;
            table.padTop(table.getPadTop() + v);
            table.padBottom(table.getPadBottom() + v);
            table.padLeft(table.getPadLeft() + v);
            table.padRight(table.getPadRight() + v);
        }

        public Cell<Separator> addTitleSeperator() {
            getTitleTable().row();
            getTitleTable().getCells().first().padBottom(UCore.s * 3.0f);
            Cell<Separator> cell = getTitleTable().add(new Separator()).expandX().fillX().height(UCore.s * 4.0f).padTop(UCore.s * 3.0f).colspan(2).padBottom(UCore.s * 3.0f);
            cell.padBottom(10.0f * UCore.s);
            padTop(getPadTop() + (17.0f * UCore.s));
            return cell;
        }

        public void addCloseButton() {
            Label titleLabel = getTitleLabel();
            Table titleTable = getTitleTable();
            VisImageButton closeButton = new VisImageButton("close-window");
            closeButton.getImageCell().size(40.0f * UCore.s);
            titleTable.add(closeButton).padRight(-getPadRight()).size(50.0f * UCore.s);
            closeButton.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    BaseDialog.this.close();
                }
            });
            closeButton.addListener(new ClickListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    event.cancel();
                    return true;
                }
            });
            if (titleLabel.getLabelAlign() == 1 && titleTable.getChildren().size == 2) {
                titleTable.getCell(titleLabel).padLeft(closeButton.getWidth() * 2.0f);
            }
            padTop(52.0f * UCore.s);
            padRight(getPadRight() + 1.0f);
            pack();
        }

        public void hide() {
            super.hide();
            Gdx.input.setOnscreenKeyboardVisible(false);
        }

        public void close() {
            super.close();
            Gdx.input.setOnscreenKeyboardVisible(false);
        }
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$ScaledDrawable */
    static class ScaledDrawable extends TextureRegionDrawable {
        public ScaledDrawable(Drawable drawable) {
            super(((TextureRegionDrawable) drawable).getRegion());
        }

        public float getMinWidth() {
            return super.getMinWidth() * UCore.s;
        }

        public float getMinHeight() {
            return super.getMinHeight() * UCore.s;
        }
    }

    public static void scaleSlider(Slider brushslider) {
        brushslider.getStyle().knob = new ScaledDrawable(brushslider.getStyle().knob);
        brushslider.getStyle().knobOver = new ScaledDrawable(brushslider.getStyle().knobOver);
        brushslider.getStyle().knobDown = new ScaledDrawable(brushslider.getStyle().knobDown);
        brushslider.getStyle().background = new ScaledDrawable(brushslider.getStyle().background);
    }

    private static String convertToString(Exception e) {
        if (e.getCause() != null) {
            return convertToString(e.getCause());
        }
        return convertToString((Throwable) e);
    }

    private static String convertToString(Throwable e) {
        if (e.getMessage().toLowerCase().contains("permission denied")) {
            return "Error: Permission denied.";
        }
        return String.valueOf(e.getClass().getSimpleName()) + ": " + e.getMessage();
    }

    public static void exportPixmap(Pixmap pixmap, FileHandle file) {
        try {
            if (!file.extension().equalsIgnoreCase("png")) {
                file = file.parent().child(String.valueOf(file.nameWithoutExtension()) + ".png");
            }
            PixmapIO.writePNG(file, pixmap);
            showInfo(Core.i.stage, "Image exported to " + file + ".");
        } catch (Exception e) {
            e.printStackTrace();
            showError(Core.i.stage, e);
        }
    }

    static Cell<? extends Actor> resizeImageCell(Cell<? extends Actor> cell) {
        float ratio = ((float) Core.i.drawgrid.canvas.width()) / ((float) Core.i.drawgrid.canvas.height());
        float isize = 400.0f * UCore.s;
        float width = isize;
        float height = isize / ratio;
        if (height > width) {
            height = isize;
            width = isize * ratio;
        }
        float sidePad = (isize - width) / 2.0f;
        float topPad = (isize - height) / 2.0f;
        return cell.size(width, height).padTop(3.0f + topPad).padBottom(topPad).padLeft(sidePad + 2.0f).padRight(sidePad + 2.0f);
    }

    /* renamed from: io.anuke.novix.ui.DialogClasses$FloatFilter */
    static class FloatFilter implements VisTextField.TextFieldFilter {
        FloatFilter() {
        }

        public boolean acceptChar(VisTextField textField, char c) {
            return Character.isDigit(c) || (c == '.' && !textField.getText().contains("."));
        }
    }
}
