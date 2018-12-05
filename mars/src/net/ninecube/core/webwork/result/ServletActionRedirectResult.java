/**
 * 
 * created on 2007-1-25
 */
package net.ninecube.core.webwork.result;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.webwork.dispatcher.ServletRedirectResult;
import com.opensymphony.webwork.dispatcher.mapper.ActionMapper;
import com.opensymphony.webwork.dispatcher.mapper.ActionMapperFactory;
import com.opensymphony.webwork.dispatcher.mapper.ActionMapping;
import com.opensymphony.webwork.views.util.UrlHelper;
import com.opensymphony.xwork.ActionInvocation;

/**
 * 
 * @author jxb
 * 
 */
public class ServletActionRedirectResult extends ServletRedirectResult {
	
	private static final long serialVersionUID = -6126889518132056284L;

	public static final String DEFAULT_PARAM = "actionName";

    protected String actionName;
    protected String namespace;
    protected String method;
    
    protected List prohibitedResultParam = Arrays.asList(new String[] { 
    		DEFAULT_PARAM, "namespace", "method", "encode", "parse", "location", 
    		"prependServletContext" });

    public void execute(ActionInvocation invocation) throws Exception {
        actionName = conditionalParse(actionName, invocation);
        if (namespace == null) {
            namespace = invocation.getProxy().getNamespace();
        } else {
            namespace = conditionalParse(namespace, invocation);
        }
        if (method == null) {
        	method = "";
        }
        else {
        	method = conditionalParse(method, invocation);
        }
        
        Map requestParameters = new HashMap();
        /* 下面的resultConfig可能会是空指针！
        ResultConfig resultConfig = (ResultConfig) invocation.getProxy().getConfig().getResults().get(
        		invocation.getResultCode());
        Map resultConfigParams = resultConfig.getParams();
        for (Iterator i = resultConfigParams.entrySet().iterator(); i.hasNext(); ) {
        	Map.Entry e = (Map.Entry) i.next();
        	if (! prohibitedResultParam.contains(e.getKey())) {
        		requestParameters.put(e.getKey().toString(), 
        				e.getValue() == null ? "": 
        					conditionalParse(e.getValue().toString(), invocation));
        	}
        }
        */
        
        ActionMapper mapper = ActionMapperFactory.getMapper();
        StringBuffer tmpLocation = new StringBuffer(mapper.getUriFromActionMapping(new ActionMapping(actionName, namespace, method, null)));
        UrlHelper.buildParametersString(requestParameters, tmpLocation, "&");
        
        location = tmpLocation.toString();

        super.execute(invocation);
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setMethod(String method) {
    	this.method = method;
    }
}
