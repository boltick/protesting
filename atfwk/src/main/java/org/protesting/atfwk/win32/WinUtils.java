package org.protesting.atfwk.win32;

import org.apache.log4j.Logger;

/**
 * Author: Alexey Bulat
 * Date: 02/01/14
 */
public class WinUtils extends Win32Utils {

    public static final Logger LOG = Logger.getLogger(WinUtils.class);

    public static boolean isWindowPresent(String title, int timeout, int iterations) {
        int currentCounter = 0;
        while (!Win32Utils.isWindowPresent(title)) {
            if(currentCounter>iterations) {
                LOG.debug("Dialog window " + title + " was not found after > " + (iterations * timeout / 1000) + " seconds...");
                return false;
            }
            delay(timeout);
            currentCounter++;
        }
        LOG.debug("Window " + title + " is present");
        return true;
    }

    public static void delay(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
