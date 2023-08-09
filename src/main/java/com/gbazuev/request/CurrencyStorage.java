package com.gbazuev.request;

import com.gbazuev.response.CommandExecutor;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class CurrencyStorage {
    private final HashMap<String, String> STORAGE = new HashMap<>();
    @Getter
    private final ArrayList<String> NAMES = new ArrayList<>();

    public CurrencyStorage() {
        try {
            ClassLoader classLoader = CommandExecutor.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("currencyresources.json");
            StringBuilder file = new StringBuilder(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));

            for (int i = 1; i < 69; i++) {
                String current = file.substring(file.indexOf(String.valueOf(i)), file.indexOf("]"));
                String rusName = current.substring(current.indexOf("name_ru") + 11, current.indexOf(",") - 1);
                String id = current.substring(current.indexOf("id") + 6, current.lastIndexOf("\""));

                NAMES.add(rusName);
                STORAGE.put(rusName.toUpperCase(), id);

                file.delete(file.indexOf(String.valueOf(i)) - 1, file.indexOf("]") + 2);
            }
        } catch (IOException e)    {
            System.err.println("Unable to load storages!");
            System.exit(1);
        }
    }

    public String getCurrencyId(String name) {
        return STORAGE.get(name.toUpperCase());
    }
}