package cs.ualberta.ca.tuneintest;

import android.test.ActivityInstrumentationTestCase2;
import cs.ualberta.ca.tunein.MainActivity;
import cs.ualberta.ca.tunein.Cache;

public class cacheTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	public cacheTest() {
		super(MainActivity.class);
	}
	
	protected void setUp() throws Exception {
		 super.setUp();
	}
	
	public void cacheInstanceTest() {
		//test if the cached comments are saved in the physical storage of the device
	}
	
	@Override
	protected void tearDown() throws Exception {
	    super.tearDown();
	}

}