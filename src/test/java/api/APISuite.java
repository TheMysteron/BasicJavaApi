package api;

import api.common.utils.DateUtilsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import api.presentation.controller.ControllerTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        ControllerTest.class,
        DateUtilsTest.class
})
public class APISuite {
}