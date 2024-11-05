package textEditorPresentationLayer;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class DocumentContentPage extends JFrame {
    private static final long serialVersionUID = 1L;

    public DocumentContentPage(String fileContent, String fileName) {
        setTitle("File Content: " + fileName);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea(fileContent);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        add(new JScrollPane(textArea));

        setVisible(true);
    }
}
