/*
* This class developed for ...
*
* Created by Evgeny Dobrokvashin (aka Stalker) on 22.09.15
*
* Copyright (c) 2015 Tander, All Rights Reserved.
*/
package ru.tander.tasks.task4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.tander.tasks.task4.config.ApplicationInitializer;
import ru.tander.tasks.task4.converter.ConverterManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main App
 *
 * @author Evgeny Dobrokvashin
 * @version 1.0 sep 2015
 *
 * Created by Stalker on 22.09.15.
 */
public class Main {
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main( String[] args ) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("JRE Version: " + System.getProperty("java.version"));
      LOG.debug("Enter to main()");
    }

    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      ApplicationInitializer.configure();

      if (args.length > 0) {
        String inputFileName = args[0];
        Path inputFile = Paths.get(inputFileName);
        if (Files.exists(inputFile)) {

          ApplicationContext context = new ClassPathXmlApplicationContext("file:./cfg/beans.xml");

          ConverterManager converterManager = (ConverterManager) context.getBean("converterManager");
          converterManager.run(inputFile);

          System.out.println("For close app press Enter key...");
          br.readLine();
        }
        else {
          System.out.format("File [%s] does not exists.%n", inputFileName);
        }
      }
      else {
        System.out.format("Format: run.bat <inputFileName.xls>%n");
      }
    } catch (Exception e) {
      LOG.error("General error: ", e);
    } finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from main()");
      }
    }
  }
}
