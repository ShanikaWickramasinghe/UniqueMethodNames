package org.wso2.am.scenario.test.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.internal.Utils;

//newly added
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;
import org.testng.reporters.HtmlHelper;


//newly added
public class CustomReport  extends TestListenerAdapter implements IReporter {
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
                               String outputDirectory) {

    }

    private static final Comparator<ITestResult> NAME_COMPARATOR = new CustomReport.NameComparator();
    private static final Comparator<ITestResult> CONFIGURATION_COMPARATOR = new CustomReport.ConfigurationComparator();
    private ITestContext m_testContext = null;
    private static String HEAD = "\n<style type=\"text/css\">\n.log { display: none;} \n.stack-trace { display: none;} \n</style>\n<script type=\"text/javascript\">\n<!--\nfunction flip(e) {\n  current = e.style.display;\n  if (current == 'block') {\n    e.style.display = 'none';\n    return 0;\n  }\n  else {\n    e.style.display = 'block';\n    return 1;\n  }\n}\n\nfunction toggleBox(szDivId, elem, msg1, msg2)\n{\n  var res = -1;  if (document.getElementById) {\n    res = flip(document.getElementById(szDivId));\n  }\n  else if (document.all) {\n    // this is the way old msie versions work\n    res = flip(document.all[szDivId]);\n  }\n  if(elem) {\n    if(res == 0) elem.innerHTML = msg1; else elem.innerHTML = msg2;\n  }\n\n}\n\nfunction toggleAllBoxes() {\n  if (document.getElementsByTagName) {\n    d = document.getElementsByTagName('div');\n    for (i = 0; i < d.length; i++) {\n      if (d[i].className == 'log') {\n        flip(d[i]);\n      }\n    }\n  }\n}\n\n// -->\n</script>\n\n";

    public CustomReport() {
    }

    public void onStart(ITestContext context) {
        this.m_testContext = context;
    }

    public void onFinish(ITestContext context) {
        generateLog(this.m_testContext, (String)null, this.m_testContext.getOutputDirectory(), this.getConfigurationFailures(), this.getConfigurationSkips(), this.getPassedTests(), this.getFailedTests(), this.getSkippedTests(), this.getFailedButWithinSuccessPercentageTests());
    }

    private static String getOutputFile(ITestContext context) {
        return context.getName() + ".html";
    }

    public static void generateTable(StringBuffer sb, String title, Collection<ITestResult> tests, String cssClass, Comparator<ITestResult> comparator) {
        sb.append("<table width='100%' border='1' class='invocation-").append(cssClass).append("'>\n").append("<tr><td colspan='4' align='center'><b>").append(title).append("</b></td></tr>\n").append("<tr>").append("<td><b>Test method</b></td>\n").append("<td width=\"30%\"><b>Exception</b></td>\n").append("<td width=\"10%\"><b>Time (seconds)</b></td>\n").append("<td><b>Instance</b></td>\n").append("</tr>\n");
        if (tests instanceof List) {
            Collections.sort((List)tests, comparator);
        }

        String id = "";
        Throwable tw = null;
        Iterator i$ = tests.iterator();

        while(i$.hasNext()) {
            ITestResult tr = (ITestResult)i$.next();
            sb.append("<tr>\n");
            ITestNGMethod method = tr.getMethod();
            sb.append("<td title='").append(tr.getTestClass().getName()).append(".").append(tr.getName()).append("()'>").append("<b>").append(tr.getName()).append("</b>");
            String testClass = tr.getTestClass().getName();
            if (testClass != null) {
                sb.append("<br>").append("Test class: " + testClass);
                String testName = tr.getTestName();
                if (testName != null) {
                    //newly changed
//                    sb.append(" (").append(testName).append(")");
                }
            }

            if (!Utils.isStringEmpty(method.getDescription())) {
                sb.append("<br>").append("Test method: ").append(method.getDescription());
            }

            Object[] parameters = tr.getParameters();
            if (parameters != null && parameters.length > 0) {
                sb.append("<br>Parameters: ");

                for(int j = 0; j < parameters.length; ++j) {
                    if (j > 0) {
                        sb.append(", ");
                    }

                    sb.append(parameters[j] == null ? "null" : parameters[j].toString());
                }
            }

            List<String> output = Reporter.getOutput(tr);
            String fullStackTrace;
            if (null != output && output.size() > 0) {
                sb.append("<br/>");
                fullStackTrace = "Output-" + tr.hashCode();
                sb.append("\n<a href=\"#").append(fullStackTrace).append("\"").append(" onClick='toggleBox(\"").append(fullStackTrace).append("\", this, \"Show output\", \"Hide output\");'>").append("Show output</a>\n").append("\n<a href=\"#").append(fullStackTrace).append("\"").append(" onClick=\"toggleAllBoxes();\">Show all outputs</a>\n");
                sb.append("<div class='log' id=\"").append(fullStackTrace).append("\">\n");
                //newly changed
                i$ = output.iterator();

                while(i$.hasNext()) {
                    String s = (String)i$.next();
                    sb.append(s).append("<br/>\n");
                }

                sb.append("</div>\n");
            }

            sb.append("</td>\n");
            tw = tr.getThrowable();
            String stackTrace = "";
            fullStackTrace = "";
            id = "stack-trace" + tr.hashCode();
            sb.append("<td>");
            if (null != tw) {
                String[] stackTraces = Utils.stackTrace(tw, true);
                fullStackTrace = stackTraces[1];
                stackTrace = "<div><pre>" + stackTraces[0] + "</pre></div>";
                sb.append(stackTrace);
                sb.append("<a href='#' onClick='toggleBox(\"").append(id).append("\", this, \"Click to show all stack frames\", \"Click to hide stack frames\")'>").append("Click to show all stack frames").append("</a>\n").append("<div class='stack-trace' id='" + id + "'>").append("<pre>" + fullStackTrace + "</pre>").append("</div>");
            }

            sb.append("</td>\n");
            long time = (tr.getEndMillis() - tr.getStartMillis()) / 1000L;
            String strTime = Long.toString(time);
            sb.append("<td>").append(strTime).append("</td>\n");
            Object instance = tr.getInstance();
            sb.append("<td>").append(instance).append("</td>");
            sb.append("</tr>\n");
        }

        sb.append("</table><p>\n");
    }

    private static String arrayToString(String[] array) {
        StringBuffer result = new StringBuffer("");
        String[] arr$ = array;
        int len$ = array.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String element = arr$[i$];
            result.append(element).append(" ");
        }

        return result.toString();
    }

    public static void generateLog(ITestContext testContext, String host, String outputDirectory, Collection<ITestResult> failedConfs, Collection<ITestResult> skippedConfs, Collection<ITestResult> passedTests, Collection<ITestResult> failedTests, Collection<ITestResult> skippedTests, Collection<ITestResult> percentageTests) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>\n<head>\n").append("<title>TestNG:  ").append(testContext.getName()).append("</title>\n").append(HtmlHelper.getCssString()).append(HEAD).append("</head>\n").append("<body>\n");
        Date startDate = testContext.getStartDate();
        Date endDate = testContext.getEndDate();
        long duration = (endDate.getTime() - startDate.getTime()) / 1000L;
        int passed = testContext.getPassedTests().size() + testContext.getFailedButWithinSuccessPercentageTests().size();
        int failed = testContext.getFailedTests().size();
        int skipped = testContext.getSkippedTests().size();
        String hostLine = Utils.isStringEmpty(host) ? "" : "<tr><td>Remote host:</td><td>" + host + "</td>\n</tr>";
        sb.append("<h2 align='center'>").append(testContext.getName()).append("</h2>").append("<table border='1' align=\"center\">\n").append("<tr>\n").append("<td>Tests passed/Failed/Skipped:</td><td>").append(passed).append("/").append(failed).append("/").append(skipped).append("</td>\n").append("</tr><tr>\n").append("<td>Started on:</td><td>").append(testContext.getStartDate().toString()).append("</td>\n").append("</tr>\n").append(hostLine).append("<tr><td>Total time:</td><td>").append(duration).append(" seconds (").append(endDate.getTime() - startDate.getTime()).append(" ms)</td>\n").append("</tr><tr>\n").append("<td>Included groups:</td><td>").append(arrayToString(testContext.getIncludedGroups())).append("</td>\n").append("</tr><tr>\n").append("<td>Excluded groups:</td><td>").append(arrayToString(testContext.getExcludedGroups())).append("</td>\n").append("</tr>\n").append("</table><p/>\n");
        sb.append("<small><i>(Hover the method name to see the test class name)</i></small><p/>\n");
        if (failedConfs.size() > 0) {
            generateTable(sb, "FAILED CONFIGURATIONS", failedConfs, "failed", CONFIGURATION_COMPARATOR);
        }

        if (skippedConfs.size() > 0) {
            generateTable(sb, "SKIPPED CONFIGURATIONS", skippedConfs, "skipped", CONFIGURATION_COMPARATOR);
        }

        if (failedTests.size() > 0) {
            generateTable(sb, "FAILED TESTS", failedTests, "failed", NAME_COMPARATOR);
        }

        if (percentageTests.size() > 0) {
            generateTable(sb, "FAILED TESTS BUT WITHIN SUCCESS PERCENTAGE", percentageTests, "percent", NAME_COMPARATOR);
        }

        if (passedTests.size() > 0) {
            generateTable(sb, "PASSED TESTS", passedTests, "passed", NAME_COMPARATOR);
        }

        if (skippedTests.size() > 0) {
            generateTable(sb, "SKIPPED TESTS", skippedTests, "skipped", NAME_COMPARATOR);
        }

        sb.append("</body>\n</html>");
        Utils.writeFile(outputDirectory, getOutputFile(testContext), sb.toString());
    }
    //newly changed
    private static void ppp(String s) {
        System.out.println("[CustomReport] " + s);
    }

    private static class ConfigurationComparator implements Comparator<ITestResult>, Serializable {
        private static final long serialVersionUID = 5558550850685483455L;

        private ConfigurationComparator() {
        }

        public int compare(ITestResult o1, ITestResult o2) {
            ITestNGMethod tm1 = o1.getMethod();
            ITestNGMethod tm2 = o2.getMethod();
            return annotationValue(tm2) - annotationValue(tm1);
        }

        private static int annotationValue(ITestNGMethod method) {
            if (method.isBeforeSuiteConfiguration()) {
                return 10;
            } else if (method.isBeforeTestConfiguration()) {
                return 9;
            } else if (method.isBeforeClassConfiguration()) {
                return 8;
            } else if (method.isBeforeGroupsConfiguration()) {
                return 7;
            } else if (method.isBeforeMethodConfiguration()) {
                return 6;
            } else if (method.isAfterMethodConfiguration()) {
                return 5;
            } else if (method.isAfterGroupsConfiguration()) {
                return 4;
            } else if (method.isAfterClassConfiguration()) {
                return 3;
            } else if (method.isAfterTestConfiguration()) {
                return 2;
            } else {
                return method.isAfterSuiteConfiguration() ? 1 : 0;
            }
        }
    }

    private static class NameComparator implements Comparator<ITestResult>, Serializable {
        private static final long serialVersionUID = 381775815838366907L;

        private NameComparator() {
        }

        public int compare(ITestResult o1, ITestResult o2) {
            String c1 = o1.getMethod().getMethodName();
            String c2 = o2.getMethod().getMethodName();
            return c1.compareTo(c2);
        }
    }
}