package org.uiflow.desktop.ui;

import java.awt.*;

/**
 * Something that can be rendered to a graphics context.
 */
public interface Renderable {

    /**
     * @param g2 graphics context to draw to.
     * @param renderArea the area to render this renderable in.
     */
    void render(Graphics2D g2, Rectangle renderArea);

}
