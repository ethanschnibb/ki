package com.ki;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

public class IntegrationTest  {

    @Test
    public void testSimulatePlatform() {
        String fixturePath = Fixture.getPath("card_payments_mixed.csv");

        String expected = (
            "\"customer_id\",\"shares\"\n" +
            "\"123\",\"735\"\n" +
            "\"456\",\"3430\"\n"
        );

        String result = LocalRunner.simulatePlatform(fixturePath, "card", new BigDecimal("1.2"));

        assertEquals(expected, result);
    }
}
