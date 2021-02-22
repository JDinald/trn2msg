package lv.poznak;

public class Transaction {

  private String transactionType;
  private String primaryAccountNumber;
  private String amountInSubmits;
  private String transactionTime;
  private String currencyCode;

  public String getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public String getPrimaryAccountNumber() {
    return primaryAccountNumber;
  }

  public void setPrimaryAccountNumber(String primaryAccountNumber) {
    this.primaryAccountNumber = primaryAccountNumber;
  }

  public String getAmountInSubmits() {
    return amountInSubmits;
  }

  public void setAmountInSubmits(String amountInSubmits) {
    this.amountInSubmits = amountInSubmits;
  }

  public String getTransactionTime() {
    return transactionTime;
  }

  public void setTransactionTime(String transactionTime) {
    this.transactionTime = transactionTime;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }
}
