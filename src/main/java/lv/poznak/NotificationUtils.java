package lv.poznak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class NotificationUtils {

    public static long totalAmount;
    private static final Logger logger = LoggerFactory.getLogger(NotificationUtils.class);

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public static Currency getCurrencyInstance(int numericCode) {
        Set<Currency> currencies = Currency.getAvailableCurrencies();
        for (Currency currency : currencies) {
            if (currency.getNumericCode() == numericCode) {
                return currency;
            }
        }
        logger.error("Currency with numeric code " + numericCode + " not found");
        return Currency.getInstance(Locale.US);
    }

    public static String formatAmount(String amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        Integer intAmount = Integer.parseInt(amount);
        totalAmount += intAmount;
        if (intAmount != 0) {
            String result = format.format(Integer.parseInt(amount) / 100.0);
            return result.replace("$", "");
        } else {
            logger.error("Transaction amount is 00.00");
            return "00.00";
        }
    }

    public static String formatTransactionType(String transactionType) {
        if (transactionType.equals("00")) {
            return "Purchase";
        } else if (transactionType.equals("01")) {
            return "Withdrawal";
        } else {
            logger.error("Non existing transaction type");
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
            logger.error("Invalid date");
        }
        String transactionAccurateTime = transactionTime.substring(8, 14);
        try {

            SimpleDateFormat format = new SimpleDateFormat("hhmmss"); // if 24 hour format
            Date d1 = format.parse(transactionAccurateTime);
            Time convertedTime = new Time(d1.getTime());
            formatTime = convertedTime.toString();

        } catch (ParseException e) {
            logger.error("Invalid time");
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
