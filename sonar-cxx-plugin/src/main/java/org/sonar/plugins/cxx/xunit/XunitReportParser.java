/*
 * Sonar C++ Plugin (Community)
 * Copyright (C) 2010 Neticoa SAS France
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
package org.sonar.plugins.cxx.xunit;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.in.ElementFilter;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.utils.ParsingUtils;
import org.sonar.api.utils.StaxParser.XmlStreamHandler;
import org.sonar.plugins.cxx.utils.EmptyReportException;

/**
 * {@inheritDoc}
 */
public class XunitReportParser implements XmlStreamHandler {
  private List<TestCase> testCases = new LinkedList<TestCase>();

  /**
   * Returns successfully parsed testcases.
   */
  public List<TestCase> getTestCases() {
    return testCases;
  }

  /**
   * {@inheritDoc}
   */
  public void stream(SMHierarchicCursor rootCursor) throws XMLStreamException {
    SMInputCursor testSuiteCursor = rootCursor.constructDescendantCursor(new ElementFilter("testsuite"));
    try{
      testSuiteCursor.getNext();
    }
    catch(com.ctc.wstx.exc.WstxEOFException eofExc){
      throw new EmptyReportException();
    }

    do{
      parseTestSuiteTag(testSuiteCursor);
    }while (testSuiteCursor.getNext() != null);
  }

  public void parseTestSuiteTag(SMInputCursor testSuiteCursor)
    throws XMLStreamException
  {
    String testSuiteName = testSuiteCursor.getAttrValue("name");
    String testFileName = testSuiteCursor.getAttrValue("filename");

    SMInputCursor childCursor = testSuiteCursor.childElementCursor();
    while (childCursor.getNext() != null) {
      String elementName = childCursor.getLocalName();
      if (elementName.equals("testsuite")) {
        parseTestSuiteTag(childCursor);
      } else if (elementName.equals("testcase")) {
        testCases.add(parseTestCaseTag(childCursor, testSuiteName, testFileName));
      }
    }
  }

  private TestCase parseTestCaseTag(SMInputCursor testCaseCursor, String tsName, String tsFilename)
      throws XMLStreamException
  {
    String classname = testCaseCursor.getAttrValue("classname");
    String name = parseTestCaseName(testCaseCursor);
    Double time = parseTime(testCaseCursor);
    String status = "ok";
    String stack = "";
    String msg = "";

    // Googletest-reports mark the skipped tests with status="notrun"
    String statusattr = testCaseCursor.getAttrValue("status");
    if ("notrun".equals(statusattr)) {
      status = "skipped";
    } else {
      SMInputCursor childCursor = testCaseCursor.childElementCursor();
      if (childCursor.getNext() != null) {
        String elementName = childCursor.getLocalName();
        if (elementName.equals("skipped")) {
          status = "skipped";
        } else if (elementName.equals("failure")) {
          status = "failure";
          msg = childCursor.getAttrValue("message");
          stack = childCursor.collectDescendantText();
        } else if (elementName.equals("error")) {
          status = "error";
          msg = childCursor.getAttrValue("message");
          stack = childCursor.collectDescendantText();
        }
      }
    }

    return new TestCase(name, time.intValue(), status, stack, msg, classname, tsName, tsFilename);
  }

  private double parseTime(SMInputCursor testCaseCursor)
      throws XMLStreamException
  {
    double time = 0.0;
    try {
      String sTime = testCaseCursor.getAttrValue("time");
      if (!StringUtils.isEmpty(sTime)) {
        Double tmp = ParsingUtils.parseNumber(sTime, Locale.ENGLISH);
        if (!Double.isNaN(tmp)) {
          time = ParsingUtils.scaleValue(tmp * 1000, 3);
        }
      }
    } catch (ParseException e) {
      throw new XMLStreamException(e);
    }

    return time;
  }

  private String parseTestCaseName(SMInputCursor testCaseCursor) throws XMLStreamException {
    String name = testCaseCursor.getAttrValue("name");
    String classname = testCaseCursor.getAttrValue("classname");
    if (classname != null) {
      name = classname + "/" + name;
    }
    return name;
  }
}
