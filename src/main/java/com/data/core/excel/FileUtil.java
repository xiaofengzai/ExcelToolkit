package com.data.core.excel;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created by szty on 2018/9/3.
 */
public class FileUtil {
    public static   String getFileHeader(BufferedInputStream inputStream, int bytes) throws IOException {
        byte[] b = new byte[bytes];
        inputStream.mark(b.length+1);
        try {
            inputStream.read(b, 0, b.length);
        } finally {
            inputStream.reset();
        }
        return bytesToHex(b);
    }
    private static String bytesToHex(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toLowerCase();
    }
}
