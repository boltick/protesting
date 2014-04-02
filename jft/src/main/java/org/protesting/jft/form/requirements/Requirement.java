package org.protesting.jft.form.requirements;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.Format;

/**
 * User: ab83625
 * Date: 28.01.2008
 * Time: 11:47:13
 */
public class Requirement {

    private static Log logger = LogFactory.getLog(Requirement.class);

    private String reqID;

    private String acceptedChars;

    private String forbiddenChars;

    private boolean isRequired;

    private String format;

    private Format formatType;

//    private boolean isRequired;

    public Requirement() {
        this.reqID = "custom";
//        isRequired = true;
        logger.debug("Requirement ["+reqID+"] is initialized");
    }

    public Requirement(String reqID) {
        this();
        this.reqID = reqID;
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }


    public String getAcceptedChars() {
        return acceptedChars;
    }

    public void setAcceptedChars(String acceptedChars) {
        this.acceptedChars = acceptedChars;
    }

    public String getForbiddenChars() {
        return forbiddenChars;
    }

    public void setForbiddenChars(String forbiddenChars) {
        this.forbiddenChars = forbiddenChars;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Format getFormatType() {
        return formatType;
    }

    public void setFormatType(Format formatType) {
        this.formatType = formatType;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("reqID=").append(reqID).append("; ");
        if (acceptedChars!=null) {
            sb.append("accepted=").append(acceptedChars).append("; ");
        }
        if (forbiddenChars!=null) {
            sb.append("rejected=").append(forbiddenChars).append("; ");
        }
        if (formatType!=null) {
            sb.append("type=").append(formatType.getClass()).append("; ");
            sb.append("format=").append(format);
        }
        sb.append("isRequired=").append(isRequired).append("; ");

        return sb.toString();
    }
}
