package com.stream.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateDifferentTest {
    @BeforeEach
    void setUp() throws Exception {
        // calculator = new Calculator();
    }

    @Test
    @DisplayName("Simple multiplication should work")
    void testMultiply() {
        //assertEquals(20, calculator.multiply(4,5), "Regular multiplication should work");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenSecondsLater = now.plusSeconds(10);

        long diff = now.until(tenSecondsLater, ChronoUnit.SECONDS);
        System.out.println(" diff " + diff);
        assertEquals(10, diff);
    }
}
