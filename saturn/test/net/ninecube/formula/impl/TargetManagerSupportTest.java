/**
 * 
 * created on 2007-5-26
 */
package net.ninecube.formula.impl;

import java.util.Arrays;
import java.util.List;

import net.ninecube.formula.Target;
import net.ninecube.formula.impl.TargetManagerSupport.TargetEntity;
import net.ninecube.formula.impl.TargetManagerSupport.TypedTargetManager;
import junit.framework.TestCase;

/**
 * 
 * @author jxb
 */
public class TargetManagerSupportTest extends TestCase {

	public void testGetTargetsByType() {
		TargetManagerSupport tm = new TargetManagerSupport(){
			public Target createTarget(Long id, String name, String type, String rule) {
				return new DefaultTarget(id, name, type, rule, null);
			}
		};
		tm.setManagers(Arrays.asList(new TestTypedTargetManager()));
		List<Target> ts = tm.getTargetsByType(Target.TARGETTYPE_ALL);
		this.assertEquals(1, ts.size());
		this.assertEquals(TestTypedTargetManager.type, ts.get(0).getType());
	}
	
	private static class TestTypedTargetManager implements TypedTargetManager {
		private static final String type = "type_test_target";
		public TargetEntity getTarget(Long id) {
			return null;
		}
		public List<TargetEntity> getTargets() {
			TargetEntity target = new TargetEntity();
			target.setId(1L);
			target.setName("test");
			target.setRule("");
			return Arrays.asList(target);
		}
		public String getType() {
			return type;
		}
	}
}
