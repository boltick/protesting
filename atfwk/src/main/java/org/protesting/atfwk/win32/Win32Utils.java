package org.protesting.atfwk.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Alexey Bulat
 * Date: 04/12/13
 */
public class Win32Utils {

    public static final Logger LOG = Logger.getLogger(Win32Utils.class);

    private static boolean isLabelFound;

    synchronized public static List<WindowInfo> getAllWindows () {
        final List<WindowInfo> windowsList = new ArrayList<WindowInfo>();
        User32.instance.EnumWindows(new WndEnumProc()
        {
            public boolean callback(WinDef.HWND hWnd, int lParam)
            {
                if (User32.instance.IsWindowVisible(hWnd)) {
                    RECT r = new RECT();
                    User32.instance.GetWindowRect(hWnd, r);
                    if (r.left>-32000) {     // minimized
                        byte[] buffer = new byte[1024];
                        User32.instance.GetWindowTextA(hWnd, buffer, buffer.length);
                        String title = Native.toString(buffer);
                        windowsList.add(new WindowInfo(hWnd, title));
                    }
                }
                return true;
            }
        }, 0);

        return windowsList;
    }


    synchronized public static WindowInfo getWindowsWithTitle(final String title) {
        final WindowInfo[] window = {null};
        User32.instance.EnumWindows(new WndEnumProc()
        {
            public boolean callback(WinDef.HWND hWnd, int lParam)
            {
                if (User32.instance.IsWindowVisible(hWnd)) {
                    RECT r = new RECT();
                    User32.instance.GetWindowRect(hWnd, r);
                    if (r.left>-32000) {     // minimized
                        byte[] buffer = new byte[1024];
                        User32.instance.GetWindowTextA(hWnd, buffer, buffer.length);
                        String windowTitle = Native.toString(buffer);
                        if(windowTitle.equalsIgnoreCase(title)) {
                            window[0] = new WindowInfo(hWnd, windowTitle);
                            return false;
                        }

                    }
                }
                return true;
            }
        }, 0);

        return window[0];
    }

    public static interface WndEnumProc extends StdCallLibrary.StdCallCallback {
        boolean callback (WinDef.HWND hWnd, int lParam);
    }

    public static interface User32 extends StdCallLibrary {
        final User32 instance = (User32) Native.loadLibrary ("user32", User32.class,  W32APIOptions.DEFAULT_OPTIONS);
        boolean isFound = false;
        boolean EnumWindows (WndEnumProc wndenumproc, int lParam);
        boolean IsWindowVisible(WinDef.HWND hWnd);
        int GetWindowRect(WinDef.HWND hWnd, RECT r);
        void GetWindowTextA(WinDef.HWND hWnd, byte[] buffer, int buflen);
        final int GW_HWNDNEXT = 2;

        int SendMessage(WinDef.HWND hWnd, int msg, int wParam, char[] lParam);
        int SendMessage(WinDef.HWND hWnd, int msg, int wParam1, int wParam2);

        boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer arg);
        boolean EnumChildWindows(WinDef.HWND parent, WNDENUMPROC callback, int lParam);

        interface WNDENUMPROC extends StdCallCallback {
            boolean callback(WinDef.HWND hWnd, Pointer arg);
        }

        int GetClassNameA(WinDef.HWND in, byte[] lpString, int size);
    }

    public static class RECT extends Structure {
        public int left,top,right,bottom;
    }

    public static class WindowInfo {
        WinDef.HWND hwnd;
        String title;
        public WindowInfo(WinDef.HWND hwnd, String title) {
            this.hwnd = hwnd;
            this.title = title;
        }

        public String toString() {
            return String.format("hwnd: %s; title: \"%s\"", hwnd!=null ? hwnd.toString() : "null", title);
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof WindowInfo)) {
                return false;
            }
            WindowInfo that = (WindowInfo) obj;
            // compare only the page titles
            return this.title.equals(that.title);
        }
    }


    synchronized public static boolean isWindowPresent(final String title) {
        WindowInfo fakeWindowWithTitle = new WindowInfo(null, title);
        return getAllWindows().contains(fakeWindowWithTitle);
    }

    public static void pressButton(final WindowInfo theWindow, final String buttonName) {
        User32.instance.EnumWindows(new WndEnumProc() {
            public boolean callback(WinDef.HWND hWnd, int lParam) {

                if (User32.instance.IsWindowVisible(hWnd)) {
                    RECT r = new RECT();
                    User32.instance.GetWindowRect(hWnd, r);
                    if (r.left>-32000) {     // minimized
                        byte[] buffer = new byte[1024];
                        User32.instance.GetWindowTextA(hWnd, buffer, buffer.length);
                        String title = Native.toString(buffer);
                        WindowInfo aWindow = new WindowInfo(hWnd, title);
                        if(aWindow.equals(theWindow)) {

                            User32.instance.EnumChildWindows(hWnd, new User32.WNDENUMPROC() {
                                public boolean callback(WinDef.HWND hWnd, Pointer userData) { // this is called for each child window that EnumChildWindows() finds - just like before with EnumWindows().
                                    byte[] textBuffer = new byte[512];
                                    User32.instance.GetWindowTextA(hWnd, textBuffer, 512);
                                    String wText = Native.toString(textBuffer);

                                    if(wText.equalsIgnoreCase(buttonName)) {
                                        User32.instance.SendMessage(hWnd,0x201 , 0, 0);
                                        User32.instance.SendMessage(hWnd, 0x202, 0, 0);
                                        LOG.debug("Button " + buttonName + " is clicked");
                                        return false;
                                    }
                                    return true;
                                }
                            }, 0);
                            return false;
                        }
                    }
                }
                return true;
            }
        }, 0);
    }


    public static void sendText(final WindowInfo theWindow, final String fieldName, final String textMessage) {
        User32.instance.EnumWindows(new WndEnumProc() {
            public boolean callback(WinDef.HWND hWnd, int lParam) {

                if (User32.instance.IsWindowVisible(hWnd)) {
                    RECT r = new RECT();
                    User32.instance.GetWindowRect(hWnd, r);
                    if (r.left>-32000) {     // minimized
                        byte[] buffer = new byte[1024];
                        User32.instance.GetWindowTextA(hWnd, buffer, buffer.length);
                        String title = Native.toString(buffer);
                        WindowInfo aWindow = new WindowInfo(hWnd, title);
                        if(aWindow.hwnd.equals(theWindow.hwnd)) {

                            User32.instance.EnumChildWindows(hWnd, new User32.WNDENUMPROC() {
                                public boolean callback(WinDef.HWND hWnd, Pointer userData) { // this is called for each child window that EnumChildWindows() finds - just like before with EnumWindows().
                                    byte[] textBuffer = new byte[512];
                                    User32.instance.GetWindowTextA(hWnd, textBuffer, 512);
                                    String wText = Native.toString(textBuffer);
                                    byte[] aTextBuffer = new byte[512];
                                    User32.instance.GetClassNameA(hWnd, aTextBuffer, 512);
                                    if(isLabelFound && new String(aTextBuffer).trim().equalsIgnoreCase("edit")) {
                                        char [] pass = Native.toCharArray(textMessage);
                                        User32.instance.SendMessage(hWnd, 0x000C, 0, pass);
                                        LOG.debug("Text is filled in the " + fieldName);
                                        isLabelFound = false;
                                        return true;
                                    }
                                    if(wText.equalsIgnoreCase(fieldName)) {
                                        LOG.debug("Window found: " + wText);
                                        isLabelFound =  true;
                                    }

                                    return true;
                                }
                            }, 0);
                            return false;
                        }

                    }
                }
                return !isLabelFound;
            }
        }, 0);

    }

}
