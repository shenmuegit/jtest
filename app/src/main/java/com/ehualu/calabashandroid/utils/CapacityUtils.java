package com.ehualu.calabashandroid.utils;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.app.MyApp;

import java.math.BigDecimal;

/**
 * add by houxiansheng 2019-12-6 15:20:30 容量工具类
 */
public final class CapacityUtils {

    private static final String[] sizeSuffixes = {"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
    private static final int[] sizeScales = {0, 0, 1, 1, 1, 2, 2, 2, 2};
    private static final int BYTE_SIZE_DIVIDER = 1024;
    private static final double BYTE_SIZE_DIVIDER_DOUBLE = 1024.0;

    private CapacityUtils() {
    }

    /**
     * Converts the file size in bytes to human readable output.
     * <ul>
     * <li>appends a size suffix, e.g. B, KB, MB etc.</li>
     * <li>rounds the size based on the suffix to 0,1 or 2 decimals</li>
     * </ul>
     *
     * @param bytes Input file size
     * @return something readable like "12 MB"
     * byte values
     */
    public static String bytesToHumanReadable(long bytes) {
        if (bytes < 0) {
            return MyApp.getAppContext().getString(R.string.common_pending);
        } else {
            double result = bytes;
            int suffixIndex = 0;
            while (result > BYTE_SIZE_DIVIDER && suffixIndex < sizeSuffixes.length) {
                result /= BYTE_SIZE_DIVIDER_DOUBLE;
                suffixIndex++;
            }

            return new BigDecimal(String.valueOf(result)).setScale(
                    sizeScales[suffixIndex], BigDecimal.ROUND_HALF_UP) + " " + sizeSuffixes[suffixIndex];
        }
    }
}
