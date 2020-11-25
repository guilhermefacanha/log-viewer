package br.com.gfsolucoesti.utils;

import java.util.Locale;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import lombok.Getter;

public class FakerUtil {

    @Getter
    private static Faker faker;
    @Getter
    private static FakeValuesService fakeValuesService;

    static {
        faker = new Faker();
        fakeValuesService = new FakeValuesService(Locale.getDefault(), new RandomService());
    }

    private FakerUtil() {
    }

    public static String getFirstName() {
        return faker.name().firstName();
    }

    public static String getLastName() {
        return faker.name().lastName();
    }

    public static String getFullName() {
        return faker.name().fullName();
    }

    public static String getEmail() {
        return faker.internet().emailAddress();
    }

    public static String getEmail(String sufix) {
        return fakeValuesService
                .bothify(faker.name().firstName().toLowerCase() + "." + faker.name().lastName().toLowerCase() + "@" + sufix);
    }

    public static String getRandom(int n) {
        return fakeValuesService.regexify("[a-z1-9]{" + n + "}");
    }

}
