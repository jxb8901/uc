package net.ninecube.util;

import junit.framework.TestCase;

/**
 * @author calvin
 */
public class GenericsUtilsTest extends TestCase {
    public void testGetGenericClass() {
        assertEquals(null, GenericsUtils.getGenericClass(TestGenericsBean.class));
        assertEquals(TestBean.class, GenericsUtils.getGenericClass(TestActualGenericsBean.class));
        assertEquals(TestBean2.class, GenericsUtils.getGenericClass(TestActualGenericsBean.class, 1));
        
        assertNull(GenericsUtils.getGenericClass(new TestGenericsBean<TestBean, TestBean2>().getClass()));
    }

    public class TestGenericsBean<T, T2> {

    }

    public class TestBean {

    }

    public class TestBean2 {

    }

    public class TestActualGenericsBean extends TestGenericsBean<TestBean, TestBean2> {

    }
}
