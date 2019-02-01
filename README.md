# UniqueMethodNames

Files that were modified to implement unique method names are as below.

1. Modified /product-apim/product-scenarios/1-manage-public-partner-private-apis/1.1-expose-service-as-rest-api/1.1.1-create-rest-api-from-scratch/src/test/java/org/wso2/am/scenario/tests/rest/api/creation/RESTApiCreationTestCase.java

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


2. Modified /product-apim/product-scenarios/1-manage-public-partner-private-apis/1.1-expose-service-as-rest-api/1.1.1-create-rest-api-from-scratch/src/test/resources/testng.xml

<listeners>
        <listener class-name="org.wso2.am.scenario.test.common.CustomReport" />
</listeners>
    
3. Added a new file as CustomReport.java under /product-apim/product-scenarios/scenarios-common/src/main/java/org/wso2/am/scenario/test/common/CustomReport.java
