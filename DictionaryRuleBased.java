/* Dictionary Rule-Based Attack
 * Source: https://hashcat.net/wiki/doku.php?id=rule_based_attack
 * 
 * Class implementasi untuk algoritma dictionary rule based attack
 */

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DictionaryRuleBased implements DictionaryAttack {

    public String crackPassword(String hashedPassword, File dictionaryFile) throws IOException, NoSuchAlgorithmException {

        BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile));
        PasswordHasher passwordHasher = new PasswordHasher();
        String word;

        while ((word = reader.readLine()) != null) {
            // Memastikan bahwa password yang diambil dari dictionary memiliki panjang 8 ~ 16 karakter saja
            if (word.length() < 8 || word.length() > 16) continue;

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

    private List<String> generateMutations(String word) {

        List<String> mutations = new ArrayList<>();
        mutations.add(word);                    // Do nothing
        mutations.add(word.toLowerCase());
        mutations.add(word.toUpperCase());
        mutations.add(capitalizeFirst(word));
        mutations.add(lowerFirstUpperRest(word));
        mutations.add(toggleCase(word));
        mutations.add(reverse(word));
        mutations.add(word + word);             // Duplicate
        mutations.add(word + reverse(word));    // Reverse end
        mutations.add(reverse(word) + word);    // Reverse front
        mutations.add(rotateLeft(word));
        mutations.add(rotateRight(word));
        mutations.add(deleteFirstChar(word));
        mutations.add(deleteLastChar(word));
        // Insertion but symbols only??? "! @ # $ % ^ & * ~ - _ + = / ? . > , < \ | [ ] { } ()"
        // Insertion like bruteforce, 3 front, 3 end, 2 front + 2 end

        return mutations;
    }

    /* Rule-Based Methods */

    private String capitalizeFirst(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
    }

    private String lowerFirstUpperRest(String word) {
        return Character.toLowerCase(word.charAt(0)) + word.substring(1).toUpperCase();
    }

    private String toggleCase(String word) {
        StringBuilder toggled = new StringBuilder();
        for (char c : word.toCharArray()) {
            toggled.append(Character.isUpperCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c));
        }
        return toggled.toString();
    }

    private String reverse(String word) {
        return new StringBuilder(word).reverse().toString();
    }

    private String rotateLeft(String word) {
        return word.substring(1) + word.charAt(0);
    }

    private String rotateRight(String word) {
        return word.charAt(word.length() - 1) + word.substring(0, word.length() - 1);
    }

    private String deleteFirstChar(String word) {
        return word.substring(1);
    }

    private String deleteLastChar(String word) {
        return word.substring(0, word.length() - 1);
    }

}
