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

import static org.sonar.sslr.tests.Assertions.assertThat;

import org.junit.Test;

public class AttributeTest extends ParserBaseTest {

  @Test
  public void classSpecifier_reallife() {
    p.setRootRule(g.rule(CxxGrammarImpl.attributeSpecifierSeq));

    assertThat(p).matches("[[attr]]");
    assertThat(p).matches("[[attr(a)]]");
    assertThat(p).matches("[[attr(\"text\")]]");
    assertThat(p).matches("[[attr(true)]]");
    assertThat(p).matches("[[attr(int)]]");
    assertThat(p).matches("[[attr(a, b, c)]]");
    assertThat(p).matches("[[nmspc::attr]]");
    assertThat(p).matches("[[nmspc::attr(args)]]");
    assertThat(p).matches("[[attr1, attr2, attr3(args)]]");
    assertThat(p).matches("[[db::id, db::auto, db::type(\"INT\")]]");
    assertThat(p).matches("[[omp::parallel(clause,clause)]]");

    assertThat(p).matches("[[noreturn]]");
    assertThat(p).matches("[[carries_dependency]]");
    assertThat(p).matches("[[deprecated]]");
    assertThat(p).matches("[[deprecated(\"reason\")]]");
    
    assertThat(p).matches("[[attr1]] [[attr2]] [[attr3]]");
  }

}
