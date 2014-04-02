package org.protesting.atfwk.win32;

/**
 * Smart Card dialog object.
 * Author: Alexey Bulat
 * Date: 02/01/14
 */
public class GemaltoSmartCardDialog extends WinDialog {

    private static final String EDITBOX_PINPASS="PIN:";
    private static final String BUTTON_OK="OK";
    private static final String BUTTON_CANCEL="CANCEL";


    public GemaltoSmartCardDialog() {
        super("Card");
    }

    public void typePinCode(String pincode) {
        type(EDITBOX_PINPASS, pincode);
    }

    public void pressOK() {
        pressButton(BUTTON_OK);
    }

    public void pressCancel() {
        pressButton(BUTTON_CANCEL);
    }

}
