package online.services.reviewing;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Locale;

public class PanelRegister extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JTextField textUsername;
	private static JPasswordField textpassword;
	private static JPasswordField textpassword2;
	public static AutocompleteJComboBox comboBox2;
	private static JLabel lbusername;
	private static JLabel lbpassword;
	private static JLabel lbpasswordreenter;
	private final transient JTextField surname;
	private final transient JTextField name;
	private static JLabel lblJob;
	protected static String temp = "";
	
	public PanelRegister() {
		super();
		this.setVisible(false);
		this.setBounds(0, 0, 480, 300);
		this.setLayout(null);
		MainFrame.gr.selectInputMethod(new Locale("el", "GR"));

		comboBox2 = MainFrame.comboBox;
		comboBox2.setBounds(190, 218, 175, 20);
		this.add(comboBox2);

		final JLabel label = new JLabel("\u03A3\u03C5\u03BC\u03C0\u03BB\u03B7\u03C1\u03CE\u03C3\u03C4\u03B5 \u03C4\u03B7\u03BD \u03A6\u03CC\u03C1\u03BC\u03B1 \u0395\u03B3\u03B3\u03C1\u03B1\u03C6\u03AE\u03C2");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Times New Roman", Font.BOLD, 14));
		label.setBounds(95, 24, 290, 18);
		add(label);

		textUsername = new JTextField();
		textUsername.setColumns(10);
		textUsername.setBounds(265, 125, 100, 20);
		this.add(textUsername);

		textpassword = new JPasswordField();
		textpassword.setEchoChar('*');
		textpassword.setBounds(265, 156, 100, 20);
		this.add(textpassword);

		textpassword2 = new JPasswordField();
		textpassword2.setEchoChar('*');
		textpassword2.setBounds(265, 187, 100, 20);
		this.add(textpassword2);

		name = new JTextField();
		((AbstractDocument) name.getDocument()).setDocumentFilter(new AlphabetOnly());
		name.setColumns(10);
		name.setBounds(265, 63, 100, 20);
		this.add(name);

		surname = new JTextField();
		((AbstractDocument) surname.getDocument()).setDocumentFilter(new AlphabetOnly());
		surname.setColumns(10);
		surname.setBounds(265, 94, 100, 20);
		this.add(surname);

		final JButton btnRegisterNow = new JButton("Register Now");
		btnRegisterNow.setBounds(95, 255, 110, 23);
		this.add(btnRegisterNow);

		final JButton btnBack = new JButton("Back");
		btnBack.setBounds(290, 255, 95, 23);
		this.add(btnBack);

		lbusername = new JLabel("Username");
		lbusername.setBounds(120, 128, 75, 14);
		this.add(lbusername);

		lbpassword = new JLabel("Password");
		lbpassword.setBounds(120, 159, 75, 14);
		this.add(lbpassword);

		lbpasswordreenter = new JLabel("Re-Enter Password");
		lbpasswordreenter.setBounds(120, 190, 120, 14);
		this.add(lbpasswordreenter);

		final JLabel lblSurname = new JLabel("Surname");
		lblSurname.setBounds(120, 97, 75, 14);
		this.add(lblSurname);

		final JLabel lblName = new JLabel("Name");
		lblName.setBounds(120, 66, 75, 14);
		this.add(lblName);

		lblJob = new JLabel("Job");
		lblJob.setBounds(120, 221, 46, 14);
		add(lblJob);

		btnRegisterNow.addActionListener(new ActionListener() { // RegisterNow button for PanelRegister frame
					public void actionPerformed(final ActionEvent button) {
						if (MainFrame.getRole() == 0) {
							addworker();
						} else {
							registernow();
						}
					}
				});

		btnBack.addActionListener(new ActionListener() { // Back button on register frame
			public void actionPerformed(final ActionEvent button) {
				back();
			}
		});
	}

	public boolean usercheck(final String user) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		int role = -1;
		boolean checker = false;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection(MainFrame.connectionUrl,MainFrame.connectionUser, MainFrame.connectionPass);
			stmt = conn.createStatement();
			final String querry = "select * from Login where username='" + user+ "';";
			result = stmt.executeQuery(querry);

			while (result.next()) {
				role = result.getInt("role");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (role == 0 || role == 1) {
			checker = true;
		} else {
			checker = false;
		}
		return checker;
	}

	public void addworker() {
		if (temp.length() > 0 && name.getText().length() > 0 && surname.getText().length() > 0) {
			final String job = temp;
			Connection conn = null;
			PreparedStatement prep_statem = null;
			Statement stmt = null;
			ResultSet result = null;
			int jid = 0;
			int idd = 0;
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				conn = DriverManager.getConnection(MainFrame.connectionUrl,MainFrame.connectionUser, MainFrame.connectionPass);
				stmt = conn.createStatement();
				String querry = "select * from Job where jobname='" + job+ "';";
				result = stmt.executeQuery(querry);
				while (result.next()) {
					jid = result.getInt("id");
				}
				querry = "select count(*) from Worker;";
				result = stmt.executeQuery(querry);
				while (result.next()) {
					idd = result.getInt(1);
				}
				result.close();
				stmt.close();
				idd++;
				prep_statem = conn.prepareStatement("insert into Worker values(?,?,?,?)");
				prep_statem.setInt(1, idd);
				prep_statem.setInt(2, jid);
				prep_statem.setString(3, name.getText());
				prep_statem.setString(4, surname.getText());
				prep_statem.executeUpdate();
				prep_statem.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String buff1 = Integer.toString(jid);
			String buff2 = Integer.toString(idd);
			while (buff1.length() < 3) {
				buff1 = '0' + buff1;
			}
			while (buff2.length() < 3) {
				buff2 = '0' + buff2;
			}
			buff1 += buff2;
			name.setText("");
			surname.setText("");
			MainFrame.textc.setText("");
			JOptionPane.showMessageDialog(MainFrame.frame,"Registration is successfully inserted!\n" + "The CodeID of the Worker is: " + buff1);
		} else {
			JOptionPane.showMessageDialog(MainFrame.frame,"Please fill all of the required fields", "Empty Fields",JOptionPane.WARNING_MESSAGE);
		}

	}

	public void adduser(final String Name,final String Sname,final String Uname,final String Pass) {
		Connection conn = null;
		PreparedStatement prep_statem = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(MainFrame.connectionUrl,MainFrame.connectionUser, MainFrame.connectionPass);
			prep_statem = conn.prepareStatement("insert into Login values(?,?,?,?,?)");
			prep_statem.setString(1, Name);
			prep_statem.setString(2, Sname);
			prep_statem.setString(3, Uname);
			prep_statem.setString(4, Pass);
			prep_statem.setInt(5, 1);
			prep_statem.executeUpdate();
			prep_statem.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void lblset() {
		if (MainFrame.getRole() == 0) {
			textUsername.setVisible(false);lbusername.setVisible(false);
			textpassword.setVisible(false);textpassword2.setVisible(false);
			lbpassword.setVisible(false);lbpasswordreenter.setVisible(false);
			comboBox2.setVisible(true);comboBox2.setBounds(190, 125, 175, 20);
			lblJob.setVisible(true);lblJob.setBounds(120, 125, 46, 14);
		}else {
			textUsername.setVisible(true);lbusername.setVisible(true);
			textpassword.setVisible(true);textpassword2.setVisible(true);
			lbpassword.setVisible(true);lbpasswordreenter.setVisible(true);
			comboBox2.setVisible(false);
			lblJob.setVisible(false);
		}
	}

	public void back() {
		name.setText("");
		surname.setText("");
		textUsername.setText("");
		textpassword.setText("");
		textpassword2.setText("");
		this.setVisible(false);
		if (MainFrame.getRole() == 0) {
			MainFrame.textc.setText("");
			MainFrame.panelchange(0);
		} else {
			comboBox2.setVisible(true);
			MainFrame.logout();
		}
	}

	public void registernow() {
		if (textUsername.getText().length() > 0 && new String(textpassword.getPassword()).length() > 0 && new String(textpassword2.getPassword()).length() > 0 && name.getText().length() > 0 && surname.getText().length() > 0) {
			if (usercheck(textUsername.getText())) {
				JOptionPane.showMessageDialog(MainFrame.frame,"Username already exist", "Username",JOptionPane.WARNING_MESSAGE);
				textUsername.setText("");
			} else if (Arrays.equals(textpassword.getPassword(),textpassword2.getPassword())) {
				adduser(name.getText(), surname.getText(),textUsername.getText(),new String(textpassword.getPassword()));
				JOptionPane.showMessageDialog(MainFrame.frame,"Registration is successfully inserted!");
				back();
			} else {
				JOptionPane.showMessageDialog(MainFrame.frame,"Password Not Match", "Password",JOptionPane.ERROR_MESSAGE);
				textpassword.setText("");
				textpassword2.setText("");
			}
		} else {
			JOptionPane.showMessageDialog(MainFrame.frame,"Please fill all of the required fields", "Empty Fields",JOptionPane.WARNING_MESSAGE);
		}
	}
}

class AlphabetOnly extends DocumentFilter {
	@Override
	public void replace(final FilterBypass fbfb,final int iii,final int ikl1,final String string,final AttributeSet asda) throws BadLocationException {
		if (string == "") {
			super.replace(fbfb, iii, ikl1, String.valueOf(""), asda);
		}
		else {
			for (int n = string.length(); n > 0; n--) {
				final char ccc = string.charAt(n - 1);// get a single character of the string
				// System.out.println(c);
				if (Character.isAlphabetic(ccc)) {// if its an alphabetic character
					super.replace(fbfb, iii, ikl1, String.valueOf(ccc), asda);// allow update to take place for the given character
				}
			}
		}
	}
}