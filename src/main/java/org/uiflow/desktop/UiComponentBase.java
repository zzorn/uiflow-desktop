package org.uiflow.desktop;

import javax.swing.*;

public abstract class UiComponentBase implements UiComponent {

    private JComponent ui;

    @Override public final JComponent getUi() {

        // Create if needed
        if (ui == null) {
            ui = createUi();
        }

        return ui;
    }

    /**
     * @return true if the UI has already been created.
     */
    public final boolean isUiCreated() {
        return ui != null;
    }

    /**
     * @return a new Swing user interface for this class.
     */
    protected abstract JComponent createUi();

}
