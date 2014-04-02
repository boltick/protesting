package org.protesting.jft.generator.symbols;

import org.protesting.jft.generator.generic.GenericGenerator;
import org.apache.commons.lang.CharRange;
import org.apache.commons.lang.CharSet;
import org.apache.commons.lang.RandomStringUtils;

import java.io.UnsupportedEncodingException;

/**
 * User: ab83625
 * Date: 26.02.2008
 * Time: 11:47:03
 */
public class StringGenerator implements GenericGenerator {

    private String charRange;

    protected static final String DEFAULT_ENC = "UTF-8";


    protected StringGenerator() {
    }

    protected StringGenerator(String charRange) {
        this.charRange = charRange;
    }

    public static StringGenerator getInstance(final String charRange) {
        return getInstance(CharSet.getInstance(charRange));
    }


    public static StringGenerator getInstance(final CharSet charSet) {
        CharRange[] charRanges = charSet.getCharRanges();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < charRanges.length; i++) {
            CharRange charRange = charRanges[i];
            char start = charRange.getStart();
            char end = charRange.getEnd();
            buffer.ensureCapacity(buffer.capacity() + (end - start));
            for (char ch = start; ch <= end; ++ch) {
                buffer.append(ch);
            }
        }

        return new StringGenerator(buffer.toString());
    }



    public static String getString(int length, String charRange, String encode) throws UnsupportedEncodingException {
        return normalizeString(getString(length, charRange), encode, length);
    }



    public static String getString(int length, String charRange) throws UnsupportedEncodingException {
        StringGenerator generator = getInstance(charRange);
        return generator.next(length);
    }


    public static String normalizeString(String s, String charset, int length) throws UnsupportedEncodingException {
        int size = s.getBytes(charset).length / s.length();
        while (true) {
            if (s.getBytes(charset).length / size > length) {
                s = s.substring(0, s.length() - 1);
            } else {
                break;
            }
        }
        return s;
    }

    /**
     * Method for creating new random string from stored source of characters
     *
     * @param length is the number of characters specified
     *
     * @return random string whose length is the number of characters specified
     */
    public String next(int length) {
        return next(length, (char)0);
    }


    public String next(int length, char exclude) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length should be greater than zero, but lenght = " + length);
        }
        if (exclude == 0) {
            return RandomStringUtils.random(length, charRange);
        } else {
            return RandomStringUtils.random(length, charRange.replaceAll(String.valueOf(exclude), ""));
        }
    }

    /**
     * Method for creating new random string in specified encoding from stored source of characters
     *
     * @param length       is the number of characters specified
     * @param encodingName - name of encoding.
     *
     * @return random string whose length is the number of characters specified
     *
     * @throws java.io.UnsupportedEncodingException
     *          - if specified encoding is not supported
     */
    public String next(int length, final String encodingName) throws UnsupportedEncodingException {
        if (length <= 0) {
            throw new IllegalArgumentException("Length should be greater than zero, but lenght = " + length);
        }
        if (DEFAULT_ENC.equals(encodingName)) {
            return next(length);
        }
        String res = RandomStringUtils.random(length, charRange);
        return new String(res.getBytes(DEFAULT_ENC), encodingName);
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        String test = getString(100, "\uff61-\uff9f");
        System.out.println(test);
    }


    public void setCharRange(String charRange) {
        this.charRange = charRange;
    }


    public String getCharRange() {
        return charRange;
    }

    public Object getSmallerThan(Object leftBorder) {
        int left = ((Integer) leftBorder).intValue();
        return next(--left);
    }

    public Object getGreaterThan(Object rightBorder) {
        int right = ((Integer) rightBorder).intValue();
        return next(++right);
    }

    public Object getValue(Object leftBorder, Object rightBorder) {
        int left = ((Integer) leftBorder).intValue();
        int right = ((Integer) rightBorder).intValue();

        String part1 = next(left);
        // TODO random
        String part2 = next(right - left);
        return part1 + part2;
    }

    public boolean validate(Object leftBorder, Object rightBorder) {
        int left = ((Integer) leftBorder).intValue();
        int right = ((Integer) rightBorder).intValue();
        if (left > right) {
            throw new IllegalArgumentException("left border ["+ left +"] is bigger than right [" + right + "]");
        }
        
        return left - right > Math.abs(1);
    }

}
