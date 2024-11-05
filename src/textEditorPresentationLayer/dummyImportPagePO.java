package textEditorPresentationLayer;

import java.util.ArrayList;

import textEditorDataAccessLayer.DocumentDAO;

public class dummyImportPagePO implements I_Importing {

	public ArrayList<DocumentDAO> mockDocuments = new ArrayList<>();
	private ArrayList<DocumentDAO> searchedDocuments = new ArrayList<>();
	private DocumentDAO selectedDocument;
	private boolean importSuccess = false;

	@Override
	public void showDocumentDescription(DocumentDAO selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

	@Override
	public void onSearch() {
		searchedDocuments.clear();
		for (DocumentDAO doc : mockDocuments) {
			if (doc.getFileName().contains("test")) {
				searchedDocuments.add(doc);
			}
		}
	}

	@Override
	public void onImport() {
		if (selectedDocument != null) {
			importSuccess = true;
		}
	}

	@Override
	public void populateTable(ArrayList<DocumentDAO> documents) {
	    mockDocuments.clear();
	    mockDocuments.addAll(documents);
	}

	public ArrayList<DocumentDAO> getSearchedDocuments() {
		return searchedDocuments;
	}

	public DocumentDAO getSelectedDocument() {
		return selectedDocument;
	}

	public boolean isImportSuccess() {
		return importSuccess;
	}
}
