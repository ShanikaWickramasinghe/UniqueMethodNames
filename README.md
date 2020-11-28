# UniqueMethodNames

Files that were modified to implement unique method names are as below can be seen

1. Modified /product-apim/product-scenarios/1-manage-public-partner-private-apis/1.1-expose-service-as-rest-api/1.1.1-create-rest-api-from-scratch/src/test/java/org/wso2/am/scenario/tests/rest/api/creation/RESTApiCreationTestCase.java

```
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import java.lang.reflect.Method;

String testInstanceName = "";

public class RESTApiCreationTestCase extends ScenarioTestBase implements ITest{

@BeforeMethod(alwaysRun = true)
    public void changeTestCaseName(Method method) {
        testInstanceName = method.getAnnotation(Test.class).description() + "_" + method.getName() +"_"+ adminUsername;
    }

    public String getTestName() {
        return testInstanceName;
    }
    
  }
```

2. Modified /product-apim/product-scenarios/1-manage-public-partner-private-apis/1.1-expose-service-as-rest-api/1.1.1-create-rest-api-from-scratch/src/test/resources/testng.xml

```
<listeners>
        <listener class-name="org.wso2.am.scenario.test.common.CustomReport" />
</listeners>

```
    
3. Added a new file as CustomReport.java under /product-apim/product-scenarios/scenarios-common/src/main/java/org/wso2/am/scenario/test/common/CustomReport.java


```
package org.wso2.am.scenario.test.common;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;
import org.testng.reporters.HtmlHelper;


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
public static void generateTable(){
//sb.append(" (").append(testName).append(")");

i$ = output.iterator();

}

private static void ppp(String s) {
        System.out.println("[CustomReport] " + s);
    }
    
   
```
