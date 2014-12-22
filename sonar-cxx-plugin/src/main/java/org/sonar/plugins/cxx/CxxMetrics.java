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
package org.sonar.plugins.cxx;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;
import org.sonar.api.measures.SumChildValuesFormula;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.XMLRuleParser;

public class CxxMetrics implements Metrics {
public static final Metric COMPILER = new Metric.Builder("CXX-COMPILER", "C++ compiler Warnings", Metric.ValueType.INT)
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(true)
      .setDomain("C++")
      .create();
public static final Metric CPPCHECK = new Metric.Builder("CXX-CPPCHECK", "CppCheck Errors", Metric.ValueType.INT)
    .setDirection(Metric.DIRECTION_WORST)
    .setQualitative(true)
    .setDomain("C++")
    .create();
public static final Metric EXTERNAL = new Metric.Builder("CXX-EXTERNAL", "External C++ rules violations", Metric.ValueType.INT)
    .setDirection(Metric.DIRECTION_WORST)
    .setQualitative(true)
    .setDomain("C++")
    .create();
public static final Metric PCLINT = new Metric.Builder("CXX-PCLINT", "PC-Lint errors", Metric.ValueType.INT)
    .setDirection(Metric.DIRECTION_WORST)
    .setQualitative(true)
    .setDomain("C++")
    .create();
public static final Metric RATS = new Metric.Builder("CXX-RATS", "RATS issues", Metric.ValueType.INT)
    .setDirection(Metric.DIRECTION_WORST)
    .setQualitative(true)
    .setDomain("C++")
    .create();
public static final Metric SQUID = new Metric.Builder("CXX-SQUID", "C++ checks", Metric.ValueType.INT)
    .setDirection(Metric.DIRECTION_WORST)
    .setQualitative(true)
    .setDomain("C++")
    .create();
public static final Metric VALGRIND = new Metric.Builder("CXX-VALGRIND", "Valgrind errors", Metric.ValueType.INT)
    .setDirection(Metric.DIRECTION_WORST)
    .setQualitative(true)
    .setDomain("C++")
    .create();
public static final Metric VALGRIND_RUNS = new Metric.Builder("cxx-valgrind-runs", "Valgrind runs", Metric.ValueType.INT)
    .setDescription("Number of valgrind reports parsed")
    .setDomain("C++")
    .setQualitative(false)
    .setFormula(new SumChildValuesFormula(true))
    .create();
public static final Metric VERAXX = new Metric.Builder("CXX-VERAXX", "Vera++ rule violations", Metric.ValueType.INT)
    .setDirection(Metric.DIRECTION_WORST)
    .setQualitative(true)
    .setDomain("C++")
    .create();
public static final Metric DEPENDENCIES = new Metric.Builder("CXX-DEPENDENCIES", "Cyclic dependency violations", Metric.ValueType.INT)
    .setDirection(Metric.DIRECTION_WORST)
    .setQualitative(true)
    .setDomain("C++")
    .create();
public static final Metric PARAM_COUNT = new Metric.Builder("cxx-parameter-count", "Parameter count", Metric.ValueType.INT)
	.setDescription("Number of parameter for a function or method")
	.setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
	.setQualitative(false)
	.create();
private void addRuleMetrics(String key, String filename, String domain) {
	XMLRuleParser xmlParser = new XMLRuleParser();
	List <Rule> rules = new ArrayList<Rule>();
	final InputStream xmlStream = getClass().getResourceAsStream(filename);
    rules.addAll(xmlParser.parse(xmlStream));
    for (Rule rule: rules) {
    	String subKey = key+"-"+rule.getKey();
    	Metric metric = new Metric.Builder("cxx-" + subKey, rule.getName(), Metric.ValueType.INT)
    		.setDescription("Number of " + rule.getName() + "errors reported by " + key)
    		.setDomain(domain)
			.setQualitative(true)
			.setBestValue(0.0).setDirection(Metric.DIRECTION_WORST)
			.setFormula(new SumChildValuesFormula(false))
			.create();
    	otherMetrics.put(subKey, metric);
    }
}
public static Metric findMetric(String section, String key) {
	String subKey = section + "-" + key;
    return otherMetrics.get(subKey);
}	
private static HashMap<String, Metric> otherMetrics = new HashMap<String, Metric> ();
public List<Metric> getMetrics() {
    List<Metric> list = new ArrayList<Metric>();
    list.add(DEPENDENCIES);
    list.add(COMPILER);
    list.add(CPPCHECK);
    list.add(EXTERNAL);
    list.add(PCLINT);
    list.add(RATS);
    list.add(SQUID);
    list.add(VALGRIND);
    list.add(VALGRIND_RUNS);
    list.add(VERAXX);
    list.add(PARAM_COUNT);
	if (otherMetrics.isEmpty()) {
		addRuleMetrics("valgrind", "/valgrind.xml", CoreMetrics.DOMAIN_TESTS);
	}
	list.addAll(otherMetrics.values());
    return list;
  }
}