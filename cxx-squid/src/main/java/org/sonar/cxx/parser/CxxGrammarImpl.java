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
package org.sonar.cxx.parser;

import static com.sonar.sslr.api.GenericTokenType.EOF;
import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static org.sonar.cxx.api.CxxTokenType.CHARACTER;
import static org.sonar.cxx.api.CxxTokenType.NUMBER;
import static org.sonar.cxx.api.CxxTokenType.STRING;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.cxx.CxxConfiguration;
import org.sonar.cxx.api.CxxKeyword;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerfulGrammarBuilder;

import com.sonar.sslr.api.Grammar;

/**
 * Based on the C++ Standard, Appendix A
 */
public enum CxxGrammarImpl implements GrammarRuleKey {
  // Misc
  BOOL,
  NULLPTR,
  LITERAL,

  // Top level components
  translationUnit,

  // Expressions
  primaryExpression,
  idExpression,
  unqualifiedId,
  qualifiedId,
  nestedNameSpecifier,
  lambdaExpression,
  lambdaIntroducer,
  lambdaCapture,
  captureDefault,
  captureList,
  capture,
  lambdaDeclarator,
  postfixExpression,
  expressionList,
  pseudoDestructorName,
  unaryExpression,
  unaryOperator,
  binaryOperator,
  newExpression,
  newPlacement,
  newTypeId,
  newDeclarator,
  noptrNewDeclarator,
  newInitializer,
  deleteExpression,
  noexceptExpression,
  castExpression,
  pmExpression,
  multiplicativeExpression,
  additiveExpression,
  shiftExpression,
  relationalExpression,
  equalityExpression,
  andExpression,
  exclusiveOrExpression,
  inclusiveOrExpression,
  logicalAndExpression,
  logicalOrExpression,
  conditionalExpression,
  assignmentExpression,
  assignmentOperator,
  par_expression,
  expression,
  constantExpression,

  // Statements
  statement,
  emptyStatement,
  labeledStatement,
  expressionStatement,
  compoundStatement,
  statementSeq,
  condition,
  ifStatement,
  switchStatement,
  switchBlockStatementGroups,
  switchBlockStatementGroup,
  switchLabelStatement,
  iterationStatement,
  forInitStatement,
  forRangeDeclaration,
  forRangeInitializer,
  jumpStatement,
  declarationStatement,

  // Declarations
  declarationSeq,
  declaration,
  blockDeclaration,
  aliasDeclaration,
  simpleDeclaration,
  staticAssertDeclaration,
  emptyDeclaration,
  attributeDeclaration,
  declSpecifier,
  recoveredDeclaration,
  vcAtlDeclaration,   //Microsoft Extension: attributed ATL

  conditionDeclSpecifierSeq,
  forRangeDeclSpecifierSeq,
  parameterDeclSpecifierSeq,
  functionDeclSpecifierSeq,
  simpleDeclSpecifierSeq,
  memberDeclSpecifierSeq,

  storageClassSpecifier,
  functionSpecifier,
  typedefName,
  typeSpecifier,
  typeSpecifierSeq,
  trailingTypeSpecifier,
  trailingTypeSpecifierSeq,
  simpleTypeSpecifier,
  typeName,
  decltypeSpecifier,
  elaboratedTypeSpecifier,
  enumName,
  enumSpecifier,
  enumHead,
  opaqueEnumDeclaration,
  enumKey,
  enumBase,
  enumeratorList,
  enumeratorDefinition,
  enumerator,
  namespaceName,
  originalNamespaceName,
  namespaceDefinition,
  namedNamespaceDefinition,
  originalNamespaceDefinition,
  extensionNamespaceDefinition,
  unnamedNamespaceDefinition,
  namespaceBlock,
  namespaceAlias,
  namespaceAliasDefinition,
  qualifiedNamespaceSpecifier,
  usingDeclaration,
  usingDirective,
  asmDefinition,
  linkageSpecification,
  attributeSpecifierSeq,
  attributeSpecifier,
  alignmentSpecifier,
  attributeList,
  attribute,
  attributeToken,
  attributeScopedToken,
  attributeNamespace,
  attributeArgumentClause,
  balancedTokenSeq,
  balancedToken,
  vcAtlAttribute,   //Microsoft Extension: attributed ATL

  // Declarators
  initDeclaratorList,
  initDeclarator,
  declarator,
  ptrDeclarator,
  noptrDeclarator,
  parametersAndQualifiers,
  trailingReturnType,
  ptrOperator,
  cvQualifierSeq,
  cvQualifier,
  refQualifier,
  declaratorId,
  typeId,
  typeIdEnclosed,
  abstractDeclarator,
  ptrAbstractDeclarator,
  noptrAbstractDeclarator,
  abstractPackDeclarator,
  noptrAbstractPackDeclarator,
  parameterDeclarationClause,
  parameterDeclarationList,
  parameterDeclaration,
  functionDefinition,
  functionBody,
  initializer,
  braceOrEqualInitializer,
  initializerClause,
  initializerList,
  bracedInitList,

  // Classes
  className,
  classSpecifier,
  classHead,
  classHeadName,
  classVirtSpecifier,
  classKey,
  memberSpecification,
  memberDeclaration,
  memberDeclaratorList,
  memberDeclarator,
  virtSpecifierSeq,
  virtSpecifier,
  pureSpecifier,

  // cli extension
  cliTopLevelVisibility,
  cliFinallyClause,
  cliFunctionModifiers,
  cliFunctionModifier,
  cliEventDefinition,
  cliEventModifiers,
  cliPropertyOrEventName,
  cliEventType,
  cliParameterArray,
  cliPropertyDefinition,
  cliPropertyDeclSpecifierSeq,
  cliPropertyBody,
  cliPropertyModifiers,
  cliPropertyIndexes,
  cliPropertyIndexParameterList,
  cliAccessorSpecification,
  cliAccessorDeclaration,
  cliDelegateSpecifier,
  cliDelegateDeclSpecifierSeq,
  cliGenericDeclaration,
  cliGenericParameterList,
  cliConstraintClauseList,
  cliConstraintItemList,
  cliGenericParameter,
  cliGenericId,
  cliGenericName,
  cliGenericArgumentList,
  cliGenericArgument,
  cliConstraintClause,
  cliConstraintItem,
  cliAttributes,
  cliAttributeSection,
  cliAttributeTargetSpecifier,
  cliAttributeTarget,
  cliAttributeList,
  cliAttribute,
  cliAttributeArguments,
  cliPositionArgumentList,
  cliNamedArgumentList,
  cliPositionArgument,
  cliNamedArgument,
  cliAttributeArgumentExpression,

  // Derived classes
  baseClause,
  baseSpecifierList,
  baseSpecifier,
  classOrDecltype,
  baseTypeSpecifier,
  accessSpecifier,

  // Special member functions
  conversionFunctionId,
  conversionTypeId,
  conversionDeclarator,
  ctorInitializer,
  memInitializerList,
  memInitializer,
  memInitializerId,

  // Overloading
  operatorFunctionId,
  operator,
  literalOperatorId,

  // Templates
  templateDeclaration,
  templateParameterList,
  templateParameterListEnclosed,
  templateParameter,
  typeParameter,
  innerTypeParameter,
  simpleTemplateId,
  templateId,
  templateName,
  templateArgumentList,
  templateArgument,
  innerSimpleTemplateId,
  innerTrailingTypeSpecifier,
  innerTypeId,
  innerTemplateId,
  innerTemplateArgumentList,
  innerTemplateArgument,
  typenameSpecifier,
  explicitInstantiation,
  explicitSpecialization,

  // Exception handling
  tryBlock,
  functionTryBlock,
  handlerSeq,
  handler,
  exceptionDeclaration,
  throwExpression,
  exceptionSpecification,
  dynamicExceptionSpecification,
  typeIdList,
  noexceptSpecification;

  public static final Logger LOG = LoggerFactory.getLogger("CxxGrammarImpl");

  public static Grammar create(CxxConfiguration conf) {
    LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();

    toplevel(b, conf);
    expressions(b);
    statements(b);
    declarations(b);
    declarators(b);
    classes(b);
    properties(b);
    derivedClasses(b);
    specialMemberFunctions(b);
    overloading(b);
    templates(b);
    generics(b);
    exceptionHandling(b);
    cliAttributes(b);

    misc(b);
    vcAttributedAtl(b);

    b.setRootRule(translationUnit);

    return b.buildWithMemoizationOfMatchesForAllRules();
  }


  private static void misc(LexerfulGrammarBuilder b) {
    // C++ Standard, Section 2.14.6 "Boolean literals"
    b.rule(BOOL).is(b.firstOf(CxxKeyword.TRUE, CxxKeyword.FALSE));
    b.rule(NULLPTR).is(CxxKeyword.NULLPTR);
    b.rule(LITERAL).is(
      b.firstOf(
        CHARACTER,
        STRING,
        NUMBER,
        BOOL,
        NULLPTR));
  }

  private static void vcAttributedAtl(LexerfulGrammarBuilder b) {
    b.rule(vcAtlAttribute).is(
      "[", b.oneOrMore(b.anyTokenButNot("]")), "]"
    );
    b.rule(vcAtlDeclaration).is(vcAtlAttribute, ";");
  }

  private static void toplevel(LexerfulGrammarBuilder b, CxxConfiguration conf) {
    if (conf.getErrorRecoveryEnabled() == true) {
      b.rule(translationUnit).is(b.zeroOrMore(b.firstOf(declaration, recoveredDeclaration)), EOF);
      b.rule(recoveredDeclaration).is(b.oneOrMore(b.nextNot(b.firstOf(declaration, EOF)), b.anyToken()));
    } else {
      b.rule(translationUnit).is(b.zeroOrMore(declaration), EOF);
    }
  }


  private static void expressions(LexerfulGrammarBuilder b) {
    b.rule(primaryExpression).is(
      b.firstOf(LITERAL,
                CxxKeyword.THIS,
                par_expression,
                idExpression,
                lambdaExpression,
                // EXTENSION: gcc's statement expression: a compound statement enclosed in parentheses may appear as an expression
                b.sequence("(", compoundStatement, ")"))
      ).skipIfOneChild();

    b.rule(par_expression).is(b.sequence("(", expression, ")"));

    b.rule(idExpression).is(b.firstOf(qualifiedId, unqualifiedId));

    b.rule(unqualifiedId).is(
      b.firstOf(
        // Mitigate ambiguity between relational operators < > and angular brackets by looking ahead
        b.sequence(templateId, b.next(b.firstOf("(", ")", "[", "]", "?", ":", binaryOperator, ",", ";", EOF))),
        IDENTIFIER,
        operatorFunctionId,
        conversionFunctionId,
        literalOperatorId,
        b.sequence("~", className),
        b.sequence("~", decltypeSpecifier),
        b.sequence("!", className),
        cliGenericId,
        CxxKeyword.DEFAULT
        )
      );

    b.rule(qualifiedId).is(
      b.firstOf(
            b.sequence(nestedNameSpecifier, b.optional(CxxKeyword.TEMPLATE), unqualifiedId),
            b.sequence("::", IDENTIFIER),
            b.sequence("::", operatorFunctionId),
            b.sequence("::", literalOperatorId),
            b.sequence("::", templateId, b.next(b.firstOf(operator, ";")))
        )
        );

    b.rule(nestedNameSpecifier).is(
      b.firstOf(
        b.sequence(b.optional("::"), typeName, "::"),
        b.sequence(b.optional("::"), namespaceName, "::"),
        b.sequence(decltypeSpecifier, "::")
        ),
      b.zeroOrMore(
        b.firstOf(
          b.sequence(IDENTIFIER, "::"),
          b.sequence(b.optional(CxxKeyword.TEMPLATE), simpleTemplateId, "::")
          )
        )
      );

    b.rule(lambdaExpression).is(lambdaIntroducer, b.optional(lambdaDeclarator), compoundStatement);

    b.rule(lambdaIntroducer).is("[", b.optional(lambdaCapture), "]");

    b.rule(lambdaCapture).is(
      b.firstOf(
        b.sequence(captureDefault, ",", captureList),
        captureList,
        captureDefault
        )
      );

    b.rule(captureDefault).is(b.firstOf("&", "="));

    b.rule(captureList).is(b.sequence(capture, b.optional("...")), b.zeroOrMore(",", b.sequence(capture, b.optional("..."))));

    b.rule(capture).is(
      b.firstOf(
        expression,
        b.sequence("&", expression)
        ));

    b.rule(lambdaDeclarator).is(
      "(", parameterDeclarationClause, ")", b.optional(CxxKeyword.MUTABLE),
      b.optional(exceptionSpecification), b.optional(attributeSpecifierSeq), b.optional(trailingReturnType)
      );

    b.rule(postfixExpression).is(
      b.firstOf(
            b.sequence(simpleTypeSpecifier, "(", b.optional(expressionList), ")"),
            b.sequence(simpleTypeSpecifier, bracedInitList),
            b.sequence(typenameSpecifier, "(", b.optional(expressionList), ")"),
            b.sequence(typenameSpecifier, bracedInitList),

            primaryExpression,

            b.sequence(CxxKeyword.DYNAMIC_CAST, typeIdEnclosed, "(", expression, ")"),
            b.sequence(CxxKeyword.STATIC_CAST, typeIdEnclosed, "(", expression, ")"),
            b.sequence(CxxKeyword.REINTERPRET_CAST, typeIdEnclosed, "(", expression, ")"),
            b.sequence(CxxKeyword.CONST_CAST, typeIdEnclosed, "(", expression, ")"),
            b.sequence(CxxKeyword.TYPEID, "(", expression, ")"),
            b.sequence(CxxKeyword.TYPEID, "(", typeId, ")")
        ),

        // postfixExpression [ expression ]
        // postfixExpression [ bracedInitList ]
        // postfixExpression ( expressionListopt )
        // postfixExpression . template opt(idExpression)
        // postfixExpression -> template opt(idExpression)
        // postfixExpression . pseudoDestructorName
        // postfixExpression -> pseudoDestructorName
        // postfixExpression ++
        // postfixExpression --

        // should replace the left recursive stuff above

        b.zeroOrMore(
        b.firstOf(
            b.sequence("[", expression, "]"),
            b.sequence("(", b.optional(expressionList), ")"),
            b.sequence(b.firstOf(".", "->"),
                b.firstOf(b.sequence(b.optional(CxxKeyword.TEMPLATE), idExpression),
                    pseudoDestructorName)),
            "++",
            "--"
        )
        )
        ).skipIfOneChild();

        b.rule(typeIdEnclosed).is(b.firstOf(
          b.sequence("<", typeId, ">"),
          b.sequence("<", innerTypeId, ">>")));

    b.rule(expressionList).is(initializerList);

    b.rule(pseudoDestructorName).is(
      b.firstOf(
        b.sequence(b.optional(nestedNameSpecifier), typeName, "::", "~", typeName),
        b.sequence(nestedNameSpecifier, CxxKeyword.TEMPLATE, simpleTemplateId, "::", "~", typeName),
        b.sequence(b.optional(nestedNameSpecifier), "~", typeName),
        b.sequence("~", decltypeSpecifier)
        )
      );

    b.rule(unaryExpression).is(
      b.firstOf(
        b.sequence(unaryOperator, castExpression),
        postfixExpression,
        b.sequence("++", castExpression),
        b.sequence("--", castExpression),
        b.sequence(CxxKeyword.SIZEOF, unaryExpression),
        b.sequence(CxxKeyword.SIZEOF, "(", typeId, ")"),
        b.sequence(CxxKeyword.SIZEOF, "...", "(", IDENTIFIER, ")"),
        b.sequence(CxxKeyword.ALIGNOF, "(", typeId, ")"),
        noexceptExpression,
        newExpression,
        deleteExpression
        )
      ).skipIfOneChild();

    b.rule(unaryOperator).is(
      b.firstOf("*", "&", "+", "-", "!", "~")
      );

    b.rule(binaryOperator).is(
      b.firstOf("||", "&&", "&", "|", "^", "==", "!=", "<=", "<", ">=", ">", "<<", ">>", "*", "/", "+", "-", assignmentOperator));
    
    b.rule(newExpression).is(
      b.firstOf(
        b.sequence(b.optional("::"), b.firstOf(CxxKeyword.NEW, CxxKeyword.GCNEW), b.optional(newPlacement), newTypeId, b.optional(newInitializer)),
        b.sequence(b.optional("::"), b.firstOf(CxxKeyword.NEW, CxxKeyword.GCNEW), newPlacement, "(", typeId, ")", b.optional(newInitializer)),
        b.sequence(b.optional("::"), b.firstOf(CxxKeyword.NEW, CxxKeyword.GCNEW), "(", typeId, ")", b.optional(newInitializer))
        )
      );

    b.rule(newPlacement).is("(", expressionList, ")");

    b.rule(newTypeId).is(typeSpecifierSeq, b.optional(newDeclarator));

    b.rule(newDeclarator).is(
      b.firstOf(
        noptrNewDeclarator,
        b.sequence(ptrOperator, b.optional(newDeclarator))
        )
      );

    b.rule(noptrNewDeclarator).is("[", expression, "]", b.optional(attributeSpecifierSeq), b.zeroOrMore("[", constantExpression, "]", b.optional(attributeSpecifierSeq)));

    b.rule(newInitializer).is(
      b.firstOf(
        b.sequence("(", b.optional(expressionList), ")"),
        bracedInitList
        )
      );

    b.rule(deleteExpression).is(b.optional("::"), CxxKeyword.DELETE, b.optional("[", "]"), castExpression);

    b.rule(noexceptExpression).is(CxxKeyword.NOEXCEPT, "(", expression, ")");

    b.rule(castExpression).is(
      b.firstOf(
        // C-COMPATIBILITY: C99 compound literals
        b.sequence("(", typeId, ")", bracedInitList ),
        b.sequence(
          b.next("(", typeId, ")"), "(", typeId, ")", castExpression),
        unaryExpression
        )
      ).skipIfOneChild();

    b.rule(pmExpression).is(castExpression, b.zeroOrMore(b.firstOf(".*", "->*"), castExpression)).skipIfOneChild();

    b.rule(multiplicativeExpression).is(pmExpression, b.zeroOrMore(b.firstOf("*", "/", "%"), pmExpression)).skipIfOneChild();

    b.rule(additiveExpression).is(multiplicativeExpression, b.zeroOrMore(b.firstOf("+", "-"), multiplicativeExpression)).skipIfOneChild();

    b.rule(shiftExpression).is(additiveExpression, b.zeroOrMore(b.firstOf("<<", ">>"), additiveExpression)).skipIfOneChild();

    b.rule(relationalExpression).is(shiftExpression, b.zeroOrMore(b.firstOf("<", ">", "<=", ">="), shiftExpression)).skipIfOneChild();

    b.rule(equalityExpression).is(relationalExpression, b.zeroOrMore(b.firstOf("==", "!="), relationalExpression)).skipIfOneChild();

    b.rule(andExpression).is(equalityExpression, b.zeroOrMore("&", equalityExpression)).skipIfOneChild();

    b.rule(exclusiveOrExpression).is(andExpression, b.zeroOrMore("^", andExpression)).skipIfOneChild();

    b.rule(inclusiveOrExpression).is(exclusiveOrExpression, b.zeroOrMore("|", exclusiveOrExpression)).skipIfOneChild();

    b.rule(logicalAndExpression).is(inclusiveOrExpression, b.zeroOrMore("&&", inclusiveOrExpression)).skipIfOneChild();

    b.rule(logicalOrExpression).is(logicalAndExpression, b.zeroOrMore("||", logicalAndExpression)).skipIfOneChild();

    b.rule(conditionalExpression).is(
      b.firstOf(
        // EXTENSION: gcc's conditional with omitted operands: the expression is optional
        b.sequence(logicalOrExpression, "?", b.optional(expression), ":", assignmentExpression),
        logicalOrExpression
        )
      ).skipIfOneChild();

    b.rule(assignmentExpression).is(
        b.firstOf(
            b.sequence(logicalOrExpression, assignmentOperator, initializerClause),
            conditionalExpression,
            throwExpression
        )
        ).skipIfOneChild();

    b.rule(assignmentOperator).is(b.firstOf("=", "*=", "/=", "%=", "+=", "-=", ">>=", "<<=", "&=", "^=", "|="));

    b.rule(expression).is(assignmentExpression, b.zeroOrMore(",", assignmentExpression));

    b.rule(constantExpression).is(conditionalExpression);
  }



  private static void statements(LexerfulGrammarBuilder b) {

    b.rule(statement).is(
      b.firstOf(
        b.sequence(b.optional(attributeSpecifierSeq), compoundStatement),
        labeledStatement,
        b.sequence(b.optional(attributeSpecifierSeq), expressionStatement),
        b.sequence(b.optional(attributeSpecifierSeq), ifStatement),
        b.sequence(b.optional(attributeSpecifierSeq), switchStatement),
        b.sequence(b.optional(attributeSpecifierSeq), iterationStatement),
        b.sequence(b.optional(attributeSpecifierSeq), jumpStatement),
        declarationStatement,
        b.sequence(b.optional(attributeSpecifierSeq), tryBlock),
        emptyStatement
        )
      );

    b.rule(emptyStatement).is(";");

    b.rule(labeledStatement).is(b.optional(attributeSpecifierSeq), IDENTIFIER, ":", statement);

    b.rule(expressionStatement).is(b.optional(expression), ";");

    b.rule(compoundStatement).is("{", b.optional(statementSeq), "}");

    b.rule(statementSeq).is(b.oneOrMore(statement));

    b.rule(ifStatement).is(
      b.sequence(CxxKeyword.IF, "(", condition, ")", statement, b.optional(CxxKeyword.ELSE, statement))
      );
    // some gnu system headers contain declarations before the first case label
    b.rule(switchStatement).is(CxxKeyword.SWITCH, "(", condition, ")", 
        b.firstOf(b.sequence("{", switchBlockStatementGroups, "}"), 
                  switchBlockStatementGroups));

    b.rule(switchBlockStatementGroups).is("{", b.optional(declarationSeq), b.zeroOrMore(switchBlockStatementGroup), "}");

    b.rule(switchBlockStatementGroup).is(switchLabelStatement, b.optional(emptyStatement), b.zeroOrMore(statement), b.optional(jumpStatement));

    b.rule(switchLabelStatement).is(b.firstOf(
        b.sequence(CxxKeyword.CASE, constantExpression, ":"),
        // EXTENSION: gcc's case range
        b.sequence(CxxKeyword.CASE, constantExpression, "...", constantExpression, ":"),
        b.sequence(CxxKeyword.DEFAULT, ":")));

    b.rule(condition).is(
        b.firstOf(
          b.sequence(b.optional(attributeSpecifierSeq), conditionDeclSpecifierSeq, declarator, b.firstOf(b.sequence("=", initializerClause), bracedInitList)),
          expression
          )
      );

    b.rule(conditionDeclSpecifierSeq).is(
      b.oneOrMore(
        b.nextNot(b.sequence(declarator, b.firstOf("=", "{"))),
        declSpecifier
        ),
      b.optional(attributeSpecifierSeq)
      );

    b.rule(iterationStatement).is(
      b.firstOf(
        b.sequence(CxxKeyword.WHILE, "(", condition, ")", statement),
        b.sequence(CxxKeyword.DO, statement, CxxKeyword.WHILE, "(", expression, ")", ";"),
        b.sequence(CxxKeyword.FOR, "(", forInitStatement, b.optional(condition), ";", b.optional(expression), ")", statement),
        b.sequence(CxxKeyword.FOR, "(", forRangeDeclaration, ":", forRangeInitializer, ")", statement),
        b.sequence(CxxKeyword.FOR, "each", "(", forRangeDeclaration, "in", forRangeInitializer, ")", statement)
        )
      );

    b.rule(forInitStatement).is(
      b.firstOf(
        expressionStatement,
        simpleDeclaration
        )
      );

    b.rule(forRangeDeclaration).is(
      b.optional(attributeSpecifierSeq),
      forRangeDeclSpecifierSeq,
      declarator
    );

    b.rule(forRangeDeclSpecifierSeq).is(
      b.oneOrMore(
        b.nextNot(b.sequence(b.optional(declarator), b.firstOf(":","in"))),
        declSpecifier,
        b.optional(attributeSpecifierSeq)
      )
    );
        
    b.rule(forRangeInitializer).is(
      b.firstOf(
        expression,
        bracedInitList
        )
      );

    b.rule(jumpStatement).is(
      b.firstOf(
        b.sequence(CxxKeyword.BREAK, ";"),
        b.sequence(CxxKeyword.CONTINUE, ";"),
        b.sequence(CxxKeyword.RETURN, b.optional(expression), ";"),
        b.sequence(CxxKeyword.RETURN, bracedInitList, ";"),
        b.sequence(CxxKeyword.GOTO, IDENTIFIER, ";")
        )
      );

    b.rule(declarationStatement).is(blockDeclaration);
  }

  private static void declarations(LexerfulGrammarBuilder b) {
    b.rule(declarationSeq).is(b.oneOrMore(declaration));

    b.rule(declaration).is(
        b.firstOf(
            blockDeclaration,
            functionDefinition,
            templateDeclaration,
            cliGenericDeclaration,
            explicitInstantiation,
            explicitSpecialization,
            linkageSpecification,
            namespaceDefinition,
            emptyDeclaration,
            attributeDeclaration,
            vcAtlDeclaration
        )
        );

    b.rule(blockDeclaration).is(
        b.firstOf(
            simpleDeclaration,
            asmDefinition,
            namespaceAliasDefinition,
            usingDeclaration,
            usingDirective,
            staticAssertDeclaration,
            aliasDeclaration,
            opaqueEnumDeclaration
        )
        );

    b.rule(aliasDeclaration).is(CxxKeyword.USING, IDENTIFIER, b.optional(attributeSpecifierSeq), "=", typeId);

    b.rule(simpleDeclaration).is(
      b.sequence(b.optional(cliAttributes), b.optional(attributeSpecifierSeq), b.optional(simpleDeclSpecifierSeq), b.optional(initDeclaratorList), ";")
        );

    b.rule(simpleDeclSpecifierSeq).is(
      b.oneOrMore(
        b.nextNot(b.sequence(b.optional(initDeclaratorList), ";")),
        declSpecifier,
        b.optional(attributeSpecifierSeq)
        )
      );

    b.rule(staticAssertDeclaration).is(CxxKeyword.STATIC_ASSERT, "(", constantExpression, ",", STRING, ")", ";");

    b.rule(emptyDeclaration).is(";");

    b.rule(attributeDeclaration).is(attributeSpecifierSeq, ";");

    b.rule(declSpecifier).is(
      b.firstOf(
        CxxKeyword.FRIEND, CxxKeyword.TYPEDEF, CxxKeyword.CONSTEXPR,
        storageClassSpecifier,
        functionSpecifier,
        typeSpecifier
        )
      );

    b.rule(storageClassSpecifier).is(
      b.firstOf(CxxKeyword.REGISTER, CxxKeyword.STATIC, CxxKeyword.THREAD_LOCAL, CxxKeyword.EXTERN, CxxKeyword.MUTABLE)
      );

    b.rule(functionSpecifier).is(
      b.firstOf(CxxKeyword.INLINE, CxxKeyword.VIRTUAL, CxxKeyword.EXPLICIT)
      );

    b.rule(typedefName).is(IDENTIFIER);

    b.rule(typeSpecifierSeq).is(b.oneOrMore(typeSpecifier), b.optional(attributeSpecifierSeq));

    b.rule(typeSpecifier).is(
      b.firstOf(
        classSpecifier,
        enumSpecifier,
        trailingTypeSpecifier
        )
      );

    b.rule(trailingTypeSpecifierSeq).is(b.oneOrMore(trailingTypeSpecifier), b.optional(attributeSpecifierSeq));

    b.rule(trailingTypeSpecifier).is(
      b.firstOf(
        simpleTypeSpecifier,
        elaboratedTypeSpecifier,
        typenameSpecifier,
        cvQualifier,
        cliDelegateSpecifier
        )
      );

    b.rule(simpleTypeSpecifier).is(
      b.firstOf(
        CxxKeyword.CHAR, CxxKeyword.CHAR16_T, CxxKeyword.CHAR32_T, CxxKeyword.WCHAR_T,
        CxxKeyword.BOOL,
        CxxKeyword.SHORT, CxxKeyword.INT, CxxKeyword.LONG, CxxKeyword.SIGNED, CxxKeyword.UNSIGNED,
        CxxKeyword.FLOAT, CxxKeyword.DOUBLE, 
        CxxKeyword.VOID, CxxKeyword.AUTO,
        decltypeSpecifier,
        b.sequence(b.optional("::"), b.optional(nestedNameSpecifier), typeName),
//        b.sequence(b.optional(b.firstOf(nestedNameSpecifier,"::")), typeName)
        b.sequence(b.optional("::"), b.optional(nestedNameSpecifier), CxxKeyword.TEMPLATE, simpleTemplateId)
        )
      );

    b.rule(typeName).is(
        b.firstOf(
            className,
            enumName,
            typedefName,
            simpleTemplateId
          )
        );

    // gcc has an extension typeof which should be mapped to decltype and which also accepts a type specifier sequence as argument.
    b.rule(decltypeSpecifier).is(CxxKeyword.DECLTYPE, "(", b.firstOf(expression, typeSpecifierSeq), ")");

    b.rule(elaboratedTypeSpecifier).is(
      b.firstOf(
        b.sequence(b.optional(cliAttributes), classKey, b.optional(nestedNameSpecifier), b.optional(CxxKeyword.TEMPLATE), simpleTemplateId),

        // TODO: the "::"-Alternative to nested-name-specifier is because of need to parse
        // stuff like "friend class ::A". Figure out if there is another way
        b.sequence(b.optional(cliAttributes), classKey, b.optional(attributeSpecifierSeq), b.optional(b.firstOf(nestedNameSpecifier, "::")), IDENTIFIER),

        b.sequence(b.optional(cliAttributes), enumKey, b.optional(nestedNameSpecifier), IDENTIFIER)
        )
      );

    b.rule(enumName).is(IDENTIFIER);

    b.rule(enumSpecifier).is(
      b.firstOf(
        b.sequence(enumHead, "{", b.optional(enumeratorList), "}"),
        b.sequence(enumHead, "{", enumeratorList, ",", "}")
        )
      );

    b.rule(enumHead).is(b.optional(vcAtlAttribute), b.optional(cliTopLevelVisibility), enumKey, b.optional(attributeSpecifierSeq), b.firstOf(b.sequence(nestedNameSpecifier, IDENTIFIER), b.optional(IDENTIFIER)), b.optional(enumBase));

    b.rule(opaqueEnumDeclaration).is(enumKey, b.optional(attributeSpecifierSeq), IDENTIFIER, b.optional(enumBase), ";");

    b.rule(enumKey).is(CxxKeyword.ENUM, b.optional(b.firstOf(CxxKeyword.CLASS, CxxKeyword.STRUCT)));

    b.rule(enumBase).is(":", typeSpecifierSeq);

    b.rule(enumeratorList).is(enumeratorDefinition, b.zeroOrMore(",", enumeratorDefinition));

    b.rule(enumeratorDefinition).is(enumerator, b.optional("=", constantExpression));

    b.rule(enumerator).is(IDENTIFIER);

    b.rule(namespaceName).is(
        b.firstOf(
          originalNamespaceName,
          namespaceAlias
          )
        );

    b.rule(originalNamespaceName).is(IDENTIFIER);

    b.rule(namespaceDefinition).is(
      b.firstOf(
        namedNamespaceDefinition,
        unnamedNamespaceDefinition
        )
      );

    b.rule(namedNamespaceDefinition).is(
      b.firstOf(
        originalNamespaceDefinition,
        extensionNamespaceDefinition
        )
      );
    // gcc allows attributes for namespaces
           
    b.rule(originalNamespaceDefinition).is(b.optional(CxxKeyword.INLINE), CxxKeyword.NAMESPACE, IDENTIFIER, namespaceBlock);

    b.rule(extensionNamespaceDefinition).is(b.optional(CxxKeyword.INLINE), CxxKeyword.NAMESPACE, originalNamespaceName, namespaceBlock);
    b.rule(unnamedNamespaceDefinition).is(b.optional(CxxKeyword.INLINE), CxxKeyword.NAMESPACE, namespaceBlock);
    
    b.rule(namespaceBlock).is("{", b.optional(declarationSeq), "}");

    b.rule(namespaceAlias).is(IDENTIFIER);

    b.rule(namespaceAliasDefinition).is(CxxKeyword.NAMESPACE, IDENTIFIER, "=", qualifiedNamespaceSpecifier, ";");

    b.rule(qualifiedNamespaceSpecifier).is(b.optional(nestedNameSpecifier), namespaceName);

    b.rule(usingDeclaration).is(
        b.firstOf(
            b.sequence(CxxKeyword.USING, b.optional(CxxKeyword.TYPENAME), nestedNameSpecifier, unqualifiedId, ";"),
            b.sequence(CxxKeyword.USING, "::", unqualifiedId, ";")
        )
        );

    b.rule(usingDirective).is(b.optional(attributeSpecifier), CxxKeyword.USING, CxxKeyword.NAMESPACE, b.optional("::"), b.optional(nestedNameSpecifier), namespaceName, ";");

    b.rule(asmDefinition).is(
      b.firstOf(
        b.sequence(CxxKeyword.ASM, "(", STRING, ")", ";"),
        b.sequence(CxxKeyword.ASM, "{", b.oneOrMore(b.nextNot(b.firstOf("}", EOF)), b.anyToken()), "}", b.optional(";")),
        b.sequence(CxxKeyword.ASM, b.oneOrMore(b.nextNot(b.firstOf(";", EOF)), b.anyToken()), ";"),
        b.sequence(CxxKeyword.ASM,
            b.optional(CxxKeyword.VOLATILE),
            "(", 
            b.zeroOrMore(b.firstOf(
                STRING,
                ":", ",",
                b.sequence("[", IDENTIFIER, "]"), 
                b.sequence("(", expression, ")" ))),
            ")", ";")
      ));
    
    b.rule(linkageSpecification).is(CxxKeyword.EXTERN, STRING, b.firstOf(b.sequence("{", b.optional(declarationSeq), "}"), declaration));

    b.rule(attributeSpecifierSeq).is(b.oneOrMore(attributeSpecifier));

    b.rule(attributeSpecifier).is(
        b.firstOf(
            b.sequence("[", "[", attributeList, "]", "]"),
            b.sequence(CxxKeyword.ASM, "(", STRING, ")"), // gcc extension
            cliAttributes,
            alignmentSpecifier
        ));

    b.rule(alignmentSpecifier).is(
        b.firstOf(
            b.sequence(CxxKeyword.ALIGNAS, "(", typeId, b.optional("..."), ")"),
            b.sequence(CxxKeyword.ALIGNAS, "(", assignmentExpression, b.optional("..."), ")")
        ));

    b.rule(attributeList).is(b.optional(attribute), b.zeroOrMore(",", attribute));

    b.rule(attribute).is(attributeToken, b.optional(attributeArgumentClause));

  b.rule(attributeToken).is(
    b.firstOf(
      IDENTIFIER,
      attributeScopedToken
      ));

  b.rule(attributeScopedToken).is(attributeNamespace, "::", IDENTIFIER);

  b.rule(attributeNamespace).is(IDENTIFIER);

  b.rule(attributeArgumentClause).is(balancedTokenSeq);

  b.rule(balancedTokenSeq).is(b.zeroOrMore(balancedToken));

  b.rule(balancedToken).is(
    b.firstOf(
      b.sequence("(", balancedTokenSeq, ")"),
      b.sequence("{", balancedTokenSeq, "}"),
      b.sequence("[", balancedTokenSeq, "]"),
      b.oneOrMore(b.nextNot(b.firstOf("(", ")", "{", "}", "[", "]", EOF)), b.anyToken())
      ));
  }

  private static void declarators(LexerfulGrammarBuilder b) {
    b.rule(initDeclaratorList).is(initDeclarator, b.zeroOrMore(",", initDeclarator));

    b.rule(initDeclarator).is(declarator, b.optional(initializer));

    b.rule(declarator).is(
        b.firstOf(
            ptrDeclarator,
            b.sequence(noptrDeclarator, parametersAndQualifiers, trailingReturnType)
        )
        );

    b.rule(ptrDeclarator).is(
        b.firstOf(
            b.sequence(ptrOperator, ptrDeclarator),
            noptrDeclarator
        )
        );

    b.rule(noptrDeclarator).is(
        b.firstOf(
            b.sequence(declaratorId, b.optional(attributeSpecifierSeq)),
            b.sequence("(", ptrDeclarator, ")")
        ),
        b.zeroOrMore(
          b.firstOf(
              parametersAndQualifiers,
              b.sequence("[", b.optional(constantExpression), "]", b.optional(attributeSpecifierSeq))
          )
          )
        );

    b.rule(parametersAndQualifiers).is("(", parameterDeclarationClause, ")" ,
                                            b.optional(attributeSpecifierSeq),
                                            b.optional(cvQualifierSeq),
                                            b.optional(cliFunctionModifiers),
                                            b.optional(refQualifier),
                                            b.optional(trailingReturnType),
                                            b.optional(exceptionSpecification)
                                            );

    b.rule(trailingReturnType).is(
        b.sequence("->", b.oneOrMore(trailingTypeSpecifierSeq), b.optional(abstractDeclarator))
    );

    b.rule(ptrOperator).is(
        b.firstOf(
            b.sequence("*", b.zeroOrMore(b.firstOf(attributeSpecifierSeq, cvQualifierSeq))),
            b.sequence("&", b.zeroOrMore(attributeSpecifierSeq)),
            b.sequence("&&", b.zeroOrMore(attributeSpecifierSeq)),
            b.sequence(nestedNameSpecifier, "*", b.zeroOrMore(b.firstOf(attributeSpecifierSeq, cvQualifierSeq))),
            //CLI handle reference
            b.sequence("^", b.optional(attributeSpecifierSeq), b.optional(cvQualifierSeq)),
            b.sequence("%", b.optional(attributeSpecifierSeq)),
            // CLI tracking reference
            b.sequence("^%", b.optional(attributeSpecifierSeq))
        )
        );

    b.rule(cvQualifierSeq).is(b.oneOrMore(cvQualifier));

    b.rule(cvQualifier).is(
        b.firstOf(CxxKeyword.CONST, CxxKeyword.VOLATILE)
        );

    b.rule(refQualifier).is(
        b.firstOf("&", "&&")
        );
    
    b.rule(declaratorId).is(
        b.firstOf(
            b.sequence(b.optional(nestedNameSpecifier), className),
            b.sequence(b.optional("..."), idExpression)
        )
        );

    b.rule(typeId).is(typeSpecifierSeq, b.optional(abstractDeclarator));

    b.rule(abstractDeclarator).is(
        b.firstOf(
            ptrAbstractDeclarator,
            b.sequence(b.optional(noptrAbstractDeclarator), parametersAndQualifiers, trailingReturnType),
            abstractPackDeclarator
        )
        );

    b.rule(ptrAbstractDeclarator).is(b.zeroOrMore(ptrOperator), b.optional(noptrAbstractDeclarator));

    b.rule(noptrAbstractDeclarator).is(
        b.optional("(", ptrAbstractDeclarator, ")"),
        b.zeroOrMore(
        b.firstOf(
            parametersAndQualifiers,
            b.sequence("[", b.optional(constantExpression), "]", b.optional(attributeSpecifierSeq))
        )
        )
        );

    b.rule(abstractPackDeclarator).is(b.zeroOrMore(ptrOperator), noptrAbstractPackDeclarator);

    b.rule(noptrAbstractPackDeclarator).is(
        "...",
        b.zeroOrMore(b.firstOf(parametersAndQualifiers,
            b.sequence("[", b.optional(constantExpression), "]", b.optional(attributeSpecifierSeq))
        )
        )
        );

    b.rule(parameterDeclarationClause).is(
        b.firstOf(
            b.sequence(parameterDeclarationList, ",", "..."),
            b.sequence(b.optional(parameterDeclarationList), b.optional("...")),
            cliParameterArray
        )
        );

    b.rule(cliParameterArray).is(b.optional(attribute), "...", parameterDeclaration);

    b.rule(parameterDeclarationList).is(parameterDeclaration, b.zeroOrMore(",", parameterDeclaration));

    b.rule(parameterDeclaration).is(
        b.firstOf(
            b.sequence(b.optional(attributeSpecifierSeq), b.optional(vcAtlAttribute), parameterDeclSpecifierSeq, declarator, b.optional("=", initializerClause)),
            b.sequence(b.optional(attributeSpecifierSeq), parameterDeclSpecifierSeq, b.optional(abstractDeclarator), b.optional("=", initializerClause)))
        );

    b.rule(parameterDeclSpecifierSeq).is(
        b.zeroOrMore(
            b.nextNot(b.sequence(b.optional(declarator), b.firstOf("=", ")", ","))),
            b.sequence(declSpecifier, b.optional("..."))
        ),
        b.optional(attributeSpecifierSeq)
        );

    b.rule(functionDefinition).is(b.optional(attributeSpecifierSeq), 
                                  b.optional(functionDeclSpecifierSeq), 
                                  declarator, 
                                  b.optional(virtSpecifierSeq), 
                                  functionBody);

    b.rule(functionDeclSpecifierSeq).is(
        b.oneOrMore(
           b.nextNot(b.sequence(declarator, b.optional(virtSpecifierSeq), functionBody)),
            declSpecifier,
            b.optional(attributeSpecifierSeq)
        )
        );

    b.rule(functionBody).is(
        b.firstOf(
            b.sequence(b.optional(ctorInitializer), compoundStatement),
            functionTryBlock,
            b.sequence("=", CxxKeyword.DELETE, ";"),
            b.sequence("=", CxxKeyword.DEFAULT, ";")
        )
        );

    b.rule(initializer).is(
        b.firstOf(
            b.sequence("(", expressionList, ")"),
            braceOrEqualInitializer
        )
        );

    b.rule(braceOrEqualInitializer).is(
      b.firstOf(
            b.sequence("=", initializerClause),
            bracedInitList
        )
      );

    b.rule(initializerClause).is(
      b.sequence(
        // C-COMPATIBILITY: C99 designated initializers
        b.optional(b.firstOf(b.sequence(".", IDENTIFIER, "="),
                             // EXTENSION: gcc's designated initializers range
                             b.sequence("[", constantExpression, "...", constantExpression, "]", "="),
                             b.sequence("[", constantExpression, "]", "="))),
        b.firstOf(
          assignmentExpression,
          bracedInitList
          )
        )
      );

    b.rule(initializerList).is(initializerClause, b.optional("..."), b.zeroOrMore(",", initializerClause, b.optional("...")));

    b.rule(bracedInitList).is(
        b.firstOf(
            b.sequence("{", initializerList, b.optional(","), "}"),
            // "{,}" is not allowed for rule bracedInitList
            b.sequence("{","}")
          )
          );
  }

  private static void classes(LexerfulGrammarBuilder b) {
    b.rule(className).is(
      b.firstOf(
        simpleTemplateId,
        b.sequence(b.optional("::"), IDENTIFIER)
        )
      );

    // gcc allows attributes after the class body
    b.rule(classSpecifier).is(b.optional(vcAtlAttribute), classHead, "{", b.optional(memberSpecification), "}", b.optional(attributeSpecifierSeq));

    b.rule(classHead).is(
        b.firstOf(
            b.sequence(b.optional(cliTopLevelVisibility), b.optional(vcAtlAttribute), classKey, b.optional(attributeSpecifierSeq), classHeadName, b.optional(classVirtSpecifier), b.optional(baseClause), b.optional(attributeSpecifierSeq)),
            b.sequence(b.optional(cliTopLevelVisibility), b.optional(vcAtlAttribute), classKey, b.optional(attributeSpecifierSeq), b.optional(baseClause))
        )
        );

    b.rule(cliTopLevelVisibility).is(
        b.firstOf(
            CxxKeyword.PUBLIC, 
            CxxKeyword.PRIVATE
            )
        );

    b.rule(classHeadName).is(b.optional(nestedNameSpecifier), className);

    b.rule(classVirtSpecifier).is(b.firstOf(CxxKeyword.FINAL, "sealed", "abstract"));

    b.rule(classKey).is(
        b.firstOf(b.sequence(b.optional(b.firstOf("ref", "value", "interface")), CxxKeyword.CLASS),
                  b.sequence(b.optional(b.firstOf("ref", "value", "interface")), CxxKeyword.STRUCT),
                  CxxKeyword.UNION
                  )
        );

    b.rule(memberSpecification).is(
        b.oneOrMore(
            b.firstOf(
                memberDeclaration, 
                b.sequence(accessSpecifier, ":")
            )
        )
        );

    b.rule(memberDeclaration).is(
       b.firstOf(
         b.sequence(
           functionDefinition,
           b.optional(emptyStatement)),
         b.sequence(
           b.optional(attributeSpecifierSeq),
           b.optional(vcAtlAttribute), 
           b.optional(b.firstOf("initonly", "literal")),
           b.optional(memberDeclSpecifierSeq),
           b.optional(memberDeclaratorList),
           emptyStatement),

         b.sequence(
           b.optional("::"),
           nestedNameSpecifier,
           b.optional(CxxKeyword.TEMPLATE),
           unqualifiedId,
           emptyStatement),
         usingDeclaration,
         staticAssertDeclaration,
         templateDeclaration,
         aliasDeclaration,
         cliGenericDeclaration,
         cliDelegateSpecifier,
         cliEventDefinition,
         cliPropertyDefinition
       )
       );

    b.rule(cliDelegateDeclSpecifierSeq).is(
        b.oneOrMore(
          b.nextNot(b.sequence(b.optional(declarator), emptyStatement)),
          declSpecifier
        )
      );
    
    b.rule(cliDelegateSpecifier).is(b.optional(cliAttributes), b.optional(cliTopLevelVisibility), "delegate", cliDelegateDeclSpecifierSeq, declarator, emptyStatement);

    b.rule(memberDeclSpecifierSeq).is(
        b.oneOrMore(
            b.nextNot(b.sequence(b.optional(memberDeclaratorList), emptyStatement)),
            declSpecifier
        ),
        b.optional(attributeSpecifierSeq)
        );

    b.rule(memberDeclaratorList).is(memberDeclarator, b.zeroOrMore(",", memberDeclarator));

    b.rule(memberDeclarator).is(
        b.firstOf(
            b.sequence(declarator, braceOrEqualInitializer),
            b.sequence(b.optional(IDENTIFIER), b.optional(attributeSpecifierSeq), ":", constantExpression),
            b.sequence(declarator, b.optional(cliFunctionModifiers), b.optional(virtSpecifierSeq), b.optional(pureSpecifier)),
            declarator
        )
        );

    b.rule(cliFunctionModifiers).is(b.oneOrMore(cliFunctionModifier));

    b.rule(cliFunctionModifier).is(
        b.firstOf( "abstract", CxxKeyword.NEW, "sealed", CxxKeyword.OVERRIDE)
        );

    b.rule(virtSpecifierSeq).is(b.oneOrMore(virtSpecifier));

    b.rule(virtSpecifier).is(
        b.firstOf(CxxKeyword.OVERRIDE, CxxKeyword.FINAL)
        );

    b.rule(pureSpecifier).is(b.sequence("=", "0"));
  }

  private static void derivedClasses(LexerfulGrammarBuilder b) {
    b.rule(baseClause).is(":", baseSpecifierList);

    b.rule(baseSpecifierList).is(baseSpecifier, b.optional("..."), b.zeroOrMore(",", baseSpecifier, b.optional("...")));

    b.rule(baseSpecifier).is(
        b.firstOf(
            b.sequence(b.optional(attributeSpecifierSeq), baseTypeSpecifier),
            b.sequence(b.optional(attributeSpecifierSeq), CxxKeyword.VIRTUAL, b.optional(accessSpecifier), baseTypeSpecifier),
            b.sequence(b.optional(attributeSpecifierSeq), accessSpecifier, b.optional(CxxKeyword.VIRTUAL), baseTypeSpecifier)
        )
        );

    b.rule(classOrDecltype).is(
        b.firstOf(
            classHeadName,
            decltypeSpecifier)
        );

    b.rule(baseTypeSpecifier).is(classOrDecltype);

    b.rule(accessSpecifier).is(
        b.firstOf(
            b.sequence(CxxKeyword.PROTECTED, CxxKeyword.PUBLIC),
            b.sequence(CxxKeyword.PUBLIC, CxxKeyword.PROTECTED),
            b.sequence(CxxKeyword.PROTECTED, CxxKeyword.PRIVATE),
            b.sequence(CxxKeyword.PRIVATE, CxxKeyword.PROTECTED),
            CxxKeyword.PRIVATE,
            CxxKeyword.PROTECTED,
            CxxKeyword.PUBLIC,
            "internal"
            )
        );
  }

  private static void specialMemberFunctions(LexerfulGrammarBuilder b) {
    b.rule(conversionFunctionId).is(CxxKeyword.OPERATOR, conversionTypeId);

    b.rule(conversionTypeId).is(typeSpecifierSeq, b.optional(conversionDeclarator));

    b.rule(conversionDeclarator).is(b.oneOrMore(ptrOperator));

    b.rule(ctorInitializer).is(":", memInitializerList);

    b.rule(memInitializerList).is(memInitializer, b.optional("..."), b.zeroOrMore(",", memInitializer, b.optional("...")));

    b.rule(memInitializer).is(
        b.firstOf(
          b.sequence(memInitializerId, "(", b.optional(expressionList), ")"), 
          b.sequence(memInitializerId, bracedInitList)
          )
        );

    b.rule(memInitializerId).is(
        b.firstOf(
            classOrDecltype,
            IDENTIFIER
        )
        );
  }

  private static void overloading(LexerfulGrammarBuilder b) {
    b.rule(operatorFunctionId).is(CxxKeyword.OPERATOR, operator);

    b.rule(operator).is(
        b.firstOf(
            b.sequence(CxxKeyword.NEW, "[", "]"),
            b.sequence(CxxKeyword.DELETE, "[", "]"),
            CxxKeyword.NEW, CxxKeyword.DELETE,
            "+", "-", "!", "=", "^=", "&=", "<=", ">=",
            b.sequence("(", ")"),
            b.sequence("[", "]"),
            "*", "<", "|=", "&&", "/",
            ">", "<<", "||", "%", "+=", ">>", "++", "^", "-=", ">>=", "--", "&", "*=", "<<=",
            ",", "|", "/=", "==", "->*", "~", "%=", "!=", "->"
        )
        );

    b.rule(literalOperatorId).is(CxxKeyword.OPERATOR, "\"\"", IDENTIFIER);
  }

  private static void properties(LexerfulGrammarBuilder b) {
    b.rule(cliPropertyOrEventName).is(b.firstOf(IDENTIFIER, CxxKeyword.DEFAULT));

    b.rule(cliPropertyDeclSpecifierSeq).is(
        b.oneOrMore(
            b.nextNot(b.sequence(declarator, b.optional(cliPropertyBody), b.optional(";"))),
            typeSpecifier
            )
        );

    b.rule(cliPropertyDefinition).is(
        b.optional(cliAttributes), 
        b.optional(cliPropertyModifiers), 
        "property", 
        b.firstOf(cliPropertyDeclSpecifierSeq, b.sequence(b.optional(nestedNameSpecifier), b.optional(cliPropertyOrEventName))), 
        declarator,
        b.firstOf(
            emptyStatement, 
            cliPropertyBody)
        );

    b.rule(cliPropertyBody).is(b.optional(cliPropertyIndexes), "{", cliAccessorSpecification, "}");

    b.rule(cliPropertyModifiers).is(b.oneOrMore(b.firstOf(CxxKeyword.VIRTUAL, CxxKeyword.STATIC)));

    b.rule(cliPropertyIndexes).is("[", cliPropertyIndexParameterList,"]");

    b.rule(cliPropertyIndexParameterList).is(typeId, b.zeroOrMore(",", typeId));

    b.rule(cliAccessorSpecification).is(b.zeroOrMore(b.optional(b.sequence(accessSpecifier, ":")), cliAccessorDeclaration));

    b.rule(cliAccessorDeclaration).is(
        b.firstOf(
            functionDefinition,
            b.sequence(b.optional(attribute), b.optional(simpleDeclSpecifierSeq), b.optional(memberDeclaratorList), ";")
            )
            );

    b.rule(cliEventDefinition).is(
        b.optional(cliAttributes), 
        b.optional(cliEventModifiers),
        "event",
        cliEventType, 
        IDENTIFIER,
        b.firstOf(emptyStatement, b.sequence("{", cliAccessorSpecification,"}"))
        );

    b.rule(cliEventModifiers).is(b.oneOrMore(b.firstOf(CxxKeyword.VIRTUAL, CxxKeyword.STATIC)));
    b.rule(cliEventType).is(
        b.firstOf(
            b.sequence(b.optional("::"), b.optional(nestedNameSpecifier), typeName, b.optional("^")),
            b.sequence(b.optional("::"), b.optional(nestedNameSpecifier), CxxKeyword.TEMPLATE, templateId, "^")
        )
        );
  }

  private static void templates(LexerfulGrammarBuilder b) {
    b.rule(templateDeclaration).is(CxxKeyword.TEMPLATE, templateParameterListEnclosed, declaration);

    b.rule(templateParameterListEnclosed).is(b.firstOf(
      b.sequence("<", templateParameterList, ">"),
      b.sequence("<", b.zeroOrMore(templateParameter, ","), innerTypeParameter, ">>")));

    b.rule(templateParameterList).is(templateParameter, b.zeroOrMore(",", templateParameter));

    b.rule(templateParameter).is(
        b.firstOf(
            b.sequence(typeParameter, b.next(b.firstOf(",", ">"))),
            // boost uses the typename keyword when passing an argument of a type determined by another argument 
            b.sequence(b.optional(CxxKeyword.TYPENAME), parameterDeclaration)
        )
        );

    b.rule(typeParameter).is(
        b.firstOf(
            b.sequence(CxxKeyword.CLASS, b.optional(IDENTIFIER), "=", typeId),
            b.sequence(CxxKeyword.CLASS, b.optional("..."), b.optional(IDENTIFIER)),
            b.sequence(CxxKeyword.TYPENAME, b.optional(IDENTIFIER), "=", typeId),
            b.sequence(CxxKeyword.TYPENAME, b.optional("..."), b.optional(IDENTIFIER)),
            b.sequence(CxxKeyword.TEMPLATE, templateParameterListEnclosed, CxxKeyword.CLASS, b.optional(IDENTIFIER), "=", idExpression),
            b.sequence(CxxKeyword.TEMPLATE, templateParameterListEnclosed, CxxKeyword.CLASS, b.optional("..."), b.optional(IDENTIFIER))
        )
        );

    b.rule(innerTypeParameter).is(
      b.firstOf(
          b.sequence(CxxKeyword.CLASS, b.optional(IDENTIFIER), "=", innerTypeId),
          b.sequence(CxxKeyword.TYPENAME, b.optional(IDENTIFIER), "=", innerTypeId),
          b.sequence(CxxKeyword.TEMPLATE, templateParameterListEnclosed, CxxKeyword.CLASS, b.optional(IDENTIFIER), "=", innerTypeId)));

    b.rule(simpleTemplateId).is(b.firstOf(
      b.sequence(templateName, "<", b.optional(templateArgumentList), ">"),
      b.sequence(templateName, "<", innerTemplateId, ">>")));

    b.rule(innerTemplateId).is(
        b.sequence(b.zeroOrMore(b.nextNot(b.sequence(innerTypeId, ">>")), templateArgument, b.optional("..."), ","),
          innerTypeId)
      );

    b.rule(innerTypeId).is(
      b.firstOf(
        innerTrailingTypeSpecifier,
        b.sequence(b.oneOrMore(typeSpecifier), innerTrailingTypeSpecifier),
        b.sequence(typeSpecifierSeq, b.optional(noptrAbstractDeclarator), parametersAndQualifiers, "->", innerTrailingTypeSpecifier)
      ));

    b.rule(innerTrailingTypeSpecifier).is(
      b.firstOf(
        // simpleTypeSpecifier:
        b.sequence(b.optional("::"), b.optional(nestedNameSpecifier), innerSimpleTemplateId),
        b.sequence(b.optional("::"), b.optional(nestedNameSpecifier), CxxKeyword.TEMPLATE, innerSimpleTemplateId),
        // elaboratedTypeSpecifier:
        b.sequence(b.optional(cliAttributes), classKey, b.optional(nestedNameSpecifier), b.optional(CxxKeyword.TEMPLATE), innerSimpleTemplateId),
        // typenameSpecifier:
        b.sequence(CxxKeyword.TYPENAME, nestedNameSpecifier, CxxKeyword.TEMPLATE, innerSimpleTemplateId)
        // cvQualifier, cliDelegateSpecifier: never end with a template
        )
      );

    b.rule(innerSimpleTemplateId).is(templateName, "<", innerTemplateArgumentList);

    b.rule(templateId).is(
        b.firstOf(
            simpleTemplateId,
            b.sequence(operatorFunctionId, "<", b.optional(templateArgumentList), ">"),
            b.sequence(literalOperatorId, "<", b.optional(templateArgumentList), ">"),
            b.sequence(operatorFunctionId, "<", innerTemplateId, ">>"),
            b.sequence(literalOperatorId, "<", innerTemplateId, ">>")
        )
        );

    b.rule(templateName).is(IDENTIFIER);

    b.rule(templateArgumentList).is(templateArgument, b.optional("..."), b.zeroOrMore(",", templateArgument, b.optional("...")));

    b.rule(innerTemplateArgumentList).is(innerTemplateArgument, b.optional("..."), b.zeroOrMore(",", innerTemplateArgument, b.optional("...")));

    b.rule(templateArgument).is(
      b.firstOf(
        b.sequence(typeId, b.next(b.firstOf(">", ",", "..."))),
        b.sequence(typenameSpecifier, b.next(b.firstOf(">", ",", "..."))), // seen in gnu system headers
        // FIXME: workaround to parse stuff like "carray<int, 10>"
        // actually, it should be covered by a constantExpression rule,
        // but it doesnt work because of ambiguity template syntax <--> relationalExpression
        b.sequence(shiftExpression, b.next(b.firstOf(">", ","))),
        b.sequence(constantExpression, b.next(b.firstOf(">", ",")))
//        idExpression
    ));

    b.rule(innerTemplateArgument).is(
      b.firstOf(
        b.sequence(typeId, b.next(b.firstOf(">>", ",", "..."))),
        b.sequence(typenameSpecifier, b.next(b.firstOf(">>", ",", "..."))), // seen in gnu system headers
        // FIXME: workaround to parse stuff like "carray<int, 10>", see above
        b.sequence(additiveExpression, b.next(b.firstOf(">>", ","))),
        b.sequence(constantExpression, b.next(b.firstOf(">>", ",")))
//        idExpression
      )
    );

    b.rule(typenameSpecifier).is(
        CxxKeyword.TYPENAME, nestedNameSpecifier,
        b.firstOf(b.sequence(b.optional(CxxKeyword.TEMPLATE), simpleTemplateId), IDENTIFIER));

    b.rule(explicitInstantiation).is(b.optional(CxxKeyword.EXTERN), CxxKeyword.TEMPLATE, declaration);

    b.rule(explicitSpecialization).is(CxxKeyword.TEMPLATE, "<", ">", declaration);
  }

  private static void generics(LexerfulGrammarBuilder b) {
    b.rule(cliGenericDeclaration).is("generic", "<", cliGenericParameterList, ">", b.optional(cliConstraintClauseList), declaration);

    b.rule(cliGenericParameterList).is(cliGenericParameter, b.zeroOrMore(",", cliGenericParameter));

    b.rule(cliGenericParameter).is(b.optional(attribute), b.firstOf(CxxKeyword.CLASS, CxxKeyword.TYPENAME), IDENTIFIER );

    b.rule(cliGenericId).is(cliGenericName, "<", cliGenericArgumentList, ">");

    b.rule(cliGenericName).is(b.firstOf(IDENTIFIER, operatorFunctionId));

    b.rule(cliGenericArgumentList).is(cliGenericArgument, b.zeroOrMore(",", cliGenericArgument));

    b.rule(cliGenericArgument).is(typeId);

    b.rule(cliConstraintClauseList).is(cliConstraintClause, b.zeroOrMore(cliConstraintClause));

    b.rule(cliConstraintClause).is("where", IDENTIFIER, ":", cliConstraintItemList);

    b.rule(cliConstraintItemList).is(cliConstraintItem, b.zeroOrMore(",", cliConstraintItem));

    b.rule(cliConstraintItem).is(b.firstOf(
        typeId, 
        b.sequence(b.firstOf("ref", "value") , b.firstOf(CxxKeyword.CLASS,CxxKeyword.STRUCT)),
        CxxKeyword.GCNEW
        ));
  }

  private static void exceptionHandling(LexerfulGrammarBuilder b) {
    b.rule(tryBlock).is(b.firstOf(
        b.sequence(CxxKeyword.TRY, compoundStatement, handlerSeq, cliFinallyClause),
        b.sequence(CxxKeyword.TRY, compoundStatement, handlerSeq),
        b.sequence(CxxKeyword.TRY, compoundStatement, cliFinallyClause)
        )
        );

    b.rule(functionTryBlock).is(b.firstOf(
        b.sequence(CxxKeyword.TRY, b.optional(ctorInitializer), compoundStatement, handlerSeq, cliFinallyClause),
        b.sequence(CxxKeyword.TRY, b.optional(ctorInitializer), compoundStatement, handlerSeq),
        b.sequence(CxxKeyword.TRY, b.optional(ctorInitializer), compoundStatement, cliFinallyClause)
        )
        );

    b.rule(handlerSeq).is(b.oneOrMore(handler));

    b.rule(cliFinallyClause).is("finally", compoundStatement);

    b.rule(handler).is(CxxKeyword.CATCH, "(", exceptionDeclaration, ")", compoundStatement);

    b.rule(exceptionDeclaration).is(
        b.firstOf(
            b.sequence(b.optional(attributeSpecifierSeq), typeSpecifierSeq, b.firstOf(declarator, b.optional(abstractDeclarator))),
            "..."
        )
        );

    b.rule(throwExpression).is(CxxKeyword.THROW, b.optional(assignmentExpression));

    b.rule(exceptionSpecification).is(
        b.firstOf(
            dynamicExceptionSpecification,
            noexceptSpecification
        )
        );

    b.rule(dynamicExceptionSpecification).is(CxxKeyword.THROW, "(", b.optional(typeIdList), ")");

    b.rule(typeIdList).is(typeId, b.optional("..."), b.zeroOrMore(",", typeId, b.optional("...")));

    b.rule(noexceptSpecification).is(CxxKeyword.NOEXCEPT, b.optional("(", constantExpression, ")"));
  }
  
  private static void cliAttributes(LexerfulGrammarBuilder b) {
    b.rule(cliAttributes).is(b.oneOrMore(cliAttributeSection));

    b.rule(cliAttributeSection).is("[", b.optional(cliAttributeTargetSpecifier), cliAttributeList,"]");

    b.rule(cliAttributeTargetSpecifier).is(cliAttributeTarget, ":");

    b.rule(cliAttributeTarget).is(
        b.firstOf(
           "assembly", CxxKeyword.CLASS, "constructor", "delegate", CxxKeyword.ENUM, "event", "field",
           "interface", "method", "parameter", "property", "returnvalue", CxxKeyword.STRUCT
        ));

    b.rule(cliAttributeList).is(cliAttribute, b.zeroOrMore(",", cliAttribute));
    b.rule(cliAttribute).is(typeName, b.optional(cliAttributeArguments));
    b.rule(cliAttributeArguments).is("(", 
        b.firstOf(
            b.optional(cliPositionArgumentList),
            b.sequence(cliPositionArgumentList, ",", cliNamedArgumentList),
            cliNamedArgumentList
            ),
        ")");

    b.rule(cliPositionArgumentList).is(cliPositionArgument, b.zeroOrMore(",", cliPositionArgument));
    b.rule(cliPositionArgument).is(cliAttributeArgumentExpression);

    b.rule(cliNamedArgumentList).is(cliNamedArgument, b.zeroOrMore(",", cliNamedArgument));
    b.rule(cliNamedArgument).is(IDENTIFIER, "=", cliAttributeArgumentExpression);
    
    b.rule(cliAttributeArgumentExpression).is(assignmentExpression);
    
  }
}
