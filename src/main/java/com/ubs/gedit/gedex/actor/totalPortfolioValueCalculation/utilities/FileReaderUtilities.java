package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileReaderUtilities {

    public static Map<String, Integer> readTickers() throws IOException {
//        InputStream is = this.getClass().getResourceAsStream("/resources/text/fileInput.txt");

//        final BufferedReader reader = new BufferedReader(new FileReader("/Users/kangbiu/Documents/Computer Science/Akka/exmaple-code-book/divideAndConquer/stocks.txt"));
        final BufferedReader reader = new BufferedReader(new FileReader("./resources/stocks.txt"));

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
}
