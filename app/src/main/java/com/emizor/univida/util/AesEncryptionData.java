package com.emizor.univida.util;

/**
 * Created by posemizor on 21-11-17.
 */

public class AesEncryptionData {

    public String iv;
    public String value;
    public String mac;

    public AesEncryptionData(String iv, String value, String mac) {
        this.iv = iv;
        this.value = value;
        this.mac = mac;
    }
}
