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

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.cxx.visitors.AbstractCxxPublicApiVisitor;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.Token;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

/**
 * Check that generates issue for undocumented API items.<br>
 * Following items are counted as public API:
 * <ul>
 * <li>classes/structures</li>
 * <li>class members (public and protected)</li>
 * <li>structure members</li>
 * <li>enumerations</li>
 * <li>enumeration values</li>
 * <li>typedefs</li>
 * <li>functions</li>
 * <li>variables</li>
 * </ul>
 * <p>
 * Public API items are considered documented if they have Doxygen comments.<br>
 * Function arguments are not counted since they can be documented in function
 * documentation and this visitor does not parse Doxygen comments.<br>
 * This visitor should be applied only on header files.<br>
 * Currently, no filtering is applied using preprocessing directive.<br>
 * <p>
 * Limitation: only "in front of the declaration" comments are considered.
 *
 * @see <a href="http://www.stack.nl/~dimitri/doxygen/manual/docblocks.html">
 *      Doxygen Manual: Documenting the code</a>
 *
 * @author Ludovic Cintrat
 *
 * @param <GRAMMAR>
 */
@Rule(
  key = "UndocumentedApi",
  name = "Public APIs should be documented",
  priority = Priority.MINOR)
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.ARCHITECTURE_CHANGEABILITY)
@SqaleConstantRemediation("5min")
public class UndocumentedApiCheck extends AbstractCxxPublicApiVisitor<Grammar> {

    private static final Logger LOG = LoggerFactory
            .getLogger("UndocumentedApiCheck");

    private static final List<String> DEFAULT_NAME_SUFFIX = Arrays.asList(".h",
            ".hh", ".hpp", ".H");

    public UndocumentedApiCheck() {
        super();
        withHeaderFileSuffixes(DEFAULT_NAME_SUFFIX);
    }

    @Override
    protected void onPublicApi(AstNode node, String id, List<Token> comments) {
        boolean commented = !comments.isEmpty();

        LOG.debug("node: " + node.getType() + " line: " + node.getTokenLine()
                + " id: '" + id + "' documented: " + commented);

        if (!commented) {
            getContext().createLineViolation(this, "Undocumented API: " + id,
                    node);
        }
    }
}
