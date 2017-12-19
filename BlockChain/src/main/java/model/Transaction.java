/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Andreas
 */
public class Transaction {
    
    int value;
    String from;
    String to;

    public Transaction(int value, String from, String to) {
        this.value = value;
        this.from = from;
        this.to = to;
    }

    public int getValue() {
        return value;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }
    
    
    
    
}
