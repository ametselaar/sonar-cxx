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
package org.sonar.plugins.cxx.squid;

import java.util.LinkedList;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.squid.SquidAstVisitor;

import org.sonar.cxx.parser.CxxGrammarImpl;
import org.sonar.plugins.cxx.squid.CxxScope.CxxScopeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
class CxxFileLogger extends SquidAstVisitor
{
	  private static Logger LOG = LoggerFactory.getLogger("CxxFileLogger");
	  private CxxScope current = null;
	  private LinkedList<AstNode> templateContext;

	  @Override
	  public void init() {
		    subscribeTo(CxxGrammarImpl.translationUnit, CxxGrammarImpl.namespaceDefinition, CxxGrammarImpl.classSpecifier,
		    		CxxGrammarImpl.compoundStatement, CxxGrammarImpl.switchStatement, CxxGrammarImpl.iterationStatement, 
		    		CxxGrammarImpl.simpleDeclaration, CxxGrammarImpl.templateDeclaration);
		    current = new CxxScope (null);
		    templateContext = new LinkedList<AstNode> ();
		  }
	  @Override
	  public void visitNode(AstNode node) {
		  String type = null;
		  if (node.is(CxxGrammarImpl.translationUnit)) {
		    String file = getContext().getFile().getName();
		    LOG.info("  ---  Parse " + file);
		  } 
		  else if (node.is(CxxGrammarImpl.namespaceDefinition)) {
			String name = "";
			AstNode child = node.getFirstChild();
			boolean implicitUsing = false;
			if (child.is(CxxGrammarImpl.unnamedNamespaceDefinition)) {
				name = "{anonymous}";
				implicitUsing = true;
			} 
			else if (child.is(CxxGrammarImpl.namedNamespaceDefinition)) {
				name = child.getFirstDescendant(GenericTokenType.IDENTIFIER).getTokenValue();
			}
			current = current.getSubscope(name, CxxScopeType.namespace);
			if (implicitUsing) {
				if (current.getParent().addSearchScope(current)) {
					LOG.debug("Implicit using namespace "+name);
				}
			}
			type = "namespace ";
		  }
		  else if (node.is(CxxGrammarImpl.classSpecifier)) {
			String name = node.getFirstDescendant(CxxGrammarImpl.className).getTokenValue();
			current = current.getSubscope(name, CxxScopeType.cxxclass);
			type = "class ";
		  } 
		  else if (node.is(CxxGrammarImpl.compoundStatement) || node.is(CxxGrammarImpl.switchStatement)) {
			  current = new CxxScope(current);
			  type = "";
		  } 
		  else if (node.is(CxxGrammarImpl.iterationStatement)) {
			  if (node.getFirstChild().getTokenValue().equals("for")) {
				  current = new CxxScope(current);
				  type = "for-scope ";
			  }
		  }
		  else if (node.is(CxxGrammarImpl.templateDeclaration)) {
			  AstNode args = node.getFirstDescendant(CxxGrammarImpl.templateParameterList);
			  if (args == null) {
				  LOG.warn("{}:{}: Cannot find template parameter list.", 
						  new Object[] { getContext().getFile().getName(), node.getToken().getLine()});
			  }
			  templateContext.add(args);
		  }
		  else if (node.is(CxxGrammarImpl.simpleDeclaration)){
			  current.addSymbol(new CxxSymbol(node, templateContext));
		  }
		  if (type != null) {
			LOG.debug("Enter "+ type + current.getFullName());
		  }
	  }
	  @Override
	  public void leaveNode(AstNode node) {
		  if (node.is(CxxGrammarImpl.namespaceDefinition, CxxGrammarImpl.classSpecifier, CxxGrammarImpl.compoundStatement,
				  CxxGrammarImpl.switchStatement)) {
				LOG.debug("Leave " + current.getFullName());
				current = current.getParent();
		   }
		  else if (node.is(CxxGrammarImpl.iterationStatement)) {
			  if (node.getFirstChild().getTokenValue().equals("for")) {
					LOG.debug("Leave " + current.getFullName());
					current = current.getParent();
			  }
		  }
		  else if (node.is(CxxGrammarImpl.templateDeclaration)) {
			  templateContext.removeLast();
		  }
		  else if (node.is(CxxGrammarImpl.translationUnit)) {
		      LOG.debug("  ---  Done");
		  }		  
	  }
}