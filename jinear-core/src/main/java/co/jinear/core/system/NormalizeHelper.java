package co.jinear.core.system;

import lombok.experimental.UtilityClass;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.Locale;

@UtilityClass
public class NormalizeHelper {

    private static final String LOCALE_EN = "en-EN";
    public static final String EMPTY_STRING = "";
    public static final String MASK_CHAR = "*";
    private static final String ACCENT_REGEX = "\\p{M}";
    private static final String ALPHANUMERIC_REGEX = "[^A-Za-z0-9]";
    private static final String ASCII_REGEX = "[^\\x00-\\x7F]";

    public static String removeAccent(String str) {
        return Normalizer.normalize(str.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll(ACCENT_REGEX, EMPTY_STRING)
                .toLowerCase(new Locale(LOCALE_EN));
    }

    public static String removeNonAlphaNumeric(String str) {
        return Normalizer.normalize(str.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll(ALPHANUMERIC_REGEX, EMPTY_STRING)
                .toLowerCase(new Locale(LOCALE_EN));
    }

    public static String removeNonAscii(String str) {
        return Normalizer.normalize(str.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll(ASCII_REGEX, EMPTY_STRING)
                .toLowerCase(new Locale(LOCALE_EN));
    }

    public static String normalizeStrictly(String str) {
        return removeNonAlphaNumeric(removeNonAscii(removeAccent(str)));
    }

    public static String maskString(String str) {
        return maskBetweenChars(str, 0, str.length());
    }

    public static String maskBetweenChars(String str, int maskStart, int maskEnd) {
        String maskedPart = MASK_CHAR.repeat(maskEnd - maskStart);
        String preMaskedPart = str.substring(0, maskStart);
        String postMaskedPart = str.substring(maskEnd);
        return preMaskedPart + maskedPart + postMaskedPart;

    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String zort = "@012345";
        System.out.println(maskBetweenChars(zort, 2, 4));
        System.out.println(maskBetweenChars(zort, 0, zort.length()));
        System.out.println(normalizeStrictly(zort));
    }
}
