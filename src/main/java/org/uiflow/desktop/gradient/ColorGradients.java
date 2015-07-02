package org.uiflow.desktop.gradient;


import java.awt.*;

/**
 * Utility class with some common color gradients.
 */
public final class ColorGradients {

    public static final ColorGradient SPECTRUM = createSpectrumGradient();

    public static final ColorGradient RAINBOW = new ColorGradient(
            new Color(200, 0, 130),
            new Color(255, 0, 0),
            new Color(255, 128, 0),
            new Color(255, 220, 0),
            new Color(150, 220, 0),
            new Color(0, 200, 0),
            new Color(0, 180, 120),
            new Color(0, 150, 255),
            new Color(0, 60, 255),
            new Color(0, 0, 255),
            new Color(150, 0, 230)
    ).makeReadOnly();

    public static final ColorGradient BLUERED = new ColorGradient(
            new Color(0, 0, 255),
            new Color(80, 160, 255),
            new Color(220, 220, 220),
            new Color(255, 160, 80),
            new Color(255, 0, 0)
    ).makeReadOnly();

    public static final ColorGradient GREYSCALE = new ColorGradient(
            new Color(0, 0, 0),
            new Color(255, 255, 255)
    ).makeReadOnly();

    public static final ColorGradient WARM_TO_COLD = new ColorGradient(
            new Color(255, 220, 0),
            new Color(255, 140, 0),
            new Color(230, 50, 0),
            new Color(200, 0, 130),
            new Color(150, 0, 160),
            new Color(100, 0, 200),
            new Color(50, 0, 230)
    ).makeReadOnly();

    public static ColorGradient createSpectrumGradient() {
        final ColorGradient gradient = new ColorGradient();
        gradient.addColor(0.0, 0, 0, 0);
        gradient.addColor(0.1, 1, 0, 0);
        gradient.addColor(0.2, 1, 0.5, 0);
        gradient.addColor(0.3, 1, 1, 0);
        gradient.addColor(0.4, 0.5, 1, 0);
        gradient.addColor(0.5, 0, 1, 0);
        gradient.addColor(0.6, 0, 1, 0.5);
        gradient.addColor(0.7, 0, 1, 1);
        gradient.addColor(0.8, 0, 0.5, 1);
        gradient.addColor(0.9, 0, 0, 1);
        gradient.addColor(1.0, 0.5, 0, 1);
        return gradient;
    }

    private ColorGradients() {
    }
}
