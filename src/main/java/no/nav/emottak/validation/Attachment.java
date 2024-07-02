package no.nav.emottak.validation;

import java.util.Base64;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class Attachment {
    
    private String contentId;
    private String mimeType;
    private byte[] attachment;
    
    public Attachment(String contentId,String mimeType ,byte[] attachment) {
	super();
	String extractFromBrackets = StringUtils.substringBetween(contentId, "<", ">");
	if (extractFromBrackets != null) {
	    this.contentId = extractFromBrackets;
	}
	else {
	    this.contentId = contentId;
	}
	this.mimeType = mimeType;
	this.attachment = attachment;
    }

    public String getContentId() {
        return contentId;
    }

    public byte[] getAttachment() {
	return attachment;
    }

    public String getMimeType() {
        return mimeType;
    }
    
    
    
    

}
