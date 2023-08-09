package com.gbazuev.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseParser {
    private String response;

    public int getNominal(String id) {
        int nominalStart = response.indexOf("<Nominal", response.indexOf(id));
        int nominalEnd = response.indexOf("</Nominal>", response.indexOf(id));
        return Integer.parseInt(response.substring(nominalStart + 9, nominalEnd));
    }

    public String getValue(String id) {
        int valueStart = response.indexOf("<Value>", response.indexOf(id));
        int valueEnd = response.indexOf("</Value>", response.indexOf(id));
        if (response.indexOf(id) == -1) return "NOT STATED";
        return response.substring(valueStart + 7, valueEnd).replace(',', '.');
    }

    public String getRangeDate(String id) {
        int dateStart = response.indexOf("<Record Date");
        int dateEnd = response.indexOf(id, dateStart);
        return response.substring(dateStart + 14, dateEnd - 6);
    }

    //Данный метод нужен для того, чтобы правильно парсился ответ с временным периодом (дата1 - дата2)
    public void clearResponseRecord() {
        StringBuilder builder = new StringBuilder(response);
        response = builder.delete(builder.indexOf("<Record"), builder.indexOf("</Record>") + 9).toString();
    }
}