package textEditorDataAccessLayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class dummyTextEditorDAO implements ITextEditorDBDAO {
	private List<DocumentDAO> documents = new ArrayList<>();

	@Override
	public ArrayList<DocumentDAO> getAllDocuments() {
		return new ArrayList<>(documents);
	}

	@Override
	public ArrayList<DocumentDAO> searchDocumentsByName(String fileName) {
		return documents.stream().filter(doc -> doc.getFileName().contains(fileName))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public boolean saveDocument(DocumentDAO document) {
		documents.add(document);
		return true;
	}

	@Override
	public boolean isFileNameExists(String fileName) throws SQLException {
		return documents.stream().anyMatch(doc -> doc.getFileName().equals(fileName));
	}

	public void clearDocuments() {
		documents.clear();
	}

	@Override
	public DocumentDAO getDocumentById(int fileID) {
	    return documents.stream()
	            .filter(doc -> doc.getFileID() == fileID)
	            .findFirst()
	            .orElse(null);
	}

}
