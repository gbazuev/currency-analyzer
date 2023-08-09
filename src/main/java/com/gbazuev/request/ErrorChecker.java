package com.gbazuev.request;


import com.gbazuev.exception.CbrDateResponseException;
import com.gbazuev.exception.InvalidCurrencyException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorChecker {

    public static void checkResponseForDateError(String response, String... date) throws CbrDateResponseException {
        if (response.contains("Error in parameters")) {
            if (date.length == 1) {
                throw new CbrDateResponseException(date[0]);
            } else {
                throw new CbrDateResponseException(date[0], date[1]);
            }
        }
    }

    public static void checkCurrencyForAvalability(String currency, String id) throws InvalidCurrencyException {
        if (id == null) {
            throw new InvalidCurrencyException(currency);
        }
    }
}
