package org.fmi.zog.aspects;

import org.apache.log4j.Logger;

/**
 * Aspect with the roll of logging the performance of different actions
 */
public aspect PerformanceAspect {
	private Logger log = Logger.getLogger(PerformanceAspect.class);
	
	pointcut startUp() : 
		execution(* org.fmi.zog.presentation.Zog.create*(..));
	
	
	Object around() : startUp() {
		long start = System.nanoTime();
		Object ret = proceed();
		long end = System.nanoTime();
		
		log.info(thisJoinPointStaticPart.getSignature() + " took " + (end-start)/1000000000.0 + "s");
		
		return ret;
	}
}
