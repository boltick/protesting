package org.protesting.atfwk.win32;

/**
 * Smart Card dialog object.
 * User: ab83625
 * Date: 02/01/14
 * To change this template use File | Settings | File Templates.
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
