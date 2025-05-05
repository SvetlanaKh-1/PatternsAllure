package ru.netology.delivery.test;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should succesful plan meeting")
    void shouldSuccessfulPlanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var firstMeetingDate = DataGenerator.generateDate(4);
        var secondMeetingDate = DataGenerator.generateDate(7);

        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(Selectors.byText("Запланировать")).click();
        $(Selectors.withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(visible);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $(Selectors.byText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Should unsuccesful plan meeting with invalid city")
    void shouldUnsuccessfulPlanMeetingWithLatinName() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var firstMeetingDate = DataGenerator.generateDate(4);

        $("[data-test-id='city'] input").setValue(DataGenerator.generateInvalidCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(Selectors.byText("Запланировать")).click();
        $("[data-test-id='city'] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    @DisplayName("Should unsuccesful plan meeting with latin name")
    void shouldUnsuccessfulPlanMeetingWithInvalidCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var firstMeetingDate = DataGenerator.generateDate(4);

        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(DataGenerator.generateLatinName("en"));
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(Selectors.byText("Запланировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    @DisplayName("Should unsuccesful plan meeting with invalid phone")
    void shouldUnsuccessfulPlanMeetingWithInvalidPhone() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var firstMeetingDate = DataGenerator.generateDate(4);

        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(DataGenerator.generateInvalidPhone("en"));
        $("[data-test-id='agreement']").click();
        $(Selectors.byText("Запланировать")).click();
        $("[data-test-id='phone'] .input__sub").shouldHave(exactText("Неверный номер мобильного телефона"));
    }
}