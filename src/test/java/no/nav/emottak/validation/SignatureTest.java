package no.nav.emottak.validation;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SignatureTest {
    
    private static Logger logger = LoggerFactory.getLogger(SignatureTest.class);
    
    
    @Test
    public void messageWithFailingSignature() throws Exception {
	DokumentValidator validator = new DokumentValidator();
	var stream = this.getClass().getClassLoader().getResourceAsStream("tekkopplysning");
	assertFalse(validator.validateEbxmlSignature(stream.readAllBytes()),"This message should have feiled signature verification");
    }
    
    @Test
    public void messageWithCorrectSignature() throws Exception {
	DokumentValidator validator = new DokumentValidator();
	var stream = this.getClass().getClassLoader().getResourceAsStream("riktigsignature");
	assertTrue(validator.validateEbxmlSignature(stream.readAllBytes()), "This message should have had correct signature"); ;
    }
    
    @Test
    public void validatePayloadOnly() throws Exception {
	DokumentValidator validator = new DokumentValidator();
	var stream = this.getClass().getClassLoader().getResourceAsStream("payloadonly");
	assertTrue(validator.validatePayloadSignature(stream.readAllBytes()));
    }

}
