package online.services.reviewing;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.im.InputContext;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainFrame {
	protected static JFrame frame;
	protected static InputContext gr = InputContext.getInstance();
	protected static String username;
	private static int role = -1;
	protected static int select = 0;
	protected static JTextComponent textc;
	protected static String connectionUrl = "jdbc:mysql://83.212.100.24:3306/softeng?useUnicode=true&characterEncoding=UTF-8";
	protected static String connectionUser = "softeng";
	protected static String connectionPass = "softeng";
	protected static JPanel panellogin = new PanelLogin();
	protected static List<String> jobdblist = new ArrayList<String>();
	protected static StringSearchable searchable = new StringSearchable(((PanelLogin) panellogin).dbjobs(jobdblist));
	protected static AutocompleteJComboBox comboBox = new AutocompleteJComboBox(searchable) {
		private static final long serialVersionUID = 1L;
		@Override
		public InputContext getInputContext() {
			return gr;
		};
	};
	protected static JPanel paneluser = new PanelUser();
	protected static JPanel panelregister = new PanelRegister();
	protected static JPanel panelviewreviews = new PanelViewReviews();
	protected static JPanel panelmakereviews = new PanelMakeReviews();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainFrame();
					MainFrame.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public MainFrame() {
		initialize();
	}

	private boolean initialize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame = new JFrame("Online Σύστημα Αξιολόγησης Υπηρεσιών");
		frame.setResizable(false);
		frame.setBounds(screenSize.width / 2 - screenSize.width / 4,screenSize.height / 2 - screenSize.height / 4, 500, 340);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panellogin, BorderLayout.CENTER);
		frame.getContentPane().add(paneluser, BorderLayout.CENTER);
		frame.getContentPane().add(panelviewreviews, BorderLayout.CENTER);
		frame.getContentPane().add(panelmakereviews, BorderLayout.CENTER);
		textc.setText("η");textc.setText("");
		return true;
	}

	protected static void panelchange(int num) {

		switch (num) {
		case 0:paneluser.setVisible(true);break;
		case 1:
			panelregister = new PanelRegister();
			frame.getContentPane().add(panelregister, BorderLayout.CENTER);
			((PanelRegister) panelregister).lblset();
			panelregister.setVisible(true);
			select = num;
			break;
		case 2:panelmakereviews.setVisible(true);break;
		case 3:
			panelviewreviews = new PanelViewReviews();
			frame.getContentPane().add(panelviewreviews, BorderLayout.CENTER);
			((PanelViewReviews) panelviewreviews).lblset();
			panelviewreviews.setVisible(true);
			select = num;
			break;
		default:break;
		}
	}

	protected static void logout() {
		username = "";
		setRole(-1);
		paneluser.setVisible(false);
		panelregister.setVisible(false);
		panelmakereviews.setVisible(false);
		panelviewreviews.setVisible(false);
		panellogin.setVisible(true);
	}

	public static int getRole() {
		return role;
	}

	public static void setRole(int role) {
		MainFrame.role = role;
	}
}

class LevelBar extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private final int gap;
	protected final List<ImageIcon> iconList;
	protected final List<JLabel> labelList = Arrays.asList(new JLabel(),new JLabel(), new JLabel(), new JLabel(), new JLabel());
	protected final ImageIcon defaultIcon;
	private int clicked = -1;

	public LevelBar(ImageIcon defaultIcon, List<ImageIcon> list, int gap) {
		super(new GridLayout(1, 5, gap * 2, gap * 2));
		this.defaultIcon = defaultIcon;
		this.iconList = list;
		this.gap = gap;
		for (JLabel l : labelList) {
			l.setIcon(defaultIcon);
			add(l);
		}
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void clear() {
		clicked = -1;
		repaintIcon(clicked);
	}

	public int getLevel() {
		return clicked;
	}

	public void setLevel(int l) {
		clicked = l;
		repaintIcon(clicked);
	}

	private int getSelectedIconIndex(Point p) {
		for (int i = 0; i < labelList.size(); i++) {
			Rectangle r = labelList.get(i).getBounds();
			r.grow(gap, gap);
			if (r.contains(p)) {
				return i;
			}
		}
		return -1;
	}

	protected void repaintIcon(int index) {
		for (int i = 0; i < labelList.size(); i++) {
			labelList.get(i).setIcon(i <= index ? iconList.get(i) : defaultIcon);
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {repaintIcon(getSelectedIconIndex(e.getPoint()));}
	@Override
	public void mouseEntered(MouseEvent e) {repaintIcon(getSelectedIconIndex(e.getPoint()));}
	@Override
	public void mouseClicked(MouseEvent e) {clicked = getSelectedIconIndex(e.getPoint());}
	@Override
	public void mouseExited(MouseEvent e) {repaintIcon(clicked);}
	@Override
	public void mouseDragged(MouseEvent e) { /* not needed */}
	@Override
	public void mousePressed(MouseEvent e) { /* not needed */}
	@Override
	public void mouseReleased(MouseEvent e) { /* not needed */}
}

class SelectedImageFilter extends RGBImageFilter {
	private final float rf, gf, bf;

	public SelectedImageFilter(float rf, float gf, float bf) {
		super();
		this.rf = Math.min(1f, rf);
		this.gf = Math.min(1f, gf);
		this.bf = Math.min(1f, bf);
		canFilterIndexColorModel = false;
	}

	@Override
	public int filterRGB(int x, int y, int argb) {
		int r = (int) (((argb >> 16) & 0xff) * rf);
		int g = (int) (((argb >> 8) & 0xff) * gf);
		int b = (int) (((argb) & 0xff) * bf);
		return (argb & 0xff000000) | (r << 16) | (g << 8) | (b);
	}

	public static ImageIcon makeStarImageIcon(ImageProducer ip, float rf,float gf, float bf) {
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(ip, new SelectedImageFilter(rf,gf, bf))));
	}
}

interface Searchable<E, V> {
	public Collection<E> search(V value);
}

class StringSearchable implements Searchable<String, String> {
	private List<String> terms = new ArrayList<String>();

	public StringSearchable(List<String> terms) {
		this.terms.addAll(terms);
	}

	@Override
	public Collection<String> search(String value) {
		List<String> founds = new ArrayList<String>();
		for (String s : terms) {
			if (s.indexOf(value) == 0) {
				founds.add(s);
			}
		}
		return founds;
	}
}

class AutocompleteJComboBox extends JComboBox<Object> {
	private static final long serialVersionUID = 1L;
	private boolean temp=false;
	public AutocompleteJComboBox(Searchable<String, String> s) {
		super();
		setEditable(true);
		Component c = getEditor().getEditorComponent();
		if (c instanceof JTextComponent) {
			MainFrame.textc = (JTextComponent) c;
			MainFrame.textc.getDocument().addDocumentListener(
					new DocumentListener() {
						@Override
						public void changedUpdate(DocumentEvent arg0) {
						}
						@Override
						public void insertUpdate(DocumentEvent arg0) {
							update();
						}
						@Override
						public void removeUpdate(DocumentEvent arg0) {
							update();
						}

						public void update() {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									temp=false;
									List<String> founds = new ArrayList<String>(MainFrame.searchable.search(MainFrame.textc.getText()));
									Set<String> foundSet = new HashSet<String>();
									for (String s : founds) {
										foundSet.add(s.toLowerCase());
									}
									Collections.sort(founds);// sort alphabetically
									setEditable(false);
									removeAllItems();
									if (!foundSet.contains(MainFrame.textc.getText().toLowerCase())) {
										addItem(MainFrame.textc.getText());
									}
									for (String s : founds) {
										if (s.matches(MainFrame.textc.getText())) {
											temp = true;
											if (MainFrame.select == 1) {
												PanelRegister.temp = MainFrame.textc.getText();
											} else if (MainFrame.select == 3) {
												PanelViewReviews.cleaner();
												PanelViewReviews.dbreviews(MainFrame.textc.getText());// database call
											}
										}
										addItem(s);
									}
									setEditable(true);
									if (MainFrame.textc.getText().length() > 0 && temp==false) {
										setPopupVisible(true);
									} else {
										setPopupVisible(false);
									}
									MainFrame.textc.requestFocus();
								}
							});
						}
					});
			MainFrame.textc.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent arg0) {
					if (MainFrame.textc.getText().length() > 0 && temp==false) {
						setPopupVisible(true);
					} else {
						setPopupVisible(false);
					}
				}
				@Override
				public void focusLost(FocusEvent arg0) {
				}
			});
		} else {
			throw new IllegalStateException("Editing component is not a JTextComponent!");
		}
	}
}