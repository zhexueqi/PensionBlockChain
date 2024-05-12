package com.example;


import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.signature.SignatureResult;
import org.fisco.bcos.sdk.model.CryptoType;

import java.math.BigInteger;

/**
 * @author zhexueqi
 * @ClassName main
 * @since 2024/4/5    9:24
 */
public class main {
    public static void main(String[] args) {
//        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
//        CryptoKeyPair keyPairFactory = cryptoSuite.getKeyPairFactory();
//        String hexPrivateKey = keyPairFactory.getHexPrivateKey();
//        System.out.println(hexPrivateKey);
//        String address = keyPairFactory.getAddress();
//        System.out.println(address);
//        SignatureResult sign = cryptoSuite.sign(hexPrivateKey, keyPairFactory);
//        String s = sign.convertToString();
//        System.out.println(s);
        CryptoSuite cryptoSuiteName = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair cryptoKeyPairName = cryptoSuiteName.getKeyPairFactory();
        String address = cryptoKeyPairName.getAddress();
        System.out.println(cryptoKeyPairName.getHexPrivateKey());
        System.out.println("Address:"+address);
        System.out.println(cryptoKeyPairName.getHexPublicKey());
        String hash = cryptoSuiteName.hash(address);
        SignatureResult sign = cryptoSuiteName.sign(hash, cryptoKeyPairName);
        System.out.println(sign.convertToString());
        String address1 = cryptoKeyPairName.getAddress();
        System.out.println("Address1:"+address1);

    }
}
