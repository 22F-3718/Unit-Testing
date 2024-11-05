package textEditorDataAccessLayer;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ITextEditorDBDAO {
	public ArrayList<DocumentDAO> getAllDocuments();

	public ArrayList<DocumentDAO> searchDocumentsByName(String fileName);

	public boolean saveDocument(DocumentDAO document);

	public boolean isFileNameExists(String fileName) throws SQLException;
	
	public DocumentDAO getDocumentById(int fileID);
}
