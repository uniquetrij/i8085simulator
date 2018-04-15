package me.uniquetrij.i8085.giu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Uniquetrij
 */
@SuppressWarnings("serial")
public class FileOpener extends JDialog
{
    //<editor-fold defaultstate="collapsed" desc="instance members">
    private JTextComponent jTextComponent;
    private JFileChooser jFileChooser;
    private File  file;
    //</editor-fold>

    public FileOpener(JTextComponent jTextComponent)
    {
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(FileOpener.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        this.jTextComponent=jTextComponent;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jFileChooser = new javax.swing.JFileChooser();
        jFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        jFileChooser.setSelectedFile(new File(System.getProperty("user.home")+"/i8085asm/assembly Programs/."));
        jFileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                jFileChooserActionPerformed(evt);
            }
        });
        jFileChooser.setFileFilter(new FileNameExtensionFilter("i8085sim Files (*.i85)", "i85"));
        jFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("assembly Files (*.asm)", "asm"));
        file=null;

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jFileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jFileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setTitle("Open");
        pack();
        setLocationRelativeTo(jTextComponent);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setModal(true);
        setVisible(true);

        //setLocationRelativeTo(caller);
    }// </editor-fold>

    private void jFileChooserActionPerformed(ActionEvent evt)
    {
        if(evt.getActionCommand().toString().equals("ApproveSelection"))
        {
            file=((javax.swing.JFileChooser)evt.getSource()).getSelectedFile();
            if(!file.exists())
            {
                JOptionPane.showConfirmDialog(this, file.getName()+"\nFile not found.\nCheck the file name and/or path and try again.", "Error Opening File", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                file=null;
                return;
            }
        }
        dispose();
    }

    public File getFile()
    {
        return file;
    }

    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new FileOpener(null).setVisible(true);
            }
        });
    }

}
