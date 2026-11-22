package co.jinear.core.task;

import co.jinear.core.model.request.task.TaskFilterRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TaskFilterRequestTaskTagsValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void validTagsPass() {
        Set<ConstraintViolation<TaskFilterRequest>> violations = validate(List.of("jin-585", "ds-ticaret-12", "JIN-1"));

        assertThat(violations).isEmpty();
    }

    @Test
    void noTagsPass() {
        Set<ConstraintViolation<TaskFilterRequest>> violations = validate(null);

        assertThat(violations).isEmpty();
    }

    @Test
    void aTagWithoutANumberFails() {
        Set<ConstraintViolation<TaskFilterRequest>> violations = validate(List.of("jin"));

        assertThat(violations).isNotEmpty();
    }

    @Test
    void aTagWithANumberTooLongForAnIntegerFails() {
        Set<ConstraintViolation<TaskFilterRequest>> violations = validate(List.of("jin-12345678901"));

        assertThat(violations).isNotEmpty();
    }

    @Test
    void aNullTagFails() {
        List<String> taskTags = new ArrayList<>();
        taskTags.add(null);

        Set<ConstraintViolation<TaskFilterRequest>> violations = validate(taskTags);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void moreThan250TagsFail() {
        List<String> taskTags = Collections.nCopies(251, "jin-1");

        Set<ConstraintViolation<TaskFilterRequest>> violations = validate(taskTags);

        assertThat(violations).isNotEmpty();
    }

    private Set<ConstraintViolation<TaskFilterRequest>> validate(List<String> taskTags) {
        TaskFilterRequest request = new TaskFilterRequest();
        request.setWorkspaceId("workspace-1");
        request.setTaskTags(taskTags);
        return validator.validate(request);
    }
}
