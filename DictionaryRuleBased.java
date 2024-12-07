/*
 * Source: https://hashcat.net/wiki/doku.php?id=rule_based_attack
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DictionaryRuleBased {

    PasswordHasher hashFunction;

    public DictionaryRuleBased() {
        this.hashFunction = new PasswordHasher();
    }

    /* VARIABLES */
    private static final int MAX_PASSWORD_LENGTH = 16;
    private static final char[] charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*?><,.".toCharArray();
    private static final char[][] replacements = {
        {'A', '4'}, {'a', '@'}, {'E', '3'}, {'O', '0'},
        {'o', '0'}, {'i', '!'}, {'I', '1'}, {'t', '+'}
    };

    public boolean crackPassword(String password) {
        System.out.println("Starting rule based attack: ");
        try (BufferedReader read = new BufferedReader(new FileReader("Dictionary.txt"))) {
            String keyword;
            while ((keyword = read.readLine()) != null) {
                
                // Do nothing (passthrough)
                if (hashFunction.hashPassword(keyword).equals(password)) {
                    return true;
                }
                System.out.println("-- do nothing failed");

                // Lowercase all letters
                if (hashFunction.hashPassword(keyword.toLowerCase()).equals(password)) {
                    return true;
                }
                System.out.println("-- lowercase failed");

                // Uppercase all letters
                if (hashFunction.hashPassword(keyword.toUpperCase()).equals(password)) {
                    return true;
                }
                System.out.println("-- uppercase failed");

                // Capitalize the first letter and lower the rest
                if (hashFunction.hashPassword(capitalizeFirstLetter(keyword)).equals(password)) {
                    return true;
                }
                System.out.println("-- capitalize failed");

                // Lower first found chacater, uppercase the rest
                if (hashFunction.hashPassword(lowerFirstUpperRest(keyword)).equals(password)) {
                    return true;
                }
                System.out.println("-- lower 1st failed");

                // Toggle the case of all characters in word (p@ssW0rd -> P@SSw0RD)
                if (hashFunction.hashPassword(toggleCase(keyword)).equals(password)) {
                    return true;
                }
                System.out.println("-- toggle all failed");
                
                // Toggle the case of characters at position N (N starts from 0) 
                for (int i = 0; i < keyword.length(); i++) {
                    if (hashFunction.hashPassword(toggleCaseAt(keyword, i)).equals(password)) {
                        return true;
                    }
                }
                System.out.println("-- toggle n post failed");

                // Reverse the entire word
                if (hashFunction.hashPassword(reverseWord(keyword)).equals(password)) {
                    return true;
                }
                System.out.println("-- reverse failed");

                // Duplicate entire word
                if (hashFunction.hashPassword(duplicateWord(keyword)).equals(password)) {
                    return true;
                }
                System.out.println("-- duplicate all failed");

                // Duplicate word reverse front (DrowssaPPassworD)
                if (hashFunction.hashPassword(duplicateWordReverseFront(keyword)).equals(password)) {
                    return true;
                }
                System.out.println("-- duplicate reverse start failed");
                
                // Duplicate word reverse end (PassworDDrowssaP)
                if (hashFunction.hashPassword(duplicateWordReverseEnd(keyword)).equals(password)) {
                    return true;
                }
                System.out.println("-- duplicate reverse end failed");

                // Rotate the word left (p@ssW0rd -> @ssW0rdp)
                if (hashFunction.hashPassword(rotateLeft(keyword)).equals(password)) {
                    return true;
                }
                System.out.println("-- rotate left failed");

                // Rotate the word right (p@ssW0rd -> dp@ssW0r)
                if (hashFunction.hashPassword(rotateRight(keyword)).equals(password)) {
                    return true;
                }
                System.out.println(("-- rotate right failed"));

                // Append 4 characters X to front
                for (int i = 1; i <= 4; i++) {
                    if (appendFront(keyword, password, i)) {
                        return true;
                    }
                }
                System.out.println("-- append front failed");

                // Append 4 characters X to end
                for (int i = 1; i <= 4; i++) {
                    if (appendEnd(keyword, password, i)) {
                        return true;
                    }
                }
                System.out.println("-- append end failed");

                // Append 2 characters X to front and 2 characters X to end
                for (int i = 1; i <= 2; i++) {
                    if (appendFrontAndEnd(keyword, password, i)) {
                        return true;
                    }
                }
                System.out.println("-- append both failed");

                // Delete first character
                if (hashFunction.hashPassword(deleteFirstChar(keyword)).equals(password)) {
                    return true;
                }
                System.out.println("-- delete first failed");

                // Delete last character
                if (hashFunction.hashPassword(deleteLastChar(keyword)).equals(password)) {
                    return true;
                }
                System.out.println("-- delete last failed");

                // Delete character at position N (N starts from 0)
                for (int i = 0; i < keyword.length(); i++) {
                    if (hashFunction.hashPassword(deleteCharAt(keyword, i)).equals(password)) {
                        return true;
                    }
                }
                System.out.println("-- delete n post failed");

                // Overwrite character at position N with symbol
                for (int i = 0; i < keyword.length(); i++) {
                    if (hashFunction.hashPassword(overwriteCharToSymbol(keyword, i)).equals(password)) {
                        return true;
                    }
                }
                System.out.println("-- overwrite symbols n post failed");

                // Overwrite all possible characters with symbols
                if (hashFunction.hashPassword(overwriteAllCharToSymbol(keyword)).equals(password)) {
                    return true;
                }
                System.out.println("-- overwrite symbols all failed");

                // Overwrite symbol at position N with character
                for (int i = 0; i < keyword.length(); i++) {
                    if (hashFunction.hashPassword(overwriteSymbolToChar(keyword, i)).equals(password)) {
                        return true;
                    }
                }
                System.out.println("-- overwrite char n post failed");

                // Overwrite all possible symbols with characters
                if (hashFunction.hashPassword(overwriteAllSymbolToChar(keyword)).equals(password)) {
                    return true;
                }
                System.out.println("-- overwrite char all failed");

                // Swap front and back character
                if (hashFunction.hashPassword(swapFrontAndBack(keyword)).equals(password)) {
                    return true;
                }
                System.out.println("-- swap frond n back failed");

                // Add more method?

            }
        } catch (IOException e) {
            System.out.println("Error reading dictionary file: " + e.getMessage());
        }

        return false;
    }

    /* METHODS */

    private String checkLength(String duplicate, String original) {
        if (duplicate.length() <= MAX_PASSWORD_LENGTH) {
            return duplicate;
        }
        else {
            return original;
        }
    }

    private String capitalizeFirstLetter(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    }

    private String lowerFirstUpperRest(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0,1).toLowerCase() + s.substring(1).toUpperCase();
    }

    private String toggleCase(String s) {
        StringBuilder sb = new StringBuilder(s.length());

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (Character.isLowerCase(c)) {
                sb.append(Character.toUpperCase(c));
            }
            else {
                sb.append(Character.toLowerCase(c));
            }
        }

        return sb.toString();
    }

    private String toggleCaseAt(String s, int idx) {
        char c = s.charAt(idx);
        StringBuilder sb = new StringBuilder(s);

        if (Character.isLowerCase(c)) {
            sb.setCharAt(idx, Character.toUpperCase(c));
        }
        else { 
            sb.setCharAt(idx, Character.toLowerCase(c));
        }

        return sb.toString();
    }

    private String reverseWord(String s) {
        return new StringBuilder(s).reverse().toString();
    }
    
    private String duplicateWord(String s) {
        String duplicated = s + s;

        return this.checkLength(duplicated, s);
    }

    private String duplicateWordReverseFront(String s) {
        String duplicated = new StringBuilder(s).reverse().toString() + s;

        return this.checkLength(duplicated, s);
    }

    private String duplicateWordReverseEnd(String s) {
        String duplicated = s + new StringBuilder(s).reverse().toString();

        return this.checkLength(duplicated, s);
    }

    private String rotateLeft(String s) {
        return s.substring(1) + s.charAt(0);
    }

    private String rotateRight(String s) {
        return s.charAt(s.length()-1) + s.substring(0, s.length()-1);
    }

    private boolean appendFront(String s, String pass, int limit) {
        if (limit == 0) {
            return false;
        }

        for (char c : charSet) {
            String appended = c + s;

            if (appended.length() <= MAX_PASSWORD_LENGTH) {
                String hashedAppended = hashFunction.hashPassword(appended);
                if (hashedAppended.equals(pass)) {
                    return true;
                }

                if (appendFront(appended, pass, limit - 1)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean appendEnd(String s, String pass, int limit) {
        if (limit == 0) {
            return false;
        }

        for (char c : charSet) {
            String appended = s + c;

            if (appended.length() <= MAX_PASSWORD_LENGTH) {
                String hashedAppended = hashFunction.hashPassword(appended);
                if (hashedAppended.equals(pass)) {
                    return true;
                }

                if (appendEnd(appended, pass, limit - 1)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean appendFrontAndEnd(String s, String pass, int limit) {
        if (limit == 0) {
            return false;
        }

        for (char c1 : charSet) {
            for (char c2 : charSet) {
                String appended = c1 + s + c2;

                if (appended.length() <= MAX_PASSWORD_LENGTH) {
                    String hashedAppended = hashFunction.hashPassword(appended);
                    if (hashedAppended.equals(pass)) {
                        return true;
                    }

                    if (appendFrontAndEnd(appended, pass, limit - 1)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private String deleteFirstChar(String s) {
        String deleted = s.substring(1);

        if (deleted.length() < 8) {
            return s;
        }
        else {
            return deleted;
        }
    } 

    private String deleteLastChar(String s) {
        String deleted = s.substring(0, s.length()-1);

        if (deleted.length() < 8) {
            return s;
        }
        else {
            return deleted;
        }
    }

    private String deleteCharAt(String s, int idx) {
        if (idx < 0 || idx >= s.length()) {
            return s;
        } 
        
        return s.substring(0, idx) + s.substring(idx + 1);
    }

    // [A, 4], [a, @], [E, 3], [O, 0], [o, 0], [i, !], [I, 1], [t, +]
    private String overwriteCharToSymbol(String s, int idx) {
        char[] chars = s.toCharArray();

        if (idx >= 0 && idx < chars.length) {
            char curr = chars[idx];

            // Cek untuk setiap pair
            for (char[] pair : replacements) {
                if (pair[0] == curr) {
                    chars[idx] = pair[1];
                    break;
                }
            }
        }

        return new String(chars);
    }

    private String overwriteAllCharToSymbol(String s) {
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char curr = chars[i];

            for (char[] pair : replacements) {
                if (pair[0] == curr) {
                    chars[i] = pair[1];
                    break;
                }
            }
        }

        return new String(chars);
    }

    private String overwriteSymbolToChar(String s, int idx) {
        char[] chars = s.toCharArray();

        if (idx >= 0 && idx < chars.length) {
            char curr = chars[idx];

            // Cek untuk setiap pair
            for (char[] pair : replacements) {
                if (pair[1] == curr) {
                    chars[idx] = pair[0];
                    break;
                }
            }
        }

        return new String(chars);
    }

    private String overwriteAllSymbolToChar(String s) {
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char curr = chars[i];

            for (char[] pair : replacements) {
                if (pair[1] == curr) {
                    chars[i] = pair[0];
                    break;
                }
            }
        }

        return new String(chars);
    }

    private String swapFrontAndBack(String s) {
        String swap = s.charAt(s.length()-1) + s.substring(1, s.length()-1) + s.charAt(0);
        return swap;
    }

}
