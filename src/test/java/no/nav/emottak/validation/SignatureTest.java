package no.nav.emottak.validation;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SignatureTest {
    
    private static Logger logger = LoggerFactory.getLogger(SignatureTest.class);
    
    
    @Test
    public void messageWithFailingSignature() throws Exception {
	DokumentValidator validator = new DokumentValidator();
	var stream = this.getClass().getClassLoader().getResourceAsStream("tekkopplysning");
	validator.validateEbxmlSignature(stream.readAllBytes());
    }
    
    @Test
    public void messageWithCorrectSignature() throws Exception {
	DokumentValidator validator = new DokumentValidator();
	var stream = this.getClass().getClassLoader().getResourceAsStream("riktigsignature");
	validator.validateEbxmlSignature(stream.readAllBytes());
    }
    
    @Test
    public void validatePayloadOnly() throws Exception {
	DokumentValidator validator = new DokumentValidator();
	var stream = this.getClass().getClassLoader().getResourceAsStream("payloadonly");
	validator.validatePayloadSignature(stream.readAllBytes());
    }

}
