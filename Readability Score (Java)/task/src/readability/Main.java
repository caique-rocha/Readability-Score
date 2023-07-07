package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String text = readFile(args[0]);
        int words = countWords(text);
        int sentences = countSentences(text);
        int characters = countCharacters(text);
        int syllables = countSyllables(text);
        int polysyllables = countPolysyllables(text);

        double scoreARI = calculateARI(words, sentences, characters);
        double scoreFK = calculateFK(words, sentences, syllables);
        double scoreSMOG = calculateSMOG(sentences, polysyllables);
        double scoreCL = calculateCL(characters, words, sentences);

        double averageAge = (scoreARI + scoreFK + scoreSMOG + scoreCL) / 4.0;


        System.out.printf("Words: %d\n", words);
        System.out.printf("Sentences: %d\n", sentences);
        System.out.printf("Characters: %d\n", characters);
        System.out.printf("Syllables: %d\n", syllables);
        System.out.printf("Polysyllables: %d\n", polysyllables);

        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String optionScore = new Scanner(System.in).nextLine();
        System.out.println();

        switch (optionScore) {

            case "ARI" -> printARI(scoreARI);
            case "FK" -> printFK(scoreFK);
            case "SMOG" -> printSMOG(scoreSMOG);
            case "CL" -> printCL(scoreCL);
            case "all" -> {

                printARI(scoreARI);
                printFK(scoreFK);
                printSMOG(scoreSMOG);
                printCL(scoreCL);

                System.out.println();
                System.out.printf("This text should be understood in average by %.2f-year-olds.", averageAge);
            }
            default -> System.out.println("Invalid option!");
        }
    }


    private static String readFile(String path) {

        StringBuilder text = new StringBuilder();
        File file = new File(path);

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNext()) {
                text.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e);
        }

        System.out.println("The text is:");
        System.out.println(text);
        System.out.println();
        return text.toString();
    }

    private static int countWords(String text) {
        return text.split(" ").length;
    }

    private static int countSentences(String text) {
        return text.split("[.!?]").length;
    }

    private static int countCharacters(String text) {
        return text.replace(" ", "").length();
    }

    private static int countSyllables(String text) {

        return text.split("[aeyuio]+[bcdfghj-np-tvwxz]*(e\\b)?").length;
    }

    private static int countPolysyllables(String text) {

        return text.split("([aeyuio]+[bcdfghj-np-tvwxz]+){2}([auyio]|e[bcdfghj-np-tvwxz]+\\b)+").length;

    }

    private static int getAge(double score) {

        int roundScore = (int) Math.ceil(score);
        return  roundScore + 5;
    }

    private static double calculateARI(int words, int sentences, int characters) {
        return 4.71 * characters / words + .5 * words /sentences - 21.43;
    }

    private static double calculateFK(int words, int sentences, int syllables) {

        return .39 * words / sentences + 11.8 * syllables / words - 15.59;
    }

    private static double calculateSMOG(int sentences, int polysyllables) {

        return 1.043 * Math.sqrt(polysyllables * 30 / sentences) + 3.1291;
    }

    private static double calculateCL(int characters, int words, int sentences) {

        int l = characters / words * 100;
        int s = sentences / words * 100;

        return 0.0588 * l - 0.296 * s - 15.8;
    }

    private static void printARI(double score) {
        System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).\n", score, getAge(score));
    }

    private static void printFK(double score) {
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).\n", score, getAge(score));
    }

    private static void printSMOG(double score) {
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).\n", score, getAge(score));
    }

    private static void printCL(double score) {
        System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds).\n", score, getAge(score));
    }
}