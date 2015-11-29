package testunit;

import static org.junit.Assert.*;
import online.services.reviewing.MainFrame;
import online.services.reviewing.PanelLogin;
import online.services.reviewing.PanelRegister;
import online.services.reviewing.PanelUser;
import online.services.reviewing.PanelMakeReviews;

import org.junit.Test;

public class MainFrameTest {

	@Test
	public void logintestUser() {
		PanelLogin tester= new PanelLogin();
		assertEquals(true, tester.logincheck("user", "user"));
	}
	
	@Test
	public void logintestAdmin() {
		PanelLogin tester= new PanelLogin();
		assertEquals(true, tester.logincheck("test", "test"));
	}
	
	@Test
	public void logintestFalse() {
		PanelLogin tester= new PanelLogin();
		assertEquals(false, tester.logincheck("adfa", "asdff"));
	}
	
	@Test
	public void registerusercheck() {
		PanelRegister tester= new PanelRegister();
		assertEquals(true, tester.usercheck("user"));
		assertEquals(false, tester.usercheck("lala"));
	}
	
	@Test
	public void panelusercheck() {
		MainFrame add = new MainFrame();
		add.setRole(0);
		PanelUser tester= new PanelUser();
		assertEquals(true, tester.lblset());
	}
	
	@Test
	public void makereviewidcheckexist() {
		PanelMakeReviews tester= new PanelMakeReviews();
		assertEquals(true, tester.existcheck(1, 1));
	}
	
	@Test
	public void makereviewidchecknotexist() {
		PanelMakeReviews tester= new PanelMakeReviews();
		assertEquals(false, tester.existcheck(20, 30));
	}
}