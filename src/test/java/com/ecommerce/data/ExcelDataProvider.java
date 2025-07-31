package com.ecommerce.data;

import com.ecommerce.utils.ExcelReader;
import org.testng.annotations.DataProvider;

public class ExcelDataProvider {
    @DataProvider(name = "excelData")
    public static Object[][] readExcel() throws Exception {
        return ExcelReader.getData("testdata.xlsx", "Sheet1");
    }
}

