package com.ecommerce.tests;

import com.ecommerce.data.ExcelDataProvider;
import org.testng.annotations.Test;

public class ExcelDrivenTest {

    @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
    public void testLogin(String username, String password) {
        System.out.println("Username: " + username + ", Password: " + password);
    }
}
