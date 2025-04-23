/*
 Done by Mohamed Tamer Younes 20235027 & Mousa Mohamed Mousa 20235042
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LFSR {

    // XOR function: Takes two bits and returns the result of XOR operation
    public static String xor(char x, char y) {
        if (x == y) {
            return "0";  // If bits are the same, return '0'
        } else {
            return "1";  // If bits are different, return '1'
        }
    }

    // LFSR class: Implements a Linear Feedback Shift Register
    static class LinearFeedbackShiftRegister {
        private String seed;  // Store the initial seed
        private List<Integer> tapPosition;  // Store the tap positions

        // Constructor to initialize LFSR with seed and tap positions
        public LinearFeedbackShiftRegister(String seed, List<Integer> tapPosition) {
            this.seed = seed;
            this.tapPosition = tapPosition;
        }

        // Generate the next bit using XOR feedback from tap positions
        public String generateNextBit() {
            String feedbackBit = "0";  // Initialize feedback bit
            for (int tap : tapPosition) {  // Loop through each tap position
                // XOR the feedback bit with the bit at the tap position
                feedbackBit = xor(feedbackBit.charAt(0), seed.charAt(seed.length() - (tap + 1)));
            }
            return feedbackBit;  // Return the computed feedback bit
        }

        // Shift the register and update the seed
        public void shiftRegister(String newBit) {
            seed = newBit + seed.substring(0, seed.length() - 1);  // Add new bit and remove the last bit
        }

        // Generate a pseudorandom sequence of bits
        public String generateSequence(int length) {
            List<String> sequence = new ArrayList<>();  // Initialize an empty list to store the sequence
            for (int i = 0; i < length; i++) {  // Loop to generate the required number of bits
                String nextBitGenerated = generateNextBit();  // Generate the next bit
                sequence.add(nextBitGenerated);  // Add the bit to the sequence
                shiftRegister(nextBitGenerated);  // Update the register
            }
            return String.join("", sequence);  // Convert the list to a string and return it
        }
    }

    // XOR-based stream cipher function
    public static String xorStreamCipher(String plaintext, String keystream) {
        StringBuilder answer = new StringBuilder();  // Initialize an empty list to store the result
        for (int i = 0; i < plaintext.length(); i++) {  // Loop through each bit of the plaintext
            char plainBit = plaintext.charAt(i);  // Get the current bit of the plaintext
            char keyBit = keystream.charAt(i % keystream.length());  // Get the corresponding keystream bit
            String answerBit = xor(plainBit, keyBit);  // Perform XOR operation
            answer.append(answerBit);  // Add the result bit to the list
        }
        return answer.toString();  // Convert the list to a string and return it
    }

    // Function to demonstrate LFSR pseudorandom sequence generation
    public static void useLFSR() {
        Scanner scanner = new Scanner(System.in);

        // Ask user to enter seed (at most 5 characters)
        System.out.print("Enter the seed in binary (at most 5 characters): ");
        String seed = scanner.nextLine();
        while (seed.length() > 5 || !seed.matches("[01]+")) {
            System.out.println("Invalid seed! Please enter a binary string of at most 5 characters.");
            System.out.print("Enter the seed in binary (at most 5 characters): ");
            seed = scanner.nextLine();
        }

        // Ask user to enter tap positions
        System.out.print("Enter tap positions as comma-separated integers: ");
        String tapInput = scanner.nextLine();
        String[] tapArray = tapInput.split(",");
        List<Integer> tapPosition = new ArrayList<>();
        for (String tap : tapArray) {
            tapPosition.add(Integer.parseInt(tap.trim()));
        }

        LinearFeedbackShiftRegister lfsr = new LinearFeedbackShiftRegister(seed, tapPosition);  // Initialize the LFSR
        String sequence = lfsr.generateSequence(100);  // Generate a sequence of 100 bits
        System.out.println("Generated Sequence (100 bits): " + sequence);  // Print the generated sequence
    }

    // Function to demonstrate XOR-based stream cipher using LFSR
    public static void useStreamCipher() {
        Scanner scanner = new Scanner(System.in);

        // Ask user to enter plain text
        System.out.print("Enter plain text: ");
        String plaintext = scanner.nextLine();

        // Convert plaintext to binary (8 bits per character)
        StringBuilder plaintextInBinary = new StringBuilder();
        for (char c : plaintext.toCharArray()) {
            plaintextInBinary.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }
        System.out.println("Plaintext in Binary: " + plaintextInBinary);  // Print the binary plaintext

        // Ask user to enter seed (at most 5 characters)
        System.out.print("Enter the seed in binary (at most 5 characters): ");
        String seed = scanner.nextLine();
        while (seed.length() > 5 || !seed.matches("[01]+")) {
            System.out.println("Invalid seed! Please enter a binary string of at most 5 characters.");
            System.out.print("Enter the seed in binary (at most 5 characters): ");
            seed = scanner.nextLine();
        }

        // Ask user to enter tap positions
        System.out.print("Enter tap positions as comma-separated integers: ");
        String tapInput = scanner.nextLine();
        String[] tapArray = tapInput.split(",");
        List<Integer> tapPosition = new ArrayList<>();
        for (String tap : tapArray) {
            tapPosition.add(Integer.parseInt(tap.trim()));
        }

        LinearFeedbackShiftRegister lfsr = new LinearFeedbackShiftRegister(seed, tapPosition);  // Initialize the LFSR
        String keyStream = lfsr.generateSequence(plaintextInBinary.length());  // Generate keystream
        System.out.println("Keystream: " + keyStream);  // Print the keystream

        // Encrypt the plaintext using the keystream
        String cipherText = xorStreamCipher(plaintextInBinary.toString(), keyStream);
        System.out.println("Ciphertext: " + cipherText);  // Print the ciphertext

        // Decrypt the ciphertext using the same keystream
        String decryption = xorStreamCipher(cipherText, keyStream);
        System.out.println("Decryption: " + decryption);  // Print the decrypted binary

        // Convert decrypted binary back to text
        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < decryption.length(); i += 8) {
            String byteString = decryption.substring(i, i + 8);
            decryptedText.append((char) Integer.parseInt(byteString, 2));
        }
        System.out.println("Decrypted Text: " + decryptedText);  // Print the decrypted text
    }

    // Main program
    public static void main(String[] args) {
        System.out.println("LFSR Pseudorandom Sequence Generation will be generated now");
        useLFSR();  // Run the LFSR demonstration

        System.out.println("\nStream Cipher will be generated now");
        useStreamCipher();  // Run the stream cipher demonstration
    }
}