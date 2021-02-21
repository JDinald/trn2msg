import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        //TODO Program accepts file
        // with transactions, where each line of 47 digits code

        //File
        //Data field 1 -> Size 2
        //Transaction type (00 – purchase, 01 – withdrawal)
        //Data field 2 -> Size 16
        //Primary account number (PAN)
        //Data field 3 -> Size 12
        //Amount in submits
        //Data field 4 -> Size 14
        //Transaction time (yyyymmddhh24miss)
        //Data field 5 -> Size 3
        //Currency code in numeric format

        // Value code of currency
        // Code 840 - usd
        // Code 978 - eur
        // Code 826 - gbp
        // Code 643 - rub

        //TODO Output xml file with
        // full info about transaction code (example in doc)

        //TODO program makes logging

        String file = "src/file.txt";
        Scanner scanner = new Scanner(new File(file));
        scanner.useDelimiter("\n");

        while(scanner.hasNext()){
            String next = scanner.next();
            String transactionType = next.substring(0,2);
            System.out.println("Transaction type (2): " + transactionType);
            String primaryAccountNumber = next.substring(2,18);
            System.out.println("Primary Account Number (16): " + primaryAccountNumber);
            String amountInSubmits = next.substring(18,30);
            System.out.println("Amount In Submits (12): " + amountInSubmits);
            String transactionTime = next.substring(30,44);
            System.out.println("Transaction time ((yyyymmddhh24miss)) (14): " + transactionTime);
            String currencyCode = next.substring(44,47);
            System.out.println("Currency code (3): " + currencyCode);
            System.out.println(next);
        }
        Notification notification = new Notification();
        notification.WriteNotificationXml("test");
        scanner.close();
    }
}
