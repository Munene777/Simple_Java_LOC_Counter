
package loc;

import java.io.*;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;

/*
 * Author of this code is Code_777(Munene Kuira)
 */
public class LocUi extends JPanel
                              implements ActionListener {
    static private String newline = "\n";
    private JTextArea log;
    private JFileChooser fc;
    private JLabel locLabel,count;

    public LocUi() {
        super(new BorderLayout());

        //Create the log first, because the action listener
        //needs to refer to it.
        locLabel= new JLabel("The No of lines");
        count= new JLabel("0");
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        JButton sendButton = new JButton("Browse a file...");
        sendButton.addActionListener(this);

        add(sendButton, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
        JPanel lower= new JPanel();
        lower.add(locLabel);
        lower.add(count);
        add(lower,BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        //Set up the file chooser.
        if (fc == null) {
            fc = new JFileChooser();
           
	    //Add a custom file filter and disable the default
	    //allowing only .java, .c and .cpp files to be accepted
            FileNameExtensionFilter filter= new FileNameExtensionFilter("Java, C and C++", 
            		"java","c","cpp");
            fc.setFileFilter(filter);
          
        }

        //Show it.
        int returnVal = fc.showDialog(LocUi.this,
                                      "Attach");

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            log.setText("File name : " + file.getName()
                       + "." + newline);
            try {
				count.setText(""+countLOC(file.getAbsolutePath()));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
        } else {
            log.setText("Attachment cancelled by user." + newline);
        }
        log.setCaretPosition(log.getDocument().getLength());

        //Reset the file chooser for the next time it's shown.
        fc.setSelectedFile(null);
    }
    
    
    
    public static int countLOC (String filename) throws FileNotFoundException, IOException
    {
       int counter=0;
        //creating a file object to contain the file been passed
        File file = new File(filename);
        
        //reading in the file using scanner
        Scanner s = new Scanner(file);
            String line;
            boolean block= false;
            //loopinr line by line
            while (s.hasNextLine()) {
            	line = s.nextLine();
            	//removing the reading whitespace
            	line= line.trim();
            	
            	//check whether the line is blank if so don't count ignore
            	if(line.length()!=0){
            		if(!block){
            			
            			//checking the start of line comments
            		if (line.charAt(0)=='/' && line.charAt(1) =='/')
            			continue;
            		//checking the start of block comments
            		if (line.charAt(0)=='/' && line.charAt(1) =='*'){
            			
            			block= true;
            			continue;
            		}
            		//when the block comment begins within a line of code
            		if(line.contains("/*"))
            			block =true;
            		
            		//incrementing the line counter
            	++counter;
                
                
            	}
            	//checking the end of the block comments
            	if(line.charAt(line.length()-1)=='/' && line.charAt(line.length()-2) =='*')
            		block=false;
               
            	}
            
                
               
            }
        
//return the final count of the actual Line of Code in a file
        return counter;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("LOC Counter");
        //set size
        frame.setSize(200, 300);
        //centreing the jframe
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new LocUi());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();/*this is a hidden
                script teating the start of a block with
                a real line of code*/
            }
        });
    }
}
