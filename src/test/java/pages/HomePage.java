package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getBrandText() {
        return driver.findElement(By.id("nava")).getText();
    }

    public String getFooterText() {
        return driver.findElement(By.cssSelector("#footc .caption p")).getText();
    }

    public SignUpPage clickSignUp() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("signin2"))).click();
        return new SignUpPage(driver);
    }

    public LoginPage clickLogIn() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login2"))).click();
        return new LoginPage(driver);
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout2"))).click();
    }

    public String getLoggedInUsername() {
        WebElement nameOfUser = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser")));
        return nameOfUser.getText();
    }

    public boolean isLoginLinkVisible() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login2")));
        return driver.findElement(By.id("login2")).isDisplayed();
    }

    public WebElement hoverOverFirstProduct() {
        WebElement product = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("#tbodyid .card-title a")));

        Actions actions = new Actions(driver);
        actions.moveToElement(product).perform();

        return product;
    }

    public ContactModal clickContact() {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Contact"))).click();
        return new ContactModal(driver);
    }

    public ProductPage clickFirstProduct() {
        wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("#tbodyid .card-title a"))).click();
        return new ProductPage(driver);
    }

    public boolean isFooterVisible() {
        return driver.findElement(By.id("footc")).isDisplayed();
    }
}
