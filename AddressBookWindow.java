package TugasBesar;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class AddressBookWindow extends JFrame implements ActionListener, PersonListener {
	// Data member pada AdbWindow
	final public static int FRAME_WIDTH = 700;
	final public static int FRAME_HEIGHT = 300;
	final public static int FRAME_X_ORIGIN = 150;
	final public static int FRAME_Y_ORIGIN = 250;
	private String title;
	private JMenu file, about;
	private JMenuItem open, save, saveAs, print, quit, aboutUs;
	private ImageIcon logo;
	private JPanel mainPanel, buttonPanel, logoPanel;
	private JLabel lblLogo;
	private JButton btnPerson;
	private JButton jbnOk;
	private String fileName = "";
	
	private BorderLayout Layout;
	
	private JFileChooser fileChooser;
	private JList personList;
	public String newCopiedFile;
	
	Font f1 = new Font("Times New Roman", 1, 12);
	Font f2 = new Font("Times New Roman", 0, 12);
	
	private boolean addStatus = false;
	private Container contentPane;
	private List<Person> listOfPerson;
	private Date lastSavedTime;
	private boolean activeStatus;
	private JButton btnAdd, btnEdit, btnDelete, btnSortByName, btnSortByZip;
	private JPanel btnPanel;
	private FormPerson frmPerson;
	
	/*--------------- Constructor AdbWindow.java ----------------------------------------------------*/
	
	public AddressBookWindow() {
		Container contentPane;
		// Set frame properties
		setTitle("Address Book");
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(false);
		setLocation(FRAME_X_ORIGIN, FRAME_Y_ORIGIN);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		contentPane = getContentPane();
		contentPane.setLayout( new BorderLayout());
		
		buatFile();
		buatAbout();
		
		// Membuat MENU BAR
		JMenuBar menu = new JMenuBar();
		menu.add(file);
		menu.add(about);
		setJMenuBar(menu);
		
		// Membuat response
		
		
		this.logo = new ImageIcon("viral-marketing.png");
		
		// LAYOUT MANAGER AdbWindow
		setBackground(Color.gray);
		// Isi JPanel
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(this.FRAME_WIDTH-150, this.FRAME_HEIGHT));
		mainPanel.setBackground(new Color(255,255,224));
		logoPanel = new JPanel();
		lblLogo = new JLabel(this.logo);
		lblLogo.setPreferredSize(new Dimension(150,150));
		lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 50, 0, 50));
		
		logoPanel.add(this.lblLogo);
		logoPanel.setBackground(Color.WHITE);
		contentPane.add(this.logoPanel, BorderLayout.WEST);
		contentPane.add(this.mainPanel, BorderLayout.EAST);
		
	}

	public static void main(String args[]) {
		// Panggil Constructor
		AddressBookWindow adbw = new AddressBookWindow();
		adbw.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String menuName;
		
		menuName = e.getActionCommand();
		
		switch(menuName){
			case "Quit": 
				System.exit(0);break;
			case "Developer":
				JDialog dlgAbout = new CustomDialog(this, "Developer", true);
				dlgAbout.setVisible(true);
				break;
			case "Open": 
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new MyCustomFilter());
			    int returnVal = fc.showOpenDialog(AddressBookWindow.this);
			    
			    if (returnVal == JFileChooser.APPROVE_OPTION) {
			    	boolean fileValid = true;
			        File file = fc.getSelectedFile();
			        String filePath = file.getAbsolutePath();
			        String fileNameFromPath = file.getName();
			        this.fileName = file.getAbsolutePath();
			        if(fileNameFromPath.equals("")){
			        	Person.showErrorMessage(this, "File name is not correct");
			        	fileValid = false;
			        }
			        String[] extension = fileNameFromPath.split("\\.");
			        if(!extension[1].equals("txt")){
			        	Person.showErrorMessage(this, "File extension is not correct");
			        	fileValid = false;
			        }
			        if(fileValid){
			        	this.openAddressBook(filePath);
			        }
			    }
			
				break;
			case "Add":
				this.frmPerson = new FormPerson(null,-1,true, this.listOfPerson, this);
				this.addStatus = frmPerson.getAddStatus();
				frmPerson.setVisible(true);
				this.disableAddressBook();
				break;
			case "Edit":
				if(this.personList.getSelectedIndex() == -1){
					Person.showErrorMessage(this, "Please select list first!");
				}else{
					this.frmPerson = new FormPerson(this.listOfPerson.get(this.personList.getSelectedIndex()),this.personList.getSelectedIndex(),false, this.listOfPerson, this);
					this.addStatus = frmPerson.getAddStatus();
					frmPerson.setVisible(true);
				}
				break;
			case "Delete":
				if(this.personList.getSelectedIndex() == -1){
					Person.showErrorMessage(this, "Please select list first!");
				}else{
					int option = JOptionPane.showConfirmDialog(this, "Are you sure want to delete "+this.listOfPerson.get(this.personList.getSelectedIndex()).getFirstName()+"?");
					if(option == 0){
						this.listOfPerson.remove(this.personList.getSelectedIndex());
						this.refreshBookAddress(this.listOfPerson);
						Person.showSuccessMessage(this, "The data is already deleted.");
					}
				}
				break;
			case "Sort by Name":
				List<String> personNameOnly = this.getAllListOfPersonName();
				for(int i=0;i<this.listOfPerson.size()-1;i++){
					for(int j=i+1;j<this.listOfPerson.size();j++){
						Person personTemp = new Person();
						if(personNameOnly.get(i).trim().compareTo(personNameOnly.get(j).trim()) > 0){
							String temp = personNameOnly.get(i);
							personNameOnly.set(i, personNameOnly.get(j));
							personNameOnly.set(j, temp);
							
							personTemp = this.listOfPerson.get(i);
							this.listOfPerson.set(i, this.listOfPerson.get(j));
							this.listOfPerson.set(j, personTemp);
						}
					}
				}
				this.refreshBookAddress(this.listOfPerson);
				break;
			case "Sort by ZIP":
				List<String> zipOnly = this.getAllListOfPersonZip();
				for(int i=0;i<this.listOfPerson.size()-1;i++){
					for(int j=i+1;j<this.listOfPerson.size();j++){
						Person personTemp = new Person();
						if(zipOnly.get(i).trim().compareTo(zipOnly.get(j).trim()) > 0){
							String temp = zipOnly.get(i);
							zipOnly.set(i, zipOnly.get(j));
							zipOnly.set(j, temp);
							
							personTemp = this.listOfPerson.get(i);
							this.listOfPerson.set(i, this.listOfPerson.get(j));
							this.listOfPerson.set(j, personTemp);
						}
					}
				}
				this.refreshBookAddress(this.listOfPerson);
				break;
			case "Save":
				if(this.fileName.equals("")){
					JOptionPane.showMessageDialog(this, "There is no Address Book can be saved","Warning",JOptionPane.WARNING_MESSAGE);
				}else{
					String strAddBook = "";
					for(int i=0;i<this.listOfPerson.size();i++){
						strAddBook += this.listOfPerson.get(i).getFirstName()+";";
						strAddBook += this.listOfPerson.get(i).getLastName()+";";
						strAddBook += this.listOfPerson.get(i).getAddress()+";";
						strAddBook += this.listOfPerson.get(i).getCity()+";";
						strAddBook += this.listOfPerson.get(i).getState()+";";
						strAddBook += this.listOfPerson.get(i).getZip()+";";
						if(i==this.listOfPerson.size()-1){
							strAddBook += this.listOfPerson.get(i).getPhoneNumber();
						}else{
							strAddBook += this.listOfPerson.get(i).getPhoneNumber()+"\n";
						}
					}
					try {
						PrintWriter out = new PrintWriter(this.fileName);
						out.println(strAddBook);
						out.flush();
						Person.showSuccessMessage(this, "Address Book is already saved.");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				break;
			case "Save As...":
				if(this.fileName.equals("")){
					JOptionPane.showMessageDialog(this, "There is no Address Book can be saved","Warning",JOptionPane.WARNING_MESSAGE);
				}else{
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileFilter(new MyCustomFilter());
					fileChooser.setDialogTitle("Specify a file to save");
					int userSelection = fileChooser.showSaveDialog(this);
					
					if(userSelection == JFileChooser.APPROVE_OPTION){
						File fileToSave = fileChooser.getSelectedFile();
						String strAddBook = "";
						for(int i=0;i<this.listOfPerson.size();i++){
							strAddBook += this.listOfPerson.get(i).getFirstName()+";";
							strAddBook += this.listOfPerson.get(i).getLastName()+";";
							strAddBook += this.listOfPerson.get(i).getAddress()+";";
							strAddBook += this.listOfPerson.get(i).getCity()+";";
							strAddBook += this.listOfPerson.get(i).getState()+";";
							strAddBook += this.listOfPerson.get(i).getZip()+";";
							if(i==this.listOfPerson.size()-1){
								strAddBook += this.listOfPerson.get(i).getPhoneNumber();
							}else{
								strAddBook += this.listOfPerson.get(i).getPhoneNumber()+"\n";
							}
						}
						try {
							PrintWriter out = new PrintWriter(fileToSave.getAbsolutePath());
							out.println(strAddBook);
							out.flush();
							this.fileName = fileToSave.getAbsolutePath();
							Person.showSuccessMessage(this, "Address Book is already saved.");
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						System.out.println("Save as file: "+fileToSave.getAbsolutePath());
					}
				}
				break;
		}
		
		
	}
	
	public List<String> getAllListOfPersonName(){
		List<String> strPersonName = new ArrayList<String>();
		for(int i=0;i<this.listOfPerson.size();i++){
			strPersonName.add(this.listOfPerson.get(i).getFirstName()+""+this.listOfPerson.get(i).getLastName());
		}
		return strPersonName;
	}
	
	public List<String> getAllListOfPersonZip(){
		List<String> strZip = new ArrayList<String>();
		for(int i=0;i<this.listOfPerson.size();i++){
			strZip.add(this.listOfPerson.get(i).getZip()+""+this.listOfPerson.get(i).getZip());
		}
		return strZip;
	}
	
	public void disableAddressBook(){
		this.btnAdd.setEnabled(false);
	}
	
	public void enableAddressBook(){
		Component[] components = this.getComponents();
	    for(int i = 0; i < components.length; i++) {
	        components[i].setEnabled(true);
	    }
	}
	
	@Override
	public void refreshBookAddress(List<Person> listOfPerson) {
		// TODO Auto-genererated method stub
		List <String> personName = new ArrayList<String>();
		for(int i=0;i<this.listOfPerson.size();i++){
			personName.add(this.listOfPerson.get(i).getFirstName());
		}
		personList = new JList(personName.toArray());
		this.mainPanel.removeAll();
		this.mainPanel.add(personList);
		this.mainPanel.revalidate();
		this.mainPanel.repaint();
		personList.setForeground(new Color(0,0,139));
		JScrollPane scrollText = new JScrollPane(personList);
		scrollText.setSize(this.mainPanel.getSize());
		scrollText.setPreferredSize(new Dimension(this.FRAME_WIDTH-200, this.FRAME_HEIGHT-90));
		scrollText.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 10));
		this.mainPanel.add(scrollText);
		contentPane.add(this.btnPanel, BorderLayout.SOUTH);
		contentPane.add(this.mainPanel, BorderLayout.EAST);
		this.enableAddressBook();
		this.setVisible(true);
		
	}
	
	public List <String> getPersonNameList(){
		List <String> personName = new ArrayList<String>();
		for(int i=0;i<this.listOfPerson.size();i++){
			personName.add(listOfPerson.get(i).getFirstName());
		}
		return personName;
	}
	
	public void openAddressBook(String _fileName){
		contentPane = this.getContentPane();
		try{
			List<Person> _person = new ArrayList<Person>();
			File file = new File(_fileName);
			String[] personString = new String[7];
			boolean valid = true;
			Scanner input = new Scanner(file);
			int i = 0;
			if(input.hasNextLine()){
				while(input.hasNextLine()){
					String temp = input.nextLine();
					personString = temp.split("\\;");
					Person personTemp = new Person(personString[0], personString[1], personString[2], personString[3], personString[4], personString[5], personString[6]);
					_person.add(personTemp);
				}
				input.close();
			}
			this.listOfPerson = _person;
			personList = new JList(this.getPersonNameList().toArray());
			personList.setForeground(new Color(0,0,139));
			JScrollPane scrollText = new JScrollPane(personList);
			scrollText.setSize(this.mainPanel.getSize());
			scrollText.setPreferredSize(new Dimension(this.FRAME_WIDTH-200, this.FRAME_HEIGHT-90));
			scrollText.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 10));
			this.mainPanel.add(scrollText);
			this.createButton();
			contentPane.add(this.btnPanel, BorderLayout.SOUTH);
			contentPane.add(this.mainPanel, BorderLayout.EAST);
			this.title = file.getName().replace(".txt", "");
			this.setTitle(title);
			this.setVisible(true);
		
		}catch(FileNotFoundException ex){
			Person.showErrorMessage(this, "File is not found");
		}catch(IndexOutOfBoundsException ex){
			Person.showErrorMessage(this, "The content of file is wrong!");
		}catch(Exception ex){
			Person.showErrorMessage(this, "File is error");
		}
		
	}
	
	public void createButton(){
		/*---------------------- ADD BUTTON ----------------------------*/
		this.btnAdd = new JButton("Add");
		this.btnAdd.setPreferredSize(new Dimension(70, FormPerson.FIELD_HEIGHT));
		this.btnAdd.setBackground(new Color(0,191,255));
		this.btnAdd.setForeground(Color.WHITE);
		this.btnAdd.addActionListener(this);
		this.btnAdd.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 10));
		/*--------------------------------------------------------------*/
		/*---------------------- EDIT BUTTON ---------------------------*/
		this.btnEdit = new JButton("Edit");
		this.btnEdit.setPreferredSize(new Dimension(70, FormPerson.FIELD_HEIGHT));
		this.btnEdit.setBackground(new Color(0,191,255));
		this.btnEdit.setForeground(Color.WHITE);
		this.btnEdit.addActionListener(this);
		this.btnEdit.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 10));
		/*--------------------------------------------------------------*/
		/*---------------------- DELETE BUTTON ---------------------------*/
		this.btnDelete = new JButton("Delete");
		this.btnDelete.setPreferredSize(new Dimension(70, FormPerson.FIELD_HEIGHT));
		this.btnDelete.setBackground(new Color(220,20,60));
		this.btnDelete.setForeground(Color.WHITE);
		this.btnDelete.addActionListener(this);
		this.btnDelete.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 10));
		/*--------------------------------------------------------------*/
		/*---------------------- SORT BY NAME BUTTON ---------------------------*/
		this.btnSortByName = new JButton("Sort by Name");
		this.btnSortByName.setPreferredSize(new Dimension(70, FormPerson.FIELD_HEIGHT));
		this.btnSortByName.setBackground(new Color(50,205,50));
		this.btnSortByName.setForeground(Color.WHITE);
		this.btnSortByName.addActionListener(this);
		this.btnSortByName.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 10));
		/*--------------------------------------------------------------*/
		/*---------------------- SORT BY NAME BUTTON ---------------------------*/
		this.btnSortByZip = new JButton("Sort by ZIP");
		this.btnSortByZip.setPreferredSize(new Dimension(70, FormPerson.FIELD_HEIGHT));
		this.btnSortByZip.setBackground(new Color(50,205,50));
		this.btnSortByZip.setForeground(Color.WHITE);
		this.btnSortByZip.addActionListener(this);
		this.btnSortByZip.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 10));
		/*--------------------------------------------------------------*/
		
		btnPanel = new JPanel();
		btnPanel.setPreferredSize(new Dimension(AddressBookWindow.FRAME_WIDTH, 55));
		btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		GridLayout gl = new GridLayout(1,5,10,10);
		btnPanel.setLayout(gl);
		btnPanel.add(btnAdd);
		btnPanel.add(btnEdit);
		btnPanel.add(btnDelete);
		btnPanel.add(btnSortByName);
		btnPanel.add(btnSortByZip);
	}
	
	private void buatFile() {
		// MENU BAR Bagian File
		file = new JMenu("File");
		file.setFont(f1);
		file.setMnemonic(KeyEvent.VK_A);
		
		open = new JMenuItem("Open");
		open.setFont(f2);
		open.addActionListener(this);
		
		save = new JMenuItem("Save");
		save.setFont(f2);
		save.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_S, 
				ActionEvent.CTRL_MASK));
		save.addActionListener(this);
		
		saveAs = new JMenuItem("Save As...");
		saveAs.setFont(f2);
		saveAs.addActionListener(this);
		
		print = new JMenuItem("Print");
		print.setFont(f2);
		print.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_P, 
				ActionEvent.CTRL_MASK));
		print.addActionListener(this);
		
		quit = new JMenuItem("Quit");
		quit.setFont(f2);
		quit.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_X, 
				ActionEvent.CTRL_MASK));
		quit.addActionListener(this);
		
		file.add(open);
		file.add(save);
		file.add(saveAs);
		file.add(print);
		file.add(quit);
	}
	
	// Membuat Custom Dialog

	class CustomDialog extends JDialog implements ActionListener {
		JButton jbnOk;
	
		CustomDialog(JFrame parent, String title, boolean modal){
			super(parent, title, modal);
			JPanel p1 = new JPanel(new FlowLayout(FlowLayout.CENTER));

			String text;
			text = "Address Book Information\n\n";
			text += "Tugas Akhir DDP2\n";
			text += "DDP2: Hisyam Fahmi, S.Kom., M.Kom\n\n";
			text += "Soultan Gani (1306481732)\n"; //Ask him to get his NPM
			text += "Novida Wayan Sari (1606954930)\n";
			
			
			JTextArea jtAreaAbout = new JTextArea(5, 21);
			jtAreaAbout.setText(text.toString());
			jtAreaAbout.setFont(new Font("Calibri", Font.BOLD, 13));
			jtAreaAbout.setEditable(false);
			jtAreaAbout.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

			p1.add(jtAreaAbout);
			getContentPane().add(p1, BorderLayout.CENTER);
			
			JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
			jbnOk = new JButton("OK");
			jbnOk.addActionListener(this);
			jbnOk.setBackground(new Color(0,191,255));
			jbnOk.setPreferredSize(new Dimension(70,30));
			jbnOk.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			jbnOk.setForeground(Color.WHITE);

			p2.add(jbnOk);
			getContentPane().add(p2, BorderLayout.SOUTH);

			setLocation(408, 270);
			setResizable(false);
			
			addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e)
					{
						Window aboutDialog = e.getWindow();
						aboutDialog.dispose();
					}
				}
			);

			pack();
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			if(event.getSource() == jbnOk)	{
				this.dispose();
			}
		}
	}
	
	private void buatAbout() {
		// MENU BAR Bagian About
		about = new JMenu("About");
		about.setFont(f1);
		about.setMnemonic(KeyEvent.VK_B);
		
		aboutUs = new JMenuItem("Developer");
		aboutUs.setFont(f2);
		aboutUs.addActionListener(this);
		
		about.add(aboutUs);
	}
}

class MyCustomFilter extends javax.swing.filechooser.FileFilter {
    @Override
    public boolean accept(File file) {
        // Allow only directories, or files with ".txt" extension
        return file.isDirectory() || file.getAbsolutePath().endsWith(".txt");
    }
    @Override
    public String getDescription() {
        // This description will be displayed in the dialog,
        // hard-coded = ugly, should be done via I18N
        return "Text documents (*.txt)";
    }
} 
