package org.uiflow.desktop.gradient;


/**
 * Utility class with some common color gradients.
 */
public class ColorGradients {

    public static final ColorGradient SPECTRUM = createSpectrumGradient();
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

    public static ColorGradient createGreyscaleGradient() {
        final ColorGradient gradient = new ColorGradient();
        gradient.addColor(0.0, 0, 0, 0);
        gradient.addColor(1.0, 1, 1, 1);
        return gradient;
    }

    private ColorGradients() {
    }
}
