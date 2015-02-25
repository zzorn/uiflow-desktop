package org.uiflow.desktop.ui;

import javax.swing.*;
import java.awt.*;

/**
 * An UI component that simply draws a specified Renderable inside itself.
 */
public class RenderableUiComponent extends UiComponentBase {

    private Renderable renderable;

    public RenderableUiComponent() {
        this(null);
    }

    public RenderableUiComponent(Renderable renderable) {
        this.renderable = renderable;
    }

    /**
     * @return the renderable to draw in the UI component.
     */
    public final Renderable getRenderable() {
        return renderable;
    }

    /**
     * @param renderable the renderable to draw in the UI component.
     */
    public final void setRenderable(Renderable renderable) {
        this.renderable = renderable;
    }

    @Override protected JComponent createUi() {
        return new JPanel() {
            private final Rectangle area = new Rectangle();

            @Override protected void paintComponent(Graphics g) {
                if (renderable != null) {
                    area.x = 0;
                    area.y = 0;
                    area.width = getWidth();
                    area.height = getHeight();

                    renderable.render((Graphics2D) g, area);
                }
                else {
                    super.paintComponent(g);
                }
            }
        };
    }
}
