package textEditorPresentationLayer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import textEditorDataAccessLayer.DocumentDAO;
import textEditorDataAccessLayer.TextEditorDBDAO;

public class ImportFilePagePO extends JFrame implements I_Importing{

    private static final long serialVersionUID = 1L;
    private JTextField searchField;
    private JButton searchButton;
    private JButton importButton;
    private JButton exitButton;
    private JTable documentTable;
    private TextEditorDBDAO documentsDAL;

    public ImportFilePagePO(HomePagePO mainPage) {
        setTitle("Import Files");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        documentsDAL = new TextEditorDBDAO();

        JPanel topPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        topPanel.add(new JLabel("Search by File Name:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        importButton = new JButton("Import to Local Drive");
        exitButton = new JButton("Exit");

        documentTable = new JTable();
        loadAllDocuments();

        documentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = documentTable.getSelectedRow();
                if (row != -1) {
                    DefaultTableModel model = (DefaultTableModel) documentTable.getModel();
                    DocumentDAO selectedDocument = (DocumentDAO) model.getValueAt(row, model.getColumnCount() - 1);
                    showDocumentDescription(selectedDocument);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSearch();
            }
        });

        importButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onImport();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(importButton);
        bottomPanel.add(exitButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(documentTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void loadAllDocuments() {
        ArrayList<DocumentDAO> documents = documentsDAL.getAllDocuments();
        populateTable(documents);
    }

    public void populateTable(ArrayList<DocumentDAO> documents) {
        String[] columnNames = { "FileID", "FileName", "CreationDate", "LastUpdateTime", "Content" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (DocumentDAO doc : documents) {
            Object[] row = {
                doc.getFileID(),
                doc.getFileName(),
                doc.getCreationDate(),
                doc.getLastUpdateTime(),
                doc
            };
            model.addRow(row);
        }

        documentTable.setModel(model);
        documentTable.removeColumn(documentTable.getColumnModel().getColumn(4));
    }

    public void onSearch() {
        String fileName = searchField.getText();
        if (!fileName.isEmpty()) {
            ArrayList<DocumentDAO> documents = documentsDAL.searchDocumentsByName(fileName);
            populateTable(documents);
        } else {
            loadAllDocuments();
        }
    }

    public void onImport() {
        int row = documentTable.getSelectedRow();
        if (row != -1) {
            DefaultTableModel model = (DefaultTableModel) documentTable.getModel();
            DocumentDAO selectedDocument = (DocumentDAO) model.getValueAt(row, model.getColumnCount() - 1);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Directory to Save File");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedDirectory = fileChooser.getSelectedFile();
                File file = new File(selectedDirectory, selectedDocument.getFileName() + ".txt");

                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(selectedDocument.getFileContent());
                    JOptionPane.showMessageDialog(this,
                            "File imported successfully to: " + selectedDirectory.getAbsolutePath());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error importing file: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a file to import.");
        }
    }

    private void onExit() {
        System.exit(0);
    }

    public void showDocumentDescription(DocumentDAO selectedDocument) {
        String description = selectedDocument.getFileContent();
        new DocumentContentPage(description, selectedDocument.getFileName());
    }
}
