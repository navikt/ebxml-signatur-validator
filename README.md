# ebxml-signatur-validator
Koden kan fungere som eksempel for signatur verifikasjon på både Ebxml og fagmelding.
I denne eksempel for verifikasjon er brukt publik sertifikaten som kommer med meldingen. I Nav E-mottak er den videre verifisert mot CPA sertifikaten som bør matche 
publik sertifikaten i signaturen.

For å teste en melding dere kan bruke den unit test: https://github.com/navikt/ebxml-signatur-validator/blob/main/src/test/java/no/nav/emottak/validation/SignatureTest.java

Den bruker tre filer som fungerer som eksempler for melding som verifiseres OK /test/resources/riktigsignature
Payload melding som verifiseres OK /test/resources/riktigsignature
Ebxml melding hvor ebxml signatur er FEIL /test/resources/tekkopplysning

VIKTIG!!!! Hvis dere vil teste sin egen ebxml melding. Det som er veldig viktig er at riktig Content-Type header er satt på toppen av filen.
Hvis vi tar som eksempel "resources/riktigsignature" filen begynner med:

Content-Type: multipart/related;
	boundary="------=_part_2ae90b8a_5e15_4f4c_95f8_550ed07c0245";
	start="<soapId-241d49ce-ed1d-4919-a6fa-b65a75495ff3>"
	
Hvor boundary pekker på første element i multipart meldingen. Mind the number of dashes!!!!!!!!!  "--" element som står i boundry har to "--" mindre en det som står i multipart requesten.
