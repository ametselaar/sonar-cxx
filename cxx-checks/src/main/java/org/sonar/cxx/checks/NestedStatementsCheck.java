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


import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Grammar;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.cxx.parser.CxxGrammarImpl;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.annotations.Tags;
import org.sonar.squidbridge.checks.SquidCheck;

import java.util.List;
import java.util.Set;

@Rule(
  key = "NestedStatements",
  name = "Control flow statements \"if\", \"switch\", \"try\" and iterators should not be nested too deeply",
  tags = { Tags.BRAIN_OVERLOAD },
  priority = Priority.MAJOR
)
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LOGIC_CHANGEABILITY)
@SqaleConstantRemediation("10min")
public class NestedStatementsCheck extends SquidCheck<Grammar> {

  private static final AstNodeType[] CHECKED_TYPES = new AstNodeType[]{
    CxxGrammarImpl.ifStatement,
    CxxGrammarImpl.switchStatement,
    CxxGrammarImpl.tryBlock,
    CxxGrammarImpl.iterationStatement
  };

  private static final int DEFAULT_MAX = 3;

  private static final String ELSE_TOKEN = "ELSE";

  @RuleProperty(defaultValue = "" + DEFAULT_MAX,
      description = "Maximum allowed control flow statement nesting depth.")
  public int max = DEFAULT_MAX;

  private Set<AstNode> checkedNodes = Sets.newHashSet();
  private int nestingLevel = 0;

  @Override
  public void init() {
    subscribeTo(CHECKED_TYPES);
  }

  @Override
  public void visitNode(AstNode node) {
    if (checkedNodes.contains(node)) {
      return;
    }

    List<AstNode> watchedDescendants = node.getDescendants(CHECKED_TYPES);

    // In the AST 'else if' blocks are technically nested, but should not increase the nesting level as they are
    // actually flat in terms of 'spaghetti code'. This bypasses the nesting increment/decrement for such blocks.
    if (isElseIf(node)) {
      visitChildren(watchedDescendants);
    } else {
      nestingLevel++;

      // If the max level is reached, stop descending the tree and create a violation
      if (nestingLevel == max + 1) {
        getContext().createLineViolation(
            this,
            "Refactor this code to not nest more than " + max + " if/switch/try/for/while/do statements.",
            node);
      } else {
        visitChildren(watchedDescendants);
      }

      nestingLevel--;
    }

    // Prevent re-checking of descendent nodes
    checkedNodes.addAll(watchedDescendants);
  }

  private void visitChildren(List<AstNode> watchedDescendants) {
    for (AstNode descendant : watchedDescendants) {
      visitNode(descendant);
    }
  }

  /**
   * @return True if the given node is the 'if' in an 'else if' construct.
   */
  private boolean isElseIf(AstNode node) {
    return node.getType() == CxxGrammarImpl.ifStatement
      && node.getParent().getPreviousAstNode().getType().toString().equals(ELSE_TOKEN);
  }
}
