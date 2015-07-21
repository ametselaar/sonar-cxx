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

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.cxx.api.CxxMetric;
import org.sonar.cxx.parser.CxxGrammarImpl;
import org.sonar.squidbridge.api.SourceFunction;
import org.sonar.squidbridge.checks.ChecksHelper;
import org.sonar.squidbridge.checks.SquidCheck;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleLinearWithOffsetRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.annotations.Tags;

@Rule(
  key = "FunctionComplexity",
  name = "Functions should not be too complex",
  tags = {Tags.BRAIN_OVERLOAD},
  priority = Priority.MAJOR)
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.UNIT_TESTABILITY)
@SqaleLinearWithOffsetRemediation(
  coeff = "1min",
  offset = "10min",
  effortToFixDescription = "per complexity point above the threshold")
public class FunctionComplexityCheck extends SquidCheck<Grammar> {

  private static final int DEFAULT_MAX = 10;

  @RuleProperty(
    key = "max",
    description = "Maximum complexity allowed",
    defaultValue = "" + DEFAULT_MAX)
  private int max = DEFAULT_MAX;

  @Override
  public void init() {
    subscribeTo(CxxGrammarImpl.functionDefinition);
  }

  @Override
  public void leaveNode(AstNode node) {
    SourceFunction sourceFunction = (SourceFunction) getContext().peekSourceCode();
    int complexity = ChecksHelper.getRecursiveMeasureInt(sourceFunction, CxxMetric.COMPLEXITY);
    if (complexity > max) {
      getContext().createLineViolation(this,
        "The Cyclomatic Complexity of this function is {0,number,integer} which is greater than {1,number,integer} authorized.",
        node,
        complexity,
        max);
    }
  }

  public void setMax(int max) {
    this.max = max;
  }
}
