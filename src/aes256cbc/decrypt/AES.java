package aes256cbc.decrypt;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
 
public class AES
{
 
    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static String ivParamStr = "c52d5a235269ed84";
    private static byte[] iv = ivParamStr.getBytes();
    private static IvParameterSpec ivParam = new IvParameterSpec(iv);
    private static final Charset UTF_8 = Charset.forName("UTF-8");
 
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 32);
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParam);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    
    public static String encryptMessageExample()
    {
    	String strToEncrypt = "Ala ma kota. Kot ma, ale... to jednak ona go posiada. Jednakże gdy przeczytamy to ponownie to...";
    	String secret = "4940ac6224143110db8c3808975a751ea071ec32147d04f2ff886e217f8f451b";
    	
    	String encryptMessage = encrypt(strToEncrypt, secret);
    	System.out.println("Encrypt message: " + encryptMessage + "\nSecret Key Length: " + secret.length());
    	
    	return encryptMessage;
    }
    
    public static void decryptMessageExample()
    {
    	String strToDecrypt = encryptMessageExample();
    	
    	// ciag wygenerwany przez openssl'a w Base64 - dlugosc 152
    	String strToDecrypt2 = "oCy1ziP08UWtsQC6nZKOtUZducQdxFyTcwT37YkhNIh/lXLpsyDZt9vKSNKyb61tRzYSISfcsNFFy/Nio4tlX2EwNtMyO2DUPOYUyFKP9eDfzsyR+lXD+YRwcr0Fl3DwyNts+c3dysiMt46KROtiZg==";

    	// ciag wygenerowany przez funkcje 'decrypt()' - dlugosc tez 152 ...
    	String secret = "4940ac6224143110db8c3808975a751ea071ec32147d04f2ff886e217f8f451b";
    	String decryptMessage = decrypt(strToDecrypt, secret);
    	
    	System.out.println("Decrypt message: " + decryptMessage + "\nDecrypt Message Length: " + strToDecrypt.length());
    }
    
    public static void encryptDecryptTest()
    {
    	//encryptMessageExample();
    	decryptMessageExample();
    	
    }
/*    
    public static void generateEncodedMessage()
    {
    	String firstHalfSecretKey = "";
    	String alphanumeric = "abcdef0123456789";
    	int alpLen = alphanumeric.length();
    	Random r = new Random();
    	for(int i = 0; i < 8; i++) { 
    	    char a = alphanumeric.charAt(r.nextInt(alpLen));
    	    firstHalfSecretKey = firstHalfSecretKey + a;
    	}
        
        String lastHalfSecretKey = "e602d1c1";
        String secretKey = firstHalfSecretKey + lastHalfSecretKey;   	
        String encryptedTextBinary = "00110000 01100111 10010011 00010010 11101101 01011100 00010111 00111101 11001010 11101001 10110110 00110001 01000100 01110011 11111110 10101101 11110110 01010000 01000011 11111001 10111110 11011111 10100100 10010100 00101001 10100011 11111101 10011000 10110001 10001001 11001011 10011010 11010011 10010110 01111010 10000001 11011001 10011011 10110111 10011010 10001001 10110100 10010010 11110001 01010111 10010111 10010011 10110010 10011110 10000110 00011000 10101111 00010100 11100111 10001110 10101101 00000000 11011001 11011010 11101111 11010001 00111101 10110111 00000001 01010101 01101001 11010101 10101011 10010111 10100111 00000010 10000011 00001101 01011000 11111010 10101001 10011001 00001110 00111011 11001101 01001011 01011110 11010001 01000110 10001000 10101110 10100101 01110100 11100110 00001101 01011111 11001111 11010100 11000000 01000001 01001101 00100100 01000101 10101100 11010110 10100000 11101000 10100111 01111110 00010101 10111110 10010101 10101010 00000111 01111001 10111011 00101110";
        String asB64 = null;
        
        try {
			asB64 = Base64.getEncoder().encodeToString(encryptedTextBinary.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        String decryptedString = AES.decrypt(asB64, secretKey);
        
        if(!(decryptedString.contains("�"))) {
        	System.out.println(secretKey + "\n" + decryptedString);
        }
    }
    
    public static void encryptMessage()
    {
    	String sign = "";
    	String alphanumeric = "abcdef0123456789";
    	int alpLen = alphanumeric.length();
    	Random r = new Random();
    	for(int i = 0; i < 8; i++) { 
    	    char a = alphanumeric.charAt(r.nextInt(alpLen));
    	    sign = sign + a;
    	}
    	
    	
    	String key = sign + "dfe602d1c1"; // d8ac31dfe602d1c1
    	String key2 = sign + "e602d1c1dc837df8bfde8b0b4ed283ebcb20f3203ba678d0a6fae5ea";
    	String decryptMessage = "5p0P5JersCDWxdTEGhBTTbRVZaFob47fuynjUd6vUJhN8AYyS7kDlsrPF+byS+dD7Kk3kzc3flKX2AV0EirqEg==";
    	String decryptMessage2 = "Z2U0ItDe8Yh45C8vMRDsfRBSIGF/pRvsSwJEZ/36/QKiwfZu4ls41GoeyxcWY8KjqKN9mJ8wNJz2leLzdEOWZXXBmx8Bq6zu5Nd53O7Krbqm8n+P/XmvUOyEhIV6KroJ4FOPMImL26lyqhFk+nAo1Q==";
    	String messagee = AES.decrypt(decryptMessage2, key2);
    	
    	if(!(messagee.contains("�"))) {
    		System.out.println(key2 + " " + messagee);
        }
    }
*/
    public static void bruteForce()
    {
    	String decryptMessage3 = "";
    	int i, j, k, l, m, n, o, p;
    	String d1, d2, d3, d4, d5, d6, d7;
    	String possibleKey1 = "";
    	String possibleKey = "";
    	String keySufix = "e602d1c1dc837df8bfde8b0b4ed283ebcb20f3203ba678d0a6fae5ea";
    	String keyX = "4940ac6224143110db8c3808975a751ea071ec32147d04f2ff886e217f8f451b";
    	String digits = "0123456789abcdef";
    	String decryptMessage = "";
    	
    	for(i = 0; i < 16; i++){
            d1 = digits.substring(i, i+1 );
            for( j = 0; j < 16; j++ ) {
                d2 = d1 + digits.substring(j, j+1 );
                for( k = 0; k < 16; k++ ) {
                    d3 = d2 + digits.substring(k, k+1 );
                    for( l = 0; l < 16; l++) {
                        d4 = d3 + digits.substring( l, l+1 );
                        for( m = 0; m < 16; m++) {
                        	d5 = d4 + digits.substring( m, m+1 );
                        	for( n=0; n < 16; n++) {
                        		d6 = d5 + digits.substring( n, n+1 );
                        		for( o = 0; o <16; o++) {
                        			d7 = d6 + digits.substring( o, o+1 );
                        			for( p = 0; p < 16; p++) {
                        				possibleKey1 = d7 + digits.substring( p, p+1 );
                        				possibleKey = possibleKey1 + keySufix;
                                        //System.out.println(possibleKey);
                                        String messagee = AES.decrypt(decryptMessage, possibleKey);
                                    	
                                    	if(!(messagee.contains("�"))) {
                                    		System.out.println(possibleKey + " " + messagee);
                                        }
                        			}
                        		}
                        	}
                        }
                    }
                }
            }
        }
    	
    	String messagee = AES.decrypt(decryptMessage3, keyX);
    	System.out.println(keyX + " " + messagee);
    	System.out.println("KONIEC");
    }
    
    public static String int2str( String s ) { 
        String[] ss = s.split( " " );
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < ss.length; i++ ) { 
            sb.append( (char)Integer.parseInt( ss[i], 2 ) );                                                                                                                                                        
        }   
        return sb.toString();
    }   
    
    public static void encodeMessage()
    {
    	String message = "Ala ma kota. Kot ma, ale... to jednak ona go posiada. Jednakże gdy przeczytamy to ponownie to...";
    	String keyM = "4940ac6224143110db8c3808975a751e";
    	
    	String message2 = "FZsU8Vw4PyazLjBGS5uVAf812tzkVbwdOvVjk6DvXPNl63gP4P3QHX4akYzG5couYpdwoEhA9leYO9BrOlixmzvwX4BX3249xOKaPb2fkdaYzv/lUKEn6MHRn2uODlLDjPOshNUVKd4hx6uxSnjOXw==";
    	String message22 = "11011110 00110100 00110000 01011111 01010001 11110010 11111001 00011000 01011000 10000010 01101100 01101110 10010111 10001010 11101001 01011110 10001101 00101011 10001001 00001111 01011101 01100011 10000000 11111010 10110100 10100101 11010111 10110010 00010100 10010011 11011110 00111000 11111010 11101110 01110101 00010000 10110010 00010100 00110101 00001111 10110110 01001010 11000100 00000001 01100110 10100011 10011010 01010001 01101110 10001000 10101011 10000101 11100100 01100100 00010101 00101101 10100101 00101000 00101010 00101001 10111001 11101110 00111011 00110110 11101000 01100000 11011011 10100110 00001110 01101110 00000000 10110000 10111111 10110111 00011010 11101011 11111101 11010011 10010100 00010010 11000011 10001101 01111100 00011001 01111110 00110001 10001111 11100110 10010000 10111101 10011111 00110010 01110111 01110111 11010011 10100000 00100001 00110010 01110011 01000001 00001110 00001101 01101110 00110101 01100111 01101111 00101011 01011001 11000111 11010100 11101010 ";
    	String message23 = message22.replaceAll(" ", "");
    	String ivM = "c52d5a235269ed84";
    	
    	String message333 = "VuRefUwC5U2iFDa3ovxHfKRKJEd9cMYowUtcQDPMywybZ1vn5C4lSN82x6gEhPRw8e6S1z5V97NJc+FuoOroLeuJU2Crv8Ije2IYxs60b9Ye1PGFxt4eiXLeUQph6DSkMTpY1OMXpTlnzxEKI2rX+g==";
    	//System.out.println(message333);
    	
    	//String encodeMessage = AES.encrypt(message, keyM);
    	//System.out.println(encodeMessage);
    	String decryptMessage = AES.decrypt(message333, keyM);
    	System.out.println(decryptMessage);
    }
    
    public static void main(String[] args) 
    {
    	//bruteForce();
//    	encodeMessage();
    	encryptDecryptTest();
    	//encryptMessageBinary();
    	/*while(true) {
    		generateEncodedMessage();
    	}*/
    }
}
