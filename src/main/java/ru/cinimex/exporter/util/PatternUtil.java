package ru.cinimex.exporter.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PatternUtil {

  private PatternUtil() {
    throw new UnsupportedOperationException("Don't try to instantiate util class!");
  }

  public static boolean nameMatchesWithPatterns(String objectName, final Map<String, List<String>> patterns) {
    List<String> includeRules = patterns.get("include") == null ? Collections.emptyList() : patterns.get("include");
    List<String> excludeRules = patterns.get("exclude") == null ? Collections.emptyList() : patterns.get("exclude");
    final String finalObjectName = objectName.trim();

    boolean matches = false;

    for (String includeRule : includeRules) {
      if (includeRule.equals(finalObjectName) || includeRule.equals("*") || (includeRule.contains("*")
          && finalObjectName
          .startsWith(includeRule.substring(0, includeRule.indexOf('*') - 1)))) {
        matches = true;
        break;
      }
    }

    if (matches) {
      for (String excludeRule : excludeRules) {
        if (excludeRule.equals(finalObjectName) || excludeRule.equals("*") || (excludeRule.contains("*")
            && finalObjectName
            .startsWith(excludeRule.substring(0, excludeRule.indexOf('*') - 1)))) {
          matches = false;
          break;
        }
      }
    }

    return matches;
  }
}
