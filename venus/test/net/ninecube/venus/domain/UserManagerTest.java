package net.ninecube.venus.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

/**
 * @author  Fox
 */
public class UserManagerTest extends AbstractEntityManagerTestCase {
	private UserManager um;

	public void testCRUD() {
		User cg = new User();
		cg.setIdno(99L);
		cg.setName("test group");
		cg.setCreationDate(new Date());
		um.save(cg);
		this.assertNotSame(new Integer(99), cg.getIdno());

		this.assertEquals(1, um.getAll().size());
		this.assertEquals(1, super.countRowsInTable("TUser"));
		
		cg = um.getById(cg.getIdno());
		this.assertNotNull(cg);
		this.assertEquals("test group", cg.getName());
		
		um.delete(cg);
		this.assertEquals(0, um.getAll().size());
		this.assertEquals(0, super.countRowsInTable("TUser"));
	}
	
	public void testRole(){
		User cg = new User();
		cg.setIdno(99L);
		cg.setName("test group");
		cg.setCreationDate(new Date());
		Role r = new Role();
		r.setName("CustomerGroup");
		r.setPermissions(Arrays.asList(new String[]{"add" , "delete"}));
		cg.setRoles(Arrays.asList(new Role[]{r}));
		um.save(cg);
		//super.setComplete();
		this.assertEquals(1,cg.getRoles().size());
		
	}
	
	public void setUserManager(UserManager cgm) {
		this.um = cgm;
	}

}
