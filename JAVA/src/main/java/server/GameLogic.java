package server;

import java.util.Random;

public class GameLogic {
    private final int PRIMES_LIMIT = 17;
    private final Random random = new Random();
    private int secretNumber;

    private boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    private int countPrimes(int n) {
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
        return count;
    }

    private int reverseDigits(int number) {
        int reversed = 0;
        while (number != 0) {
            int digit = number % 10;  // Extract last digit
            reversed = reversed * 10 + digit;  // Append digit to reversed number
            number /= 10;  // Remove last digit
        }
        return reversed;
    }

    private int[] getPrimeArray(){
        int numOfPrimes = countPrimes(PRIMES_LIMIT);
        int[] primes = new int[numOfPrimes];
        int j = 0;
        for (int i = 0; i < PRIMES_LIMIT; i++) {
            if (isPrime(i)) {
                primes[j] = i;
                j++;
            }
        }
        return primes;
    }

    private void adjustToLimit(){
        if (this.secretNumber >= 100) this.secretNumber /= 2;
        else if (this.secretNumber < 50) this.secretNumber *= 2;
    }

    private void generateSecret() {
        this.secretNumber = random.nextInt(101);
        int[] primes = getPrimeArray();
        if (secretNumber % 2 == 0){
            this.secretNumber = reverseDigits(secretNumber);
        } else {
            int randomPrime = primes[random.nextInt(primes.length)];
            this.secretNumber += randomPrime;
        }
        adjustToLimit();
    }

    public int validateGuess(String input) throws IllegalArgumentException {
        try {
            int guess = Integer.parseInt(input);
            if (guess <= 1 || guess >= 100) {
                throw new IllegalArgumentException("Number out of range, please guess between 1 and 100.");
            }
            return guess;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input, please enter a number.");
        }
    }

    public boolean checkGuessCorrectness(int guess) {
        return guess == secretNumber;
    }

    public void generatePrefix(int guess) {
        int formatChoice = random.nextInt(3);
        String prefix;

        switch (formatChoice) {
            case 0:
                prefix = (guess % 2 == 0)
                        ? "The number you selected is " + guess + " and it is even!"
                        : "The number you selected is " + guess + " and it is odd!";
                break;
            case 1:
                prefix = (guess > 100)
                        ? "You selected " + guess + ", a number greater than 100! Great choice!"
                        : "You selected " + guess + ", which is a small number!";
                break;
            case 2:
                int randomFact = random.nextInt(100);
                prefix = "The number " + guess + " has a special fact: " + randomFact + " is a random number generated.";
                break;
            default:
                prefix = "You selected " + guess + ".";
        }

        if (guess >= 0 && guess <= 50) {
            prefix += " Your guess is within the safe zone!";
        } else if (guess > 50 && guess <= 150) {
            prefix += " Be careful! Your guess is in the uncertain range.";
        } else {
            prefix += " Your guess is in the high-risk zone!";
        }

        System.out.println(prefix);  // Prints the prefix instead of returning it
    }
}

