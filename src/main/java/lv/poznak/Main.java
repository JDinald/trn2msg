package lv.poznak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws IOException {

    List<String> commandList = new ArrayList<>();

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd");
    LocalDateTime now = LocalDateTime.now();

    logger.info("Start of file conversion");

    List<Transaction> transactionList = new ArrayList<>();
    String fileToConvert = "file.txt";
    String convertedFile = "trn2msg" + dtf.format(now) + ".xml";

    if (args.length == 0) {
      logger.info("No arguments were given");
    } else {
      for (String a : args) {
        commandList.add(a);
      }
      fileToConvert = commandList.get(0);

      if (commandList.get(1) == null) {
        logger.info("No converted file name");
      } else {
        convertedFile = commandList.get(1);
      }
    }

    Scanner scanner = new Scanner(new File(fileToConvert));
    scanner.useDelimiter("\n");

    while (scanner.hasNext()) {
      Transaction transaction = new Transaction();

      String next = scanner.next();
      transaction.setTransactionType(next.substring(0, 2));
      transaction.setPrimaryAccountNumber(next.substring(2, 18));
      transaction.setAmountInSubmits(next.substring(18, 30));
      transaction.setTransactionTime(next.substring(30, 44));
      transaction.setCurrencyCode(next.substring(44, 47));

      transactionList.add(transaction);
    }
    Notification notification = new Notification();
    notification.WriteNotificationXml(transactionList, convertedFile);
    scanner.close();

    logger.info("End of file conversion");
  }
}
