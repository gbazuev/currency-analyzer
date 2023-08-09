package com.gbazuev.connection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Url {

    public static String buildTodayUrl()   {
        return "https://cbr.ru/scripts/XML_daily.asp?";
    }

    public static String buildUrlWithDate(String date)  {
        return "https://cbr.ru/scripts/XML_daily.asp?date_req=" + date;
    }

    public static String buildUrlWithRange(String dateStart, String dateEnd, String valuteId)    {
        return "https://cbr.ru/scripts/XML_dynamic.asp?date_req1=" + dateStart + "&date_req2=" + dateEnd + "&VAL_NM_RQ=" + valuteId;
    }
}
