package com.ecommerce.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;

import java.util.Iterator;
import java.util.Set;

public class Interview2Test {

    WebDriver webDriver;

    @BeforeMethod
    public void setUp() throws InterruptedException {
        webDriver = WebDriverManager.chromedriver().create();
        webDriver.manage().window().maximize();
        webDriver.get("https://rahulshettyacademy.com/loginpagePractise/");
        System.out.println("Current Tab URL: " + webDriver.getCurrentUrl());

        Thread.sleep(3000);
    }

    @Test(priority = 0)
    public void newTab() throws InterruptedException {
        WebElement documentLink = webDriver.findElement(By.cssSelector("[href*='documents-request']"));
        documentLink.click();
        Thread.sleep(2000);

        Set<String> windows = webDriver.getWindowHandles();
        Iterator<String> it = windows.iterator();

        String parentTab = it.next();
        String childTab = it.next();

        // Switch to new tab
        webDriver.switchTo().window(childTab);
        System.out.println("Switched to: " + webDriver.getCurrentUrl());

        Thread.sleep(2000);

        // Extract domain from .red class element
        WebElement redTextElement = webDriver.findElement(By.className("red"));
        String text = redTextElement.getText();
        String domain = "";

        if (text != null && !text.isEmpty()) {
            String[] arrayText = text.split("@");
            if (arrayText.length > 1) {
                domain = arrayText[1].split(" ")[0];
                System.out.println("Extracted domain: " + domain);
            } else {
                throw new RuntimeException("Invalid format after @");
            }
        } else {
            throw new RuntimeException("No text found in .red element");
        }

        // Switch back to original tab
        webDriver.switchTo().window(parentTab);
        System.out.println("Switched to: " + webDriver.getCurrentUrl());

        Thread.sleep(2000);

        // Fill username field
        webDriver.findElement(By.id("username")).sendKeys(domain);
        System.out.println("Username filled with: " + domain);
    }

    @Test(priority = 1)
    public void newTab2() throws InterruptedException {
        WebElement documentLink = webDriver.findElement(By.cssSelector("[href*='documents-request']"));
        documentLink.click();
        Thread.sleep(2000);

        // Store parent tab
        String parentTab = webDriver.getWindowHandle();
        Set<String> handles = webDriver.getWindowHandles();

        for (String handle : handles) {
            if (!handle.equals(parentTab)) {
                webDriver.switchTo().window(handle);
                System.out.println("Switched to: " + webDriver.getCurrentUrl());
                break;
            }
        }

        // Extract from new tab
        WebElement redTextElement = webDriver.findElement(By.className("red"));
        String text = redTextElement.getText();
        String domain = text.split("@")[1].split(" ")[0];
        System.out.println("Extracted domain: " + domain);

        // Switch back to parent tab
        webDriver.switchTo().window(parentTab);
        System.out.println("Switched to: " + webDriver.getCurrentUrl());
        Thread.sleep(2000);
        webDriver.findElement(By.id("username")).sendKeys(domain);
        System.out.println("Filled username with: " + domain);
    }




    @AfterMethod
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}












//    const documentLink = page.locator("[href*='documents-request']");
//    const [newPage]=await Promise.all(
//        [
//        context.waitForEvent('page'),//listen for any new page pending,rejected,fulfilled
//            documentLink.click(),
//
//                    ])//new page is opened
//                    */
//
//                    // agar jayada links hota to may be it not work
//                    const [newPage] = await Promise.all([
//                                                        context.waitForEvent('page'),
//      page.click('a[target=_blank]') // opens new tab
//              ]);
//
//   const  text = await newPage.locator(".red").textContent();
//    if (text) {
//      const arrayText = text.split("@")
//      const domain =  arrayText[1].split(" ")[0]
//        console.log(domain);
//        await page.locator("#username").fill(domain);
//        console.log(await page.locator("#username").textContent());
//    } else {
//        throw new Error("Text content not found for selector .red");
//    }




