package textEditorPresentationLayer;

import java.util.ArrayList;

import textEditorDataAccessLayer.DocumentDAO;

public interface I_Importing {
	public void showDocumentDescription(DocumentDAO selectedDocument);
	public void onSearch();
	public void onImport();
	public void populateTable(ArrayList<DocumentDAO> documents);
}
