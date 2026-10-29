package com.iitu.zholdas_zhannura_student_api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Deliberate assessment error on the student's isolated CI demonstration branch. */
class ControlledFailureTests {
    @Test void controlledFailureForZholdasZhannura() {
        assertTrue(false, "Intentional midterm test failure for Zhannura Zholdas; fix on this branch only.");
    }
}
