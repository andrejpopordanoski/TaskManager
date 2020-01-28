package com.example.taskmanager;
import com.example.taskmanager.Models.Collaborator;
import com.example.taskmanager.Models.Project;
import com.example.taskmanager.Models.Task;
import com.example.taskmanager.Models.User;
import com.example.taskmanager.Models.UserProject;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ProjectTest {
    //Test getAllCollabsFromType
    @RunWith(Parameterized.class)
    public static class ParametrizedTestsGetCollabs {
        private Project project;
        String collabType;
        List<Collaborator> expectedResult;
        public ParametrizedTestsGetCollabs(String collabType, List<Collaborator> expectedResult) {
            this.collabType = collabType;
            this.expectedResult = expectedResult;
        }
        private List<Collaborator> collabs = new ArrayList<>(Arrays.asList(
                new Collaborator("1546", "Director", "c1@gmail.com"),
                new Collaborator("423432", "Project Manager", "c2@gmail.com"),
                new Collaborator("5234", "Project Manager", "c3@gmail.com"),
                new Collaborator("464542", "Tester", "c4@gmail.com"),
                new Collaborator("6546342", "Director", "c5@gmail.com"),
                new Collaborator("86756453", "Tester", "c6@gmail.com"),
                new Collaborator("234", "Director", "c7@gmail.com"),
                new Collaborator("53464", "Project Manager", "c8@gmail.com")
        ));
        @Parameters
        public static Collection<Object[]> collabTypes() {
            return Arrays.asList(new Object[][]{
                    {"Director", new ArrayList<>(Arrays.asList(
                            new Collaborator("1546", "Director", "c1@gmail.com"),
                            new Collaborator("6546342", "Director", "c5@gmail.com"),
                            new Collaborator("234", "Director", "c7@gmail.com")
                    ))},
                    {"Tester", new ArrayList<Collaborator>(Arrays.asList(
                            new Collaborator("464542", "Tester", "c4@gmail.com"),
                            new Collaborator("86756453", "Tester", "c6@gmail.com")
                    ))},
                    {"", new ArrayList<Collaborator>()},
                    {null, new ArrayList<Collaborator>()},
            });
        }
        @Before
        public void setup() {
            project = new Project("Proekt 1", "12345");
            for (Collaborator c :
                    this.collabs) {
                project.addCollaborator(c);
            }
        }
        @Test
        public void TestGetCollabsByType() {
            assertEquals(this.expectedResult,
                    project.getAllCollabsFromType(this.collabType));
        }
    }


    //Test deleteCollaboratorWithEmail
    @RunWith(Parameterized.class)
    public static class ParametrizedTestsDeleteCollabs {
        private Project project;
        String email;
        Boolean expectedResult;
        public ParametrizedTestsDeleteCollabs(String email, boolean expectedResult) {
            this.email = email;
            this.expectedResult = expectedResult;
        }
        private List<Collaborator> collabs = new ArrayList<>(Arrays.asList(
                new Collaborator("1546", "Director", "c1@gmail.com"),
                new Collaborator("423432", "Project Manager", "c2@gmail.com"),
                new Collaborator("5234", "Project Manager", "c3@gmail.com"),
                new Collaborator("464542", "Tester", "c4@gmail.com"),
                new Collaborator("6546342", "Director", "c5@gmail.com"),
                new Collaborator("86756453", "Tester", "c6@gmail.com"),
                new Collaborator("234", "Director", "c7@gmail.com"),
                new Collaborator("53464", "Project Manager", "c8@gmail.com")
        ));
        @Parameters
        public static Collection<Object[]> collabTypes() {
            return Arrays.asList(new Object[][]{
                    {"c2@gmail.com", true},
                    {"jas@gmail.com", false},
                    {"sdadasdsa", false},
                    {"", false},
                    {null, false},
            });
        }
        @Before
        public void setup() {
            project = new Project("Proekt 1", "12345");
            for (Collaborator c :
                    this.collabs) {
                project.addCollaborator(c);
            }
        }
        @Test
        public void TestDeleteCollaboratorWithEmail() {
            assertEquals(this.expectedResult,
                    project.deleteCollaboratorWithEmail(this.email));
        }
    }


    //Test getAllTasksFromState
    @RunWith(Parameterized.class)
    public static class ParametrizedTestsGetAllTasksFromState {
        private Project project;
        String state;
        ArrayList<Task> expectedResult;
        public ParametrizedTestsGetAllTasksFromState(String state, ArrayList<Task> expectedResult) {
            this.state = state;
            this.expectedResult = expectedResult;
        }
        private List<Task> tasks = new ArrayList<>(Arrays.asList(
                new Task("nameee", "", "High", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c2@gmail.com"), "TODO", "12344"),
                new Task("nameee two", "", "Low", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c2@gmail.com"), "DONE", "12344"),
                new Task("nameee three", "", "Medium", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c1@gmail.com"), "TEST", "12344"),
                new Task("nameee four", "", "Medium", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c1@gmail.com"), "DONE", "12344"),
                new Task("nameee five", "", "Low", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c1@gmail.com"), "TODO", "12344"),
                new Task("nameee six", "", "High", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c2@gmail.com"), "TODO", "12344"),
                new Task("nameee seven", "", "Medium", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c2@gmail.com"), "TEST", "12344"),
                new Task("nameee eight", "", "Medium", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c1@gmail.com"), "TEST", "12344")
        ));
        @Parameters
        public static Collection<Object[]> taskStates() {
            return Arrays.asList(new Object[][]{
                    {"TODO", new ArrayList<>(Arrays.asList(
                            new Task("nameee", "", "High", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c2@gmail.com"), "TODO", "12344"),
                            new Task("nameee five", "", "Low", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c1@gmail.com"), "TODO", "12344"),
                            new Task("nameee six", "", "High", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c2@gmail.com"), "TODO", "12344")
                    ))},
                    {"TEST", new ArrayList<>(Arrays.asList(
                            new Task("nameee three", "", "Medium", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c1@gmail.com"), "TEST", "12344"),
                            new Task("nameee seven", "", "Medium", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c2@gmail.com"), "TEST", "12344"),
                            new Task("nameee eight", "", "Medium", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c1@gmail.com"), "TEST", "12344")
                    ))},
                    {"", new ArrayList<Collaborator>()},
                    {null, new ArrayList<Collaborator>()},
            });
        }
        @Before
        public void setup() {
            project = new Project("Proekt 1", "12345");
            for (Task t :
                    this.tasks) {
                project.addTask(t);
            }
        }
        @Test
        public void TestGetTasksByState() {
            assertEquals(this.expectedResult,
                    project.getAllTasksFromState(this.state));
        }
    }

    //Test getAllTasksFromStateAndCollaborator
    @RunWith(Parameterized.class)
    public static class ParametrizedTestsGetAllTasksFromStateCollab {
        private Project project;
        ArrayList<String> stateCollab;
        ArrayList<Task> expectedResult;
        public ParametrizedTestsGetAllTasksFromStateCollab(ArrayList<String> stateCollab, ArrayList<Task> expectedResult) {
            this.stateCollab = stateCollab;
            this.expectedResult = expectedResult;
        }
        private List<Task> tasks = new ArrayList<>(Arrays.asList(
                new Task("nameee", "", "High", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c2@gmail.com"), "TODO", "12345"),
                new Task("nameee two", "", "Low", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c2@gmail.com"), "DONE", "12345"),
                new Task("nameee three", "", "Medium", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c1@gmail.com"), "TEST", "12345"),
                new Task("nameee four", "", "Medium", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c1@gmail.com"), "DONE", "12345"),
                new Task("nameee five", "", "Low", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c1@gmail.com"), "TODO", "12345"),
                new Task("nameee six", "", "High", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c2@gmail.com"), "TODO", "12345"),
                new Task("nameee seven", "", "Medium", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c2@gmail.com"), "TEST", "12345"),
                new Task("nameee eight", "", "Medium", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c1@gmail.com"), "TEST", "12345")
        ));
        @Parameters
        public static Collection<Object[]> taskStatesCollabs() {
            return Arrays.asList(new Object[][]{
                    {new ArrayList<>(Arrays.asList("TODO", "c2@gmail.com")), new ArrayList<>(Arrays.asList(
                            new Task("nameee", "", "High", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c2@gmail.com"), "TODO", "12345"),
                            new Task("nameee six", "", "High", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c2@gmail.com"), "TODO", "12345")
                    ))},
                    {new ArrayList<>(Arrays.asList("TEST", "c1@gmail.com")), new ArrayList<>(Arrays.asList(
                            new Task("nameee three", "", "Medium", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c1@gmail.com"), "TEST", "12345"),
                            new Task("nameee eight", "", "Medium", Calendar.getInstance(), new Collaborator("423432", "Project Manager", "c1@gmail.com"), "TEST", "12345")
                    ))},
                    {new ArrayList<>(Arrays.asList("DONE", "")), new ArrayList<Collaborator>()},
                    {new ArrayList<>(Arrays.asList("TODO", null)), new ArrayList<Collaborator>()},
                    {new ArrayList<>(Arrays.asList("", null)), new ArrayList<Collaborator>()},
                    {new ArrayList<>(Arrays.asList("", "")), new ArrayList<Collaborator>()},
                    {new ArrayList<>(Arrays.asList(null, "")), new ArrayList<Collaborator>()},
                    {new ArrayList<>(Arrays.asList(null, null)), new ArrayList<Collaborator>()},
                    {new ArrayList<>(Arrays.asList(null, "c1@gmail.com")), new ArrayList<Collaborator>()},
                    {new ArrayList<>(Arrays.asList("", "c2@gmail.com")), new ArrayList<Collaborator>()},
                    {new ArrayList<>(Arrays.asList("INPROGRESS", "c2@gmail.com")), new ArrayList<Collaborator>()},
                    {new ArrayList<>(Arrays.asList("DONE", "jas@gmail.com")), new ArrayList<Collaborator>()}
            });
        }
        @Before
        public void setup() {
            project = new Project("Proekt 1", "12345");
            for (Task t :
                    this.tasks) {
                project.addTask(t);
            }
        }
        @Test
        public void TestGetTasksByState() {
            assertEquals(this.expectedResult,
                    project.getAllTaksksFromStateAndCollaborator(this.stateCollab.get(0), this.stateCollab.get(1)));
        }
    }

}