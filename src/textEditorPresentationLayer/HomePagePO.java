package textEditorPresentationLayer;

import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class HomePagePO extends JFrame {

    private static final long serialVersionUID = 1L;
    private JButton createButton;
    private JButton importButton;
    private JButton manageButton;
    private JButton exitButton;

    public HomePagePO() {
        setTitle("File Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        createButton = new JButton("Create Files");
        createButton.setPreferredSize(new Dimension(200, 50));

        importButton = new JButton("Import Files");
        importButton.setPreferredSize(new Dimension(200, 50));

        manageButton = new JButton("Manage Files");
        manageButton.setPreferredSize(new Dimension(200, 50));

        exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(200, 50));

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCreate();
            }
        });

        importButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onImport();
            }
        });

        manageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onManage();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        });

        add(createButton);
        add(importButton);
        add(manageButton);
        add(exitButton);

        setVisible(true);
    }

    private void onCreate() {
    	this.setVisible(false);
        new CreateFilePage();
    }

    private void onImport() {
        this.setVisible(false);
        new ImportFilePagePO(this);
    }

    private void onManage() {
        JOptionPane.showMessageDialog(this, "Manage Files functionality clicked");
    }

    private void onExit() {
        System.exit(0);
    }

    public static void main(String[] args) {
        new HomePagePO();
    }

}
