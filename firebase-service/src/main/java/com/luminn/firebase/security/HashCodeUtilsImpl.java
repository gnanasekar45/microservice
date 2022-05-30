package com.luminn.firebase.security;


import com.luminn.firebase.config.ConfigUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;
import java.util.Random;

@Component
public class HashCodeUtilsImpl implements HashCodeUtils {

    private static final Logger log = LoggerFactory.getLogger(HashCodeUtilsImpl.class);

    @Value("${iterations}")
    private int iterations;

    @Value("${varSitLocation}")
    private int varSitLocation;

    @Value("${hashCount}")
    private int hashCount;

    @Value("${hashStart}")
    private int hashStart;

    @Value("${hashEnd}")
    private int hashEnd;

    @Value("${fixedSit}")
    private String fixedSit;

    //only for JUnit tests.
    //throws NumberFormatException
    protected void setPropertiesForTest(String propertiesFileName) {
        Properties properties = ConfigUtils.getProperties(propertiesFileName);
        iterations = Integer.valueOf(properties.getProperty("iterations"));
        varSitLocation = Integer.valueOf(properties.getProperty("varSitLocation"));
        hashCount = Integer.valueOf(properties.getProperty("hashCount"));
        hashStart = Integer.valueOf(properties.getProperty("hashStart"));
        hashEnd = Integer.valueOf(properties.getProperty("hashEnd"));
        fixedSit = (String) properties.get("fixedSit");
    }


    /**
     * I. Encrypt passwords using one-way techniques, this is, digests.
     * <p>
     * II. Match input and stored passwords by comparing digests, not unencrypted strings.
     * <p>
     * III. Use a salt containing at least 8 random bytes, and attach these random bytes, undigested, to the result.
     * <p>
     * IV. Iterate the hash function at least 1,000 times.
     * <p>
     * V. Prior to digesting, perform string-to-byte sequence translation using a fixed encoding, preferably UTF-8.
     * <p>
     * VI. Finally, apply BASE64 encoding and store the digest as an US-ASCII character string.
     *
     * @param passwordText
     * @return
     */
    public String generatePasswordFirstTime(String passwordText) {
        String generatedPassword = null;
        //varsit generated from random....
        String varSit = getVarSit();
        generatedPassword = generatePasswordGivenVSit(varSit, passwordText);
        return generatedPassword;
    }

    private String generatePasswordGivenVSit(String varSit, String passwordText) {
        String generatedPassword = null;
        try {
            byte[] varSitBytes = varSit.getBytes(Charset.forName("UTF-8"));
            String algorithm = "SHA-512";
            MessageDigest md = MessageDigest.getInstance(algorithm);
            String varFixedSit = fixedSit + varSit;
            md.update(varFixedSit.getBytes(Charset.forName("UTF-8")));
            byte[] bytes = md.digest(passwordText.getBytes(Charset.forName("UTF-8")));

            //i use a large number here since this login is only done once per relatively long time.
            for (int i = 0; i < iterations; i++) {
                bytes = md.digest(bytes);
            }

            byte[] combinedBytes = ArrayUtils.addAll(bytes, varSitBytes);
            generatedPassword = Base64.getEncoder().encodeToString(combinedBytes);

        } catch (NoSuchAlgorithmException ex) {
            log.error("HashCodeUtilsImpl.generatePasswordGivenVSit(): " + ex.getMessage());
        }
        return generatedPassword;
    }

    public boolean validPasswordCheck(String existingEncodedPassword, String passwordText) {
        boolean validStatus = false;

        byte[] existingDecodedPasswordBytes = Base64.getDecoder().decode(existingEncodedPassword);

        byte[] varSitBytes =
                ArrayUtils.subarray(
                        existingDecodedPasswordBytes,
                        varSitLocation,
                        existingDecodedPasswordBytes.length);

        String varSit = new String(varSitBytes, Charset.forName("UTF-8"));
        String generatedPassword = generatePasswordGivenVSit(varSit, passwordText);

        if (generatedPassword.equals(existingEncodedPassword)) {
            validStatus = true;
        }

        return validStatus;
    }

    private String getVarSit() {
        String sit = RandomStringUtils.random(
                hashCount,
                hashStart,
                hashEnd,
                false,
                false,
                null,
                new Random(System.currentTimeMillis()));
        //System.out.println("getVarSit(): sit=" + sit);

        return sit;
    }

}
