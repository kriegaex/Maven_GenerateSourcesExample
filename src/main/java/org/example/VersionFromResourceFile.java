/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the  "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Example code taken from the Apache Xalan project for demonstration purposes only
 */
public class VersionFromResourceFile {
  private static final String POM_PROPERTIES_JAR = "org/example/version.properties";
  private static final String POM_PROPERTIES_FILE_SYSTEM = "target/classes/" + POM_PROPERTIES_JAR;
  private static final String VERSION_NUMBER_PATTERN = "^(\\d+)[.](\\d+)[.](D)?(\\d+)(-SNAPSHOT)?$";
  private static final String NO_VERSION = "0.0.0";

  private static String version = NO_VERSION;
  private static int majorVersionNum;
  private static int releaseVersionNum;
  private static int maintenanceVersionNum;
  private static int developmentVersionNum;

  private static boolean snapshot;

  static {
    readProperties();
    parseVersionNumber();
  }

  private static void readProperties() {
    Properties pomProperties = new Properties();
    try (InputStream fromJar = VersionFromResourceFile.class.getClassLoader().getResourceAsStream(POM_PROPERTIES_JAR)) {
      if (fromJar != null) {
        pomProperties.load(fromJar);
        version = pomProperties.getProperty("version", NO_VERSION);
      } else {
        try (FileInputStream fromFileSystem = new FileInputStream(POM_PROPERTIES_FILE_SYSTEM)) {
          pomProperties.load(fromFileSystem);
          version = pomProperties.getProperty("version", NO_VERSION);
        }
      }
    } catch (IOException e) {
      new RuntimeException("Cannot read properties file to extract version number information: ", e)
        .printStackTrace();
    }
  }

  private static void parseVersionNumber() {
    Matcher matcher = Pattern.compile(VERSION_NUMBER_PATTERN).matcher(version);
    if (matcher.find()) {
      majorVersionNum = Integer.parseInt(matcher.group(1));
      releaseVersionNum = Integer.parseInt(matcher.group(2));
      if (matcher.group(3) == null) {
        maintenanceVersionNum = Integer.parseInt(matcher.group(4));
      } else {
        developmentVersionNum = Integer.parseInt(matcher.group(4));
      }
      snapshot = matcher.group(5) != null && !matcher.group(5).isEmpty();
    } else {
      System.err.println(
        "Cannot match version \"" + version + "\" " +
          "against expected pattern \"" + VERSION_NUMBER_PATTERN + "\""
      );
    }
  }

  public static String getVersion() {
    return getProduct() + " " + getImplementationLanguage() + " "
      + getMajorVersionNum() + "." + getReleaseVersionNum() + "."
      + ((getDevelopmentVersionNum() > 0) ?
      ("D" + getDevelopmentVersionNum()) : ("" + getMaintenanceVersionNum()))
      + (isSnapshot() ? "-SNAPSHOT" : "");
  }

  public static void main(String argv[]) {
    System.out.println(getVersion());
  }

  public static String getProduct() {
    return "FromResourceFile";
  }

  public static String getImplementationLanguage() {
    return "Java";
  }

  public static int getMajorVersionNum() {
    return majorVersionNum;
  }

  public static int getReleaseVersionNum() {
    return releaseVersionNum;
  }

  public static int getMaintenanceVersionNum() {
    return maintenanceVersionNum;
  }

  public static int getDevelopmentVersionNum() {
    return developmentVersionNum;
  }

  public static boolean isSnapshot() {
    return snapshot;
  }
}
