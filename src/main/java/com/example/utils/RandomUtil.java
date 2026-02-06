package com.example.utils;

import net.datafaker.Faker;

import java.util.Random;

public class RandomUtil {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    private static final String[] PREFIXES = {
            "Global", "Tech", "Prime", "Blue", "Next", "Dynamic", "Smart", "Quantum", "Alpha", "Nova"
    };

    private static final String[] CORE_WORDS = {
            "Solutions", "Systems", "Technologies", "Industries", "Enterprises",
            "Networks", "Labs", "Works", "Consulting", "Innovations"
    };

    private static final String[] SUFFIXES = {
            "Inc", "LLC", "Ltd", "Group", "Corp"
    };

//    /**
//     * Generates a random meaningful company name
//     * @return company name string
//     */
//    public static String generateRandomCompanyName() {
//        String prefix = PREFIXES[random.nextInt(PREFIXES.length)];
//        String core = CORE_WORDS[random.nextInt(CORE_WORDS.length)];
//        String suffix = SUFFIXES[random.nextInt(SUFFIXES.length)];
//        int number = random.nextInt(1000); // optional numeric suffix for uniqueness
//        return prefix + " " + core + " " + suffix + " " + number;
//    }

    public static String generateRandomCompanyName() {
        return faker.company().name();
    }

    /**
     * Generate a random full name
     */
    public static String generateRandomFullName() {
        return faker.name().fullName();
    }

    /**
     * Generate a random first name
     */
    public static String generateRandomFirstName() {
        return faker.name().firstName();
    }

    /**
     * Generate a random last name
     */
    public static String generateRandomLastName() {
        return faker.name().lastName();
    }

    /**
     * Generate a random email (unique)
     */
    public static String generateRandomEmail() {
        return faker.internet().emailAddress();
    }

    /**
     * Generate a random phone number
     */
    public static String generateRandomPhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }
}
