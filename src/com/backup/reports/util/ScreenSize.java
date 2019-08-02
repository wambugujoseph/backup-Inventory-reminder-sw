package com.backup.reports.util;

import java.awt.*;

/**
 * Resize the window to 90% height and width of the Screen size
 */
public class ScreenSize {
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private int width =(int) (gd.getDisplayMode().getWidth()* 0.9);
    private int height = (int) (gd.getDisplayMode().getHeight() *0.9);

    public static int GetPrefScreenWidth() {
        return new ScreenSize().width;
    }

    public static int GetPrefScreenHeight() {
        return new ScreenSize().height;
    }
}
