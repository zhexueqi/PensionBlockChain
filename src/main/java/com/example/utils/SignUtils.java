package com.example.utils;


import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.signature.ECDSASignatureResult;
import org.fisco.bcos.sdk.crypto.signature.SignatureResult;
import org.fisco.bcos.sdk.model.CryptoType;
import org.springframework.stereotype.Component;

/**
 * @author zhexueqi
 * @ClassName SignUtils
 * @since 2024/4/5    9:31
 */
@Component
public class SignUtils {
    public static ECDSASignatureResult generateSigantureWithSecp256k1(CryptoSuite cryptoSuite, CryptoKeyPair cryptoKeyPair, String data)
    {
        // 计算传入数据的哈希(keccak256哈希算法)
        String hashData = cryptoSuite.hash(data);
        // 生成签名
        return (ECDSASignatureResult)(cryptoSuite.sign(hashData, cryptoKeyPair));
    }

    // 验证secp256k1签名（入参为String）
    public static boolean verifySignature(SignatureResult signatureResult, CryptoKeyPair keyPair, String data)
    {
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        // 计算data的哈希(keccak256k1哈希算法)
        String hashData = cryptoSuite.hash(data);
        // 验证签名
        return cryptoSuite.verify(keyPair.getHexPublicKey(), hashData, signatureResult.convertToString());
    }
}
