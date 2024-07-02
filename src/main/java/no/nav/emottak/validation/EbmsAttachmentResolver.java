package no.nav.emottak.validation;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.resolver.ResourceResolverContext;
import org.apache.xml.security.utils.resolver.ResourceResolverException;
import org.apache.xml.security.utils.resolver.ResourceResolverSpi;

class EbmsAttachmentResolver extends ResourceResolverSpi {
    
    private List<Attachment> attachments;
    private String CID_PREFIX = "cid:";
    
    public EbmsAttachmentResolver(List<Attachment> attachments) {
	this.attachments = attachments;
    }
    
    public boolean engineCanResolveURI(ResourceResolverContext context) {
         if (context.uriToResolve.startsWith(CID_PREFIX)) {
            return attachments.stream().anyMatch(t-> {
        	
        	return context.uriToResolve.substring(CID_PREFIX.length()).equals( t.getContentId());
        	});
        } else {
           return  false;
        }
    }

    public XMLSignatureInput engineResolveURI(ResourceResolverContext context ) throws ResourceResolverException {
       if (!context.uriToResolve.startsWith(CID_PREFIX)) {
            throw new ResourceResolverException(
                context.uriToResolve,
                new Object[]{"Reference URI does not start with $CID_PREFIX"},
                context.uriToResolve,
                context.baseUri
            );
       }
       Attachment result = attachments.stream().filter(att -> context.uriToResolve.substring(CID_PREFIX.length()).equalsIgnoreCase(att.getContentId())).findFirst().orElse(null);
        
       if (result == null) throw new ResourceResolverException(
            context.uriToResolve,
            new Object[]{"Reference URI ${context.uriToResolve} does not exist!"},
            context.uriToResolve,
            context.baseUri
        );
 
       XMLSignatureInput xmlSigInput = new XMLSignatureInput(result.getAttachment());
       xmlSigInput.setSourceURI(context.uriToResolve);
       xmlSigInput.setMIMEType(result.getMimeType());
       return xmlSigInput;
       
       
    }
}