// app/src/main/java/com/example/testresultapp/util/PredefinedTests.java
package com.example.testresultapp.util;

import java.util.Arrays;
import java.util.List;

/**
 * Třída obsahující předem definované testy ETCS
 */
public class PredefinedTests {

    // Statický seznam definovaných testů
    public static final List<TestDefinition> ETCS_TESTS = Arrays.asList(
            new TestDefinition("Komunikace DMI-EVC", "Test komunikace mezi DMI a EVC jednotkou"),
            new TestDefinition("Funkce nouzového brzdění", "Test funkčnosti nouzového brzdění při aktivaci"),
            new TestDefinition("Test odometru", "Kontrola přesnosti měření vzdálenosti"),
            new TestDefinition("Přenos balízy", "Test přenosu dat z traťové balízy do OBU"),
            new TestDefinition("Zobrazení na DMI", "Kontrola správného zobrazení informací na DMI"),
            new TestDefinition("Přechod do módu SR", "Test přechodu z Full Supervision do Staff Responsible"),
            new TestDefinition("Kontrola rychlosti", "Test správného sledování rychlosti a brzdicích křivek"),
            new TestDefinition("Národní hodnoty", "Test nahrání a použití národních hodnot"),
            new TestDefinition("Hlasové volání", "Test komunikace s dispečerem pomocí GSM-R"),
            new TestDefinition("Reakce na překročení rychlosti", "Test reakce systému při překročení povolené rychlosti")
    );

    public static class TestDefinition {
        private String name;
        private String description;

        public TestDefinition(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
    }
}