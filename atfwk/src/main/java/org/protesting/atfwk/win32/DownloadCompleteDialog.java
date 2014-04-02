package org.protesting.atfwk.win32;

/**
 * Created with IntelliJ IDEA.
 * User: ab83625
 * Date: 02/01/14
 * To change this template use File | Settings | File Templates.
 */
public class DownloadCompleteDialog extends WinDialog {

    private static final String BUTTON_OPEN="&Open";
    private static final String BUTTON_OPEN_FOLDER="Open &Folder";
    private static final String BUTTON_CLOSE="Close";

    public DownloadCompleteDialog() {
        super("Download complete");
    }

    public void pressOpen() {
        pressButton(BUTTON_OPEN);
    }

    public void pressOpenFolder() {
        pressButton(BUTTON_OPEN_FOLDER);
    }

    public void pressClose() {
        pressButton(BUTTON_CLOSE);
    }
}
