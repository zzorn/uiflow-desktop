package org.uiflow.desktop.drawcontext;

import org.flowutils.Ranged;

/**
 * Implementation neutral drawing context.
 *
 * By default one unit is one pixel.
 *
 * @param <COLOR> the color class used by this DrawContext.
 * @param <FONT>  the font class used by this DrawContext.
 * @param <IMAGE> the image class used by this DrawContext.
 */
public interface DrawContext<COLOR, FONT, IMAGE> {

    float getWidth();
    float getHeight();

/*
    float getPixelWidth();
    float getPixelHeight();
*/

    float getPixelsPerInchX();
    float getPixelsPerInchY();

    float getGap();
    float getSmallGap();
    float getLargeGap();

    FONT getDefaultFont();
    FONT getFont(String fontName);
    FONT getFont(String fontName, float fontSize);
    // TODO: Maybe add function that gets font with any variants applied (bold, italic, etc)

    float getFontHeight(FONT font);
    float getFontHeightBaselineToBottom(FONT font);
    float getFontHeightBaselineToTop(FONT font);
    float getTextWidth(FONT font, String text);

    /**
     * @return if true, hints that edges of drawn shapes should be smoothed with antialiasing.
     */
    boolean getAntialias();

    /**
     * @param antialias if true, hints that edges of drawn shapes should be smoothed with antialiasing.
     * @return the previous state of antialiasing.
     */
    boolean setAntialias(boolean antialias);


    /**
     * Clears the whole draw context to the specified color
     * @param color color to fill the drawing with.
     */
    void clear(COLOR color);

    // Lines
    void drawLine(COLOR color, float x1, float y1, float x2, float y2, float width);
    void drawLine(COLOR color, float x1, float y1, float x2, float y2);

    // Rectangles
    void drawRectangle(COLOR color, float x1, float y1, float width, float height, float lineWidth);
    void fillRectangle(COLOR fillColor, float x1, float y1, float width, float height);
    void fillRectangle(COLOR fillColor, float x1, float y1, float width, float height, COLOR outlineColor, float outlineWidth);

    // Circles
    void drawOval(COLOR color, float centerX, float centerY, float width, float height, float lineWidth);
    void fillOval(COLOR fillColor, float centerX, float centerY, float width, float height);
    void fillOval(COLOR fillColor, float centerX, float centerY, float width, float height, COLOR outlineColor, float outlineWidth);

    // Triangle
    void drawTriangle(COLOR color, float x1, float y1, float x2, float y2, float x3, float y3, float lineWidth);
    void fillTriangle(COLOR fillColor, float x1, float y1, float x2, float y2, float x3, float y3);
    void fillTriangle(COLOR fillColor, float x1, float y1, float x2, float y2, float x3, float y3, COLOR outlineColor, float outlineWidth);

    // Text
    void drawText(COLOR color, float x, float y, String text);
    void drawText(COLOR color, float x, float y, String text, FONT font);
    void drawText(COLOR color, float x, float y, String text, FONT font, float alignX, float alignY);
    void drawText(COLOR color, float x, float y, String text, FONT font, float alignX, float alignY, COLOR outlineColor);

    // Images
    void drawImage(IMAGE image, float x, float y);
    void drawImage(IMAGE image, float x, float y, float width, float height);




}
