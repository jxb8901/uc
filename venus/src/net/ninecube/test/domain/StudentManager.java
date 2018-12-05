package net.ninecube.test.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.ninecube.core.domain.AbstractEntityManager;
import net.ninecube.venus.domain.CustomerGroup;
import net.ninecube.venus.domain.CustomerGroupManager;

public class StudentManager extends AbstractEntityManager<Student> {
	private static final Log log = LogFactory.getLog(StudentManager.class);

	@Override
	protected Student onSave(Student cg) {
		log.debug("****" + cg.getClassmate());
		return super.onSave(cg);
	}

}
