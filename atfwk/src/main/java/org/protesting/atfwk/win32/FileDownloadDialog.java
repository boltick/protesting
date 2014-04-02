package org.protesting.atfwk.win32;

/**
 * Created with IntelliJ IDEA.
 * User: ab83625
 * Date: 02/01/14
 * To change this template use File | Settings | File Templates.
 */
public class FileDownloadDialog extends WinDialog {

    private static final String BUTTON_OPEN="&Open";
    private static final String BUTTON_SAVE="&Save";
    private static final String BUTTON_CANCEL="Cancel";

    protected FileDownloadDialog(String title) {
        super(title);
    }

    public FileDownloadDialog() {
        this("File Download");
    }

    public void pressButtonSave() {
        pressButton(BUTTON_SAVE);
    }

    public void pressButtonOpen() {
        pressButton(BUTTON_OPEN);
    }

    public void pressButtonCancel() {
        pressButton(BUTTON_CANCEL);
    }

}
