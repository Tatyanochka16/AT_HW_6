package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class TransMoney {
    private SelenideElement sumField = $("[data-test-id=amount] .input__control");
    private SelenideElement fromField = $("[data-test-id=from] .input__control");
    private SelenideElement button = $("[data-test-id=action-transfer]");

    private SelenideElement errorMassage = $("[data-test-id = error-notification] .notification__content");

    public DashboardPage toTransMoney(String sum, DataHelper.CardInfo cardInfo) {
        makeTransfer(sum, cardInfo);
        return new DashboardPage();
    }

    public void makeTransfer(String sum, DataHelper.CardInfo cardInfo) {
        sumField.setValue(sum);
        fromField.setValue(cardInfo.getCardNumber());
        button.click();
    }

    public void findErrorMassage(String expectedText) {
        errorMassage.shouldHave(text(expectedText));
    }
}
