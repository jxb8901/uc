/**
 * 
 * created on 2007-6-11
 */
package com.opensymphony.webwork.dispatcher.mapper;

import com.opensymphony.webwork.dispatcher.mapper.DefaultActionMapper;
import com.opensymphony.webwork.dispatcher.mapper.DefaultActionMapper.ParameterAction;

/**
 * 在webwork的映射器上增加处理namespace的能力
 * @author jxb
 */
public class VenusActionMapper extends DefaultActionMapper {

	static {
		prefixTrie.put(ACTION_PREFIX, new ParameterAction() {
			public void execute(String key, ActionMapping mapping) {
				String name = key.substring(ACTION_PREFIX.length());
				int bang = name.indexOf('!');
				if (bang != -1) {
					String method = name.substring(bang + 1);
					mapping.setMethod(method);
					name = name.substring(0, bang);
				}
				/* process namespace */
				int index = name.lastIndexOf("/");
				if (index != -1) {
					String namespace = name.substring(0, index);
					mapping.setNamespace(namespace);
					name = name.substring(index + 1);
				}
				/* end */

				mapping.setName(name);
			}
		});
	}

}
