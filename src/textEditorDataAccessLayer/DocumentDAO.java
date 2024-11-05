package textEditorDataAccessLayer;

import java.sql.Date;
import java.sql.Timestamp;

public class DocumentDAO {
    private int fileID;
    private String fileName;
    private Date creationDate;
    private Timestamp lastUpdateTime;
    private String fileContent;

    public DocumentDAO(int fileID, String fileName, Date creationDate, Timestamp lastUpdateTime, String fileContent) 
    {
        this.fileID = fileID;
        this.fileName = fileName;
        this.creationDate = creationDate;
        this.lastUpdateTime = lastUpdateTime;
        this.fileContent = fileContent;
    }
    
    public DocumentDAO() {}

    public int getFileID() {
        return fileID;
    }

    public String getFileName() {
        return fileName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getFileContent() {
        return fileContent;
    }
    
    public void setFileID(int fileID) {
    	this.fileID=fileID;
    }

    public void setFileName(String fileName) {
    	this.fileName=fileName;
    }

    public void setCreationDate(Date creationDate) {
    	this.creationDate=creationDate;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
    	this.lastUpdateTime=lastUpdateTime;
    }

    public void setFileContent(String fileContent) {
    	this.fileContent=fileContent;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
