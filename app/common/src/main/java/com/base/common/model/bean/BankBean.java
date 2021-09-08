package com.base.common.model.bean;

import com.base.common.app.BaseConstant;

import java.util.List;

public class BankBean {

    /**
     * cardType : DC
     * bank : ICBC
     * key : 6217231804002468346
     * messages : []
     * validated : true
     * stat : ok
     */

    private String cardType;
    private String bank;
    private String key;
    private boolean validated;//true  验证通过
    private String stat;
    private List<?> messages;

    public String getCardIcon() {
        return BaseConstant.bank_icon_url + bank;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public List<?> getMessages() {
        return messages;
    }

    public void setMessages(List<?> messages) {
        this.messages = messages;
    }
}
