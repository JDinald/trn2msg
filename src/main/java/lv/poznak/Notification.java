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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class Notification {

  NotificationUtils notificationUtils = new NotificationUtils();

  public void WriteNotificationXml(List<Transaction> transactionList, String convertedTransaction) {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

      Document document = documentBuilder.newDocument();
      Element rootElement = document.createElement("root");
      document.appendChild(rootElement);

      Element msgList = document.createElement("msg-list");
      rootElement.appendChild(msgList);

      if (!transactionList.isEmpty()) {
        transactionList.stream()
            .forEach(
                transaction -> {
                  Element msg = document.createElement("msg");
                  msg.appendChild(
                      document.createTextNode(
                          notificationUtils.formatTransactionType(transaction.getTransactionType())
                              + " with card "
                              + notificationUtils.maskCardNumber(
                                  transaction.getPrimaryAccountNumber(), "######******####")
                              + " on "
                              + notificationUtils.formatDateAndTime(
                                  transaction.getTransactionTime())
                              + ", amount "
                              + notificationUtils.formatAmount(transaction.getAmountInSubmits())
                              + " "
                              + notificationUtils
                                  .getCurrencyInstance(
                                      Integer.parseInt(transaction.getCurrencyCode()))
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
                    + notificationUtils.getTotalAmount()
                    + "\" "
                    + "date="
                    + "\""
                    + dtf.format(now)
                    + "\""));
        rootElement.appendChild(totals);
      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(document);
      StreamResult result = new StreamResult(new File("file.xml"));

      transformer.transform(source, result);

    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    }
  }
}
