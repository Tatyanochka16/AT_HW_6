package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

class MoneyTransferTest {

    DashboardPage dashboardPage;
    CardInfo firstCardInfo;
    CardInfo secondCardInfo;
    int firstCardBalance;
    int secondCardBalance;


    @BeforeEach
    void setup() {
        var loginPage = open("http://localhost:9999", LoginPageV2.class);
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCardInfo = getFirstCardInfo();
        secondCardInfo = getSecondCardInfo();
        firstCardBalance = dashboardPage.getCardBalance(0);
        secondCardBalance = dashboardPage.getCardBalance(1);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsFS() {

        int sum = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = dashboardPage.getCardBalance(0) - sum;
        var expectedBalanceSecondCard = dashboardPage.getCardBalance(1) + sum;
        var transMoney = dashboardPage.selectCardForTransfer(secondCardInfo);
        dashboardPage = transMoney.toTransMoney(String.valueOf(sum), firstCardInfo);
        dashboardPage.reloadDashboardPage();
        var actualBalanceForFirstCard = dashboardPage.getCardBalance(0);
        var actualBalanceForSecondCard = dashboardPage.getCardBalance(1);
        assertAll(() -> assertEquals(expectedBalanceFirstCard,
                        actualBalanceForFirstCard),
                () -> assertEquals(expectedBalanceSecondCard, actualBalanceForSecondCard));
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsSF() {
        int sum = generateValidAmount(secondCardBalance);
        var expectedBalanceSecondCard = dashboardPage.getCardBalance(1) - sum;
        var expectedBalanceFirstCard = dashboardPage.getCardBalance(0) + sum;
        var transMoney = dashboardPage.selectCardForTransfer(firstCardInfo);
        dashboardPage = transMoney.toTransMoney(String.valueOf(sum), secondCardInfo);
        dashboardPage.reloadDashboardPage();
        var actualBalanceForSecondCard = dashboardPage.getCardBalance(1);
        var actualBalanceForFirstCard = dashboardPage.getCardBalance(0);
        assertAll(() -> assertEquals(expectedBalanceFirstCard,
                        actualBalanceForFirstCard),
                () -> assertEquals(expectedBalanceSecondCard, actualBalanceForSecondCard));
    }

    @Test
    void shouldErrorMassage() {
        int sum = generateInvalidAmount(secondCardBalance);
        var thirdCardInfo = new CardInfo("5559000000000003", "0f3f5c2a-249e-4c3d-8287-09f7a039391d");
        var transMoney = dashboardPage.selectCardForTransfer(firstCardInfo);
        dashboardPage = transMoney.toTransMoney(String.valueOf(sum), thirdCardInfo);
        transMoney.findErrorMassage("Ошибка! Произошла ошибка");

    }


}

