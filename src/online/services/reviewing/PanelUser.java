package online.services.reviewing;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.SystemColor;

public class PanelUser extends JPanel { 
	private static final long serialVersionUID = 2L; 
	private static JButton btnRegister; 
	private static JButton btnMakeAReview; 
	private static JButton btnSearchReview;
	private static JLabel lblMakeAReview; 
	private static JLabel lblSearchAReview; 

	public PanelUser() { 
		super();
		this.setVisible(false);
		this.setBounds(0, 0, 480, 300);
		this.setLayout(null);

		final JLabel labelx = new JLabel("\u0395\u03C0\u03B9\u03BB\u03AD\u03BE\u03C4\u03B5 \u03BC\u03AF\u03B1 \u03B1\u03C0\u03CC \u03C4\u03B9\u03C2 \u03C0\u03B1\u03C1\u03B1\u03BA\u03AC\u03C4\u03C9 \u03B5\u03BD\u03AD\u03C1\u03B3\u03B5\u03B9\u03B5\u03C2:");
		labelx.setHorizontalAlignment(SwingConstants.CENTER);
		labelx.setFont(new Font("Times New Roman", Font.BOLD, 14));
		labelx.setBounds(105, 25, 270, 17);
		this.add(labelx);

		btnSearchReview = new JButton("");
		btnSearchReview.setBackground(SystemColor.control);
		btnSearchReview.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent button) { 
				review(3);
			}
		});

		btnMakeAReview = new JButton("");
		btnMakeAReview.setBackground(Color.WHITE);
		btnMakeAReview.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent button) {
				review(2);
			}
		});
		btnMakeAReview.setBounds(65, 87, 150, 150);
		btnMakeAReview.setIcon(new ImageIcon(getClass().getResource(
				"/images/evaluation.png")));
		this.add(btnMakeAReview);

		this.add(btnSearchReview);

		final JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent button) { 
				MainFrame.logout();
			}
		});
		btnLogOut.setBounds(350, 255, 95, 23);
		this.add(btnLogOut);

		btnRegister = new JButton("RegisterWorker");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent button) { 
				review(1);
			}
		});
		btnRegister.setBounds(138, 120, 203, 23);
		this.add(btnRegister);

		lblMakeAReview = new JLabel("Make a Review");
		lblMakeAReview.setHorizontalAlignment(SwingConstants.CENTER);
		lblMakeAReview.setBounds(65, 62, 150, 14);
		this.add(lblMakeAReview);

		lblSearchAReview = new JLabel("Search for a Review");
		lblSearchAReview.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearchAReview.setBounds(265, 62, 150, 14);
		this.add(lblSearchAReview);

	}

	public static boolean lblset() {
		switch (MainFrame.getRole()) {
		case 0: {
			btnRegister.setVisible(true);btnRegister.setBounds(138, 86, 203, 23);
			btnMakeAReview.setVisible(false);lblMakeAReview.setVisible(false);
			lblSearchAReview.setVisible(false);
			btnSearchReview.setText("Search for a Review");
			btnSearchReview.setIcon(null);
			btnSearchReview.setBounds(138, 156, 203, 23);
			btnSearchReview.setBackground(null);
			break;
		}
		case 1: {
			btnRegister.setVisible(false);
			btnMakeAReview.setVisible(true);lblMakeAReview.setVisible(true);
			lblSearchAReview.setVisible(true);
			btnSearchReview.setText("");
			btnSearchReview.setIcon(new ImageIcon(PanelUser.class.getResource("/images/search.png")));
			btnSearchReview.setBounds(265, 87, 150, 150);
			btnSearchReview.setBackground(Color.WHITE);
			break;
		}
		default:
			break;
		}
		return true;
	}

	protected void review(final int change) { 
		MainFrame.panelchange(change);
		this.setVisible(false);
	}
}