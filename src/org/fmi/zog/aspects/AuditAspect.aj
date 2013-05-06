package org.fmi.zog.aspects;

import org.apache.log4j.Logger;

public aspect AuditAspect {
	
	private static Logger log = Logger.getLogger(AuditAspect.class);
	
	pointcut auditUIChanges() : 
		execution( * org.fmi.zog.presentation.*.*(..));
	
	before() : auditUIChanges() {
		log.info(thisJoinPointStaticPart.getSignature().toString());
	}
}
