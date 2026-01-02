package com.techie.designPattern.creationalDesignPattern;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrototypeDesignPatternTest {

    @Test
    void textDocument_defensiveCopyAndClone_independence() {
        List<String> initialTags = new ArrayList<>();
        initialTags.add("draft");
        initialTags.add("2026");

        TextDocument original = new TextDocument("Report", "Initial content", "Alice", initialTags);
        // Modify external list after construction
        initialTags.add("external-modified");

        // original should not see the external modification (defensive copy)
        assertFalse(original.tags().contains("external-modified"), "Original should not reflect external list changes");

        // clone should be a different instance but equal in value
        TextDocument cloned = (TextDocument) original.clone();
        assertNotSame(original, cloned, "clone must not be the same reference as original");
        assertEquals(original, cloned, "clone should be equal to original by value");

        // the internal tag list references should not be the same object
        assertNotSame(original.tags(), cloned.tags(), "tag lists should be independent copies");
    }

    @Test
    void textDocument_withContent_createsModifiedCopy() {
        List<String> tags = new ArrayList<>();
        tags.add("t1");

        TextDocument original = new TextDocument("Title", "v1", "Bob", tags);
        TextDocument updated = original.withContent("v2");

        assertNotSame(original, updated, "withContent should return a new instance");
        assertEquals("v2", updated.content(), "updated content must match");
        assertEquals(original.title(), updated.title(), "title should be preserved");
        assertEquals(original.author(), updated.author(), "author should be preserved");
        assertEquals(original.tags(), updated.tags(), "tags should be preserved by value");
    }

    @Test
    void spreadsheet_deepCopy_and_clone_independence() {
        List<List<String>> table = new ArrayList<>();
        table.add(new ArrayList<>(List.of("A1", "B1", "C1")));
        table.add(new ArrayList<>(List.of("A2", "B2", "C2")));

        SpreadsheetDocument sheet = new SpreadsheetDocument("Sheet1", table, 2, 3);

        // modify external source after construction
        table.get(0).set(0, "A1-modified");

        // sheet should not reflect external changes (deep copy in constructor)
        assertFalse(sheet.getContent().contains("A1-modified"), "sheet should not reflect external table modifications");

        // clone and then mutate clone's internal data; original should not change
        SpreadsheetDocument clone = (SpreadsheetDocument) sheet.clone();
        assertNotSame(sheet, clone, "clone should be different reference");
        assertEquals(sheet.getContent(), clone.getContent(), "clone should have same content initially by value");

        // mutate clone's internal data
        clone.data().get(0).set(0, "CLONE-A1");
        // after mutation, clone content changes
        assertTrue(clone.getContent().contains("CLONE-A1"), "clone should reflect its own modifications");
        // original should remain unchanged
        assertFalse(sheet.getContent().contains("CLONE-A1"), "original should not reflect clone modifications");
    }
}

