package textEditorDataAccessLayer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class TextEditorDBDAO {

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/textfiles";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public ArrayList<DocumentDAO> getAllDocuments() {
        ArrayList<DocumentDAO> documents = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM documents")) {

            while (rs.next()) {
                int fileID = rs.getInt("FileID");
                String fileName = rs.getString("FileName");
                Date creationDate = rs.getDate("CreationDate");
                Timestamp lastUpdateTime = rs.getTimestamp("LastUpdateTime");
                String fileContent = rs.getString("FileContent");
                documents.add(new DocumentDAO(fileID, fileName, creationDate, lastUpdateTime, fileContent));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public ArrayList<DocumentDAO> searchDocumentsByName(String fileName) {
        ArrayList<DocumentDAO> documents = new ArrayList<>();
        String query = "SELECT * FROM documents WHERE FileName LIKE ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + fileName + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int fileID = rs.getInt("FileID");
                String name = rs.getString("FileName");
                Date creationDate = rs.getDate("CreationDate");
                Timestamp lastUpdateTime = rs.getTimestamp("LastUpdateTime");
                String fileContent = rs.getString("FileContent");
                documents.add(new DocumentDAO(fileID, name, creationDate, lastUpdateTime, fileContent));
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return documents;
    }
    
    
    public boolean saveDocument(DocumentDAO document) {
        String query = "INSERT INTO documents (FileName, CreationDate, LastUpdateTime, FileContent) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, document.getFileName());
            pstmt.setDate(2, document.getCreationDate());
            pstmt.setTimestamp(3, document.getLastUpdateTime());
            pstmt.setString(4, document.getFileContent());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isFileNameExists(String fileName) throws SQLException {
        String query = "SELECT COUNT(*) FROM documents WHERE FileName = ?";
        try (PreparedStatement stmt = DriverManager.getConnection(query).prepareStatement(query)) {
            stmt.setString(1, fileName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

}
