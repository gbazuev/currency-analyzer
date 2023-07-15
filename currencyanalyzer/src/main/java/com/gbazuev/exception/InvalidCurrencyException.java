package com.gbazuev.exception;

public class InvalidCurrencyException extends Exception {
    public InvalidCurrencyException(String currency)  {
        super("\"" + currency + "\" - incorrect currency name!");
    }
}
