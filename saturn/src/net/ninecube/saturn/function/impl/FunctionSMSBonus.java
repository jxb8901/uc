package net.ninecube.saturn.function.impl;

import java.util.List;
import java.util.Map;

import net.ninecube.saturn.Context;
import net.ninecube.saturn.function.Function;
import net.ninecube.saturn.function.operation.SmsOperation;

public class FunctionSMSBonus implements Function {

	public Object execute(Context context, List indexArgs, Map namedArgs) {
		String msg = (String)indexArgs.get(0);
		SmsOperation smsop = new SmsOperation(msg);
		System.out.println("send sms ....");
		smsop.execute(context);
		return null;
	}

}
