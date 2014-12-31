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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.sonar.api.CoreProperties;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.Settings;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.resources.Resource;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.utils.WildcardPattern;

import org.sonar.plugins.cxx.utils.DirectoryScanner;


public class TestUtils {
  public static Issuable mockIssuable() {
    Issue issue = mock(Issue.class);
    Issuable.IssueBuilder issueBuilder = mock(Issuable.IssueBuilder.class);
    when(issueBuilder.build()).thenReturn(issue);
    when(issueBuilder.ruleKey((RuleKey)anyObject())).thenReturn(issueBuilder);
    when(issueBuilder.line((Integer)anyObject())).thenReturn(issueBuilder);
    when(issueBuilder.message((String)anyObject())).thenReturn(issueBuilder);
    Issuable issuable = mock(Issuable.class);
    when(issuable.newIssueBuilder()).thenReturn(issueBuilder);
    return issuable;
  }

  public static ResourcePerspectives mockPerspectives(Issuable issuable) {
    ResourcePerspectives perspectives = mock(ResourcePerspectives.class);
    when(perspectives.as((Class) anyObject(), (Resource) anyObject())).thenReturn(issuable);
    return perspectives;
  }

  public static File loadResource(String resourceName) {
    URL resource = TestUtils.class.getResource(resourceName);
    File resourceAsFile = null;
    try {
      resourceAsFile = new File(resource.toURI());
    } catch (URISyntaxException e) {
      System.out.println("Cannot load resource: " + resourceName);
    }

    return resourceAsFile;
  }

  /**
   * @return  default mock project
   */
  public static Project mockProject() {
    List<File> empty = new ArrayList<File>();
    return mockProject(loadResource("/org/sonar/plugins/cxx/reports-project"), empty, empty);
  }

  /**
   * Mock project
   * @param baseDir project base dir
   * @param sourceDirs project source files
   * @param testDirs project test files
   * @return  mocked project
   */
  public static Project mockProject(File baseDir, List<File> sourceDirs, List<File> testDirs) {
    List<File> mainSourceFiles = scanForSourceFiles(sourceDirs);
    List<File> testSourceFiles = scanForSourceFiles(testDirs);

    List<InputFile> mainFiles = fromSourceFiles(mainSourceFiles);
    List<InputFile> testFiles = fromSourceFiles(testSourceFiles);

    ProjectFileSystem fileSystem = mock(ProjectFileSystem.class);
    when(fileSystem.getBasedir()).thenReturn(baseDir);
    when(fileSystem.getSourceCharset()).thenReturn(Charset.defaultCharset());
    when(fileSystem.getSourceFiles(mockCxxLanguage())).thenReturn(mainSourceFiles);
    when(fileSystem.getTestFiles(mockCxxLanguage())).thenReturn(testSourceFiles);
    when(fileSystem.mainFiles(CxxLanguage.KEY)).thenReturn(mainFiles);
    when(fileSystem.testFiles(CxxLanguage.KEY)).thenReturn(testFiles);
    when(fileSystem.getSourceDirs()).thenReturn(sourceDirs);
    when(fileSystem.getTestDirs()).thenReturn(testDirs);

    Project project = mock(Project.class);
    when(project.getFileSystem()).thenReturn(fileSystem);
    CxxLanguage lang = mockCxxLanguage();
    when(project.getLanguage()).thenReturn(lang);
    when(project.getLanguageKey()).thenReturn(lang.getKey());
    // only for testing, Configuration is deprecated
    Configuration configuration = mock(Configuration.class);
    when(configuration.getBoolean(CoreProperties.CORE_IMPORT_SOURCES_PROPERTY,
        CoreProperties.CORE_IMPORT_SOURCES_DEFAULT_VALUE)).thenReturn(true);
    when(project.getConfiguration()).thenReturn(configuration);

    return project;
  }

  public static ModuleFileSystem mockFileSystem(File baseDir,
                                                List<File> sourceDirs, List<File> testDirs) {
    ModuleFileSystem fs = mock(ModuleFileSystem.class);
    when(fs.sourceCharset()).thenReturn(Charset.forName("UTF-8"));
    when(fs.baseDir()).thenReturn(baseDir);
    when(fs.sourceDirs()).thenReturn(sourceDirs);
    when(fs.testDirs()).thenReturn(testDirs);

    List<File> mainSourceFiles = scanForSourceFiles(sourceDirs);
    List<File> testSourceFiles = scanForSourceFiles(testDirs);

    when(fs.files(any(FileQuery.class))).thenReturn(mainSourceFiles);

    return fs;
  }

  public static ModuleFileSystem mockFileSystem() {
    File baseDir = loadResource("/org/sonar/plugins/cxx/reports-project");
    List<File> empty = new ArrayList<File>();
    return mockFileSystem(baseDir, empty, empty);
  }

  private static List<InputFile> fromSourceFiles(List<File> sourceFiles) {
    List<InputFile> result = new ArrayList<InputFile>();
    for (File file : sourceFiles) {
      InputFile inputFile = mock(InputFile.class);
      when(inputFile.getFile()).thenReturn(new File(file, ""));
      result.add(inputFile);
    }
    return result;
  }

  public static CxxLanguage mockCxxLanguage() {
    return new CxxLanguage(new Settings());
  }

  private static List<File> scanForSourceFiles(List<File> sourceDirs) {
    List<File> result = new ArrayList<File>();
    String[] suffixes = mockCxxLanguage().getFileSuffixes();
    String[] includes = new String[suffixes.length];
    for (int i = 0; i < includes.length; ++i) {
      includes[i] = "**/*" + suffixes[i];
    }

    for (File baseDir : sourceDirs) {
      for (String include : includes){
        DirectoryScanner scanner = new DirectoryScanner(baseDir, WildcardPattern.create(include));
        result.addAll(scanner.getIncludedFiles());
      }
    }

    return result;
  }
}
