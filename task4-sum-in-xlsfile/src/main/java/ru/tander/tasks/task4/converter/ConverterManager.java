/*
* This class realize ...
*
* Created by Evgeny Dobrokvashin (aka Stalker) on 02.10.2015 
*
* Copyright (c) 2015 MegaFon, All Rights Reserved.
*/

package ru.tander.tasks.task4.converter;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tander.tasks.task4.config.ApplicationConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * @author Evgeny Dobrokvashin
 *         Created by Stalker on 02.10.2015.
 * @version 1.0 Oct 2015
 */
public class ConverterManager {
  private static final Logger LOG = LoggerFactory.getLogger(ConverterManager.class);

  public void run(Path inputFile) {
    double result = convertAndSumDataInFile(inputFile);

    System.out.format("Sum row data in file: %f %s%n", result, ApplicationConfig.SumInCurrency);
  }

  private double convertAndSumDataInFile(Path inputFile) {
    double result = 0;

    try(InputStream doc = Files.newInputStream(inputFile);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(doc)) {
      HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
      Iterator<Row> rowIterator = hssfSheet.iterator();
      HSSFRow row = null;
      while (rowIterator.hasNext()) {
        row = (HSSFRow)rowIterator.next();
        String currency = row.getCell(1).getStringCellValue();
        double value = row.getCell(0).getNumericCellValue();
        if (currency.equals(ApplicationConfig.SumInCurrency)) {
          result += value;
        }
        else {
          result += value * ApplicationConfig.ExchRateUSD2EUR;
        }
      }

    } catch (IOException e) {
      LOG.error("Error during open file " + inputFile.toString() + ".", e);
    }

    return result;
  }
}
