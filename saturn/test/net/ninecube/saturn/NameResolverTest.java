package net.ninecube.saturn;

import net.ninecube.saturn.database.DatabaseManager;
import junit.framework.TestCase;

public class NameResolverTest extends TestCase {
	private NameResolver<Object> nameResolver;
	private Context context;
	
	public void setUp(){
		nameResolver = NameResolverProvider.getDefaultChainResolver();
		context = new Context(DatabaseManager.get());
	}
	
	public void testValidate() {
		this.assertEquals(false, nameResolver.validate(context, "abc"));
		this.assertEquals(null, nameResolver.resolve(context, "abc"));
	}

}
