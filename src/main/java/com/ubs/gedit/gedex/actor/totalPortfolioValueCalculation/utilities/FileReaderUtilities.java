package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileReaderUtilities {

    public static Map<String, Integer> readTickers(final String fileName) throws IOException {

        final BufferedReader reader = new BufferedReader(new FileReader("./resources/" + fileName));

        final Map<String, Integer> stocks = new HashMap<String, Integer>();

        String stockInfo = null;
        while((stockInfo = reader.readLine()) != null) {
          final String[] stockInfoData = stockInfo.split(",");
          final String stockTicker = stockInfoData[0];
          final Integer quantity = Integer.valueOf(stockInfoData[1]);

          stocks.put(stockTicker, quantity);
        }

        return stocks;
    }

    public static Map<String, Integer> readTickers() throws IOException {
        return readTickers("stocks.txt");
    }
}
