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

import org.junit.Rule;
import org.junit.Test;
import org.sonar.cxx.CxxAstScanner;
import org.sonar.squid.api.CheckMessage;
import org.sonar.squid.api.SourceFile;

import static org.fest.assertions.Assertions.assertThat;

import com.sonar.sslr.squid.checks.CheckMessagesVerifierRule;

public class UndocumentedApiCheckTest {

    @Rule
    public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

    @SuppressWarnings("unchecked")
    @Test
    public void detected() {
        SourceFile file = CxxAstScanner.scanSingleFile(new File(
                "src/test/resources/checks/UndocumentedApiCheck/no_doc.h"), new UndocumentedApiCheck());

        checkMessagesVerifier.verify(file.getCheckMessages()).next().atLine(6)
                .next().atLine(11).next().atLine(13).next().atLine(14).next()
                .atLine(17).next().atLine(19).next().atLine(21).next()
                .atLine(23).next().atLine(26).next().atLine(48).next()
                .atLine(52).next().atLine(53).next().atLine(56).next()
                .atLine(58).next().atLine(60).next().atLine(63).next()
                .atLine(65).next().atLine(68).next().atLine(74).next().atLine(77);

        for (CheckMessage msg : file.getCheckMessages()) {
            assertThat(msg.formatDefaultMessage()).isNotEmpty();
        }
    }
}
