package online.services.reviewing;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JEditorPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageProducer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PanelMakeReviews extends JPanel {
	private static final long serialVersionUID = 1L;
	private JEditorPane editorPane;
	private LevelBar lvlbar;
	public static JTextField codetxt;

	public PanelMakeReviews() {
		this.setVisible(false);
		this.setBounds(0, 0, 480, 300);
		this.setLayout(null);

		JLabel label = new JLabel("\u0391\u03BE\u03B9\u03BF\u03BB\u03CC\u03B3\u03B7\u03C3\u03B7 \u03A5\u03C0\u03B7\u03C1\u03B5\u03C3\u03AF\u03B1\u03C2");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Times New Roman", Font.BOLD, 14));
		label.setBounds(157, 25, 165, 17);
		add(label);

		final JLabel label_1 = new JLabel("\u0393\u03C1\u03AC\u03C8\u03C4\u03B5 \u03C4\u03B1 \u03C3\u03C7\u03CC\u03BB\u03B9\u03B1 \u03C3\u03B1\u03C2");
		label_1.setBounds(35, 99, 143, 14);
		add(label_1);

		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.setBounds(350, 255, 95, 23);
		this.add(btnLogOut);

		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(35, 255, 89, 23);
		this.add(btnSubmit);

		editorPane = new JEditorPane();
		editorPane.setBackground(Color.WHITE);
		editorPane.setBounds(35, 124, 410, 109);
		this.add(editorPane);

		JButton btnBack = new JButton("Back");
		btnBack.setBounds(248, 255, 89, 23);
		this.add(btnBack);

		ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/images/31g.png"));
		ImageProducer ip = defaultIcon.getImage().getSource();
		ImageIcon yStar = SelectedImageFilter.makeStarImageIcon(ip, 1f, 1f, 0f);
		List<ImageIcon> list = Arrays.asList(yStar, yStar, yStar, yStar, yStar);
		lvlbar = new LevelBar(defaultIcon, list, 1);
		lvlbar.setBounds(165, 93, 110, 26);
		this.add(lvlbar);

		codetxt = new JTextField();
		codetxt.setBounds(100, 62, 47, 20);
		((AbstractDocument) codetxt.getDocument())
				.setDocumentFilter(new NumOnly(6));
		this.add(codetxt);

		JLabel lblId = new JLabel("\u039A\u03C9\u03B4\u03B9\u03BA\u03CC ID");
		lblId.setBounds(35, 65, 68, 14);
		add(lblId);

		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				back();
			}
		});

		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				codetxt.setText("");
				lvlbar.setLevel(-1);
				editorPane.setText("");
				MainFrame.logout();
			}
		});

		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeReview();
			}
		});

	}

	protected void back() {
		codetxt.setText("");
		lvlbar.setLevel(-1);
		editorPane.setText("");
		MainFrame.panelchange(0);
		this.setVisible(false);
	}

	public boolean existcheck(int jid, int wid) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		boolean checker = false;
		int num = -1;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection(MainFrame.connectionUrl,MainFrame.connectionUser, MainFrame.connectionPass);
			stmt = conn.createStatement();
			String querry = "select count(*) from Worker where id='" + wid+ "' and jid='" + jid + "';";
			rs = stmt.executeQuery(querry);

			while (rs.next()) {
				num = rs.getInt(1);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (num > 0) {
			checker = true;
		} else {
			checker = false;
		}
		return checker;
	}

	protected void makeReview() {
		String reviewtxt = editorPane.getText();
		if (reviewtxt.length() > 0 && !(Pattern.matches("\\s{2,}", reviewtxt))&& lvlbar.getLevel() > -1 && codetxt.getText().length() > 5) {
			int jid = Integer.parseInt(codetxt.getText().substring(0, 3));
			int wid = Integer.parseInt(codetxt.getText().substring(3, 6));
			if (!(existcheck(jid, wid))) {
				codetxt.setText("");
				JOptionPane.showMessageDialog(MainFrame.frame,"Code ID doesn't Exist", "Code ID Error",JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					Connection conn = null;
					ResultSet rs = null;
					PreparedStatement ps = null;
					PreparedStatement pstmt = null;
					int id = 0;
					Class.forName("com.mysql.jdbc.Driver");
					
					conn = DriverManager.getConnection(MainFrame.connectionUrl,MainFrame.connectionUser, MainFrame.connectionPass);
					pstmt = conn.prepareStatement("select count(*) from Reviews");
					rs = pstmt.executeQuery();
					if (rs.next()) {
						id = rs.getInt(1);
					}
					pstmt.close();
					id++;
					ps = conn.prepareStatement("insert into Reviews values(?,?,?,?,?,?)");
					
					ps.setInt(1, id);
					ps.setInt(2, jid);
					ps.setInt(3, wid);
					ps.setInt(4, lvlbar.getLevel() + 1);
					ps.setString(5, MainFrame.username);
					ps.setString(6, reviewtxt);
					
					ps.executeUpdate();
					
					JOptionPane.showMessageDialog(MainFrame.frame,"The Review is successfully inserted!");
					
					codetxt.setText("");
					editorPane.setText("");
					lvlbar.setLevel(-1);
					
					ps.close();
					rs.close();
					conn.close();
				} catch (Exception e1) {
					e1.printStackTrace();;
				}
			}
		} else {
			if (codetxt.getText().length() < 6) {
				JOptionPane.showMessageDialog(MainFrame.frame,"Code ID must be 6 digits", "Code ID",JOptionPane.WARNING_MESSAGE);
			} else if (lvlbar.getLevel() == -1) {
				JOptionPane.showMessageDialog(MainFrame.frame,"Please give a Star Rate", "Star Rate",JOptionPane.WARNING_MESSAGE);
			} else if (!(Pattern.matches("^[a-zA-Z][0-9]+$", reviewtxt))) {
				editorPane.setText("");
				JOptionPane.showMessageDialog(MainFrame.frame,"Please leave a comment", "Comment Field",JOptionPane.WARNING_MESSAGE);
			}
		}
	}
}

class NumOnly extends DocumentFilter {
	private int num;

	public NumOnly(int i) {
		num = i;
	}

	private int len(String text) {
		int lengt = text.length();
		return lengt;
	}

	@Override
	public void replace(FilterBypass fb, int i, int i1, String string,
			AttributeSet as) throws BadLocationException {
		if (string == "")
			super.replace(fb, i, i1, String.valueOf(""), as);
		else {
			for (int n = string.length(); n > 0; n--) {
				char c = string.charAt(n - 1);
				// System.out.println(c);
				if (Character.isDigit(c) && len(PanelMakeReviews.codetxt.getText()) < num) {
					super.replace(fb, i, i1, String.valueOf(c), as);
				}
			}
		}
	}
}