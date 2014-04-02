package org.protesting.jft.form.fields;

import org.protesting.jft.form.Template;
import org.protesting.jft.form.requirements.Requirement;

/**
 * User: ab83625
 * Date: 18.01.2008
 * Time: 17:13:33
 */
public class FieldFactory {

    public static final byte FIELD_EDIT_BOX = 0;
    public static final byte FIELD_COMBO_BOX = 1;
    public static final byte FIELD_CHECK_BOX = 2;
    public static final byte FIELD_TEXT_AREA = 3;
    public static final byte FIELD_BUTTON = 4;
    public static final byte FIELD_RADIO_BUTTON = 5;
    public static final byte FIELD_UNKNOWN = 6;

    public static final String [] FIELD_TYPE_ARRAY = {  "edit_box", "combo_box", "check_box",
                                                        "text_area", "button", "radio_button", "unknown"  };

    public static Field getField(String name, byte fieldType, Template template) {
        if (template == null) {
            switch(fieldType) {
                case FIELD_EDIT_BOX:
                    return new FieldEditBox(name);
                case FIELD_COMBO_BOX:
                    return new FieldComboBox(name);
                case FIELD_CHECK_BOX:
                    return new FieldCheckBox(name);
                case FIELD_TEXT_AREA:
                    return new FieldTextArea(name);
                case FIELD_BUTTON:
                    return new FieldButton(name);
                case FIELD_RADIO_BUTTON:
                    return new FieldRadioButton(name);
                case FIELD_UNKNOWN:
                    return new FieldUnknown(name);
                default:
                    throw new IllegalArgumentException("Unsupported filed type: " + fieldType);
            }
        }
        return new CustomField(name, FIELD_TYPE_ARRAY[fieldType], template);
    }


    public static Field getField(String name, byte fieldType) {
        switch(fieldType) {
            case FIELD_EDIT_BOX:
                return new FieldEditBox(name);
            case FIELD_COMBO_BOX:
                return new FieldComboBox(name);
            case FIELD_CHECK_BOX:
                return new FieldCheckBox(name);
            case FIELD_TEXT_AREA:
                return new FieldTextArea(name);
            case FIELD_BUTTON:
                return new FieldButton(name);
            case FIELD_RADIO_BUTTON:
                return new FieldRadioButton(name);
            case FIELD_UNKNOWN:
                return new FieldUnknown(name);
            default:
                throw new IllegalArgumentException("Unsupported filed type: " + fieldType);
        }
    }


    public static Field getField(String name, byte fieldType, Requirement requirement) {
        switch(fieldType) {
            case FIELD_EDIT_BOX:
                return new FieldEditBox(name, requirement);
            case FIELD_COMBO_BOX:
                return new FieldComboBox(name, requirement);
            case FIELD_CHECK_BOX:
                return new FieldCheckBox(name, requirement);
            case FIELD_TEXT_AREA:
                return new FieldTextArea(name, requirement);
            case FIELD_RADIO_BUTTON:
                return new FieldRadioButton(name,requirement);
            case FIELD_UNKNOWN:
                return new FieldUnknown(name,requirement);
            default:
                throw new IllegalArgumentException("Unsupported filed type: " + fieldType);
        }
    }


    public static byte getFieldIdByType(String type) {
        for (byte i = 0; i < FIELD_TYPE_ARRAY.length; i++) {
            if (FIELD_TYPE_ARRAY[i].equalsIgnoreCase(type)) {
                return i;
            }
        }
        return -1;
    }

}
