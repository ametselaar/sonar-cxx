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

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;

import java.util.regex.Pattern;
import org.sonar.cxx.parser.CxxGrammarImpl;
import org.sonar.squidbridge.annotations.Tags;

@Rule(
  key = "ClassName",
  priority = Priority.MINOR,
  name = "Class names should comply with a naming convention",
  tags = Tags.CONVENTION
)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("5min")
@ActivatedByDefault
public class ClassNameCheck extends SquidCheck<Grammar> {

  private static final String DEFAULT = "^[A-Z_][a-zA-Z0-9]+$";

  @RuleProperty(
    key = "format",
    defaultValue = "" + DEFAULT)
  public String format = DEFAULT;
  private Pattern pattern = null;

  @Override
  public void init() {
    pattern = Pattern.compile(format);
    subscribeTo(CxxGrammarImpl.classSpecifier);
  }

  @Override
  public void visitNode(AstNode astNode) {
    AstNode nameNode = astNode.getFirstDescendant(CxxGrammarImpl.className);
    if (nameNode != null) {
      String className = nameNode.getTokenValue();
      if (!pattern.matcher(className).matches()) {
        getContext().createLineViolation(this,
          "Rename class \"{0}\" to match the regular expression {1}.", astNode, className, format);
      }
    }
  }

}
