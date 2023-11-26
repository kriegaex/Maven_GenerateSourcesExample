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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Example code taken from the Apache Xalan project for demonstration purposes only
 */
public class VersionFromResourceFile {
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
    // IMPLEMENTATION NOTE: Class.getResourceAsStream uses a *relative* path by
    // default, in contrast to Classloader.getResourceAsStream, which uses an
    // *absolute* one. This is not clearly documented in the JDK, only
    // noticeable by the absence of the word "absolute" in
    // Class.getResourceAsStream javadocs. For more details, see
    // https://www.baeldung.com/java-class-vs-classloader-getresource.
    //
    // Because we expect the properties file to be in the same directory/package
    // as this class, the relative path comes in handy and as a bonus is also
    // relocation-friendly (think Maven Shade).
    try (InputStream fromResource = VersionFromResourceFile.class.getResourceAsStream("version.properties")) {
      if (fromResource != null) {
        pomProperties.load(fromResource);
        version = pomProperties.getProperty("version", NO_VERSION);
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
