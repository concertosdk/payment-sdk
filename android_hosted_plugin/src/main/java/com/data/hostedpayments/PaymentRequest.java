package com.data.hostedpayments;


import org.json.JSONObject;

public class PaymentRequest {

    private String amount;
    private String actionCode;
    private String currency;

    private String email;
    private String address;
    private String city;
    private String stateCode;
    private String zip;
    private String countryCode;

    private String trackId;

    private String cardOperation;
    private String cardToken;
    private String tokenType;

    private String transactionId;

    private String metadata;

    /**
     * Merchant can send any additional request object.
     * Example:
     * airline
     * hotel
     * insurance
     * etc.
     */
    private JSONObject airlinedata;

    public PaymentRequest() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getCardOperation() {
        return cardOperation;
    }

    public void setCardOperation(String cardOperation) {
        this.cardOperation = cardOperation;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public JSONObject getairlinedata() {
        return airlinedata;
    }

    public void setairlinedata(JSONObject airlinedata) {
        this.airlinedata = airlinedata;
    }
}
