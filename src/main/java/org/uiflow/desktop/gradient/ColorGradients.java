package org.uiflow.desktop.gradient;


import java.awt.*;

/**
 * Utility class with some common color gradients.
 */
public class ColorGradients {

    public static final ColorGradient SPECTRUM = createSpectrumGradient();
    public static final ColorGradient RAINBOW = createRainbowGradient();
    public static final ColorGradient BLUERED = createBlueRedGradient();
    public static final ColorGradient GREYSCALE = createGreyscaleGradient();

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

    public static ColorGradient createRainbowGradient() {
        final ColorGradient gradient = new ColorGradient();
        gradient.addColor(0.0, new Color(200,0,130));
        gradient.addColor(0.1, new Color(255,0,0));
        gradient.addColor(0.2, new Color(255,128,0));
        gradient.addColor(0.3, new Color(255, 220, 0));
        gradient.addColor(0.4, new Color(150, 220, 0));
        gradient.addColor(0.5, new Color(0, 200, 0));
        gradient.addColor(0.6, new Color(0, 180, 120));
        gradient.addColor(0.7, new Color(0, 150, 255));
        gradient.addColor(0.8, new Color(0, 60, 255));
        gradient.addColor(0.9, new Color(0, 0, 255));
        gradient.addColor(1.0, new Color(150, 0, 230));
        return gradient;
    }

    public static ColorGradient createGreyscaleGradient() {
        final ColorGradient gradient = new ColorGradient();
        gradient.addColor(0.0, new Color(0,0,0));
        gradient.addColor(1.0, new Color(255, 255, 255));
        return gradient;
    }

    public static ColorGradient createBlueRedGradient() {
        final ColorGradient gradient = new ColorGradient();
        gradient.addColor(0.0,  new Color(0, 0, 255));
        gradient.addColor(0.25, new Color(80, 160, 255));
        gradient.addColor(0.5,  new Color(220, 220, 220));
        gradient.addColor(0.75, new Color(255, 160, 80));
        gradient.addColor(1.0,  new Color(255, 0, 0));
        return gradient;
    }

    private ColorGradients() {
    }
}
