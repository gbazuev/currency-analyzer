package com.gbazuev.request;

import com.gbazuev.exception.CbrDateResponseException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Validator {


    public static String validateDate(String date) throws CbrDateResponseException {
         return checkDateLength(validateDateSeparators(date));
    }

    private static String validateDateSeparators(String date)    {
        StringBuilder builder = new StringBuilder(date);
        char[] separators = {'.', '-', '\\', '_', '|'};

        for (int i = 0; i < builder.length(); i++) {
            for (char separator : separators)   {
                if (builder.charAt(i) == separator)    {
                    int start = builder.indexOf(String.valueOf(separator));
                    builder.replace(start, start + 1, "/");
                }
            }
        }
        return builder.toString();
    }

    private static String checkDateLength(String date) {
        StringBuilder builder = new StringBuilder(date);
        if (builder.substring(0, builder.indexOf("/")).length() != 2) {
            builder.insert(0, '0');
        }
        if (builder.substring(builder.indexOf("/") + 1, builder.lastIndexOf("/")).length() != 2) {
            builder.insert(builder.indexOf("/") + 1, '0');
        }
        return builder.toString();
    }
}
