// Vytvořit novou třídu DatabaseInitializer.java
package com.example.testresultapp.database;

import android.content.Context;

import com.example.testresultapp.database.entity.TestDefinitionEntity;
import com.example.testresultapp.repository.ETCSRepository;

public class DatabaseInitializer {
    public static void initializeDb(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);

        // Kontrola, zda již existují nějaké testové definice
        if (db.testDefinitionDao().getTestDefinitionsCount() == 0) {
            // Vytvoření základních testových definic
            TestDefinitionEntity test1 = new TestDefinitionEntity();
            test1.setName("Komunikace ETCS - ETCS OBU");
            test1.setDescription("Test komunikace palubní jednotky se systémem ETCS");

            TestDefinitionEntity test2 = new TestDefinitionEntity();
            test2.setName("Brzda - Nouzové brzdění");
            test2.setDescription("Test funkčnosti nouzového brzdění");

            TestDefinitionEntity test3 = new TestDefinitionEntity();
            test3.setName("Rychloměr - Snímač rychlosti");
            test3.setDescription("Test přesnosti měření rychlosti");

            // Vložení do databáze
            db.testDefinitionDao().insertSync(test1);
            db.testDefinitionDao().insertSync(test2);
            db.testDefinitionDao().insertSync(test3);
        }
    }
}