import java.io.*;
import java.security.NoSuchAlgorithmException;

public class DictionaryBruteForce implements DictionaryAttack {

    private static final char[] CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*~-_+=/?.,<>\\|[]{}()".toCharArray();

    public String crackPassword(String hashedPassword, File dictionaryFile) throws IOException, NoSuchAlgorithmException {
        System.out.println("Starting Brute Force Attack");
        BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile));
        PasswordHasher passwordHasher = new PasswordHasher();
        String word;
        int counter = 1;

        while ((word = reader.readLine()) != null) {
            if (word.length() < 8 || word.length() > 16) continue; // Enforce password length constraint

            System.out.println("Iterasi brute force ke-" + counter);

            // Check original word
            if (passwordHasher.hashPassword(word).equals(hashedPassword)) {
                reader.close();
                return word;
            }

            // Check mutations of the word
            for (int i = 1; i <= 3; i++) {
                // System.out.println("Append " + i + " char(s) in front");
                if (checkMutations(word, i, true, hashedPassword, passwordHasher)) {
                    reader.close();
                    return word;
                }
            }

            for (int i = 1; i <= 3; i++) {
                // System.out.println("Append " + i + " char(s) in back");
                if (checkMutations(word, i, false, hashedPassword, passwordHasher)) {
                    reader.close();
                    return word;
                }
            }

            for (int i = 1; i <= 2; i++) {
                for (int j = 1; j <= 2; j++) {
                    // System.out.println("Append " + i + " char(s) in front and " + j + " char(s) in back");
                    if (checkFrontAndBackMutations(word, i, j, hashedPassword, passwordHasher)) {
                        reader.close();
                        return word;
                    }
                }
            }

            counter++;
        }

        reader.close();
        return null;
    }

    // Check mutations by appending characters at a position (front or back)
    private boolean checkMutations(String base, int count, boolean addToFront, String hashedPassword, PasswordHasher passwordHasher) throws NoSuchAlgorithmException {
        return generateCombinationsAndCheck("", count, base, addToFront, hashedPassword, passwordHasher);
    }

    // Optimized check for front and back mutations
    private boolean checkFrontAndBackMutations(String base, int frontCount, int backCount, String hashedPassword, PasswordHasher passwordHasher) throws NoSuchAlgorithmException {
        // Generate combinations for front and back separately
        String[] frontCombinations = generateCombinations(frontCount, true);
        String[] backCombinations = generateCombinations(backCount, false);

        // Now combine the front and back combinations and check for hash match
        for (String front : frontCombinations) {
            for (String back : backCombinations) {
                String mutated = front + base + back;
                if (passwordHasher.hashPassword(mutated).equals(hashedPassword)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Generate all possible combinations of characters of a given length (for front or back part)
    private String[] generateCombinations(int length, boolean isFront) {
        // Total combinations = CHARSET.length^length
        int totalCombinations = (int) Math.pow(CHARSET.length, length);
        String[] combinations = new String[totalCombinations];

        // Loop through all possible combinations
        for (int i = 0; i < totalCombinations; i++) {
            StringBuilder combination = new StringBuilder();
            int temp = i;
            for (int j = 0; j < length; j++) {
                combination.insert(isFront ? 0 : combination.length(), CHARSET[temp % CHARSET.length]);
                temp /= CHARSET.length;
            }
            combinations[i] = combination.toString();
        }

        return combinations;
    }

    // Generate combinations of characters and immediately check if the mutated word's hash matches the target hash
    private boolean generateCombinationsAndCheck(String current, int remaining, String base, boolean addToFront, String hashedPassword, PasswordHasher passwordHasher) throws NoSuchAlgorithmException {
        if (remaining == 0) {
            String mutated = addToFront ? current + base : base + current;
            if (passwordHasher.hashPassword(mutated).equals(hashedPassword)) {
                return true;
            }
            return false;
        }

        for (char c : CHARSET) {
            if (generateCombinationsAndCheck(current + c, remaining - 1, base, addToFront, hashedPassword, passwordHasher)) {
                return true;
            }
        }
        return false;
    }
}
