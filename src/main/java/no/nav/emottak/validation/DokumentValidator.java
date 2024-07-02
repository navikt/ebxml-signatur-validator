package no.nav.emottak.validation;

import java.io.ByteArrayInputStream;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class DokumentValidator {
    
    private Session session = mockSession();
    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private static Logger logger = LoggerFactory.getLogger(DokumentValidator.class);
    
    
    
    public DokumentValidator() throws Exception {
	 org.apache.xml.security.Init.init();
	
	 dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
	 dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
	 dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
	 dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
	 dbf.setXIncludeAware(false);
	 dbf.setExpandEntityReferences(false);
	 dbf.setNamespaceAware(true);
	
    }
    
    public boolean validateEbxmlSignature(byte [] rawMessage) throws Exception {
	ByteArrayInputStream in = new ByteArrayInputStream(rawMessage);
	MimeMessage mimeMessage = new MimeMessage(session, in);
	if (! (mimeMessage.getContent() instanceof MimeMultipart)) {
	    logger.error("Message is not multipart message. You need to have correct Content-Type header over your ebxml message.");
	    throw new RuntimeException("Message is not multipart message.");
	}
	
	MimeMultipart multipart = (MimeMultipart)mimeMessage.getContent();
	var dokument = multipart.getBodyPart(0);
	var payload = multipart.getBodyPart(1);
	var db = dbf.newDocumentBuilder();
	Document ebxmlDoc = db.parse(dokument.getInputStream());
	var nodeList = ebxmlDoc.getElementsByTagNameNS(Constants.SignatureSpecNS, Constants._TAG_SIGNATURE);
	
	XMLSignature signature = new XMLSignature((Element)nodeList.item(0),Constants.SignatureSpecNS);
	
	validateEbxmlSignature(signature, payload.getInputStream().readAllBytes());
	
	var contentId = payload.getHeader("Content-Id") == null || payload.getHeader("Content-Id").length == 0  ? null : payload.getHeader("Content-Id")[0] ;
	var contentType = payload.getHeader("Content-Type") == null || payload.getHeader("Content-Type").length == 0  ? null : payload.getHeader("Content-Type")[0];
	
	if (StringUtils.isEmpty(contentId)) {
	    logger.error("Content-Id header mangler fra payload.");
	}
	if (StringUtils.isEmpty(contentType)) {
	    logger.error("Content-Type header mangler fra payload.");
	}
	
	signature.addResourceResolver(new EbmsAttachmentResolver(List.of(new Attachment(contentId, contentType, payload.getInputStream().readAllBytes()))));
	return signature.checkSignatureValue(signature.getKeyInfo().getX509Certificate());
	
    }
    
    public boolean validatePayloadSignature(byte [] rawMessage) throws Exception {
	var db = dbf.newDocumentBuilder();
	Document document = db.parse(new ByteArrayInputStream(rawMessage));
	var nodeList = document.getElementsByTagNameNS(Constants.SignatureSpecNS, Constants._TAG_SIGNATURE);
	XMLSignature signature = new XMLSignature((Element)nodeList.item(0),Constants.SignatureSpecNS);
	return signature.checkSignatureValue(signature.getKeyInfo().getX509Certificate());
    }
    
    private void validateEbxmlSignature(XMLSignature signature,byte []attachment) throws Exception {
	var certificateFromSignature = signature.getKeyInfo().getX509Certificate();
	
	System.out.println(certificateFromSignature.getSerialNumber().toString(16));
	

    }
    
    
    private Session mockSession() {
        var properties = new Properties();
        properties.put("mail.pop3.socketFactory.fallback", "false");
        properties.put("mail.pop3.socketFactory.port", "3110");
        properties.put("mail.pop3.host", "localhost");
        properties.put("mail.store.protocol", "pop3");

        return Session.getDefaultInstance(properties);
    }

}
