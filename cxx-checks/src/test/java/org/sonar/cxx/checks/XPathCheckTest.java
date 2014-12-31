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

import java.io.File;

import org.junit.Test;
import org.sonar.cxx.CxxAstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

public class XPathCheckTest {

  @Test
  public void xpathWithoutFilePattern() {
    XPathCheck check = new XPathCheck();
    check.xpathQuery = "//declaration";
    check.message = "Avoid declarations!! ";

    SourceFile file = CxxAstScanner.scanSingleFile(new File("src/test/resources/checks/xpath.cc"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(1).withMessage(check.message)
      .noMore();
  }

  @Test
  public void xpathWithFilePattern1() {
    XPathCheck check = new XPathCheck();
    check.matchFilePattern = "/**/*.cc"; // all files with .cc file extension
    check.xpathQuery = "//declaration";
    check.message = "Avoid declarations!! ";

    SourceFile file = CxxAstScanner.scanSingleFile(new File("src/test/resources/checks/xpath.cc"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(1).withMessage(check.message)
      .noMore();
  }

  @Test
  public void xpathWithFilePattern2() {
    XPathCheck check = new XPathCheck();
    check.matchFilePattern = "/**/test/**/xpath.cc"; // all files with filename xpath.cc in a subdirectory with name test
    check.xpathQuery = "//declaration";
    check.message = "Avoid declarations!! ";

    SourceFile file = CxxAstScanner.scanSingleFile(new File("src/test/resources/checks/xpath.cc"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(1).withMessage(check.message)
      .noMore();
  }

  @Test
  public void xpathWithFilePattern3() {
    XPathCheck check = new XPathCheck();
    check.matchFilePattern = "/**/*.xxx"; // all files with .xxx file extension
    check.xpathQuery = "//declaration";
    check.message = "Avoid declarations!! ";

    SourceFile file = CxxAstScanner.scanSingleFile(new File("src/test/resources/checks/xpath.cc"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .noMore();
  }

}
