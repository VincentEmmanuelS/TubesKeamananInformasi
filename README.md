Dibuat untuk memenuhi Tugas Besar mata kuliah Keamanan Informasi

## Topic: Analisis Perbandingan Dictionary Brute Force Hybrid Attack dan Rule-Based Dictionary Attack
* @author Michael William Iswadi        / 6182201019
* @author Vincent Emmanuel Suwardy      / 6182201067
* @author Alexander Vinchent Nathanael  / 6182201089
* @author Lintang Kastara Erlangga      / 6182201097

---
# PLEASE READ THIS FILE BEFORE START TESTING THE CODE OR DOING ANY CHANGES TO THE FILES!!

Explanation for .txt files:
- Dictionary.txt: 
  > Berisi dictionary list password dalam bentuk text untuk melakukan dictionary attack.
  > [Source:-]()
- Password.txt:
  > Berisi list password yang akan diuji (akan di crack menggunakan algoritma).
  > [Source:-]()
- HashedPassword.txt: 
  > Berisi list hashed password dari Password.txt -> di compile dengan HashedPasswordGenerator.java

---

Explanation for .java files (available in comment section on every java files also):
- Main.java: 
  > Class yang akan membandingkan kedua algoritma dictionary attack.
  > Input akan berupa list password yang sudah di hash dengan SHA-256 sebelumnya.
  > Output akan berupa password string (atau "-" jika tidak berhasil) dan waktu yang dibutuhkan untuk menyerang.
- DictionaryAttack.java: 
  > Interface DictionaryAttack
  > Hanya untuk memastikan bahwa kedua algoritma attack berasal dari dictionary attack.
    - DictionaryBruteForce.java: 
      > Class implementasi untuk algoritma dictionary brute force hybrid attack.
    - DictionaryRuleBased.java: 
      > Class implementasi untuk algoritma dictionary rule based attack.
      > [Source: hashcat](https://hashcat.net/wiki/doku.php?id=rule_based_attack)
- PasswordHasher.java: 
  > Class yang akan melakukan hash untuk password menggunakan algoritma SHA-256.
- HashedPasswordGenerator.java: 
  > Class sementara yang akan digunakan untuk melakukan enkripsi password string ke hashed password dengan SHA-256.
  > OutputFile akan selalu di overwrite setiap kali melakukan run (menghindari adanya hashedPassword yang tertinggal ketika list password diganti).

---

Flow explanation:
- Make sure Dictionary.txt is not empty
- Start from input password in Password.txt
- Run HashedPasswordGenerator.java -> wait until the compile is complete
- Make sure HashedPassword.txt is not empty after running HashedPasswordGenerator.java
- Run Main.java -> wait until the compile is complete
- The result will be shown in the Terminal Console

---

TO DO:
- Save the run results in Result.txt
- Make a documentation
