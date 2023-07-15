package com.gbazuev.exception;

public class CbrDateResponseException extends Exception {
    public CbrDateResponseException(String date)   {
        super("\"" + date + "\" - incorrect date!");
    }

    public CbrDateResponseException(String firstDate, String secondDate)    {
        super("\"" + firstDate + " - " + secondDate + "\" - incorrect dates!");
    }
}
