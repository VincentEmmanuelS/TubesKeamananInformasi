/* Dictionary Brute Force Hybrid Attack
 * 
 * Class implementasi untuk algoritma dictionary brute force hybrid attack
 */

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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

    private List<String> generateMutations(String base) {
        // String[] mutations = new String[CHARSET.length];
        // for (int i = 0; i < CHARSET.length; i++) {
        //     mutations[i] = base + CHARSET[i];
        // }

        List<String> mutations = new ArrayList<>();
        mutations.add(base); // Original word

        // Add 1-3 characters in front
        for (int i = 1; i <= 3; i++) {
            mutations.addAll(addCharactersAtPosition(base, i, true));
        }

        // Add 1-3 characters at the end
        for (int i = 1; i <= 3; i++) {
            mutations.addAll(addCharactersAtPosition(base, i, false));
        }

        // Add 1-2 characters at the front and 1-2 at the back
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 2; j++) {
                mutations.addAll(addCharactersFrontAndBack(base, i, j));
            }
        }

        return mutations;
    }

    private List<String> addCharactersAtPosition(String base, int count, boolean addToFront) {
        List<String> results = new ArrayList<>();

        generateCombinations("", count, base, addToFront, results);

        return results;
    }

    private void generateCombinations(String current, int remaining, String base, boolean addToFront, List<String> results) {
        if (remaining == 0) {
            results.add(addToFront ? current + base : base + current);
            return;
        }

        for (char c : CHARSET) {
            generateCombinations(current + c, remaining - 1, base, addToFront, results);
        }
    }

    private List<String> addCharactersFrontAndBack(String base, int frontCount, int backCount) {
        // List<String> results = new ArrayList<>();
        // List<String> frontCombinations = addCharactersAtPosition(base, frontCount, true);

        // for (String frontMutated : frontCombinations) {
        //     String pureBase = frontMutated.substring(frontCount); // Remove added front part
        //     results.addAll(addCharactersAtPosition(pureBase, backCount, false));
        // }

        List<String> results = new ArrayList<>();
        List<String> frontCombinations = new ArrayList<>();
        generateCombinations("", frontCount, "", true, frontCombinations);
    
        for (String front : frontCombinations) {
            List<String> backCombinations = new ArrayList<>();
            generateCombinations("", backCount, "", false, backCombinations);
    
            for (String back : backCombinations) {
                results.add(front + base + back);
            }
        }
    
        return results;
        
    }

}
