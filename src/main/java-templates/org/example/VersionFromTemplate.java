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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Example code taken from the Apache Xalan project for demonstration purposes only
 */
public class VersionFromTemplate {
  private static final String VERSION = "${project.version}";
  private static final String VERSION_NUMBER_PATTERN = "^(\\d+)[.](\\d+)[.](D)?(\\d+)(-SNAPSHOT)?$";
  private static int majorVersionNum;
  private static int releaseVersionNum;
  private static int maintenanceVersionNum;
  private static int developmentVersionNum;

  private static boolean snapshot;

  static {
    parseVersionNumber();
  }

  private static void parseVersionNumber() {
    Matcher matcher = Pattern.compile(VERSION_NUMBER_PATTERN).matcher(VERSION);
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
        "Cannot match version \"" + VERSION + "\" " +
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
    return "FromTemplate";
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
