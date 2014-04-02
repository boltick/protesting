package org.protesting.atfwk.win32;

/**
 * Created with IntelliJ IDEA.
 * User: ab83625
 * Date: 02/01/14
 * To change this template use File | Settings | File Templates.
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
