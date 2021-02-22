package lv.poznak;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Notification {

  public static int totalAmount;

  public void WriteNotificationXml(List<Transaction> transactionList) {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    DateTimeFormatter dtfForLog = DateTimeFormatter.ofPattern("yyMMdd");
    LocalDateTime now = LocalDateTime.now();

//    try {
//      File logFile = new File("trn2msg_" + dtfForLog.format(now)+ ".log");
//      if (logFile.createNewFile()) {
//        System.out.println("File created: " + logFile.getName());
//      } else {
//        System.out.println("File already exists.");
//      }
//    } catch (IOException e) {
//      System.out.println("An error occurred.");
//      e.printStackTrace();
//    }



    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

      Document document = documentBuilder.newDocument();
      Element rootElement = document.createElement("root");
      document.appendChild(rootElement);

      Element msgList = document.createElement("msg-list");
      rootElement.appendChild(msgList);

      transactionList.stream()
          .forEach(
              transaction -> {
                Element msg = document.createElement("msg");
                msg.appendChild(
                    document.createTextNode(
                        formatTransactionType(transaction.getTransactionType())
                            + " with card "
                            + maskCardNumber(
                                transaction.getPrimaryAccountNumber(), "######******####")
                            + " on "
                            + formatDateAndTime(transaction.getTransactionTime())
                            + ", amount "
                            + formatAmount(transaction.getAmountInSubmits())
                            + " "
                            + getCurrencyInstance(Integer.parseInt(transaction.getCurrencyCode()))
                                .toString()
                                .toLowerCase(Locale.ROOT)
                            + "."));
                msgList.appendChild(msg);
              });

      Element totals = document.createElement("totals");
      totals.appendChild(
          document.createTextNode(
              "cnt="
                  + "\""
                  + transactionList.size()
                  + "\" "
                  + "sum="
                  + "\""
                  + totalAmount
                  + "\" "
                  + "date="
                  + "\""
                  + dtf.format(now)
                  + "\""));
      rootElement.appendChild(totals);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(document);
      //TODO file.xml
      StreamResult result = new StreamResult(new File("file.xml"));

      transformer.transform(source, result);

      System.out.println("File saved!");

    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    }
  }

  public static Currency getCurrencyInstance(int numericCode) {
    Set<Currency> currencies = Currency.getAvailableCurrencies();
    for (Currency currency : currencies) {
      if (currency.getNumericCode() == numericCode) {
        return currency;
      }
    }
    // TODO LOG
    throw new IllegalArgumentException("Currency with numeric code " + numericCode + " not found");
  }

  public static String formatAmount(String amount) {
    NumberFormat format = NumberFormat.getCurrencyInstance();
    Integer intAmount = Integer.parseInt(amount);
    totalAmount += intAmount;
    if (intAmount != 0) {
      String result = format.format(Integer.parseInt(amount) / 100.0);
      return result.replace("$", "");
    } else return "00.00";
  }

  public static String formatTransactionType(String transactionType) {
    if (transactionType.equals("00")) {
      return "Purchase";
    } else if (transactionType.equals("01")) {
      return "Withdrawal";
    } else {
      // TODO LOG
      return "ERROR";
    }
  }

  public static String formatDateAndTime(String transactionTime) {
    String transactionDate = transactionTime.substring(0, 8);
    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
    String formatDate = new String();
    String formatTime = new String();
    try {
      Date date = originalFormat.parse(transactionDate);
      SimpleDateFormat newFormat = new SimpleDateFormat("dd.MM.yyyy");
      formatDate = newFormat.format(date);
    } catch (ParseException e) {
      // TODO log
    }
    String transactionAccurateTime = transactionTime.substring(8, 14);
    try {

      SimpleDateFormat format = new SimpleDateFormat("hhmmss"); // if 24 hour format
      Date d1 = format.parse(transactionAccurateTime);
      Time convertedTime = new Time(d1.getTime());
      formatTime = convertedTime.toString();

    } catch (ParseException e) {
      // TODO log
    }
    return formatDate + " " + formatTime;
  }

  public static String maskCardNumber(String cardNumber, String mask) {
    int index = 0;
    StringBuilder maskedNumber = new StringBuilder();
    for (int i = 0; i < mask.length(); i++) {
      char c = mask.charAt(i);
      if (c == '#') {
        maskedNumber.append(cardNumber.charAt(index));
        index++;
      } else if (c == '*') {
        maskedNumber.append(c);
        index++;
      } else {
        maskedNumber.append(c);
      }
    }
    return maskedNumber.toString();
  }
}
