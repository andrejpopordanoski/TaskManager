package com.example.taskmanager;
import com.example.taskmanager.Models.User;
import com.example.taskmanager.Models.UserProject;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class UserTest {

    @RunWith(Parameterized.class)
    public static class ParameerizedTests {

        private User user;
        String projectId;
        Boolean expectedResult;
        public ParameerizedTests(String projectId, boolean expectedResult) {
            this.projectId = projectId;
            this.expectedResult = expectedResult;

        }
        private List<UserProject> projectList = new ArrayList<>(Arrays.asList(
                new UserProject("1546", "Director"),
                new UserProject("423432", "Project Manager"),
                new UserProject("5234", "Project Manager"),
                new UserProject("464542", "Tester"),
                new UserProject("6546342", "Director"),
                new UserProject("86756453", "Tester"),
                new UserProject("234", "Director"),
                new UserProject("53464", "Project Manager")
        ));

        @Parameters
        public static Collection<Object[]> projectIDs() {
            return Arrays.asList(new Object[][]{
                    {"234", true},
                    {"323232", false},
                    {null, false},
                    {"", false},

            });
        }
        @Before
        public void setup() {

            user = new User();
            user.setProjectList(projectList);
        }

        @Test
        public void TestProjectRemoval() {



            assertEquals(this.expectedResult,
                    user.removeUserProjectWithId(this.projectId));
        }
    }




}
