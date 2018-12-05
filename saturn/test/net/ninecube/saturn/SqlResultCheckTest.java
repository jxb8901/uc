package net.ninecube.saturn;

import java.util.ArrayList;
import java.util.List;

import net.ninecube.db.DBUtil;
import net.ninecube.test.DBTestCase;

public class SqlResultCheckTest extends DBBaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		consInitData();
	}

	private void consInitData(){
		DBUtil.executeUpdate("delete from POINTCAL ", null);
		for(int i = 0 ; i < 10 ; i++){
			DBUtil.executeUpdate("insert into POINTCAL(CUSTOMERID,POINT) values(" + i + ", " + (100 + i) + ")"
					, null);
		}
	}
	
	public void testCheck() {
		String erst = "POINTCAL\n";
		erst += "CUSTOMERID	POINT\n";
		for(int i = 0 ; i < 10 ; i++)erst += "" + i + "	" + (100 + i) + "\n";
		erst += "";
		SqlResultCheck schecker = new SqlResultCheck(erst);
		schecker.check();
		
	}

}
