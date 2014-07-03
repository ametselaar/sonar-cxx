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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.SumChildValuesFormula;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.XMLRuleParser;

public final class CxxMetrics implements org.sonar.api.measures.Metrics {
	public static final Metric PARAM_COUNT = new Metric.Builder("cxx-parameter-count", "Parameter count", Metric.ValueType.INT)
			.setDescription("Number of parameter for a function or method")
			.setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
			.setQualitative(false)
			.create();
	public static final Metric VALGRIND_RUNS = new Metric.Builder("cxx-valgrind-runs", "Valgrind runs", Metric.ValueType.INT)
			.setDescription("Number of valgrind reports parsed")
			.setDomain(CoreMetrics.DOMAIN_TESTS)
			.setQualitative(false)
			.setFormula(new SumChildValuesFormula(true))
			.create();
	public static final Metric VALGRIND_ERRORS = new Metric.Builder("cxx-valgrind-errors", "Valgrind errors", Metric.ValueType.INT)
			.setDescription("Number of errors reported by valgrind")
			.setDomain(CoreMetrics.DOMAIN_TESTS)
			.setQualitative(true)
			.setBestValue(0.0).setDirection(Metric.DIRECTION_WORST)
			.setFormula(new SumChildValuesFormula(true))
			.create();
	private static HashMap<String, Metric> otherMetrics = new HashMap<String, Metric> ();
	public List<Metric> getMetrics() {
		if (otherMetrics.isEmpty()) {
			// TODO: unduplicate string constants
			addRuleMetrics("valgrind", "/valgrind.xml", CoreMetrics.DOMAIN_TESTS);
		}
		ArrayList<Metric> result = new ArrayList<Metric>();
		result.addAll(Arrays.asList(PARAM_COUNT, VALGRIND_RUNS, VALGRIND_ERRORS ));
		result.addAll(otherMetrics.values());
		return result;
	}
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
}
