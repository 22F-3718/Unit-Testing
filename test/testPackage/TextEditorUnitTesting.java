package testPackage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import textEditorDataAccessLayer.DocumentDAO;
import textEditorDataAccessLayer.dummyTextEditorDAO;

public class TextEditorUnitTesting {

	dummyTextEditorDAO dbDAO;

	@BeforeEach
	public void setUp() {
		dbDAO = new dummyTextEditorDAO();
		dbDAO.clearDocuments();
	}

	private Object[] getTestCaseData(String testCaseId) {
		try {
			Workbook workbook = Workbook.getWorkbook(new File("TestCases.xls"));
			Sheet sheet = workbook.getSheet(0);

			for (int i = 1; i < sheet.getRows(); i++) {
				Cell idCell = sheet.getCell(0, i);
				if (idCell.getContents().equals(testCaseId)) {
					String docName = sheet.getCell(1, i).getContents();
					String content = sheet.getCell(2, i).getContents();
					workbook.close();
					return new Object[] { docName, content };
				}
			}
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
		String expectedDocName = "sample_document";
		String content = "Sample content for the document.";
		dbDAO.saveDocument(new DocumentDAO(1, expectedDocName, new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), content));
		ArrayList<DocumentDAO> documents = dbDAO.searchDocumentsByName("sample");
		assertNotNull(documents, "Search should return a non-null list");
		assertTrue(documents.size() > 0, "Search should return results for matching names");
		documents.forEach(doc -> assertTrue(doc.getFileName().contains("sample"),
				"Each document name should contain the search term"));
	}

	@Test
	@DisplayName("Testing search by document name - empty string")
	public void testSearchDocumentsByEmptyName() {
		Object[] data = getTestCaseData("Test2");
		dbDAO.saveDocument(new DocumentDAO(1, (String) data[0], new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), (String) data[1]));
		ArrayList<DocumentDAO> documents = dbDAO.searchDocumentsByName("");
		assertNotNull(documents, "Search with empty string should return a non-null list");
		assertTrue(documents.size() > 0, "Search with empty string should return all documents");
	}

	@Test
	@DisplayName("Testing save new document")
	public void testSaveNewDocument() {
		Object[] data = getTestCaseData("Test3");
		DocumentDAO doc = new DocumentDAO(2, (String) data[0], new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), (String) data[1]);
		boolean isSaved = dbDAO.saveDocument(doc);
		assertTrue(isSaved, "New document should be saved successfully");
		assertTrue(dbDAO.getAllDocuments().contains(doc), "Saved document should be in the list");
	}

	@Test
	@DisplayName("Testing save document with long content")
	public void testSaveDocumentWithLongContent() {
		Object[] data = getTestCaseData("Test4");
		DocumentDAO doc = new DocumentDAO(3, (String) data[0], new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), (String) data[1]);
		boolean isSaved = dbDAO.saveDocument(doc);
		assertTrue(isSaved, "Document with long content should be saved successfully");
	}

	@Test
	@DisplayName("Testing save document with null content")
	public void testSaveDocumentWithNullContent() {
		Object[] data = getTestCaseData("Test5");
		DocumentDAO doc = new DocumentDAO(4, (String) data[0], new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), null);
		boolean isSaved = dbDAO.saveDocument(doc);
		assertTrue(isSaved, "Document with null content should still be saved");
	}

	@Test
	@DisplayName("Testing save document with special characters in filename")
	public void testSaveDocumentSpecialCharactersInFilename() {
		Object[] data = getTestCaseData("Test6");
		DocumentDAO doc = new DocumentDAO(5, (String) data[0], new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), (String) data[1]);
		boolean isSaved = dbDAO.saveDocument(doc);
		assertTrue(isSaved, "Document with special characters in filename should be saved");
	}

	@Test
	@DisplayName("Testing retrieval of documents after multiple insertions")
	public void testMultipleDocumentInsertions() {
		Object[] data1 = getTestCaseData("Test7");
		Object[] data2 = getTestCaseData("Test8");

		DocumentDAO doc1 = new DocumentDAO(6, (String) data1[0], new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), (String) data1[1]);
		DocumentDAO doc2 = new DocumentDAO(7, (String) data2[0], new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), (String) data2[1]);

		dbDAO.saveDocument(doc1);
		dbDAO.saveDocument(doc2);

		ArrayList<DocumentDAO> documents = dbDAO.getAllDocuments();
		assertTrue(documents.size() >= 2, "There should be at least two documents saved");
	}

	@Test
	@DisplayName("Testing retrieval of document by ID")
	public void testRetrieveDocumentById() {
		Object[] data = getTestCaseData("Test9");
		DocumentDAO doc = new DocumentDAO(9, (String) data[0], new Date(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), (String) data[1]);
		dbDAO.saveDocument(doc);

		DocumentDAO retrievedDoc = dbDAO.getDocumentById(doc.getFileID());
		assertNotNull(retrievedDoc, "Document retrieval by ID should not return null");
		assertTrue(retrievedDoc.getFileID() == doc.getFileID(),
				"Retrieved document ID should match the saved document ID");
	}
}
