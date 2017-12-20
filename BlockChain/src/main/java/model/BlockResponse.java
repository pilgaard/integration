/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author Andreas
 */
public class BlockResponse {
    public int id;
    public int nonce;
    public String previous;
    public String hash;
    public ArrayList<TransactionResponse> transactions;
}
