package org.protesting.atfwk;

import java.awt.*;

/**
 * ATFwk wrapper for awt.Robot
 * User: ab83625
 * Date: 30.06.2010
 * To change this template use File | Settings | File Templates.
 */
public class ATFwkRobot extends Robot {

    public ATFwkRobot() throws AWTException {
        super();
    }

    /**
     * Send keys
     * @param keys - keys to be sent
     */
    public void sendKeys(String keys) {
         for (int i = 0; i < keys.length(); i++) {
            int toBeSent = keys.codePointAt(i);
            toBeSent = toBeSent >= 65 & toBeSent <= 122 ? (keys.codePointAt(i) - 32) : keys.codePointAt(i);
            keyPress(toBeSent);
            keyRelease(toBeSent);
            delay(10);
        }
    }


    public static void main(String[] args) throws AWTException {
        new ATFwkRobot().sendKeys("1234567890 test string !'\\ ?!.,/");
    }

}
