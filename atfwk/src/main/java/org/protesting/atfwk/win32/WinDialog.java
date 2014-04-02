package org.protesting.atfwk.win32;

/**
 * Author: Alexey Bulat
 * Date: 02/01/14
 */
public class WinDialog {

    public String windowName;
    private WinUtils.WindowInfo window;
    private boolean isPresent;

    public WinDialog(String windowName) {
        this.windowName = windowName;
        this.isPresent = WinUtils.isWindowPresent(windowName, 1000, 30);
        this.window = WinUtils.getWindowsWithTitle(windowName);
    }

    String getWindowName() {
        return windowName;
    }

    WinUtils.WindowInfo getWindow() {
        return window;
    }

    void pressButton(String caption) {
        WinUtils.pressButton(window, caption);
    }

    void type(String editBoxName, String text) {
        WinUtils.sendText(window, editBoxName, text);
    }

    public boolean isPresent() {
        return isPresent;
    }
}
