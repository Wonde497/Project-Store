package com.verifone.demo.emv.data_handlers;

public class terminal {
    private String terminal_id;
    private String merchant_id;
    private String merchant_name;
    private String merchant_address;
    private String terminal_mode;
    private String currency;
    private String comm_type;

    private String transmission_No;
    private String h_sequence_Number;

    private String amount_1;
    private String approval_Code;
    private String authentication_Code;
    private String response_Display;
    private String sequence_Number;
    private String emv_response_data;
    private String settlmenttime;
    //....................TXN
    private String manualcard;
    private String preauth;
    private String preauthcmp;
    private String purchase;
    private String balance;
    private String refund;
    private String phoneauth;
    private String deposit;

    private String transaction_type;
    private String status;

    private String ipaddress;
    private String port;
//..........................................................
     public void setSettlmenttime(String settlmenttime) {
    this.settlmenttime = settlmenttime;
}

    public String getTerminal_id() {
        return terminal_id;
    }
    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public String getMerchant_id() { return merchant_id; }
    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }
    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getMerchant_address() {
        return merchant_address;
    }
    public void setMerchant_address(String merchant_address) { this.merchant_address = merchant_address;}

    public String getTerminal_mode() { return terminal_mode; }
    public void setTerminal_mode(String terminal_mode) { this.terminal_mode = terminal_mode;}

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getComm_type() { return comm_type; }
    public void setComm_type(String comm_type) { this.comm_type = comm_type; }

//..................................................................
    public String getTransmission_No() { return transmission_No; }
    public void setTransmission_No(String transmission_No) { this.transmission_No = transmission_No; }

    public String getH_sequence_Number() { return h_sequence_Number; }
    public void setH_sequence_Number(String h_sequence_Number) { this.h_sequence_Number = h_sequence_Number; }

  //..........................................................................................BatRec
    public String getAmount_1() { return amount_1; }
    public void setAmount_1(String amount_1) { this.amount_1 = amount_1; }

    public String getApproval_Code() { return approval_Code; }
    public void setApproval_Code(String approval_Code) { this.approval_Code = approval_Code; }

    public String getAuthentication_Code() { return authentication_Code; }
    public void setAuthentication_Code(String authentication_Code) { this.authentication_Code = authentication_Code; }

    public String getResponse_Display() { return response_Display; }
    public void setResponse_Display(String response_Display) { this.response_Display = response_Display; }

    public String getSequence_Number() { return sequence_Number; }
    public void setSequence_Number(String sequence_Number) { this.sequence_Number = sequence_Number; }

    public String getEmv_response_data() { return emv_response_data; }
    public void setEmv_response_data(String emv_response_data) { this.emv_response_data = emv_response_data; }

    //............................................................................txn
    public String getManualcard() { return manualcard; }
    public void setManualcard(String manualcard) { this.manualcard = manualcard; }

    public String getPreauth() { return preauth; }
    public void setPreauth(String preauth) { this.preauth = preauth; }

    public String getPreauthcmp() { return preauthcmp; }
    public void setPreauthcmp(String preauthcmp) { this.preauthcmp = preauthcmp; }

    public String getPurchase() { return purchase; }
    public void setPurchase(String purchase) { this.purchase = purchase; }

    public String getMagstrip() { return balance; }
    public void setMagstrip(String magstrip) { this.balance = magstrip; }

    public String getRefund() { return refund; }
    public void setRefund(String refund) { this.refund = refund; }

    public String getSettlment() { return phoneauth; }
    public void setSettlment(String settlment) { this.phoneauth = settlment; }

    public String getDeposit() { return deposit; }
    public void setDeposit(String deposit) { this.deposit = deposit; }

    public String getTransaction_type() { return transaction_type; }
    public void setTransaction_type(String transaction_type) { this.transaction_type = transaction_type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
//ipport
    public String getIpaddress() { return ipaddress; }
    public void setIpaddress(String ipaddress) { this.ipaddress = ipaddress; }

    public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }

    public String getSettlmenttime() { return settlmenttime; }
    public void setSsettlment(String settlment) { this.settlmenttime = settlmenttime; }

}
