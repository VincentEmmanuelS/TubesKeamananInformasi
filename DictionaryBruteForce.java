/* Dictionary Brute Force Hybrid Attack
 * 
 * Class implementasi untuk algoritma dictionary brute force hybrid attack
 */

import java.io.*;
import java.security.NoSuchAlgorithmException;

public class DictionaryBruteForce implements DictionaryAttack {
    
    private static final char[] CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    public String crackPassword(String hashedPassword, File dictionaryFile) throws IOException, NoSuchAlgorithmException {

        BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile));
        PasswordHasher passwordHasher = new PasswordHasher();
        String word;

        while ((word = reader.readLine()) != null) {
            if (word.length() < 8 || word.length() > 16) continue; // Enforce password length constraint
            for (String mutation : generateMutations(word)) {
                if (passwordHasher.hashPassword(mutation).equals(hashedPassword)) {
                    reader.close();
                    return mutation;
                }
            }
        }

        reader.close();
        return null;

    }

    /* Brute Force Method */

    private String[] generateMutations(String base) {
        String[] mutations = new String[CHARSET.length];
        for (int i = 0; i < CHARSET.length; i++) {
            mutations[i] = base + CHARSET[i];
        }
        return mutations;
    }

}
