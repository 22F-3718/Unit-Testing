package textEditorPresentationLayer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import textEditorBusinessLogicLayer.TextEditorBO;
import textEditorDataAccessLayer.DocumentDAO;
import textEditorDataAccessLayer.ITextEditorDBDAO;

public class CreateFilePage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private JTextField fileNameField;
	private JButton saveButton;
	private JButton saveLocalButton;
	private JButton exitButton;
	private JButton clearButton;
	private JButton backButton;
	private TextEditorBO textEditorBO;
	private ITextEditorDBDAO documentsDAL;

	public CreateFilePage() {
		setTitle("Create New File");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		textEditorBO = new TextEditorBO(documentsDAL);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		JPanel panel = new JPanel(new FlowLayout());
		panel.add(new JLabel("File Name:"));
		fileNameField = new JTextField(20);
		panel.add(fileNameField);

		saveButton = new JButton("Save to Database");
		saveLocalButton = new JButton("Save to Local Drive");
		clearButton = new JButton("Clear");
		backButton = new JButton("Back");
		exitButton = new JButton("Exit");

		panel.add(saveButton);
		panel.add(saveLocalButton);
		panel.add(clearButton);
		panel.add(backButton);
		panel.add(exitButton);

		add(new JScrollPane(textArea), BorderLayout.CENTER);
		add(panel, BorderLayout.SOUTH);

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					onSaveToDB();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		saveLocalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSaveToLocalDrive();
			}
		});

		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClear();
			}
		});

		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					onBack();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onExit();
			}
		});

		setVisible(true);
	}

	private void onSaveToDB() throws SQLException {
		String fileName = fileNameField.getText();
		String fileContent = textArea.getText();

		if (fileName.isEmpty() || fileContent.isEmpty()) {
			JOptionPane.showMessageDialog(this, "File Name and Content cannot be empty!");
			return;
		}

		DocumentDAO newDocument = new DocumentDAO();
		newDocument.setFileName(fileName);
		newDocument.setFileContent(fileContent);
		newDocument.setCreationDate(new java.sql.Date(new Date().getTime()));
		newDocument.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));

		boolean isSaved = textEditorBO.saveDocumentToDB(newDocument);
		if (isSaved) {
			JOptionPane.showMessageDialog(this, "File saved to database successfully.");
		} else {
			JOptionPane.showMessageDialog(this, "Failed to save file to database.");
		}
	}

	private void onSaveToLocalDrive() {
		String fileName = fileNameField.getText();
		String fileContent = textArea.getText();

		if (fileName.isEmpty() || fileContent.isEmpty()) {
			JOptionPane.showMessageDialog(this, "File Name and Content cannot be empty!");
			return;
		}

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save File to Local Drive");
		fileChooser.setSelectedFile(new File(fileName + ".txt"));

		int userSelection = fileChooser.showSaveDialog(this);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			try (FileWriter writer = new FileWriter(file)) {
				writer.write(fileContent);
				JOptionPane.showMessageDialog(this, "File saved to local drive successfully.");
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
			}
		}
	}

	private void onClear() {
		textArea.setText("");
		fileNameField.setText("");
	}

	private void onBack() throws SQLException {
		int choice = JOptionPane.showConfirmDialog(this, "Do you want to save the document before exiting?",
				"Save Document", JOptionPane.YES_NO_CANCEL_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			onSaveToDB();
			dispose();
			new HomePagePO();
		} else if (choice == JOptionPane.NO_OPTION) {
			dispose();
			new HomePagePO();
		}
	}

	private void onExit() {
		dispose();
	}
}
