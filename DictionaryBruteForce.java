/* Dictionary Brute Force Hybrid Attack
 * 
 * Class implementasi untuk algoritma dictionary brute force hybrid attack
 * Brute force dictionary and toggled dictionary
 */

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DictionaryBruteForce implements DictionaryAttack {

    private static final char[] CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*~-_+=/?.,<>\\|[]{}()".toCharArray();

    // Crack the password using a brute force approach with a dictionary
    public String crackPassword(String hashedPassword, File dictionaryFile) throws IOException, NoSuchAlgorithmException, InterruptedException, ExecutionException {

        System.out.println("\n > Starting Brute Force Attack");
        BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile));
        PasswordHasher passwordHasher = new PasswordHasher();
        String word;
        int counter = 1;

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        while ((word = reader.readLine()) != null) {
            if (word.length() < 8 || word.length() > 16) continue; // Enforce password length constraint

            System.out.println("  >> Brute Force attempt #" + counter);

            // Create tasks for checking the word and mutations in parallel
            List<Callable<String>> tasks = new ArrayList<>();
            final String finalWord = word; 
            tasks.add(() -> checkPassword(finalWord, hashedPassword, passwordHasher));
            tasks.add(() -> checkPassword(toggleFirstCharCase(finalWord), hashedPassword, passwordHasher));

            // Check mutations in parallel as well
            for (int i = 1; i <= 3; i++) {
                final int finalI = i;
                tasks.add(() -> checkMutations(finalWord, finalI, true, hashedPassword, passwordHasher));
                tasks.add(() -> checkMutations(finalWord, finalI, false, hashedPassword, passwordHasher));
            }

            for (int i = 1; i <= 2; i++) {
                for (int j = 1; j <= 2; j++) {
                    final int finalI = i;
                    final int finalJ = j;
                    tasks.add(() -> checkFrontAndBackMutations(finalWord, finalI, finalJ, hashedPassword, passwordHasher));
                }
            }

            // Execute all tasks and check for a match
            List<Future<String>> results = executorService.invokeAll(tasks);
            for (Future<String> result : results) {
                String matchedWord = result.get();
                if (matchedWord != null) {
                    reader.close();
                    executorService.shutdown();
                    return matchedWord;
                }
            }

            counter++;
        }

        reader.close();
        executorService.shutdown();
        return null;
    }

    // Check if the password matches the hashed password
    private String checkPassword(String word, String hashedPassword, PasswordHasher passwordHasher) throws NoSuchAlgorithmException {
        if (word != null && passwordHasher.hashPassword(word).equals(hashedPassword)) {
            return word;
        }
        return null;
    }

    // Toggle the first character's case and return the modified word
    private String toggleFirstCharCase(String word) {
        if (word == null || word.isEmpty()) return null;

        char firstChar = word.charAt(0);
        char toggledChar;

        if (Character.isLowerCase(firstChar)) {
            toggledChar = Character.toUpperCase(firstChar);
        } else if (Character.isUpperCase(firstChar)) {
            toggledChar = Character.toLowerCase(firstChar);
        } else {
            return null; // No toggle case possible for non-alphabetic characters
        }

        return toggledChar + word.substring(1);
    }

    // Check mutations of the word by adding characters to front or back
    private String checkMutations(String base, int count, boolean addToFront, String hashedPassword, PasswordHasher passwordHasher) throws NoSuchAlgorithmException {
        if (generateCombinationsAndCheck("", count, base, addToFront, hashedPassword, passwordHasher)) {
            return base;  // Return the mutated base if found
        }
        return null;
    }

    // Check mutations by adding characters to both front and back
    private String checkFrontAndBackMutations(String base, int frontCount, int backCount, String hashedPassword, PasswordHasher passwordHasher) throws NoSuchAlgorithmException {
        String[] frontCombinations = generateCombinations(frontCount, true);
        String[] backCombinations = generateCombinations(backCount, false);

        for (String front : frontCombinations) {
            for (String back : backCombinations) {
                String mutated = front + base + back;
                if (passwordHasher.hashPassword(mutated).equals(hashedPassword)) {
                    return mutated;  // Return the mutated word if matched
                }
            }
        }
        return null;  // Return null if no match found
    }

    // Generate all combinations of a given length for appending characters
    private String[] generateCombinations(int length, boolean isFront) {
        int totalCombinations = (int) Math.pow(CHARSET.length, length);
        String[] combinations = new String[totalCombinations];

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

    // Generate combinations and check if the hashed password matches
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