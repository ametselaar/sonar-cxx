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
package org.sonar.cxx.visitors;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.squidbridge.measures.MetricDef;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.Token;

/**
 * Visitor that counts documented and undocumented API items.<br>
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
// @Rule(key = "UndocumentedApi", description =
// "All public APIs should be documented", priority = Priority.MINOR)
public class CxxPublicApiVisitor<GRAMMAR extends Grammar> extends
        AbstractCxxPublicApiVisitor<Grammar> {

    private static final Logger LOG = LoggerFactory
            .getLogger("CxxPublicApiVisitor");

    private final MetricDef undocumented;
    private final MetricDef api;

    public interface PublicApiHandler {
        void onPublicApi(AstNode node, String id, List<Token> comments);
    };

    private PublicApiHandler handler;

    public CxxPublicApiVisitor(MetricDef publicDocumentedApi,
            MetricDef publicUndocumentedApi) {
        super();
        api = publicDocumentedApi;
        undocumented = publicUndocumentedApi;
    }

    @Override
    protected void onPublicApi(AstNode node, String id, List<Token> comments) {
        boolean commented = !comments.isEmpty();

        LOG.debug("node: " + node.getType() + " line: " + node.getTokenLine()
                + " id: '" + id + "' documented: " + commented);

        if (handler != null) {
            handler.onPublicApi(node, id, comments);
        }

        if (!commented) {
            getContext().peekSourceCode().add(undocumented, 1);
        }

        getContext().peekSourceCode().add(api, 1);
    }

    public void setHandler(PublicApiHandler handler) {
        this.handler = handler;
    }
}
