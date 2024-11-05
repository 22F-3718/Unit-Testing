package testPackage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import textEditorDataAccessLayer.DocumentDAO;
import textEditorPresentationLayer.dummyImportPagePO;

class TextEditorImportPageTesting {

	private dummyImportPagePO dummyPage;

	@BeforeEach
	@DisplayName("Setting up test environment")
	void setUp() {
		dummyPage = new dummyImportPagePO();
	}

	@Test
	@DisplayName("Test Populate Table with Documents")
	void testPopulateTable() {
		ArrayList<DocumentDAO> documents = new ArrayList<>();
		documents.add(new DocumentDAO(1, "testFile1", Date.valueOf(LocalDate.of(2022, 1, 1)),
				Timestamp.from(LocalDateTime.of(2022, 2, 1, 0, 0).atZone(ZoneId.systemDefault()).toInstant()),
				"Content 1"));
		documents.add(new DocumentDAO(2, "sampleFile2", Date.valueOf(LocalDate.of(2022, 1, 2)),
				Timestamp.from(LocalDateTime.of(2022, 2, 2, 0, 0).atZone(ZoneId.systemDefault()).toInstant()),
				"Content 2"));

		dummyPage.populateTable(documents);

		assertEquals(2, dummyPage.mockDocuments.size(), "Documents not correctly populated");
	}

	@Test
	@DisplayName("Test Search with Matching File Name")
	void testOnSearchWithMatchingFileName() {
		ArrayList<DocumentDAO> documents = new ArrayList<>();
		documents.add(new DocumentDAO(1, "testFile1", Date.valueOf(LocalDate.of(2022, 1, 1)),
				Timestamp.from(LocalDateTime.of(2022, 2, 1, 0, 0).atZone(ZoneId.systemDefault()).toInstant()),
				"Content 1"));
		documents.add(new DocumentDAO(2, "sampleFile2", Date.valueOf(LocalDate.of(2022, 1, 2)),
				Timestamp.from(LocalDateTime.of(2022, 2, 2, 0, 0).atZone(ZoneId.systemDefault()).toInstant()),
				"Content 2"));

		dummyPage.populateTable(documents);
		dummyPage.onSearch();

		assertEquals(1, dummyPage.getSearchedDocuments().size(), "Search result incorrect");
		assertEquals("testFile1", dummyPage.getSearchedDocuments().get(0).getFileName(),
				"Search did not return correct document");
	}

	@Test
	@DisplayName("Test Search with No Matching File Name")
	void testOnSearchWithNoMatchingFileName() {
		ArrayList<DocumentDAO> documents = new ArrayList<>();
		documents.add(new DocumentDAO(1, "sampleFile1", Date.valueOf(LocalDate.of(2022, 1, 1)),
				Timestamp.from(LocalDateTime.of(2022, 2, 1, 0, 0).atZone(ZoneId.systemDefault()).toInstant()),
				"Content 1"));

		dummyPage.populateTable(documents);
		dummyPage.onSearch();

		assertTrue(dummyPage.getSearchedDocuments().isEmpty(), "Search should return no results");
	}

	@Test
	@DisplayName("Test Show Document Description")
	void testShowDocumentDescription() {
		DocumentDAO document = new DocumentDAO(1, "testFile1", Date.valueOf(LocalDate.of(2022, 1, 1)),
				Timestamp.from(LocalDateTime.of(2022, 2, 1, 0, 0).atZone(ZoneId.systemDefault()).toInstant()),
				"Content 1");

		dummyPage.showDocumentDescription(document);
		assertEquals(document, dummyPage.getSelectedDocument(), "Selected document was not set correctly");
	}

	@Test
	@DisplayName("Test On Import with Selected Document")
	void testOnImportWithSelectedDocument() {
		DocumentDAO document = new DocumentDAO(1, "testFile1", Date.valueOf(LocalDate.of(2022, 1, 1)),
				Timestamp.from(LocalDateTime.of(2022, 2, 1, 0, 0).atZone(ZoneId.systemDefault()).toInstant()),
				"Content 1");

		dummyPage.showDocumentDescription(document);
		dummyPage.onImport();

		assertTrue(dummyPage.isImportSuccess(), "Import should succeed when a document is selected");
	}

	@Test
	@DisplayName("Test On Import without Selected Document")
	void testOnImportWithoutSelectedDocument() {
		dummyPage.onImport();
		assertFalse(dummyPage.isImportSuccess(), "Import should fail when no document is selected");
	}

	@Test
	@DisplayName("Test Populate Table with No Documents")
	void testEmptyPopulateTable() {
		ArrayList<DocumentDAO> documents = new ArrayList<>();
		dummyPage.populateTable(documents);
		assertTrue(dummyPage.getSearchedDocuments().isEmpty(),
				"Table should be empty after populating with no documents");
	}

	@Test
	@DisplayName("Test Searched Documents after Multiple Searches")
	void testSearchedDocumentsAfterMultipleSearches() {
		ArrayList<DocumentDAO> documents = new ArrayList<>();
		documents.add(new DocumentDAO(1, "testFile1", Date.valueOf(LocalDate.of(2022, 1, 1)),
				Timestamp.from(LocalDateTime.of(2022, 2, 1, 0, 0).atZone(ZoneId.systemDefault()).toInstant()),
				"Content 1"));
		documents.add(new DocumentDAO(2, "sampleFile2", Date.valueOf(LocalDate.of(2022, 1, 2)),
				Timestamp.from(LocalDateTime.of(2022, 2, 2, 0, 0).atZone(ZoneId.systemDefault()).toInstant()),
				"Content 2"));

		dummyPage.populateTable(documents);
		dummyPage.onSearch();
		assertEquals(1, dummyPage.getSearchedDocuments().size(), "First search result incorrect");

		dummyPage.onSearch();
		assertEquals(1, dummyPage.getSearchedDocuments().size(), "Second search result incorrect");
	}

	@Test
	@DisplayName("Test Show Document Description Updates Selected Document")
	void testShowDocumentDescriptionUpdatesSelectedDocument() {
		DocumentDAO document1 = new DocumentDAO(1, "testFile1", Date.valueOf(LocalDate.of(2022, 1, 1)),
				Timestamp.from(LocalDateTime.of(2022, 2, 1, 0, 0).atZone(ZoneId.systemDefault()).toInstant()),
				"Content 1");
		DocumentDAO document2 = new DocumentDAO(2, "sampleFile2", Date.valueOf(LocalDate.of(2022, 1, 2)),
				Timestamp.from(LocalDateTime.of(2022, 2, 2, 0, 0).atZone(ZoneId.systemDefault()).toInstant()),
				"Content 2");

		dummyPage.showDocumentDescription(document1);
		assertEquals(document1, dummyPage.getSelectedDocument(), "First document not selected correctly");

		dummyPage.showDocumentDescription(document2);
		assertEquals(document2, dummyPage.getSelectedDocument(), "Selected document did not update correctly");
	}

	@Test
	@DisplayName("Test Initial Import Success Value")
	void testIsImportSuccessInitialValue() {
		assertFalse(dummyPage.isImportSuccess(), "Initial import success should be false");
	}
}
