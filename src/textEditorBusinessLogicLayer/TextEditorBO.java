package textEditorBusinessLogicLayer;

import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import textEditorDataAccessLayer.DocumentDAO;
import textEditorDataAccessLayer.ITextEditorDBDAO;

public class TextEditorBO {

    private ITextEditorDBDAO documentsDAL;

    public TextEditorBO(ITextEditorDBDAO documentsDAL) {
    	this.documentsDAL=documentsDAL;
    }

    public ArrayList<DocumentDAO> searchDocuments(String fileName) {
        return documentsDAL.searchDocumentsByName(fileName);
    }

    public ArrayList<DocumentDAO> getAllDocuments() {
        return documentsDAL.getAllDocuments();
    }
    
    public boolean saveDocumentToDB(DocumentDAO document) throws SQLException {
        if (documentsDAL.isFileNameExists(document.getFileName())) {
            throw new SQLIntegrityConstraintViolationException("Duplicate entry for filename: " + document.getFileName());
        }
        return documentsDAL.saveDocument(document);
    }

    
    public boolean importFile(DocumentDAO selectedDocument) {
        try {
            File directory = new File("ImportedFiles");
            directory.mkdirs();
            File file = new File(directory, selectedDocument.getFileName() + ".txt");
            FileWriter writer = new FileWriter(file);
            writer.write(selectedDocument.getFileContent());
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
}
