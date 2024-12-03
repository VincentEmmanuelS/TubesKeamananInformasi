/**
 * @Note Max append adalah 4 (depan dan belakang), dan kombinasi depan-belakang maksimal 2.
 */

 import java.io.BufferedReader;
 import java.io.FileReader;
 import java.io.IOException;
 
 public class DictionaryBruteForce {
 
     private static final int MAX_PASSWORD_LENGTH = 16;
     private static final char[] charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*?><,.".toCharArray();
 
     public boolean crackPassword(String password) {
         try (BufferedReader read = new BufferedReader(new FileReader("DictionaryDemo.txt"))) {
             String keyword;
             while ((keyword = read.readLine()) != null) {
 
                 // Append 4 characters X to front
                for (int i = 1; i <= 4; i++) {
                    if (appendFront(keyword, password, i)) {
                        return true;
                    }
                }

                // Append 4 characters X to end
                for (int i = 1; i <= 4; i++) {
                    if (appendEnd(keyword, password, i)) {
                        return true;
                    }
                }

                // Append 2 characters X to front and 2 characters X to end
                for (int i = 1; i <= 2; i++) {
                    if (appendFrontAndEnd(keyword, password, i)) {
                        return true;
                    }
                }
 
             }
         } catch (IOException e) {
             System.out.println("Error reading dictionary file: " + e.getMessage());
         }
 
         return false;
     }
 
     /* METHODS */
     private boolean appendFront(String s, String pass, int limit) {
         if (limit == 0) {
             return false;
         }
 
         for (char c : charSet) {
             String appended = c + s;
 
             if (appended.length() <= MAX_PASSWORD_LENGTH) {
                 if (appended.equals(pass)) {
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
                 if (appended.equals(pass)) {
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
                     if (appended.equals(pass)) {
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
 }
 