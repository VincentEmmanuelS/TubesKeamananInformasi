/* Dictionary Rule-Based Attack
 * Source: https://hashcat.net/wiki/doku.php?id=rule_based_attack
 * 
 * Class implementasi untuk algoritma dictionary rule based attack
 */

package DictionaryAttack;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import PasswordHash.PasswordHasher;

public class DictionaryRuleBased implements DictionaryAttack {

    private int count = 1;

    public String crackPassword(String hashedPassword, File dictionaryFile) throws IOException, NoSuchAlgorithmException {

        System.out.println(" > Starting Rule-Based Attack");
        count = 1;
        BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile));
        PasswordHasher passwordHasher = new PasswordHasher();
        String word;

        while ((word = reader.readLine()) != null) {
            System.out.println("  >> Rule-Based attempt #" + count);
            count++;

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

        /* Original Word Mutations */
        mutations.add(word);                        // Do nothing
        mutations.add(word.toLowerCase());          // To lower case
        mutations.add(word.toUpperCase());          // To upper case
        mutations.add(capitalizeFirst(word));       // Capitalize first
        mutations.add(lowerFirstUpperRest(word));   // Lower first, uppercase the rest
        mutations.add(toggleCase(word));            // Toggle case
        mutations.add(reverse(word));               // Reverse
        mutations.add(word + word);                 // Duplicate
        mutations.add(word + reverse(word));        // Reverse end
        mutations.add(reverse(word) + word);        // Reverse front
        mutations.add(rotateLeft(word));            // Rotate left
        mutations.add(rotateRight(word));           // Rotate right
        mutations.add(deleteFirstChar(word));       // Delete first char
        mutations.add(deleteLastChar(word));        // Delete last char

        /* Mutations with Symbols and Bitwise Operations */
        mutations.addAll(insertSymbols(word));      // Insert symbols
        mutations.add(bitwiseShiftLeft(word));      // Bitwise shift left
        mutations.add(bitwiseShiftRight(word));     // Bitwire shift right
        mutations.add(asciiIncrement(word));        // ASCII increment
        mutations.add(asciiDecrement(word));        // ASCII decrement

        /* Character Replacements */
        mutations.add(replaceNPlusOne(word));               // Replace char n+1
        mutations.add(replaceNMinusOne(word));              // Replace char n-1
        mutations.add(duplicateBlockFront(word));           // Duplicate block front
        mutations.add(duplicateBlockBack(word));            // Duplicate block back
        mutations.add(replaceCharWithSymbol(word));         // Replace char with symbol
        mutations.add(replaceSymbolWithChar(word));         // Replace symbol with char
        mutations.addAll(replaceAllWithSymbol(word));       // Replace all chars with symbols
        mutations.addAll(replaceAllWithCharacter(word));    // Replace all chars with other char
        mutations.addAll(replaceCharacterWithSymbol(word)); // Replace char with symbol
        mutations.addAll(replaceSymbolWithCharacter(word)); // Replace symbol with char 

        /* Mutation Combinations */
        mutations.addAll(combineMutations(word));

        return mutations;
    }

    /* Mutation Combinations */

    private List<String> combineMutations(String word) {
        List<String> combinedMutations = new ArrayList<>();

        // Capitalize First + Other Mutations
        combinedMutations.add(capitalizeFirst(word) + reverse(word));
        combinedMutations.add(capitalizeFirst(word) + rotateLeft(word));
        combinedMutations.add(capitalizeFirst(word) + rotateRight(word));
        combinedMutations.add(capitalizeFirst(word) + deleteFirstChar(word));
        combinedMutations.add(capitalizeFirst(word) + deleteLastChar(word));
        combinedMutations.add(capitalizeFirst(word) + insertSymbols(word));
        combinedMutations.add(capitalizeFirst(word) + bitwiseShiftLeft(word));
        combinedMutations.add(capitalizeFirst(word) + bitwiseShiftRight(word));
        combinedMutations.add(capitalizeFirst(word) + asciiIncrement(word));
        combinedMutations.add(capitalizeFirst(word) + asciiDecrement(word));
        combinedMutations.add(capitalizeFirst(word) + replaceNPlusOne(word));
        combinedMutations.add(capitalizeFirst(word) + replaceNMinusOne(word));
        combinedMutations.add(capitalizeFirst(word) + duplicateBlockFront(word));
        combinedMutations.add(capitalizeFirst(word) + duplicateBlockBack(word));
        combinedMutations.add(capitalizeFirst(word) + replaceCharWithSymbol(word));
        combinedMutations.add(capitalizeFirst(word) + replaceSymbolWithChar(word));
        combinedMutations.add(capitalizeFirst(word) + replaceAllWithSymbol(word));
        combinedMutations.add(capitalizeFirst(word) + replaceAllWithCharacter(word));

        // Lowercase First Upper Rest + Other Mutations
        combinedMutations.add(lowerFirstUpperRest(word) + reverse(word));
        combinedMutations.add(lowerFirstUpperRest(word) + rotateLeft(word));
        combinedMutations.add(lowerFirstUpperRest(word) + rotateRight(word));
        combinedMutations.add(lowerFirstUpperRest(word) + deleteFirstChar(word));
        combinedMutations.add(lowerFirstUpperRest(word) + deleteLastChar(word));
        combinedMutations.add(lowerFirstUpperRest(word) + insertSymbols(word));
        combinedMutations.add(lowerFirstUpperRest(word) + bitwiseShiftLeft(word));
        combinedMutations.add(lowerFirstUpperRest(word) + bitwiseShiftRight(word));
        combinedMutations.add(lowerFirstUpperRest(word) + asciiIncrement(word));
        combinedMutations.add(lowerFirstUpperRest(word) + asciiDecrement(word));
        combinedMutations.add(lowerFirstUpperRest(word) + replaceNPlusOne(word));
        combinedMutations.add(lowerFirstUpperRest(word) + replaceNMinusOne(word));
        combinedMutations.add(lowerFirstUpperRest(word) + duplicateBlockFront(word));
        combinedMutations.add(lowerFirstUpperRest(word) + duplicateBlockBack(word));
        combinedMutations.add(lowerFirstUpperRest(word) + replaceCharWithSymbol(word));
        combinedMutations.add(lowerFirstUpperRest(word) + replaceSymbolWithChar(word));
        combinedMutations.add(lowerFirstUpperRest(word) + replaceAllWithSymbol(word));
        combinedMutations.add(lowerFirstUpperRest(word) + replaceAllWithCharacter(word));

        // Toggle Case + Other Mutations
        combinedMutations.add(toggleCase(word) + reverse(word));
        combinedMutations.add(toggleCase(word) + rotateLeft(word));
        combinedMutations.add(toggleCase(word) + rotateRight(word));
        combinedMutations.add(toggleCase(word) + deleteFirstChar(word));
        combinedMutations.add(toggleCase(word) + deleteLastChar(word));
        combinedMutations.add(toggleCase(word) + insertSymbols(word));
        combinedMutations.add(toggleCase(word) + bitwiseShiftLeft(word));
        combinedMutations.add(toggleCase(word) + bitwiseShiftRight(word));
        combinedMutations.add(toggleCase(word) + asciiIncrement(word));
        combinedMutations.add(toggleCase(word) + asciiDecrement(word));
        combinedMutations.add(toggleCase(word) + replaceNPlusOne(word));
        combinedMutations.add(toggleCase(word) + replaceNMinusOne(word));
        combinedMutations.add(toggleCase(word) + duplicateBlockFront(word));
        combinedMutations.add(toggleCase(word) + duplicateBlockBack(word));
        combinedMutations.add(toggleCase(word) + replaceCharWithSymbol(word));
        combinedMutations.add(toggleCase(word) + replaceSymbolWithChar(word));
        combinedMutations.add(toggleCase(word) + replaceAllWithSymbol(word));
        combinedMutations.add(toggleCase(word) + replaceAllWithCharacter(word));

        // Reverse + Other Mutations
        combinedMutations.add(reverse(word) + rotateLeft(word));
        combinedMutations.add(reverse(word) + rotateRight(word));
        combinedMutations.add(reverse(word) + deleteFirstChar(word));
        combinedMutations.add(reverse(word) + deleteLastChar(word));
        combinedMutations.add(reverse(word) + insertSymbols(word));
        combinedMutations.add(reverse(word) + bitwiseShiftLeft(word));
        combinedMutations.add(reverse(word) + bitwiseShiftRight(word));
        combinedMutations.add(reverse(word) + asciiIncrement(word));
        combinedMutations.add(reverse(word) + asciiDecrement(word));
        combinedMutations.add(reverse(word) + replaceNPlusOne(word));
        combinedMutations.add(reverse(word) + replaceNMinusOne(word));
        combinedMutations.add(reverse(word) + duplicateBlockFront(word));
        combinedMutations.add(reverse(word) + duplicateBlockBack(word));
        combinedMutations.add(reverse(word) + replaceCharWithSymbol(word));
        combinedMutations.add(reverse(word) + replaceSymbolWithChar(word));
        combinedMutations.add(reverse(word) + replaceAllWithSymbol(word));
        combinedMutations.add(reverse(word) + replaceAllWithCharacter(word));

        // Rotate Left + Other Mutations
        combinedMutations.add(rotateLeft(word) + rotateRight(word));
        combinedMutations.add(rotateLeft(word) + deleteFirstChar(word));
        combinedMutations.add(rotateLeft(word) + deleteLastChar(word));
        combinedMutations.add(rotateLeft(word) + insertSymbols(word));
        combinedMutations.add(rotateLeft(word) + bitwiseShiftLeft(word));
        combinedMutations.add(rotateLeft(word) + bitwiseShiftRight(word));
        combinedMutations.add(rotateLeft(word) + asciiIncrement(word));
        combinedMutations.add(rotateLeft(word) + asciiDecrement(word));
        combinedMutations.add(rotateLeft(word) + replaceNPlusOne(word));
        combinedMutations.add(rotateLeft(word) + replaceNMinusOne(word));
        combinedMutations.add(rotateLeft(word) + duplicateBlockFront(word));
        combinedMutations.add(rotateLeft(word) + duplicateBlockBack(word));
        combinedMutations.add(rotateLeft(word) + replaceCharWithSymbol(word));
        combinedMutations.add(rotateLeft(word) + replaceSymbolWithChar(word));
        combinedMutations.add(rotateLeft(word) + replaceAllWithSymbol(word));
        combinedMutations.add(rotateLeft(word) + replaceAllWithCharacter(word));

        // Rotate Right + Other Mutations
        combinedMutations.add(rotateRight(word) + deleteFirstChar(word));
        combinedMutations.add(rotateRight(word) + deleteLastChar(word));
        combinedMutations.add(rotateRight(word) + insertSymbols(word));
        combinedMutations.add(rotateRight(word) + bitwiseShiftLeft(word));
        combinedMutations.add(rotateRight(word) + bitwiseShiftRight(word));
        combinedMutations.add(rotateRight(word) + asciiIncrement(word));
        combinedMutations.add(rotateRight(word) + asciiDecrement(word));
        combinedMutations.add(rotateRight(word) + replaceNPlusOne(word));
        combinedMutations.add(rotateRight(word) + replaceNMinusOne(word));
        combinedMutations.add(rotateRight(word) + duplicateBlockFront(word));
        combinedMutations.add(rotateRight(word) + duplicateBlockBack(word));
        combinedMutations.add(rotateRight(word) + replaceCharWithSymbol(word));
        combinedMutations.add(rotateRight(word) + replaceSymbolWithChar(word));
        combinedMutations.add(rotateRight(word) + replaceAllWithSymbol(word));
        combinedMutations.add(rotateRight(word) + replaceAllWithCharacter(word));

        return combinedMutations;
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
