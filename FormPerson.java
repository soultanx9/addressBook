package TugasBesar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FormPerson extends JFrame implements ActionListener{
	private String title;
	private String fileName;
	private boolean mode;
	private List<Person> listPerson;
	private JTextField txtFirstName, txtLastName, txtCity, txtZip, txtPhoneNumber, txtState;
	private JLabel lblInstruction, lblFirstName, lblLastName, lblCity, lblZip, lblPhoneNumber, lblAddress, lblState;
	private JTextArea txaAddress;
	private JButton btnSave;
	private JButton btnCancel;
	private JPanel mainPanel, leftPanel,rightPanel, btnPanel;
	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 420;
	public static final int FIELD_HEIGHT = 30;
	public static final int LEFT_WIDTH = 150;
	private Color bckColor;
	private boolean addStatus= false;
	private String[] lang = {"Cannot be empty"};
	private PersonListener listener;
	private int index;
	
	public FormPerson(Person _person, int personIndex, boolean mode,List<Person> person, PersonListener _listener){
		this.listPerson = person;
		this.listener = _listener;
		this.index = personIndex;
		this.mode = mode;
		Container container = this.getContentPane();
		bckColor = new Color(255,255,255);
		this.mainPanel= new JPanel();
		this.mainPanel.setPreferredSize(new Dimension(this.FRAME_WIDTH, this.FRAME_HEIGHT));
		this.mainPanel.setLayout(new BorderLayout());
		this.mainPanel.setBackground(bckColor);
		this.leftPanel = new JPanel();
		this.leftPanel.setPreferredSize(new Dimension(this.LEFT_WIDTH, this.FRAME_HEIGHT));
		this.leftPanel.setLayout(new FlowLayout());
		this.leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 20));
		this.leftPanel.setBackground(bckColor);
		this.leftPanel.setForeground(Color.WHITE);
		this.rightPanel = new JPanel();
		this.rightPanel.setPreferredSize(new Dimension(this.FRAME_WIDTH-this.LEFT_WIDTH, this.FRAME_HEIGHT));
		this.rightPanel.setLayout(new FlowLayout());
		this.rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));
		this.rightPanel.setBackground(bckColor);
		this.rightPanel.setForeground(Color.WHITE);
		if(mode){
			this.title = "Add Person";
		}else{
			this.title = "Edit Person";
		}
		this.setTitle(this.title);
		this.setSize(this.FRAME_WIDTH, this.FRAME_HEIGHT);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		//this.setLocation(AddressBookWindow.FRAME_X_LOCATION+100, AddressBookWindow.FRAME_HEIGHT); //temporary line
		
		
	/*---------------- LABEL DECLARATION--------------------------------------*/
		this.lblInstruction = new JLabel("Please insert the fields bellow");
		this.lblInstruction.setPreferredSize(new Dimension(this.LEFT_WIDTH, this.FIELD_HEIGHT*2));
		this.lblInstruction.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 50));
		this.lblInstruction.setForeground(new Color(250,128,114));
		this.lblFirstName = new JLabel("First Name");
		this.setLabelStyle(this.lblFirstName);
		this.lblLastName = new JLabel("Last Name");
		this.setLabelStyle(this.lblLastName);
		this.lblAddress = new JLabel("Address");
		this.lblAddress.setPreferredSize(new Dimension(this.LEFT_WIDTH, this.FIELD_HEIGHT*3));
		this.lblAddress.setForeground(new Color(128,128,128));
		this.lblCity = new JLabel("City");
		this.setLabelStyle(this.lblCity);
		this.lblState = new JLabel("State");
		this.setLabelStyle(this.lblState);
		this.lblZip = new JLabel("ZIP Code");
		this.setLabelStyle(this.lblZip);
		this.lblPhoneNumber = new JLabel("PhoneNumber");
		this.setLabelStyle(this.lblPhoneNumber);
		this.leftPanel.add(this.lblFirstName);
		this.leftPanel.add(this.lblLastName);
		this.leftPanel.add(this.lblAddress);
		this.leftPanel.add(this.lblCity);
		this.leftPanel.add(this.lblState);
		this.leftPanel.add(this.lblZip);
		this.leftPanel.add(this.lblPhoneNumber);
		
	/*---------------- END OF LABEL DECLARRATION -----------------------------*/
		
	/*---------------- FIELD DECLARATION ------------------------------------------------------------------------------*/
		/*---------------- FIRST NAME FIELD -------------------------------------------------------------------------*/
		this.txtFirstName = new JTextField();
		this.setInputStyle(this.txtFirstName);
		if(!mode){
			this.txtFirstName.setText(_person.getFirstName());
			this.txtFirstName.setEditable(false);
			this.txtFirstName.setBackground(new Color(176,196,222));
		}
		this.rightPanel.add(this.txtFirstName);		
		/*---------------- END OF FIRST NAME FIELD ------------------------------------------------------------------*/
		/*---------------- LAST NAME FIELD -------------------------------------------------------------------------*/
		this.txtLastName = new JTextField();
		this.setInputStyle(this.txtLastName);
		if(!mode){
			this.txtLastName.setText(_person.getLastName());
		}
		this.rightPanel.add(this.txtLastName);		
		/*---------------- END OF LAST NAME FIELD ------------------------------------------------------------------*/
		/*---------------- LAST NAME FIELD -------------------------------------------------------------------------*/
		this.txaAddress = new JTextArea();
		this.txaAddress.setPreferredSize(new Dimension(this.FRAME_WIDTH-this.LEFT_WIDTH-50, this.FIELD_HEIGHT*3));
		this.txaAddress.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.txaAddress.setBackground(new Color(255,239,213));
		if(!mode){
			this.txaAddress.setText(_person.getAddress());
		}
		this.rightPanel.add(this.txaAddress);		
		/*---------------- END OF LAST NAME FIELD ------------------------------------------------------------------*/
		/*---------------- LAST NAME FIELD -------------------------------------------------------------------------*/
		this.txtCity = new JTextField();
		this.setInputStyle(this.txtCity);
		if(!mode){
			this.txtCity.setText(_person.getCity());
		}
		this.rightPanel.add(this.txtCity);		
		/*---------------- END OF LAST NAME FIELD ------------------------------------------------------------------*/
		/*---------------- STATE FIELD -------------------------------------------------------------------------*/
		this.txtState = new JTextField();
		this.setInputStyle(this.txtState);
		if(!mode){
			this.txtState.setText(_person.getState());
		}
		this.rightPanel.add(this.txtState);		
		/*---------------- END OF STATE FIELD ------------------------------------------------------------------*/
		/*---------------- LAST NAME FIELD -------------------------------------------------------------------------*/
		this.txtZip = new JTextField();
		this.setInputStyle(this.txtZip);
		if(!mode){
			this.txtZip.setText(_person.getZip());
		}
		this.rightPanel.add(this.txtZip);		
		/*---------------- END OF LAST NAME FIELD ------------------------------------------------------------------*/
		/*---------------- LAST NAME FIELD -------------------------------------------------------------------------*/
		this.txtPhoneNumber = new JTextField();
		this.setInputStyle(this.txtPhoneNumber);
		if(!mode){
			this.txtPhoneNumber.setText(_person.getPhoneNumber());
		}
		this.rightPanel.add(this.txtPhoneNumber);		
		/*---------------- END OF LAST NAME FIELD ------------------------------------------------------------------*/
		
		this.btnPanel = new JPanel();
		this.btnPanel.setLayout(new FlowLayout());
		this.btnPanel.setPreferredSize(new Dimension(this.FRAME_WIDTH, 50));
		this.btnPanel.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		this.btnSave = new JButton("Save");
		this.btnSave.setPreferredSize(new Dimension(70,this.FIELD_HEIGHT));
		this.btnSave.setBackground(new Color(0,191,255));
		this.btnSave.setForeground(Color.WHITE);
		this.btnSave.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.btnCancel = new JButton("Cancel");
		this.btnCancel.setPreferredSize(new Dimension(90,this.FIELD_HEIGHT));
		this.btnCancel.setBackground(new Color(255,165,0));
		this.btnCancel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.btnCancel.setForeground(Color.WHITE);
		this.btnCancel.addActionListener(this);
		this.btnSave.setAlignmentX(Component.RIGHT_ALIGNMENT);
		this.btnSave.addActionListener(this);
		this.btnPanel.add(this.btnSave);
		this.btnPanel.add(this.btnCancel);
		
		
	/*---------------- END OF FIELD DECLARATION*/
		this.mainPanel.add(this.lblInstruction, BorderLayout.NORTH);
		this.mainPanel.add(this.leftPanel, BorderLayout.WEST);
		this.mainPanel.add(this.rightPanel, BorderLayout.EAST);
		this.mainPanel.add(this.btnPanel, BorderLayout.SOUTH);
		container.add(this.mainPanel);
		
		this.setLocation(200,150);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}
	
	public void setInputStyle(JTextField txt){
		txt.setPreferredSize(new Dimension(this.FRAME_WIDTH-this.LEFT_WIDTH-50, this.FIELD_HEIGHT));
		txt.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		txt.setBackground(new Color(255,239,213));
		txt.setForeground(new Color(128,128,128));
		txt.setFont(new Font(txt.getFont().getName(),Font.BOLD,txt.getFont().getSize()));
	}
	
	public void setLabelStyle(JLabel lbl){
		lbl.setPreferredSize(new Dimension(this.LEFT_WIDTH, this.FIELD_HEIGHT));
		lbl.setForeground(new Color(128,128,128));
	}
	
	
	
	public boolean getAddStatus(){
		return this.addStatus;
	}
	
	
	public void CancelData(){
		this.setVisible(false);
		this.dispose();
	}
	
	public JTextField getTxtFirstName(){
		return this.txtFirstName;
	}
	
	public JLabel getLblFirstName(){
		return this.lblFirstName;
	}
	
	public JTextField getTxtLastName(){
		return this.txtLastName;
	}
	
	public JLabel getLblLastName(){
		return this.lblLastName;
	}
	
	public JTextArea getTxaAddress(){
		return this.txaAddress;
	}
	
	public JLabel getLblAddress(){
		return this.lblAddress;
	}
	
	public JTextField getTxtCity(){
		return this.txtCity;
	}
	
	public JLabel getLblCity(){
		return this.lblCity;
	}
	
	public JTextField getTxtState(){
		return this.txtState;
	}
	
	public JLabel getLblState(){
		return this.lblState;
	}
	public JTextField getTxtZip(){
		return this.txtZip;
	}
	
	public JLabel getLblZip(){
		return this.lblZip;
	}
	public JTextField getTxtPhoneNumber(){
		return this.txtPhoneNumber;
	}
	
	public JLabel getLblPhoneNumber(){
		return this.lblPhoneNumber;
	}
	public String getFileName(){
		return this.fileName;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String menuName;
		
		menuName = e.getActionCommand();
		
		switch(menuName){
			case "Save":this.SaveData();break;
			case "Cancel":this.CancelData();break;
		}
	}
	
	public void SaveData(){
		boolean status = true;
		String message = "";
		String _firstName, _lastName, _address, _city, _state, _zip, _phoneNumber;
		/*------------------------ FIELD Validation ---------------------------------------------*/
		if(this.getTxtFirstName().getText().equals("")){
			message += this.getLblFirstName().getText()+" "+this.lang[0]+"\n";
			status = false;
		}
		
		if(this.getTxtLastName().getText().equals("")){
			message += this.getLblLastName().getText()+" "+this.lang[0]+"\n";
			status = false;
		}
		if(this.getTxaAddress().getText().equals("")){
			message += this.getLblAddress().getText()+" "+this.lang[0]+"\n";
			status = false;
		}
		if(this.getTxtCity().getText().equals("")){
			message += this.getLblCity().getText()+" "+this.lang[0]+"\n";
			status = false;
		}
		if(this.getTxtState().getText().equals("")){
			message += this.getLblState().getText()+" "+this.lang[0]+"\n";
			status = false;
		}
		if(this.getTxtZip().getText().equals("")){
			message += this.getLblZip().getText()+" "+this.lang[0]+"\n";
			status = false;
		}
		if(this.getTxtPhoneNumber().getText().equals("")){
			message += this.getLblPhoneNumber().getText()+" "+this.lang[0]+"\n";
			status = false;
		}else{
			if(!Person.isPhoneNumberValid(this.getTxtPhoneNumber().getText())){
				message += "Phone Number is Incorrect Format\n";
				status = false;
			}
		}
		/*---------------------------------------------------------------------------------------*/
		
		if(!status){
			Person.showErrorMessage(this, message);
		}else{
			_firstName = this.getTxtFirstName().getText();
			_lastName = this.getTxtLastName().getText();
			_address = this.getTxaAddress().getText();
			_city = this.getTxtCity().getText();
			_state = this.getTxtState().getText();
			_zip = this.getTxtZip().getText();
			_phoneNumber = this.getTxtPhoneNumber().getText();
			Person person = new Person(_firstName, _lastName, _address, _city, _state, _zip, _phoneNumber);
			if(mode){
				this.listPerson.add(person);
			}else{
				this.listPerson.set(this.index, person); 
			}
			Person.showSuccessMessage(this, "Data is already saved.");
			this.addStatus = true;
			this.dispose();
			this.listener.refreshBookAddress(this.listPerson);
			
		}
	}
	
	public List<Person> getAllPerson(){
		return this.listPerson;
	}
	
}
