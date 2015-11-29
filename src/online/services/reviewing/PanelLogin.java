package online.services.reviewing;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List; 

public class PanelLogin extends JPanel {
	private static final long serialVersionUID = 1L;
	private transient int role = -1;
	final private transient JTextField textUsername;
	final private transient JPasswordField textPassword;

	public PanelLogin() {
		super();
		
		this.setBounds(0, 0, 480, 300);
		this.setLayout(null);

		final JLabel lblpic = new JLabel("");
		lblpic.setBounds(265, 60, 170, 170);
		this.add(lblpic);
		lblpic.setHorizontalAlignment(SwingConstants.CENTER);
		lblpic.setIcon(new ImageIcon(getClass().getResource("/images/eval.png")));

		final JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(35, 103, 75, 14);
		this.add(lblUsername);

		textUsername = new JTextField();
		textUsername.setBounds(115, 100, 100, 20);
		this.add(textUsername);
		textUsername.setColumns(10);

		final JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(35, 134, 75, 14);
		this.add(lblPassword);

		textPassword = new JPasswordField();
		textPassword.setEchoChar('*');
		textPassword.setBounds(115, 131, 100, 20);
		this.add(textPassword);

		final JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent button) {
				login();
			}
		});
		btnLogin.setBounds(35, 255, 95, 23);
		this.add(btnLogin);

		final JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent button) {
				register();
			}
		});
		btnRegister.setBounds(190, 255, 95, 23);
		this.add(btnRegister);

		final JButton btnQuit = new JButton("Quit");
		btnQuit.setBounds(350, 255, 95, 23);
		this.add(btnQuit);

		final JLabel lblTitle = new JLabel("\u039A\u03B1\u03BB\u03C9\u03C3\u03BF\u03C1\u03AF\u03C3\u03B1\u03C4\u03B5 \u03C3\u03C4\u03BF Online \u03A3\u03CD\u03C3\u03C4\u03B7\u03BC\u03B1 \u0391\u03BE\u03B9\u03BF\u03BB\u03CC\u03B3\u03B7\u03C3\u03B7\u03C2 \u03A5\u03C0\u03B7\u03C1\u03B5\u03C3\u03B9\u03CE\u03BD");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblTitle.setBounds(20, 25, 440, 19);
		this.add(lblTitle);

		btnQuit.addActionListener(new ActionListener() { // Quit button for Panel login frame
			@Override
			public void actionPerformed(final ActionEvent button) {
				System.exit(0);
			}
		});
	}

	public boolean logincheck(final String user, final String pass) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		boolean checker = false;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection(MainFrame.connectionUrl,MainFrame.connectionUser, MainFrame.connectionPass);
			stmt = conn.createStatement();
			final String querry = "select * from Login where username='" + user + "' and password='" + pass + "';";
			result = stmt.executeQuery(querry);

			while (result.next()) {
				role = result.getInt("role");
			}
			result.close();
			stmt.close();
			conn.close();
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

	public void register() {
		textUsername.setText("");
		textPassword.setText("");
		this.setVisible(false);
		MainFrame.panelchange(1);
	}

	public void login() {
		if (textUsername.getText().length() > 0 && new String(textPassword.getPassword()).length() > 0) {
			if (!(logincheck(textUsername.getText(),new String(textPassword.getPassword())))) {
				JOptionPane.showMessageDialog(MainFrame.frame,"Wrong Username/Password!");
			} else {
				MainFrame.username = textUsername.getText();
				MainFrame.setRole(this.role);
				this.role = -1;
				textUsername.setText("");
				textPassword.setText("");
				this.setVisible(false);
				// System.out.println("role= " + role);
				PanelUser.lblset();
				MainFrame.panelchange(0);
			}
		} else {
			JOptionPane.showMessageDialog(MainFrame.frame,"Enter Username and Password");
		}
	}

	public List<String> dbjobs(final List<String> jobdblist) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection(MainFrame.connectionUrl,MainFrame.connectionUser, MainFrame.connectionPass);
			stmt = conn.createStatement();
			final String querry = "select * from Job ";
			result = stmt.executeQuery(querry);
			while (result.next()) {
				jobdblist.add(result.getString("jobname"));
			}
			result.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobdblist;
	}
}