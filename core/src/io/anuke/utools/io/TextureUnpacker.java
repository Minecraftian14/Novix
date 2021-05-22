package io.anuke.utools.io;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;

/* renamed from: io.anuke.utools.io.TextureUnpacker */
public class TextureUnpacker {
    private static final String ATLAS_FILE_EXTENSION = ".atlas";
    private static final String DEFAULT_OUTPUT_PATH = "output";
    private static final String HELP = "Usage: atlasFile [imageDir] [outputDir]";
    private static final int NINEPATCH_PADDING = 1;
    private static final String OUTPUT_TYPE = "png";

    private int parseArguments(String[] args) {
        int numArgs = args.length;
        if (numArgs < 1) {
            return 0;
        }
        boolean extension = args[0].substring(args[0].length() - ATLAS_FILE_EXTENSION.length()).equals(ATLAS_FILE_EXTENSION);
        boolean directory = true;
        if (numArgs >= 2) {
            directory = true & checkDirectoryValidity(args[1]);
        }
        if (numArgs == 3) {
            directory &= checkDirectoryValidity(args[2]);
        }
        if (!extension || !directory) {
            numArgs = 0;
        }
        return numArgs;
    }

    private boolean checkDirectoryValidity(String directory) {
        try {
            new File(directory).getCanonicalPath();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void splitAtlas(TextureAtlas.TextureAtlasData atlas, String outputDir) {
        BufferedImage splitImage;
        String extension;
        File outputDirFile = new File(outputDir);
        if (!outputDirFile.exists()) {
            outputDirFile.mkdirs();
            System.out.println(String.format("Creating directory: %s", new Object[]{outputDirFile.getPath()}));
        }
        Iterator<TextureAtlas.TextureAtlasData.Page> it = atlas.getPages().iterator();
        while (it.hasNext()) {
            TextureAtlas.TextureAtlasData.Page page = it.next();
            BufferedImage img = null;
            try {
                img = ImageIO.read(page.textureFile.file());
            } catch (IOException e) {
                printExceptionAndExit(e);
            }
            Iterator<TextureAtlas.TextureAtlasData.Region> it2 = atlas.getRegions().iterator();
            while (it2.hasNext()) {
                TextureAtlas.TextureAtlasData.Region region = it2.next();
                System.out.println(String.format("Processing image for %s: x[%s] y[%s] w[%s] h[%s], rotate[%s]", new Object[]{region.name, Integer.valueOf(region.left), Integer.valueOf(region.top), Integer.valueOf(region.width), Integer.valueOf(region.height), Boolean.valueOf(region.rotate)}));
                if (region.page == page) {
                    if (region.splits == null) {
                        splitImage = extractImage(img, region, outputDirFile, 0);
                        extension = OUTPUT_TYPE;
                    } else {
                        splitImage = extractNinePatch(img, region, outputDirFile);
                        extension = String.format("9.%s", new Object[]{OUTPUT_TYPE});
                    }
                    Object[] objArr = new Object[2];
                    objArr[0] = region.index == -1 ? region.name : String.valueOf(region.name) + "_" + region.index;
                    objArr[1] = extension;
                    File imgOutput = new File(outputDirFile, String.format("%s.%s", objArr));
                    File imgDir = imgOutput.getParentFile();
                    if (!imgDir.exists()) {
                        System.out.println(String.format("Creating directory: %s", new Object[]{imgDir.getPath()}));
                        imgDir.mkdirs();
                    }
                    try {
                        ImageIO.write(splitImage, OUTPUT_TYPE, imgOutput);
                    } catch (IOException e2) {
                        printExceptionAndExit(e2);
                    }
                }
            }
        }
    }

    private BufferedImage extractImage(BufferedImage page, TextureAtlas.TextureAtlasData.Region region, File outputDirFile, int padding) {
        BufferedImage splitImage;
        if (region.rotate) {
            BufferedImage srcImage = page.getSubimage(region.left, region.top, region.height, region.width);
            splitImage = new BufferedImage(region.width, region.height, page.getType());
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(90.0d));
            transform.translate(0.0d, (double) (-region.width));
            new AffineTransformOp(transform, 2).filter(srcImage, splitImage);
        } else {
            splitImage = page.getSubimage(region.left, region.top, region.width, region.height);
        }
        if (padding <= 0) {
            return splitImage;
        }
        BufferedImage paddedImage = new BufferedImage(splitImage.getWidth() + (padding * 2), splitImage.getHeight() + (padding * 2), page.getType());
        Graphics2D g2 = paddedImage.createGraphics();
        g2.drawImage(splitImage, padding, padding, (ImageObserver) null);
        g2.dispose();
        return paddedImage;
    }

    private BufferedImage extractNinePatch(BufferedImage page, TextureAtlas.TextureAtlasData.Region region, File outputDirFile) {
        BufferedImage splitImage = extractImage(page, region, outputDirFile, 1);
        Graphics2D g2 = splitImage.createGraphics();
        g2.setColor(Color.BLACK);
        int startX = region.splits[0] + 1;
        int endX = ((region.width - region.splits[1]) + 1) - 1;
        int startY = region.splits[2] + 1;
        int endY = ((region.height - region.splits[3]) + 1) - 1;
        if (endX >= startX) {
            g2.drawLine(startX, 0, endX, 0);
        }
        if (endY >= startY) {
            g2.drawLine(0, startY, 0, endY);
        }
        if (region.pads != null) {
            int padStartX = region.pads[0] + 1;
            int padEndX = ((region.width - region.pads[1]) + 1) - 1;
            int padStartY = region.pads[2] + 1;
            int padEndY = ((region.height - region.pads[3]) + 1) - 1;
            g2.drawLine(padStartX, splitImage.getHeight() - 1, padEndX, splitImage.getHeight() - 1);
            g2.drawLine(splitImage.getWidth() - 1, padStartY, splitImage.getWidth() - 1, padEndY);
        }
        g2.dispose();
        return splitImage;
    }

    private void printExceptionAndExit(Exception e) {
        e.printStackTrace();
        System.exit(1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x005b, code lost:
        r4 = r11[1];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x005d, code lost:
        r1 = r11[0];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0021, code lost:
        r3 = new java.io.File(r1).getParentFile().getAbsolutePath();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x002e, code lost:
        if (r4 != null) goto L_0x0031;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0030, code lost:
        r4 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0031, code lost:
        if (r5 != null) goto L_0x003e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0033, code lost:
        r5 = new java.io.File(r3, DEFAULT_OUTPUT_PATH).getAbsolutePath();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x003e, code lost:
        r6.splitAtlas(new com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData(new com.badlogic.gdx.files.FileHandle(r1), new com.badlogic.gdx.files.FileHandle(r4), false), r5);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void main(java.lang.String[] r11) {
        /*
            r10 = 2
            r8 = 1
            r9 = 0
            io.anuke.utools.io.TextureUnpacker r6 = new io.anuke.utools.io.TextureUnpacker
            r6.<init>()
            r1 = 0
            r4 = 0
            r5 = 0
            r7 = 3
            java.lang.String[] r11 = new java.lang.String[r7]
            java.lang.String r7 = "/home/anuke/Projects/Koru/core/assets/ui/uiskin.atlas"
            r11[r9] = r7
            java.lang.String r7 = "/home/anuke/Projects/Koru/core/assets/ui/"
            r11[r8] = r7
            java.lang.String r7 = "/home/anuke/Projects/Koru/core/assets/ui/out"
            r11[r10] = r7
            int r7 = r6.parseArguments(r11)
            switch(r7) {
                case 0: goto L_0x0051;
                case 1: goto L_0x005d;
                case 2: goto L_0x005b;
                case 3: goto L_0x0059;
                default: goto L_0x0021;
            }
        L_0x0021:
            java.io.File r2 = new java.io.File
            r2.<init>(r1)
            java.io.File r7 = r2.getParentFile()
            java.lang.String r3 = r7.getAbsolutePath()
            if (r4 != 0) goto L_0x0031
            r4 = r3
        L_0x0031:
            if (r5 != 0) goto L_0x003e
            java.io.File r7 = new java.io.File
            java.lang.String r8 = "output"
            r7.<init>(r3, r8)
            java.lang.String r5 = r7.getAbsolutePath()
        L_0x003e:
            com.badlogic.gdx.graphics.g2d.TextureAtlas$TextureAtlasData r0 = new com.badlogic.gdx.graphics.g2d.TextureAtlas$TextureAtlasData
            com.badlogic.gdx.files.FileHandle r7 = new com.badlogic.gdx.files.FileHandle
            r7.<init>((java.lang.String) r1)
            com.badlogic.gdx.files.FileHandle r8 = new com.badlogic.gdx.files.FileHandle
            r8.<init>((java.lang.String) r4)
            r0.<init>(r7, r8, r9)
            r6.splitAtlas(r0, r5)
        L_0x0050:
            return
        L_0x0051:
            java.io.PrintStream r7 = java.lang.System.out
            java.lang.String r8 = "Usage: atlasFile [imageDir] [outputDir]"
            r7.println(r8)
            goto L_0x0050
        L_0x0059:
            r5 = r11[r10]
        L_0x005b:
            r4 = r11[r8]
        L_0x005d:
            r1 = r11[r9]
            goto L_0x0021
        */
        throw new UnsupportedOperationException("Method not decompiled: p003io.anuke.utools.p005io.TextureUnpacker.main(java.lang.String[]):void");
    }
}
