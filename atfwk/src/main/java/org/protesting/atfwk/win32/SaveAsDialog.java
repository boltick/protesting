package org.protesting.atfwk.win32;

/**
 * Author: Alexey Bulat
 * Date: 02/01/14
 */
public class SaveAsDialog extends WinDialog {

    private static final String BUTTON_SAVE="&Save";
    private static final String BUTTON_CANCEL="Cancel";
    private static final String EDITBOX_FILENAME="File &name:";

    public SaveAsDialog() {
        super("Save As");
    }

    public void typeFileName(String filename) {
        type(EDITBOX_FILENAME, filename);
    }

    public void pressSave() {
        pressButton(BUTTON_SAVE);
    }

    public void pressCancel() {
        pressButton(BUTTON_CANCEL);
    }
}
