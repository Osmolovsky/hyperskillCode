package readability;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class readability {
    
    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }   
    
    private static int getAge(double score) {
        int age = 0;
        switch((int) Math.floor(score)) {
            case 1: {age = 6; break;}
            case 2: {age = 7; break;}
            case 3: {age = 9; break;}
            case 4: {age = 10; break;}
            case 5: {age = 11; break;}
            case 6: {age = 12; break;}
            case 7: {age = 13; break;}
            case 8: {age = 14; break;}
            case 9: {age = 15; break;}
            case 10: {age = 16; break;}
            case 11: {age = 17; break;}
            case 12: {age = 18; break;}
            case 13: {age = 24; break;}
            case 14: {age = 25; break;}
            default: break;
        }
        return age;
    }
    
    private static boolean isVowel(char letter) {
    return letter == 'A' || letter == 'a'
            || letter == 'E' || letter == 'e'
            || letter == 'O' || letter == 'o'
            || letter == 'I' || letter == 'i'
            || letter == 'U' || letter == 'u';
    }
    
    private static int countSyllables(String word)
    {
        //System.out.print("Counting syllables in " + word + "...");
        int numSyllables = 0;
        boolean newSyllable = true;
        String vowels = "aeiouy";
        char[] cArray = word.toCharArray();
        for (int i = 0; i < cArray.length; i++)
        {
            if (i == cArray.length-1 && Character.toLowerCase(cArray[i]) == 'e' 
                    && newSyllable && numSyllables > 0) {
                numSyllables--;
            }
            if (newSyllable && vowels.indexOf(Character.toLowerCase(cArray[i])) >= 0) {
                newSyllable = false;
                numSyllables++;
            }
            else if (vowels.indexOf(Character.toLowerCase(cArray[i])) < 0) {
                newSyllable = true;
            }
        }
        //System.out.println( "found " + numSyllables);
        return numSyllables;
    }

    private static int calcAri (int charCount, int wordCount, int sentenceCount) {
        double score = 4.71 * ((double) charCount / (double) wordCount) + 0.5 * ((double) wordCount / (double) sentenceCount) - 21.43;
        System.out.println("Automated Readability Index: " + Math.round(score * 100.0) / 100.0 + " (about " + getAge(score) + " year olds).");
        return getAge(score);
    }
    
    private static int calcFk (int wordCount, int sentenceCount, int syllablesCount) {
        double score = 0.39 * ((double) wordCount / (double) sentenceCount) + 11.8 * ((double) syllablesCount / (double) wordCount) - 15.59;
        System.out.println("Flesch–Kincaid readability tests: " + Math.round(score * 100.0) / 100.0 + " (about " + getAge(score) + " year olds).");
        return getAge(score);
    }
    
    private static int calcSmog (int sentenceCount, int polysyllablesCount) {
        double score = 1.043 * Math.sqrt((double) polysyllablesCount * (30 / (double) sentenceCount)) + 3.1291;
        System.out.println("Simple Measure of Gobbledygook: " + Math.round(score * 100.0) / 100.0 + " (about " + getAge(score) + " year olds).");
        return getAge(score);
    }
    
    private static int calcCl (int charCount, int wordCount, int sentenceCount) {
        double charPer100w = ((double) charCount / (double) wordCount) * 100;
        double sentencePer100w = (double) sentenceCount / (double) wordCount * 100;
        double score = 0.0588 * charPer100w - 0.296 * sentencePer100w - 15.8;
        System.out.println("Coleman–Liau index: " + Math.round(score * 100.0) / 100.0 + " (about " + getAge(score) + " year olds).");
        return getAge(score);
    }
     
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String path = args[0];
        String input = "";
        try {
            input = readFileAsString(path);
        } catch (IOException e) {
            System.out.println("Cannot read file: " + e.getMessage());
        }
        String regex = "[!.?]+";
        String[] sentence = input.split(regex);
        String[][] words = new String[sentence.length][]; 
        int wordCount = 0;
        int sentenceCount = 0;
        int charCount;
        int syllablesCount = 0;
        int polysyllablesCount = 0;
        
        for (int i = 0; i < sentence.length; i++) {
            words[i] = sentence[i].trim().split(" ");
            wordCount = wordCount + words[i].length;

 
            for (int j = 0; j < words[i].length; j++) {
                int cur = countSyllables(words[i][j]);
                syllablesCount = syllablesCount + cur;
                if (cur > 2) {
                    polysyllablesCount++;
                }
            }
            sentenceCount++;
        }
        charCount = input.replaceAll(" |\n|\t","").split("").length;
       
        System.out.println("The text is: ");
        System.out.println(input);
        System.out.println();
           
        System.out.println("Words: " + (int) wordCount);
        System.out.println("Sentences: " + (int) sentenceCount);
        System.out.println("Characters: " + (int) charCount);
        System.out.println("Syllables: " + (int) syllablesCount);
        System.out.println("Polysyllables: " + (int) polysyllablesCount);
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
        String select = scanner.next();
        switch (select) {
            case "ARI": {
                calcAri(charCount, wordCount, sentenceCount);
                break;
            }
            case "FK": {
                calcFk(wordCount , sentenceCount, syllablesCount);
                break;
            }
            case "SMOG": {
                calcSmog(sentenceCount, polysyllablesCount);
                break;
            }
            case "CL": {
                calcCl(charCount, wordCount, sentenceCount);
                break;
            }
            case "all": {
                double avgAge = 0;
                avgAge = avgAge + calcAri(charCount, wordCount, sentenceCount);
                avgAge = avgAge + calcFk(wordCount , sentenceCount, syllablesCount);;
                avgAge = avgAge + calcSmog(sentenceCount, polysyllablesCount);
                avgAge = avgAge + calcCl(charCount, wordCount, sentenceCount);
                
                System.out.println(avgAge);
                avgAge = (double) avgAge / 4.0;
                System.out.println("This text should be understood in average by " + avgAge + " year olds.");
                break;
            }
            default: {
                System.out.println("Error. Unknown type.");
                break;
            }
        }

        
    }
}

