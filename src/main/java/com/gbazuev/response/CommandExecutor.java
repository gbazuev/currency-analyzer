package com.gbazuev.response;

import com.gbazuev.connection.Downloader;
import com.gbazuev.connection.Url;
import com.gbazuev.exception.CbrDateResponseException;
import com.gbazuev.exception.InvalidCurrencyException;
import com.gbazuev.exception.UserRequestException;
import com.gbazuev.request.CurrencyStorage;
import com.gbazuev.request.ErrorChecker;
import com.gbazuev.request.Validator;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

@NoArgsConstructor
public class CommandExecutor {
    private final static Scanner scanner = new Scanner(System.in);
    private final static CurrencyStorage storage = new CurrencyStorage();
    private final static ResponseParser parser = new ResponseParser();

    public static void execute() throws Exception {
        printDescription();

        while (true) {
            String[] message = scanner.nextLine().split(":\\s");
            String command = message[0];
            try {
                switch (command) {
                    case "--get --today" -> printToday(message);
                    case "--get --date" -> printDesiredDate(message);
                    case "--get --interval" -> printInterval(message);
                    case "--exit" -> System.exit(0);
                    case "--help" -> printDescription();
                    default -> throw new UserRequestException(Arrays.toString(message));
                }
            } catch (UserRequestException e) {
                System.out.println(e.getMessage());
            }

            //splitting requests and responses
            System.out.print("\n");
        }
    }

    static void printDescription() throws IOException {
       ClassLoader classLoader = CommandExecutor.class.getClassLoader();
       InputStream inputStream = classLoader.getResourceAsStream("description.txt");
       System.out.println(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
    }

    static void printToday(String[] message) throws IOException {
        parser.setResponse(Downloader.downloadWebPage(Url.buildTodayUrl()));

        //If message contains only --get --today
        if (message.length == 1) {
            for (String name : storage.getNames()) {
                String id = storage.getCurrencyId(name);
                String value = parser.getValue(id);
                if (value.equals("NOT STATED")) {
                    continue;
                }
                System.out.println(parser.getNominal(id) + " " + name + " = " + value);
            }
        }

        //If request contains currencies
        else {
            String[] currencies = message[1].split(",\\s");
            for (String currency : currencies) {
                try {
                    String id = storage.getCurrencyId(currency);
                    ErrorChecker.checkCurrencyForAvalability(currency, id);
                    System.out.println(parser.getNominal(id) + " " + currency + " = " + parser.getValue(id));
                } catch (InvalidCurrencyException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    static void printDesiredDate(String[] message) throws Exception {
        String[] arguments = message[1].split(",\\s");

        //API call
        try {
            String date = Validator.validateDate(arguments[0]);
            String url = Url.buildUrlWithDate(date);
            parser.setResponse(Downloader.downloadWebPage(url));
            ErrorChecker.checkResponseForDateError(parser.getResponse(), date);
        } catch (CbrDateResponseException e) {
            System.err.println(e.getMessage());
            return;
        }

        //If request contains only a date
        if (arguments.length == 1) {
            for (String name : storage.getNames()) {
                String currencyId = storage.getCurrencyId(name);
                String value = parser.getValue(currencyId);
                if (value.equals("NOT STATED")) {
                    continue;
                }
                System.out.println(parser.getNominal(currencyId) + " " + name + " = " + parser.getValue(currencyId));
            }
        }

        //if request also contains currencies
        else {
            for (int i = 1; i < arguments.length; i++) {
                try {
                    String id = storage.getCurrencyId(arguments[i]);
                    ErrorChecker.checkCurrencyForAvalability(arguments[i], id);
                    System.out.println(parser.getNominal(id) + " " + arguments[i] + " = " + parser.getValue(id));
                } catch (InvalidCurrencyException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    static void printInterval(String[] message) throws Exception {
        String[] arguments = message[1].split(",\\s");
        String currency = arguments[2];
        String id = storage.getCurrencyId(currency);

        try {
            ErrorChecker.checkCurrencyForAvalability(currency, id);

            String firstDate = Validator.validateDate(arguments[0]);
            String secondDate = Validator.validateDate(arguments[1]);
            String url = Url.buildUrlWithRange(firstDate, secondDate, id);

            parser.setResponse(Downloader.downloadWebPage(url));
            ErrorChecker.checkResponseForDateError(parser.getResponse(), firstDate, secondDate);
        } catch (InvalidCurrencyException | CbrDateResponseException e) {
            System.err.println(e.getMessage());
            return;
        }

        System.out.println(parser.getNominal(id) + " " + currency);
        while (true) {
            try {
                System.out.println(parser.getRangeDate(id) + " = " + parser.getValue(id));
                parser.clearResponseRecord();
            } catch (StringIndexOutOfBoundsException e) {
                break;
            }
        }
    }
}
