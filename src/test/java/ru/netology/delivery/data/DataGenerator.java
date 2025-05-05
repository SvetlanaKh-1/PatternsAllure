package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        return LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateCity() {
        String[] cities = new String[]{"Москва", "Санкт-Петербург", "Красноярск", "Новосибирск", "Владивосток", "Омск", "Уфа", "Майкоп", "Тюмень", "Махачкала"};
        return cities[new Random().nextInt(cities.length)];
    }

    public static String generateInvalidCity() {
        String[] invalidCities = new String[]{"Ачинск", "Подольск", "Минусинск", "Выборг", "Орск", "Норильск", "Рубцовск", "Канск", "Нерюнгри", "Сочи"};
        return invalidCities[new Random().nextInt(invalidCities.length)];
    }

    public static String generateName(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return (faker.name().lastName() + " " + faker.name().firstName());
    }

    public static String generateLatinName(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return (faker.name().lastName() + " " + faker.name().firstName());
    }

    public static String generatePhone(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.phoneNumber().phoneNumber();
    }

    public static String generateInvalidPhone(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.numerify("#####");
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            return new UserInfo(generateCity(), generateName(locale), generatePhone(locale));
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}
