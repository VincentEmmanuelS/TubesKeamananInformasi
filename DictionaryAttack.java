import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface DictionaryAttack {
    public String crackPassword(String hashedPassword, File dictionaryFile) throws IOException, NoSuchAlgorithmException;
}
