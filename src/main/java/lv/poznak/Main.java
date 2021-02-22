package lv.poznak;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        List<Transaction> transactionList = new ArrayList<>();

        //TODO program makes logging
        String file = null;

        if (args.length == 0) {
            System.out.println("no arguments were given.");
            file = "src/file.txt";
        }
        else {
            for (String a : args) {
                System.out.println(a);
            }
        }
        Scanner scanner = new Scanner(new File(file));
        scanner.useDelimiter("\n");

        while(scanner.hasNext()){
            Transaction transaction = new Transaction();

            String next = scanner.next();
            transaction.setTransactionType(next.substring(0,2));
            transaction.setPrimaryAccountNumber(next.substring(2,18));
            transaction.setAmountInSubmits(next.substring(18,30));
            transaction.setTransactionTime(next.substring(30,44));
            transaction.setCurrencyCode(next.substring(44,47));

            transactionList.add(transaction);
        }
        Notification notification = new Notification();
        notification.WriteNotificationXml(transactionList);
        scanner.close();
    }
}
