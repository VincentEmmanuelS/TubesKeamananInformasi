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

        System.out.println("Starting Rule-Based Attack");
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
        System.out.println("Do nothing");
        mutations.add(word);                    // Do nothing
        System.out.println("To lower case");
        mutations.add(word.toLowerCase());
        System.out.println("To upper case");
        mutations.add(word.toUpperCase());
        System.out.println("Capitalized first");
        mutations.add(capitalizeFirst(word));
        System.out.println("Lower first upper rest");
        mutations.add(lowerFirstUpperRest(word));
        System.out.println("Toggle case");
        mutations.add(toggleCase(word));
        System.out.println("Reverse word");
        mutations.add(reverse(word));
        System.out.println("Duplicate word");
        mutations.add(word + word);             // Duplicate
        System.out.println("Duplicate reverse end");
        mutations.add(word + reverse(word));    // Reverse end
        System.out.println("Duplicate reverse front");
        mutations.add(reverse(word) + word);    // Reverse front
        System.out.println("Rotate left");
        mutations.add(rotateLeft(word));
        System.out.println("Rotate right");
        mutations.add(rotateRight(word));
        System.out.println("Delete first character");
        mutations.add(deleteFirstChar(word));
        System.out.println("Delete last character");
        mutations.add(deleteLastChar(word));
        System.out.println("Insert symbols");
        mutations.addAll(insertSymbols(word));
        System.out.println("Bitwise shift left");
        mutations.add(bitwiseShiftLeft(word));
        System.out.println("Bitwise shift right");
        mutations.add(bitwiseShiftRight(word));
        System.out.println("ASCII increment");
        mutations.add(asciiIncrement(word));
        System.out.println("ASCII decrement");
        mutations.add(asciiDecrement(word));
        System.out.println("Replace N +1");
        mutations.add(replaceNPlusOne(word));
        System.out.println("Replace N -1");
        mutations.add(replaceNMinusOne(word));
        System.out.println("Duplicate block front");
        mutations.add(duplicateBlockFront(word));
        System.out.println("Duplicate block back");
        mutations.add(duplicateBlockBack(word));
        System.out.println("Replace char with symbol");
        mutations.add(replaceCharWithSymbol(word));
        System.out.println("Replace symbol with char");
        mutations.add(replaceSymbolWithChar(word));
        System.out.println("Replace all with symbols");
        mutations.addAll(replaceAllWithSymbol(word));
        System.out.println("Replace all with characters");
        mutations.addAll(replaceAllWithCharacter(word));
        System.out.println("Replace char with symbol #2");
        mutations.addAll(replaceCharacterWithSymbol(word));
        System.out.println("Replace symbol with char #2");
        mutations.addAll(replaceSymbolWithCharacter(word));
        System.out.println("End.");
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
