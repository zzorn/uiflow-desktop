package org.uiflow.desktop.drawcontext;

import org.flowutils.Check;

/**
 * Common functionality for DrawContexts.
 */
public abstract class DrawContextBase<COLOR, FONT, IMAGE> implements DrawContext<COLOR, FONT, IMAGE> {

    protected static final float SMALL_GAP_FACTOR = 0.5f;
    protected static final float LARGE_GAP_FACTOR = 2f;

    protected static final float DEFAULT_GAP_PIXELS = 10f;
    private static final float DEFAULT_LINE_WIDTH = 1f;

    protected static final float DEFAULT_TEXT_ALIGN_Y = 0f;
    protected static final float DEFAULT_TEXT_ALIGN_X = 0f;

    protected static final float DEFAULT_PIXELS_PER_INCH = 96;

    private float gap = DEFAULT_GAP_PIXELS;

    @Override public float getSmallGap() {
        return getGap() * SMALL_GAP_FACTOR;
    }

    @Override public float getLargeGap() {
        return getGap() * LARGE_GAP_FACTOR;
    }

    @Override public float getGap() {
        return gap;
    }

    public void setGap(float gap) {
        Check.positiveOrZero(gap, "gap");
        this.gap = gap;
    }

    @Override public void clear(COLOR color) {
        fillRectangle(color, 0, 0, getWidth(), getHeight());
    }

    @Override public float getPixelsPerInchX() {
        return getPixelsPerInch();
    }

    @Override public float getPixelsPerInchY() {
        return getPixelsPerInch();
    }

    /**
     * Either override this, or override getPixelsPerInchX and Y.  Or leave it as it is to return a default hardcoded value.
     * @return pixels per inch on the current display.
     */
    protected float getPixelsPerInch() {
        return DEFAULT_PIXELS_PER_INCH;
    }

    @Override public void drawLine(COLOR color, float x1, float y1, float x2, float y2) {
        drawLine(color, x1, y1, x2, y2, DEFAULT_LINE_WIDTH);
    }

    @Override public void fillRectangle(COLOR fillColor,
                                        float x1,
                                        float y1,
                                        float width,
                                        float height,
                                        COLOR outlineColor,
                                        float outlineWidth) {
        fillRectangle(fillColor, x1, y1, width, height);
        drawRectangle(outlineColor, x1, y1, width, height, outlineWidth);
    }

    @Override public void fillOval(COLOR fillColor,
                                   float centerX,
                                   float centerY,
                                   float width,
                                   float height,
                                   COLOR outlineColor,
                                   float outlineWidth) {
        fillOval(fillColor, centerX, centerY, width, height);
        drawOval(outlineColor, centerX, centerY, width, height, outlineWidth);
    }

    @Override public void fillTriangle(COLOR fillColor,
                                       float x1,
                                       float y1,
                                       float x2,
                                       float y2,
                                       float x3,
                                       float y3,
                                       COLOR outlineColor,
                                       float outlineWidth) {
        fillTriangle(fillColor, x1, y1, x2, y2, x3, y3);
        drawTriangle(outlineColor, x1, y1, x2, y2, x3, y3, outlineWidth);
    }

    @Override public void drawText(COLOR color, float x, float y, String text) {
        drawText(color, x, y, text, getDefaultFont());
    }

    @Override public void drawText(COLOR color, float x, float y, String text, FONT font) {
        drawText(color, x, y, text, font, DEFAULT_TEXT_ALIGN_X, DEFAULT_TEXT_ALIGN_Y);
    }

    @Override public void drawText(COLOR color,
                                   float x,
                                   float y,
                                   String text,
                                   FONT font,
                                   float alignX,
                                   float alignY,
                                   COLOR outlineColor) {

        // Draw outline...
        drawText(outlineColor, x-1, y-1, text, font, alignX, alignY);
        drawText(outlineColor, x  , y-1, text, font, alignX, alignY);
        drawText(outlineColor, x+1, y-1, text, font, alignX, alignY);
        drawText(outlineColor, x-1, y, text, font, alignX, alignY);
        drawText(outlineColor, x+1, y, text, font, alignX, alignY);
        drawText(outlineColor, x-1, y+1, text, font, alignX, alignY);
        drawText(outlineColor, x  , y+1, text, font, alignX, alignY);
        drawText(outlineColor, x+1, y+1, text, font, alignX, alignY);

        // Draw text
        drawText(color, x, y, text, font, alignX, alignY);
    }
}
