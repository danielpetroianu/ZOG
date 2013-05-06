package org.fmi.zog.aspects;

import org.apache.log4j.Logger;

/**
 * Aspect with the role of logging every function call
 */
public aspect AuditAspect {
	private static Logger log = Logger.getLogger(AuditAspect.class);
	
	/*
	 * audit UI
	 */
	pointcut auditUIChanges() : 
		execution( * org.fmi.zog.presentation.*.*(..));
	
	
	
	before() : auditUIChanges() {
		log.debug(thisJoinPointStaticPart.getSignature().toString());
	}
}
