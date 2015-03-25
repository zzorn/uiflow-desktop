package org.uiflow.desktop.ui;

import org.flowutils.Check;
import org.uiflow.desktop.drawcontext.SwingDrawContext;

import javax.swing.*;
import java.awt.*;

/**
 * An UI component that simply draws a specified Renderable inside itself.
 */
public class RenderableUiComponent extends UiComponentBase {

    private Renderable renderable;
    private Dimension preferredSize = new Dimension(24, 24);

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

    public int getPreferredWidth() {
        return preferredSize.width;
    }

    public void setPreferredWidth(int preferredWidth) {
        Check.positiveOrZero(preferredWidth, "preferredWidth");
        preferredSize.width = preferredWidth;
    }

    public int getPreferredHeight() {
        return preferredSize.height;
    }

    public void setPreferredHeight(int preferredHeight) {
        Check.positiveOrZero(preferredHeight, "preferredHeight");
        preferredSize.height = preferredHeight;
    }

    public final void setPreferredSize(int width, int height) {
        setPreferredWidth(width);
        setPreferredHeight(height);
    }

    public Dimension getPreferredSize() {
        return preferredSize;
    }

    public void setPreferredSize(Dimension preferredSize) {
        this.preferredSize.setSize(preferredSize);
    }

    @Override protected JComponent createUi() {
        return new JPanel() {
            private final SwingDrawContext swingDrawContext = new SwingDrawContext();

            @Override protected void paintComponent(Graphics g) {
                if (renderable != null) {
                    swingDrawContext.setContext(g, this);

                    renderable.render(swingDrawContext);

                    swingDrawContext.clearContext();
                }
                else {
                    super.paintComponent(g);
                }
            }

            @Override public Dimension getPreferredSize() {
                return preferredSize;
            }
        };
    }
}
