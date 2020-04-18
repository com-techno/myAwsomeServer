package util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import objects.User;
import org.graalvm.compiler.lir.LIRInstruction;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class HashUtils {

    static final long validTime = 2000000 * 1000;

    public static String getToken(User user) throws NoSuchAlgorithmException {
        long expires = new Date().getTime() + validTime;
        return user.username + "@" + expires + "@" + genHash(user.username, expires);
    }

    public static String getURLEncodedToken(User user) throws NoSuchAlgorithmException {
        return URLEncoder.encode(getToken(user));
    }

    public static boolean checkToken(String token) {
        String[] data = URLDecoder.decode(token).split("@");
        long expires = Long.parseLong(data[1]);
        long now = new Date().getTime();
        if (expires < now) return false;
        data[2] = URLDecoder.decode(data[2]);
        data[2] = data[2].replace(" ", "+");
        try {
            String trueHash = URLDecoder.decode(genHash(data[0], expires));
            if (data[2].equals(trueHash)) return true;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String genHash(String username, long expires) throws NoSuchAlgorithmException {
        MessageDigest sha512 = MessageDigest.getInstance("sha-512");
        return URLEncoder.encode(Base64.encode(sha512.digest((username + expires + "techno salt")
                .replace(" ", "g")
                .replace("!", "A")
                .replace("*", "B")
                .replace("'", "C")
                .replace("(", "D")
                .replace(")", "E")
                .replace(";", "F")
                .replace(":", "G")
                .replace("@", "H")
                .replace("&", "I")
                .replace("=", "J")
                .replace("+", "K")
                .replace("$", "L")
                .replace(",", "M")
                .replace("/", "N")
                .replace("?", "O")
                .replace("#", "P")
                .replace("[", "Q")
                .replace("]", "R")
                .getBytes())));
    }

}