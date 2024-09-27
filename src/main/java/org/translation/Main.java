package org.translation;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {
    private static final CountryCodeConverter COUNTRY_CODE_CONVERTER = new CountryCodeConverter();
    private static final LanguageCodeConverter LANGUAGE_CODE_CONVERTER = new LanguageCodeConverter();

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {

        Translator translator = new JSONTranslator(null);
        // Translator translator = new InLabByHandTranslator();

        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        while (true) {
            String country = promptForCountry(translator);
            String quit = "quit";
            if (quit.equalsIgnoreCase(country)) {
                break;
            }
            // Convert country name to Alpha-3 code
            String countryCode = COUNTRY_CODE_CONVERTER.fromCountry(country);
            if ("Code not found".equals(countryCode)) {
                System.out.println("Invalid country selected.");
                continue;
            }

            String language = promptForLanguage(translator, country);
            if (quit.equalsIgnoreCase(language)) {
                break;
            }

            // Convert language name to 2-letter code
            String languageCode = LANGUAGE_CODE_CONVERTER.fromLanguage(language);
            if ("Code not found".equals(languageCode)) {
                System.out.println("Invalid language selected.");
                continue;
            }

            // Output the translation
            String translation = translator.translate(countryCode, languageCode);
            System.out.println(country + " in " + language + " is " + translation);

            System.out.println("Press enter to continue or type 'quit' to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if (quit.equalsIgnoreCase(textTyped)) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator) {
        List<String> countryCodes = translator.getCountries();

        List<String> countryNames = countryCodes.stream()
                .map(COUNTRY_CODE_CONVERTER::fromCountryCode).sorted().collect(Collectors.toList());

        countryNames.forEach(System.out::println);

        System.out.println("Select a country from above (or type 'quit' to exit):");

        Scanner s = new Scanner(System.in);
        return s.nextLine();

    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator, String country) {

        String countryCode = COUNTRY_CODE_CONVERTER.fromCountry(country);

        List<String> languageCodes = translator.getCountryLanguages(countryCode);

        List<String> languageNames = languageCodes.stream()
                .map(LANGUAGE_CODE_CONVERTER::fromLanguageCode).sorted().collect(Collectors.toList());

        languageNames.forEach(System.out::println);

        System.out.println("Select a language from above (or type 'quit' to exit):");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
