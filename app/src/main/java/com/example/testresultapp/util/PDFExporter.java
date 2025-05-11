// app/src/main/java/com/example/testresultapp/util/PDFExporter.java
package com.example.testresultapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.testresultapp.database.entity.StationEntity;
import com.example.testresultapp.database.entity.TestDefinitionEntity;
import com.example.testresultapp.database.entity.TestResultEntity;
import com.example.testresultapp.database.entity.TrainJourneyEntity;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PDFExporter {
    private final Context context;

    public PDFExporter(Context context) {
        this.context = context;
    }

    public File exportToPDF(
            TrainJourneyEntity journey,
            List<StationEntity> stations,
            Map<Integer, List<TestResultEntity>> testResultsMap,
            Map<Integer, TestDefinitionEntity> testDefinitionsMap
    ) throws IOException {
        // Vytvoření souboru v externím úložišti
        File pdfFolder = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
        }

        File pdfFile = new File(pdfFolder, "ETCS_Test_" + journey.getTrainNumber() + "_" + journey.getDate().replace(".", "_") + ".pdf");

        // Vytvoření PDF dokumentu
        PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Hlavička dokumentu
        document.add(new Paragraph("ETCS Test Report")
                .setFontSize(20)
                .setBold());

        document.add(new Paragraph("Train: " + journey.getTrainNumber()));
        document.add(new Paragraph("Date: " + journey.getDate()));
        document.add(new Paragraph("\n"));

        // Procházení stanic a testů
        for (StationEntity station : stations) {
            document.add(new Paragraph("Station: " + station.getName())
                    .setFontSize(16)
                    .setBold());
            document.add(new Paragraph("Planned arrival: " + station.getPlannedArrivalTime()));

            List<TestResultEntity> stationTests = testResultsMap.get(station.getId());

            if (stationTests != null && !stationTests.isEmpty()) {
                // Tabulka testů
                Table table = new Table(UnitValue.createPercentArray(new float[]{30, 20, 30, 20}));
                table.setWidth(UnitValue.createPercentValue(100));

                // Hlavička tabulky
                table.addHeaderCell("Test");
                table.addHeaderCell("Result");
                table.addHeaderCell("Rescheduled time");
                table.addHeaderCell("Photo");

                // Řádky tabulky
                for (TestResultEntity testResult : stationTests) {
                    TestDefinitionEntity testDef = testDefinitionsMap.get(testResult.getTestDefinitionId());

                    if (testDef != null) {
                        table.addCell(new Cell().add(new Paragraph(testDef.getName())));
                        table.addCell(new Cell().add(new Paragraph(testResult.getResult())));
                        table.addCell(new Cell().add(new Paragraph(
                                testResult.getRescheduledTime() != null ? testResult.getRescheduledTime() : "-")));

                        // Pokud existuje fotografie, přidáme ji
                        Cell photoCell = new Cell();
                        if (testResult.getPhotoPath() != null) {
                            try {
                                File photoFile = new File(testResult.getPhotoPath());
                                if (photoFile.exists()) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                                    if (bitmap != null) {
                                        Image image = new Image(ImageDataFactory.create(photoFile.getAbsolutePath()));
                                        image.setWidth(UnitValue.createPointValue(100));
                                        photoCell.add(image);
                                    } else {
                                        photoCell.add(new Paragraph("No photo"));
                                    }
                                } else {
                                    photoCell.add(new Paragraph("No photo"));
                                }
                            } catch (Exception e) {
                                photoCell.add(new Paragraph("Error loading photo"));
                            }
                        } else {
                            photoCell.add(new Paragraph("No photo"));
                        }
                        table.addCell(photoCell);
                    }
                }

                document.add(table);
            } else {
                document.add(new Paragraph("No tests recorded for this station."));
            }

            document.add(new Paragraph("\n"));
        }

        document.close();
        return pdfFile;
    }
}