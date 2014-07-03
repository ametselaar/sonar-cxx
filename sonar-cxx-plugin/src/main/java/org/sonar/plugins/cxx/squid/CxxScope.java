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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CxxScope {
	public enum CxxScopeType { global, namespace, cxxclass, block}
	private CxxScope parent;
	private String name;
	private CxxScopeType type;
	private List<CxxScope> subscopes;
	private List<CxxScope> searchScopes;
	private List<CxxSymbol> symbols;
	private Map<String, String> aliases;
	
	public CxxScope(CxxScope parent) {
		this(parent, "", (parent == null) ? CxxScopeType.global : CxxScopeType.block);
	}
	private CxxScope(CxxScope parent, String name, CxxScopeType type) {
		this.parent = parent;
		this.name = name;
		this.type = type;
		this.subscopes = new ArrayList<CxxScope>();
		this.searchScopes = new ArrayList<CxxScope>();
		this.symbols = new ArrayList<CxxSymbol>();
		this.aliases = new HashMap<String, String>();
	}
	public CxxScope getSubscope(String name, CxxScopeType type) {
		CxxScope res = null;
		for (CxxScope scope : subscopes) {
			if (scope.getName().equals(name))
			{
				if (scope.getType() == type) {
					res = scope;
					break;
				}
				else {
					// todo: issue warning here
				}
			}
		}
		if (res == null) {
			res = new CxxScope(this, name, type);
			subscopes.add(res);
		}
		return res;
	}
	public CxxScope getScope(String name, CxxScopeType type) {
		String [] names = name.split("::");
		return getScope(names, type);
	}
	private CxxScope getScope(String[] names, CxxScopeType type2) {
		CxxScope curr = this;
		int n = 0;
		if (names[0].equals("")) {
			while (curr.getParent() != null) {
				curr = curr.getParent();
			}
			n++;
		}
		boolean first = true;
		while (n < names.length) {
			boolean found = false;
			ArrayList<CxxScope> tmp = new ArrayList<CxxScope> ();
			tmp.add(curr);
			tmp.addAll(curr.searchScopes);
			for (CxxScope xscope: tmp) {
				if (found) {
					break;
				}
				for (CxxScope scope: xscope.subscopes) {
					if (scope.getName().equals(names[n])) {
						curr = scope;
						n++;
						found = true;
						first = false;
						break;
					}					
				}
			}
			if (!found) {
				if (first && curr.getParent() != null) {
					curr = curr.getParent();
				}
				else {
				    curr = curr.getSubscope(names[n], n+1 == names.length ? type2 : CxxScopeType.namespace );
				    n++;
				    first = false;
				}
			}
		}
		return curr;
	}
	public boolean addSearchScope(CxxScope scope) {
		if (!searchScopes.contains(scope)) {
		   return searchScopes.add(scope);
		}
		return false;
	}
	
	public String getName() { return name; }
	public CxxScopeType getType() { return type; }
	public String getFullName() {
		String res = "";
		String pName =  parent == null ? "" : parent.getFullName();
		if (type == CxxScopeType.block) {
			Matcher rx = Pattern.compile("(.*\\.local_)([0-9]*)$").matcher(pName);
			if (rx.matches()) {
				int n = Integer.parseInt(rx.group(2));
				res = rx.group(1)+Integer.toString(n+1);
			}
			else {
				res = pName + ".local_0";
			}
		}
		else {
			res = pName + (pName.equals("::") ? "" : "::") + name;
		}
		return  res;
	}
	@Override
    public String toString() {
    	return "CxxScope [" + getFullName() + "]";
    }
	public CxxScope getParent() {
		return parent;
	}
	public void addSymbol(CxxSymbol cxxSymbol) {
		symbols.add(cxxSymbol);
	}
}