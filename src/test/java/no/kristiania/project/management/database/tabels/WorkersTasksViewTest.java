package no.kristiania.project.management.database.tabels;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WorkersTasksViewTest {

    @Test
    void shouldSetAllValues(){
        WorkersTasksView workersTasksView = new WorkersTasksView();
        workersTasksView.setName("FirstName");
        workersTasksView.setLastName("LastName");
        workersTasksView.setAddress("Address");
        workersTasksView.setDescription("Description");
        workersTasksView.setStatus("Status");

        assertThat(workersTasksView).hasNoNullFieldsOrProperties();
    }
}