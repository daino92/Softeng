package online.services.reviewing;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.im.InputContext;
import java.awt.image.ImageProducer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PanelViewReviews extends JPanel {
	private static final long serialVersionUID = 1L;
	private static LevelBar lvlbar;
	private static JEditorPane editorPane;
	private static JLabel lblphone;
	private static JLabel phone;
	private static JButton btnNext;
	private static JButton btnPrevious;
	public static AutocompleteJComboBox comboBox1;
	public static boolean chkpanel = false;
	private transient int meter = 0;
	static ArrayList<String> name = new ArrayList<String>();
	static ArrayList<String> reviews = new ArrayList<String>();
	private static JLabel lblName;
	private static JLabel lblusername;
	protected static String temp;
	private static int reviewsize = 1; //for reviews. line 219
	
	public PanelViewReviews() {
		super();
		MainFrame.gr.selectInputMethod(new Locale("el", "GR"));
		this.setVisible(false);
		this.setBounds(0, 0, 480, 300);
		this.setLayout(null);
		final InputContext greek = InputContext.getInstance();
		greek.selectInputMethod(new Locale("el", "GR"));
		final JLabel label = new JLabel("\u03A0\u03C1\u03BF\u03B2\u03BF\u03BB\u03AE \u0391\u03BE\u03B9\u03BF\u03BB\u03BF\u03B3\u03AE\u03C3\u03B5\u03C9\u03BD \u03A5\u03C0\u03B7\u03C1\u03B5\u03C3\u03B9\u03CE\u03BD");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Times New Roman", Font.BOLD, 14));
		label.setBounds(95, 24, 290, 18);
		add(label);

		final JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent button) {
				MainFrame.textc.setText("");
				cleaner();
				MainFrame.logout();
			}
		});
		btnLogOut.setBounds(350, 255, 95, 23);
		this.add(btnLogOut);

		final ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/images/31g.png"));
		final ImageProducer im_pr = defaultIcon.getImage().getSource();
		final ImageIcon yStar = SelectedImageFilter.makeStarImageIcon(im_pr, 1f, 1f, 0f);
		final List<ImageIcon> list = Arrays.asList(yStar, yStar, yStar, yStar, yStar);
		lvlbar = new LevelBar(defaultIcon, list, 1);
		lvlbar.removeMouseListener(lvlbar);
		lvlbar.removeMouseMotionListener(lvlbar);
		lvlbar.setBounds(35, 53, 110, 26);
		this.add(lvlbar);

		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setBackground(Color.WHITE);
		editorPane.setBounds(35, 124, 410, 109);
		this.add(editorPane);

		comboBox1 = MainFrame.comboBox;
		comboBox1.setBounds(270, 80, 175, 20);
		this.add(comboBox1);

		final JButton btnBack = new JButton("Back");
		btnBack.setBounds(248, 255, 89, 23);
		this.add(btnBack);

		final JLabel lblsearch = new JLabel("\u0391\u03BD\u03B1\u03B6\u03AE\u03C4\u03B7\u03C3\u03B7 \u03A5\u03C0\u03B7\u03C1\u03B5\u03C3\u03AF\u03B1:");
		lblsearch.setBounds(270, 64, 122, 14);
		add(lblsearch);

		lblphone = new JLabel("\u03A4\u03B7\u03BB\u03AD\u03C6\u03C9\u03BD\u03BF \u0395\u03C0\u03B9\u03BA\u03BF\u03B9\u03BD\u03C9\u03BD\u03AF\u03B1\u03C2:");
		lblphone.setBounds(35, 101, 145, 14);
		add(lblphone);

		phone = new JLabel("");
		phone.setBounds(190, 101, 80, 14);
		add(phone);

		btnPrevious = new JButton("Previous");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent button) {
				btnNext.setEnabled(true);
				meter--;
				if (meter == 0) {
					btnPrevious.setEnabled(false);
				}
				lblName.setText(name.get(meter * 3) + " "+ name.get(meter * 3 + 1));
				lblName.setBounds(35, 83, (name.get(meter * 3).length()+ name.get(meter * 3 + 1).length() + 1) * 8, 14);
				lblusername.setText(name.get(meter * 3 + 2));
				editorPane.setText(reviews.get(meter));
			}
		});
		btnPrevious.setBounds(35, 255, 89, 23);
		btnPrevious.setEnabled(false);
		add(btnPrevious);

		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent button) {
				btnPrevious.setEnabled(true);
				meter++;
				if (meter == reviews.size() - 1) {
					btnNext.setEnabled(false);
				}
				lblName.setText(name.get(meter * 3) + " "+ name.get(meter * 3 + 1));
				lblName.setBounds(35, 83, (name.get(meter * 3).length()+ name.get(meter * 3 + 1).length() + 1) * 8, 14);
				lblusername.setText(name.get(meter * 3 + 2));
				editorPane.setText(reviews.get(meter));
			}
		});
		btnNext.setBounds(134, 255, 89, 23);
		btnNext.setEnabled(false);
		add(btnNext);

		lblName = new JLabel("");
		lblName.setBounds(35, 83, 118, 14);
		lblName.setVisible(false);
		add(lblName);

		lblusername = new JLabel("");
		lblusername.setBounds(135, 101, 80, 14);
		lblusername.setVisible(false);
		add(lblusername);

		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent button) {
				MainFrame.textc.setText("");
				back();
			}
		});
	}

	public void lblset() {
		if (MainFrame.getRole() == 0){ 
			lblusername.setVisible(true);lblName.setVisible(true);
			lblphone.setText("Αξιολόγηση από:");phone.setVisible(false);
			lvlbar.setBounds(35, 53, 110, 26);
		}else{
			lblusername.setVisible(false);lblName.setVisible(false);
			lblphone.setText("Τηλέφωνο Επικοινωνίας:");
			lvlbar.setBounds(35, 68, 110, 26);
		}
	}

	protected void back() {
		cleaner();
		MainFrame.panelchange(0);
		this.setVisible(false);
	}

	public static void cleaner() {
		lblName.setText("");
		lblusername.setText("");
		btnNext.setEnabled(false);
		btnPrevious.setEnabled(false);
		name.clear();
		reviews.clear();
		lvlbar.setLevel(-1);
		editorPane.setText("");
		phone.setText("");
	}

	protected static void dbreviews(final String job) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		double star = 0;
		int count = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection(MainFrame.connectionUrl,MainFrame.connectionUser, MainFrame.connectionPass);
			stmt = conn.createStatement();
			final String que = "SELECT phone FROM Job where jobname = '" + job + "';";
			result = stmt.executeQuery(que);
			while (result.next()) {
				phone.setText(result.getString("phone"));
			}
			final String querry = "SELECT Worker.name, Worker.surname, Job.jobname,Job.phone, Reviews.stars, Reviews.username, Reviews.text FROM Reviews INNER JOIN Worker ON Worker.id = Reviews.wid INNER JOIN Job ON Job.id = Reviews.jid AND Job.jobname = '"+ job + "';";
			result = stmt.executeQuery(querry);
			while (result.next()) {
				name.add(result.getString("name"));
				name.add(result.getString("surname"));
				name.add(result.getString("username"));
				star += result.getInt("stars");
				count += 1;
				reviews.add(result.getString("text"));
			}
			result.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!(reviews.isEmpty())) {
			if (reviews.size() > reviewsize) {
				btnNext.setEnabled(true);
			}
			star = Math.round(star / count);
			lblName.setText(name.get(0) + " " + name.get(1));
			lblName.setBounds(35, 83, (name.get(0).length() + name.get(1).length() + 1) * 8, 14);
			lblusername.setText(name.get(2));
			editorPane.setText(reviews.get(0));
			lvlbar.setLevel(((int) star) - 1);
		}
	}
}
