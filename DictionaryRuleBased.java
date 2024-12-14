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
        mutations.addAll(insertSymbols(word));
        mutations.add(bitwiseShiftLeft(word));
        mutations.add(bitwiseShiftRight(word));
        mutations.add(asciiIncrement(word));
        mutations.add(asciiDecrement(word));
        mutations.add(replaceNPlusOne(word));
        mutations.add(replaceNMinusOne(word));
        mutations.add(duplicateBlockFront(word));
        mutations.add(duplicateBlockBack(word));
        mutations.add(replaceCharWithSymbol(word));
        mutations.add(replaceSymbolWithChar(word));
        mutations.addAll(replaceAllWithSymbol(word));
        mutations.addAll(replaceAllWithCharacter(word));
        mutations.addAll(replaceCharacterWithSymbol(word));
        mutations.addAll(replaceSymbolWithCharacter(word));
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

    private List<String> insertSymbols(String word) {
        String symbols = "!@#$%^&*~-_+=/?.,<>\\|[]{}()";
        List<String> mutations = new ArrayList<>();

        for (char symbol : symbols.toCharArray()) {
            mutations.add(symbol + symbol + symbol + word);             // Triple front
            mutations.add(word + symbol + symbol + symbol);             // Triple end
            mutations.add(symbol + symbol + word + symbol + symbol);    // Double front and end
        }

        return mutations;
    }

    private String bitwiseShiftLeft(String word) {
        StringBuilder result = new StringBuilder();
        for (char c : word.toCharArray()) {
            result.append((char) (c << 1));
        }
        return result.toString();
    }

    private String bitwiseShiftRight(String word) {
        StringBuilder result = new StringBuilder();
        for (char c : word.toCharArray()) {
            result.append((char) (c >> 1));
        }
        return result.toString();
    }

    private String asciiIncrement(String word) {
        StringBuilder result = new StringBuilder();
        for (char c : word.toCharArray()) {
            result.append((char) (c + 1));
        }
        return result.toString();
    }

    private String asciiDecrement(String word) {
        StringBuilder result = new StringBuilder();
        for (char c : word.toCharArray()) {
            result.append((char) (c - 1));
        }
        return result.toString();
    }

    private String replaceNPlusOne(String word) {
        if (word.length() < 2) return word;
        char[] chars = word.toCharArray();
        for (int i = 0; i < word.length() - 1; i++) {
            chars[i] = chars[i + 1];
        }
        return new String(chars);
    }

    private String replaceNMinusOne(String word) {
        if (word.length() < 2) return word;
        char[] chars = word.toCharArray();
        for (int i = word.length() - 1; i > 0; i--) {
            chars[i] = chars[i - 1];
        }
        return new String(chars);
    }

    private String duplicateBlockFront(String word) {
        return word + word.substring(0, word.length() / 2);
    }

    private String duplicateBlockBack(String word) {
        return word.substring(word.length() / 2) + word;
    }

    private String replaceCharWithSymbol(String word) {
        String symbols = "!@#$%^&*~-_+=/?.,<>\\|[]{}()";
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = symbols.charAt(i % symbols.length());
        }
        return new String(chars);
    }

    private String replaceSymbolWithChar(String word) {
        StringBuilder result = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                result.append('x'); // Replace symbols with a generic char
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private List<String> replaceAllWithSymbol(String word) {
        String symbols = "!@#$%^&*~-_+=/?.,<>\\|[]{}()";
        List<String> mutations = new ArrayList<>();

        for (char symbol : symbols.toCharArray()) {
            mutations.add(String.valueOf(symbol).repeat(word.length()));
        }

        return mutations;
    }

    private List<String> replaceAllWithCharacter(String word) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        List<String> mutations = new ArrayList<>();

        for (char c : chars.toCharArray()) {
            mutations.add(String.valueOf(c).repeat(word.length()));
        }

        return mutations;
    }

    private List<String> replaceSymbolWithCharacter(String word) {
        // Mapping of symbols to interchangeable characters
        String[][] symbolToCharMapping = {
            {"@", "a"},
            {"3", "e"},
            {"1", "l"},
            {"0", "o"},
            {"$", "s"},
            {"7", "t"},
            {"+", "t"},
            {"!", "i"}
        };

        List<String> mutations = new ArrayList<>();

        for (String[] mapping : symbolToCharMapping) {
            String symbol = mapping[0];
            String character = mapping[1];
            if (word.contains(symbol)) {
                mutations.add(word.replace(symbol, character));
            }
        }

        return mutations;
    }

    // Reverse: Replace character with symbol
    private List<String> replaceCharacterWithSymbol(String word) {
        // Mapping of characters to interchangeable symbols
        String[][] charToSymbolMapping = {
            {"a", "@"},
            {"e", "3"},
            {"l", "1"},
            {"o", "0"},
            {"s", "$"},
            {"t", "7"},
            {"t", "+"},
            {"i", "!"}
        };

        List<String> mutations = new ArrayList<>();

        for (String[] mapping : charToSymbolMapping) {
            String character = mapping[0];
            String symbol = mapping[1];
            if (word.contains(character)) {
                mutations.add(word.replace(character, symbol));
            }
        }

        return mutations;
    }
}
