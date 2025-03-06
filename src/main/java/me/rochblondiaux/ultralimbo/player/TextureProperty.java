package me.rochblondiaux.ultralimbo.player;

import java.security.*;
import java.util.Base64;

import org.jetbrains.annotations.Nullable;

public record TextureProperty(String name, String value, String signature) {

    public TextureProperty(String name, String value, @Nullable String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    @Override
    @Nullable
    public String signature() {
        return this.signature;
    }

    public boolean isSignatureValid(PublicKey publicKey) {
        if (signature() == null) {
            return false;
        }
        try {
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicKey);
            signature.update(this.value.getBytes());
            return signature.verify(Base64.getDecoder().decode(this.signature));
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return false;
    }
}