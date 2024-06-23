Modified Affine Cipher

This Java program implements a modified version of the Affine Cipher, a type of monoalphabetic substitution cipher. The program includes functionalities to encrypt, decrypt, and decipher text files using specified keys. The cipher works by transforming characters using the formula:

E(x) = (ax + b) mod m

where:
E(x) is the encrypted character
- a and b are the keys
- x is the original character
- m is the modulus (128 in this implementation, covering the ASCII character set)

Features

Encrypt
Encrypts a plain text file using the specified alpha and beta keys.

Decrypt
Decrypts an encrypted text file using the specified alpha and beta keys.

Decipher
Attempts to break the encryption without knowing the keys by using a dictionary file for analysis.

Usage

The program accepts three main commands: `encrypt`, `decrypt`, and `decipher`.

Encrypt

java ModifiedAffineCipher encrypt [input-text-file] [destination-file] [alpha] [beta]

Example:

java ModifiedAffineCipher encrypt plain.txt encrypted.txt 5 8


Decrypt
java ModifiedAffineCipher decrypt [encrypted-text-file] [destination-file] [alpha] [beta]

Example:
java ModifiedAffineCipher decrypt encrypted.txt decrypted.txt 5 8

Decipher
java ModifiedAffineCipher decipher [encrypted-text-file] [destination-file] [dictionary-file]

Example:
java ModifiedAffineCipher decipher encrypted.txt analysis.txt dictionary.txt


Implementation Details

Extended Euclidean Algorithm
The program uses the Extended Euclidean Algorithm to compute the modular inverse of the alpha key, necessary for the decryption process.

Valid Key Check
The `isKeyValid` method ensures that the chosen keys are valid by checking the greatest common divisor (GCD) conditions.

Encryption and Decryption
The `encrypt` and `decrypt` methods read the input files, apply the Affine Cipher transformation, and write the results to the destination files.

Deciphering
The `decipher` method performs a brute-force search over possible key pairs, using a dictionary to identify the most likely valid decryption.

Example Dictionary File
The dictionary file used for the `decipher` command should contain a list of valid words separated by spaces or new lines.

Error Handling
The program provides usage instructions when incorrect arguments are provided and checks for invalid key pairs, prompting the user to select another key pair if necessary.

Build and Run
Ensure you have Java installed on your machine. Compile and run the program using the following commands:

For compiling:
javac ModifiedAffineCipher.java

For running:
java ModifiedAffineCipher [command] [arguments]

License
This project is licensed under the MIT License - see the LICENSE file for details.



