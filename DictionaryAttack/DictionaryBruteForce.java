package DictionaryAttack;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import PasswordHash.PasswordHasher;

public class DictionaryBruteForce implements DictionaryAttack {

    private static final char[] CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*~-_+=/?.,<>\\|[]{}()".toCharArray();

    public String crackPassword(String hashedPassword, File dictionaryFile) throws IOException, NoSuchAlgorithmException, InterruptedException, ExecutionException {

        System.out.println("\n > Starting Brute Force Attack");
        BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile));
        PasswordHasher passwordHasher = new PasswordHasher();
        String word;
        int counter = 1;

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<String>> results = new ArrayList<>();

        while ((word = reader.readLine()) != null) {
            if (word.length() < 8 || word.length() > 16) continue;

            System.out.println("  >> Brute Force attempt #" + counter);
            final String finalWord = word;

            // Submit combined tasks to minimize overhead
            results.add(executorService.submit(() -> processWord(finalWord, hashedPassword, passwordHasher)));

            counter++;
        }

        reader.close();

        // Process results
        for (Future<String> result : results) {
            String matchedWord = result.get();
            if (matchedWord != null) {
                executorService.shutdown();
                return matchedWord;
            }
        }

        executorService.shutdown();
        return null;
    }

    private String processWord(String word, String hashedPassword, PasswordHasher passwordHasher) throws NoSuchAlgorithmException {
        // Check the base word
        if (checkPassword(word, hashedPassword, passwordHasher) != null) {
            return word;
        }

        // Check toggled case of the first character
        String toggledWord = toggleFirstCharCase(word);
        if (toggledWord != null && checkPassword(toggledWord, hashedPassword, passwordHasher) != null) {
            return toggledWord;
        }

        // Check mutations
        for (int i = 1; i <= 3; i++) {
            if (checkMutations(word, i, true, hashedPassword, passwordHasher) != null ||
                checkMutations(word, i, false, hashedPassword, passwordHasher) != null) {
                return word;
            }
        }

        // Check front and back mutations
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 2; j++) {
                if (checkFrontAndBackMutations(word, i, j, hashedPassword, passwordHasher) != null) {
                    return word;
                }
            }
        }

        return null;
    }

    private String checkPassword(String word, String hashedPassword, PasswordHasher passwordHasher) throws NoSuchAlgorithmException {
        return passwordHasher.hashPassword(word).equals(hashedPassword) ? word : null;
    }

    private String toggleFirstCharCase(String word) {
        if (word == null || word.isEmpty()) return null;

        char firstChar = word.charAt(0);
        char toggledChar = Character.isLowerCase(firstChar) ? Character.toUpperCase(firstChar)
                              : Character.isUpperCase(firstChar) ? Character.toLowerCase(firstChar)
                              : firstChar;

        return toggledChar + word.substring(1);
    }

    private String checkMutations(String base, int count, boolean addToFront, String hashedPassword, PasswordHasher passwordHasher) throws NoSuchAlgorithmException {
        return generateCombinationsAndCheck(new StringBuilder(), count, base, addToFront, hashedPassword, passwordHasher) ? base : null;
    }

    private String checkFrontAndBackMutations(String base, int frontCount, int backCount, String hashedPassword, PasswordHasher passwordHasher) throws NoSuchAlgorithmException {
        StringBuilder frontCombination = new StringBuilder();
        StringBuilder backCombination = new StringBuilder();

        for (int i = 0; i < Math.pow(CHARSET.length, frontCount); i++) {
            for (int j = 0; j < Math.pow(CHARSET.length, backCount); j++) {
                frontCombination.setLength(0);
                backCombination.setLength(0);

                for (int fc = 0, temp = i; fc < frontCount; fc++) {
                    frontCombination.append(CHARSET[temp % CHARSET.length]);
                    temp /= CHARSET.length;
                }

                for (int bc = 0, temp = j; bc < backCount; bc++) {
                    backCombination.append(CHARSET[temp % CHARSET.length]);
                    temp /= CHARSET.length;
                }

                String mutated = frontCombination + base + backCombination;
                if (passwordHasher.hashPassword(mutated).equals(hashedPassword)) {
                    return mutated;
                }
            }
        }
        return null;
    }

    private boolean generateCombinationsAndCheck(StringBuilder current, int remaining, String base, boolean addToFront, String hashedPassword, PasswordHasher passwordHasher) throws NoSuchAlgorithmException {
        if (remaining == 0) {
            String mutated = addToFront ? current + base : base + current;
            return passwordHasher.hashPassword(mutated).equals(hashedPassword);
        }

        for (char c : CHARSET) {
            current.append(c);
            if (generateCombinationsAndCheck(current, remaining - 1, base, addToFront, hashedPassword, passwordHasher)) {
                return true;
            }
            current.setLength(current.length() - 1);
        }
        return false;
    }
}
