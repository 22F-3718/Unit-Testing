package testPackage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import textEditorDataAccessLayer.DocumentDAO;
import textEditorDataAccessLayer.dummyTextEditorDAO;

public class TextEditorUnitTesting {

	dummyTextEditorDAO dbDAO;

	@BeforeEach
	public void setUp() {
		dbDAO = new dummyTextEditorDAO();
		dbDAO.clearDocuments();
	}

	@Test
	@DisplayName("Testing database connection")
	public void testDatabaseConnection() {
		assertNotNull(dbDAO, "Database connection should be initialized");
	}

	@Test
	@DisplayName("Testing retrieval of all documents")
	public void testGetAllDocuments() {
		ArrayList<DocumentDAO> documents = dbDAO.getAllDocuments();
		assertNotNull(documents, "Documents list should not be null");
	}

	@Test
	@DisplayName("Testing search by document name - valid name")
	public void testSearchDocumentsByName() {
		dbDAO.saveDocument(new DocumentDAO(1, "sampleDoc", new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), "Sample content"));
		ArrayList<DocumentDAO> documents = dbDAO.searchDocumentsByName("sample");
		assertNotNull(documents, "Search should return a non-null list");
		assertTrue(documents.size() > 0, "Search should return results for matching names");
		documents.forEach(doc -> assertTrue(doc.getFileName().contains("sample"),
				"Each document name should contain the search term"));
	}

	@Test
	@DisplayName("Testing search by document name - empty string")
	public void testSearchDocumentsByEmptyName() {
		dbDAO.saveDocument(new DocumentDAO(1, "Doc1", new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), "Content 1"));
		ArrayList<DocumentDAO> documents = dbDAO.searchDocumentsByName("");
		assertNotNull(documents, "Search with empty string should return a non-null list");
		assertTrue(documents.size() > 0, "Search with empty string should return all documents");
	}

	@Test
	@DisplayName("Testing save new document")
	public void testSaveNewDocument() {
		DocumentDAO doc = new DocumentDAO(2, "TestDoc", new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), "Test document content");
		boolean isSaved = dbDAO.saveDocument(doc);
		assertTrue(isSaved, "New document should be saved successfully");
		assertTrue(dbDAO.getAllDocuments().contains(doc), "Saved document should be in the list");
	}

	@Test
	@DisplayName("Testing save document with long content")
	public void testSaveDocumentWithLongContent() {
		StringBuilder longContent = new StringBuilder();
		for (int i = 0; i < 10000; i++) {
			longContent.append("LongContent ");
		}
		DocumentDAO doc = new DocumentDAO(3, "LongContentDoc", new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), longContent.toString());
		boolean isSaved = dbDAO.saveDocument(doc);
		assertTrue(isSaved, "Document with long content should be saved successfully");
	}

	@Test
	@DisplayName("Testing save document with null content")
	public void testSaveDocumentWithNullContent() {
		DocumentDAO doc = new DocumentDAO(4, "NullContentDoc", new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), null);
		boolean isSaved = dbDAO.saveDocument(doc);
		assertTrue(isSaved, "Document with null content should still be saved");
	}

	@Test
	@DisplayName("Testing save document with special characters in filename")
	public void testSaveDocumentSpecialCharactersInFilename() {
		DocumentDAO doc = new DocumentDAO(5, "Special_!@#$_File", new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), "Special content");
		boolean isSaved = dbDAO.saveDocument(doc);
		assertTrue(isSaved, "Document with special characters in filename should be saved");
	}

	@Test
	@DisplayName("Testing retrieval of documents after multiple insertions")
	public void testMultipleDocumentInsertions() {
		DocumentDAO doc1 = new DocumentDAO(6, "File1", new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), "Content 1");
		DocumentDAO doc2 = new DocumentDAO(7, "File2", new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), "Content 2");

		dbDAO.saveDocument(doc1);
		dbDAO.saveDocument(doc2);

		ArrayList<DocumentDAO> documents = dbDAO.getAllDocuments();
		assertTrue(documents.size() >= 2, "There should be at least two documents saved");
	}

	@Test
	@DisplayName("Testing retrieval of document by ID")
	public void testRetrieveDocumentById() {
	    DocumentDAO doc = new DocumentDAO(9, "RetrieveByIdDoc", new Date(System.currentTimeMillis()),
	            new Timestamp(System.currentTimeMillis()), "Content for ID retrieval test");
	    dbDAO.saveDocument(doc);

	    DocumentDAO retrievedDoc = dbDAO.getDocumentById(doc.getFileID());
	    assertNotNull(retrievedDoc, "Document retrieval by ID should not return null");
	    assertTrue(retrievedDoc.getFileID() == doc.getFileID(), "Retrieved document ID should match the saved document ID");
	}

	

	
}
