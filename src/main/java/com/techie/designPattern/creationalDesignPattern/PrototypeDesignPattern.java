package com.techie.designPattern.creationalDesignPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * USE CASE: Document Template System
 * PROBLEM: Creating new objects by copying existing ones
 * JDK 17 FEATURE: Records + clone pattern
 */

sealed interface Document permits TextDocument, SpreadsheetDocument {
    Document clone();
    String getContent();
}

record TextDocument(
        String title,
        String content,
        String author,
        List<String> tags
) implements Document {
    // Defensive copy constructor
    public TextDocument {
        tags = List.copyOf(tags);
    }

    @Override
    public Document clone() {
        return new TextDocument(title, content, author, new ArrayList<>(tags));
    }

    @Override
    public String getContent() {
        return content;
    }

    // Create modified copy
    public TextDocument withContent(String newContent) {
        return new TextDocument(title, newContent, author, tags);
    }
}

record SpreadsheetDocument(
        String name,
        List<List<String>> data,
        int rows,
        int columns
) implements Document {
    public SpreadsheetDocument {
        // Deep copy for mutable data
        data = data.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    @Override
    public Document clone() {
        return new SpreadsheetDocument(name, data, rows, columns);
    }

    @Override
    public String getContent() {
        return data.toString();
    }
}

public class PrototypeDesignPattern {
    public static void main(String[] args) {
        // --- TextDocument demo ---
        List<String> initialTags = new ArrayList<>();
        initialTags.add("draft");
        initialTags.add("2026");

        TextDocument originalText = new TextDocument("Report", "Initial content", "Alice", initialTags);
        System.out.println("Original Text: " + originalText);

        // Clone (creates new TextDocument with a copy of tags)
        TextDocument clonedText = (TextDocument) originalText.clone();
        System.out.println("Cloned Text:   " + clonedText);

        // Modify the source list after constructing original to verify defensive copy
        initialTags.add("modified-external-list");
        System.out.println("External initialTags modified: " + initialTags);
        System.out.println("Original Text tags after external modification: " + originalText.tags());
        System.out.println("Cloned Text tags after external modification:   " + clonedText.tags());

        // Create a changed copy using withContent
        TextDocument updated = originalText.withContent("Updated content");
        System.out.println("Updated Text (withContent): " + updated);

        System.out.println("originalText == clonedText: " + (originalText == clonedText));
        System.out.println("originalText.equals(clonedText): " + originalText.equals(clonedText));

        // --- SpreadsheetDocument demo ---
        List<List<String>> table = new ArrayList<>();
        table.add(new ArrayList<>(List.of("A1", "B1", "C1")));
        table.add(new ArrayList<>(List.of("A2", "B2", "C2")));

        SpreadsheetDocument sheet = new SpreadsheetDocument("Sheet1", table, 2, 3);
        System.out.println("Original Sheet content: " + sheet.getContent());

        // Clone the spreadsheet
        SpreadsheetDocument sheetClone = (SpreadsheetDocument) sheet.clone();
        System.out.println("Cloned Sheet content:   " + sheetClone.getContent());

        // Mutate original table after construction to verify deep copy at construction time
        table.get(0).set(0, "A1-modified");
        System.out.println("External table after modification: " + table);
        System.out.println("Original Sheet content after external change: " + sheet.getContent());
        System.out.println("Cloned Sheet content after external change:   " + sheetClone.getContent());

        // Mutate the original sheet's internal data list (if we could) to ensure clone is independent
        // Note: the record stored a deep copy, so modifying the original external source does not affect records.

        System.out.println("Demo complete.");
    }
}
