/*
 * Sonar C++ Plugin (Community)
 * Copyright (C) 2011 Waleri Enns and CONTACT Software GmbH
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.cxx.checks;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import com.google.common.io.Files;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import com.sonar.sslr.api.Grammar;
import org.sonar.api.utils.SonarException; //@todo: deprecated, see http://javadocs.sonarsource.org/4.5.2/apidocs/deprecated-list.html
import org.sonar.cxx.visitors.CxxCharsetAwareVisitor;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.api.utils.PathUtils;
import org.sonar.api.utils.WildcardPattern;
import com.sonar.sslr.api.AstNode;
import org.sonar.squidbridge.annotations.NoSqale;
import org.sonar.squidbridge.annotations.RuleTemplate;

@Rule(
  key = "LineRegularExpression",
  name = "Line RegEx rule",
  priority = Priority.MAJOR)
@RuleTemplate
@NoSqale
public class LineRegularExpressionCheck extends SquidCheck<Grammar> implements CxxCharsetAwareVisitor {

  private static final String DEFAULT_MATCH_FILE_PATTERN = "";
  private static final boolean DEFAULT_INVERT_FILE_PATTERN = false;
  private static final String DEFAULT_REGULAR_EXPRESSION = "";
  private static final boolean DEFAULT_INVERT_REGULAR_EXPRESSION = false;
  private static final String DEFAULT_MESSAGE = "The regular expression matches this line";

  private Charset charset;
  private Pattern pattern;

  @RuleProperty(
    key = "matchFilePattern",
    description = "Ant-style matching patterns for path",
    defaultValue = DEFAULT_MATCH_FILE_PATTERN)
  public String matchFilePattern = DEFAULT_MATCH_FILE_PATTERN;

  @RuleProperty(
    key = "invertFilePattern",
    description = "Invert file pattern comparison",
    defaultValue = "" + DEFAULT_INVERT_FILE_PATTERN)
  public boolean invertFilePattern = DEFAULT_INVERT_FILE_PATTERN;

  @RuleProperty(
    key = "regularExpression",
    description = "The regular expression",
    defaultValue = DEFAULT_REGULAR_EXPRESSION)
  public String regularExpression = DEFAULT_REGULAR_EXPRESSION;

  @RuleProperty(
    key = "invertRegularExpression",
    description = "Invert regular expression comparison",
    defaultValue = "" + DEFAULT_INVERT_REGULAR_EXPRESSION)
  public boolean invertRegularExpression = DEFAULT_INVERT_REGULAR_EXPRESSION;

  @RuleProperty(
    key = "message",
    description = "The violation message",
    defaultValue = DEFAULT_MESSAGE)
  public String message = DEFAULT_MESSAGE;

  @Override
  public void init() {
    try {
      pattern = Pattern.compile(regularExpression);
    } catch (PatternSyntaxException e) {
      throw new SonarException(e); //@todo SonarException has been deprecated, see http://javadocs.sonarsource.org/4.5.2/apidocs/deprecated-list.html
    }
  }

  @Override
  public void setCharset(Charset charset) {
    this.charset = charset;
  }

  @Override
  public void visitFile(AstNode fileNode) {
    if (fileNode != null) {
      if (compare(invertFilePattern, matchFile())) {
        List<String> lines;
        try {
          lines = Files.readLines(getContext().getFile(), charset);
        } catch (IOException e) {
          throw new SonarException(e); //@todo SonarException has been deprecated, see http://javadocs.sonarsource.org/4.5.2/apidocs/deprecated-list.html
        }
        for (int i = 0; i < lines.size(); ++i) {
          Matcher matcher = pattern.matcher(lines.get(i));
          if (compare(invertRegularExpression, matcher.find())) {
            getContext().createLineViolation(this, message, i + 1);
          }
        }
      }
    }
  }

  private boolean matchFile() {
    if (!matchFilePattern.isEmpty()) {
      WildcardPattern filePattern = WildcardPattern.create(matchFilePattern);
      String path = PathUtils.sanitize(getContext().getFile().getPath());
      return filePattern.match(path);
    }
    return true;
  }

  private boolean compare(boolean invert, boolean condition) {
    return invert ? !condition : condition;
  }
}
