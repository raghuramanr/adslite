package com.raghu.adslite.utils;

/*
Implementation to convert long id to short URL.
 */
public class ShortUrlGenerator
{
    // 62 possible characters used in shortURL
    private static char map[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    // Function to generate a short url from long 64 bit ID
    public static String idToShortURL(long n)
    {
        StringBuffer shorturl = new StringBuffer();
        // Convert given integer id to a base 62 number
        while (n > 0)
        {
            int remainder = (int) Math.floorMod(n, 62L);
            shorturl.append(map[remainder]);
            n = n / 62;
        }
        // Reverse shortURL to complete base conversion
        return shorturl.reverse().toString();
    }
}
