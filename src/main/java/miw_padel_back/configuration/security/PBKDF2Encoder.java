package miw_padel_back.configuration.security;

import miw_padel_back.domain.exceptions.PasswordEncodeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class PBKDF2Encoder implements PasswordEncoder {

    @Value("${miwpadelback.password.encoder.secret}")
    private String secret;

    @Value("${miwpadelback.password.encoder.iteration}")
    private Integer iteration;

    @Value("${miwpadelback.password.encoder.keylength}")
    private Integer keyLength;

    @Override
    public String encode(CharSequence password) {
        try {
            byte[] encodedPassword = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                    .generateSecret(new PBEKeySpec(password.toString().toCharArray(), this.secret.getBytes(), this.iteration, this.keyLength))
                    .getEncoded();
            return Base64.getEncoder().encodeToString(encodedPassword);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new PasswordEncodeException();
        }
    }

    @Override
    public boolean matches(CharSequence password, String possiblePassword) {
        return encode(password).equals(possiblePassword);
    }

}
