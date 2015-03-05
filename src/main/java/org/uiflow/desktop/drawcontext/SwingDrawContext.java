package org.uiflow.desktop.drawcontext;

import org.flowutils.Check;
import org.flowutils.MathUtils;

import javax.swing.*;
import java.awt.*;

import static org.flowutils.Check.notNull;

/**
 * DrawContext that draws on a Swing Graphics context of some size.
 * Not thread safe.
 */
public final class SwingDrawContext extends DrawContextBase<Color, Font, Image> {

    private Graphics2D g2;
    private int width;
    private int height;

    // Temporary arrays to hold triangle coordiantes.
    private final int[] triangleXCoords = new int[3];
    private final int[] triangleYCoords = new int[3];

    // TODO: Ensure setContext has been called before any draw methods are carried out?

    // TODO: Allow registering available fonts in DrawContextBase?


    public SwingDrawContext() {
    }

    /**
     * Creates a new SwingDrawContext with the specified Graphics context, and context size.
     * @param g Graphics context
     * @param width width that can be drawn on
     * @param height height that can be drawn on
     */
    public SwingDrawContext(Graphics g, int width, int height) {
        setContext(g, width, height);
    }

    /**
     * Creates a new SwingDrawContext with the specified Graphics context, and context size of the specified Swing component.
     * @param g Graphics context
     * @param component Swing or AWT component, used to get the size of the graphics context.
     */
    public SwingDrawContext(Graphics g, Component component) {
        setContext(g, component);
    }

    public void setContext(Graphics g, Component component) {
        notNull(component, "component");
        setContext(g, component.getWidth(), component.getHeight());
    }

    public void setContext(Graphics g, int width, int height) {
        setGraphics2D((Graphics2D) g);
        setWidth(width);
        setHeight(height);
    }

    public Graphics2D getGraphics2D() {
        return g2;
    }

    public void setGraphics2D(Graphics2D g2) {
        notNull(g2, "g2");
        this.g2 = g2;
    }

    public void setWidth(int width) {
        Check.positive(width, "width");
        this.width = width;
    }

    public void setHeight(int height) {
        Check.positive(height, "height");
        this.height = height;
    }

    @Override public float getWidth() {
        return width;
    }

    @Override public float getHeight() {
        return height;
    }

    @Override public float getPixelsPerInch() {
        try {
            return Toolkit.getDefaultToolkit().getScreenResolution();
        }
        catch(HeadlessException e) {
            // If we are running in headless mode, use the default value, so that we can still use the draw context.
            return DEFAULT_PIXELS_PER_INCH;
        }
    }

    @Override public Font getDefaultFont() {
        return g2.getFont();
    }

    @Override public Font getFont(String fontName) {
        return Font.getFont(fontName);
    }

    @Override public Font getFont(String fontName, float fontSize) {
        final Font font = getFont(fontName);
        return font.deriveFont(fontSize);
    }

    @Override public float getFontHeight(Font font) {
        return g2.getFontMetrics(font).getHeight();
    }

    @Override public float getFontHeightBaselineToBottom(Font font) {
        return g2.getFontMetrics(font).getMaxDescent();
    }

    @Override public float getFontHeightBaselineToTop(Font font) {
        return g2.getFontMetrics(font).getMaxAscent();
    }

    @Override public float getTextWidth(Font font, String text) {
        return g2.getFontMetrics(font).stringWidth(text);
    }

    @Override public boolean getAntialias() {
        return g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING) == RenderingHints.VALUE_ANTIALIAS_ON;
    }

    @Override public boolean setAntialias(boolean antialias) {
        boolean wasAntialias = getAntialias();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            antialias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        return wasAntialias;
    }

    @Override public void drawLine(Color color, float x1, float y1, float x2, float y2) {
        g2.setColor(color);
        g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    @Override public void drawLine(Color color, float x1, float y1, float x2, float y2, float lineWidth) {
        g2.setColor(color);
        final Stroke oldStroke = setLineWidth(lineWidth);
        g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        g2.setStroke(oldStroke);
    }

    @Override public void drawRectangle(Color color, float x1, float y1, float width, float height, float lineWidth) {
        g2.setColor(color);
        final Stroke oldStroke = setLineWidth(lineWidth);
        g2.drawRect((int) x1, (int) y1, (int) width, (int) height);
        g2.setStroke(oldStroke);
    }

    @Override public void fillRectangle(Color fillColor, float x1, float y1, float width, float height) {
        g2.setColor(fillColor);
        g2.fillRect((int) x1, (int) y1, (int) width, (int) height);
    }

    @Override public void drawOval(Color color,
                                   float centerX,
                                   float centerY,
                                   float width,
                                   float height,
                                   float lineWidth) {
        g2.setColor(color);
        final Stroke oldStroke = setLineWidth(lineWidth);
        g2.drawOval((int) (centerX - 0.5f * width),
                    (int) (centerY - 0.5f * height),
                    (int) width,
                    (int) height);
        g2.setStroke(oldStroke);
    }

    @Override public void fillOval(Color fillColor, float centerX, float centerY, float width, float height) {
        g2.setColor(fillColor);
        g2.fillOval((int) (centerX - 0.5f * width),
                    (int) (centerY - 0.5f * height),
                    (int) width,
                    (int) height);
    }

    @Override public void drawTriangle(Color color,
                                       float x1,
                                       float y1,
                                       float x2,
                                       float y2,
                                       float x3,
                                       float y3,
                                       float lineWidth) {
        g2.setColor(color);
        final Stroke oldStroke = setLineWidth(lineWidth);
        triangleXCoords[0] = (int) x1;
        triangleXCoords[1] = (int) x2;
        triangleXCoords[2] = (int) x3;
        triangleYCoords[0] = (int) y1;
        triangleYCoords[1] = (int) y2;
        triangleYCoords[2] = (int) y3;
        g2.drawPolygon(triangleXCoords, triangleYCoords, 3);
        g2.setStroke(oldStroke);
    }

    @Override public void fillTriangle(Color fillColor, float x1, float y1, float x2, float y2, float x3, float y3) {
        g2.setColor(fillColor);
        triangleXCoords[0] = (int) x1;
        triangleXCoords[1] = (int) x2;
        triangleXCoords[2] = (int) x3;
        triangleYCoords[0] = (int) y1;
        triangleYCoords[1] = (int) y2;
        triangleYCoords[2] = (int) y3;
        g2.fillPolygon(triangleXCoords, triangleYCoords, 3);
    }


    @Override public void drawText(Color color, float x, float y, String text, Font font, float alignX, float alignY) {
        g2.setColor(color);
        final Font oldFont = g2.getFont();
        g2.setFont(font);

        // Calculate alignment
        final float textWidth = getTextWidth(font, text);
        final float fontHeight = getFontHeight(font);
        y += getFontHeightBaselineToBottom(font);

        g2.drawString(text,
                      x + MathUtils.mix(alignX, 0, textWidth),
                      y - MathUtils.mix(alignY, 0, fontHeight));
        g2.setFont(oldFont);
    }

    @Override public void drawImage(Image image, float x, float y) {
        g2.drawImage(image, (int)x, (int)y, null);
    }

    @Override public void drawImage(Image image, float x, float y, float width, float height) {
        g2.drawImage(image, (int)x, (int)y, (int)width, (int)height, null);
    }

    /**
     * Assumes the current stroke has width one, and does not change the stroke if width is one.
     * @return previous stroke used by the graphics context.
     */
    private Stroke setLineWidth(float width) {
        final Stroke oldStroke = g2.getStroke();
        if (width != 1) {
            g2.setStroke(new BasicStroke(width));
        }
        return oldStroke;
    }
}
