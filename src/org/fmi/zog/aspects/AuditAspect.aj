package org.fmi.zog.aspects;

public aspect AuditAspect {
	
	pointcut auditUIChanges() : 
		execution(public * org.fmi.zog.presentation.*.*(..));
	
	before() : auditUIChanges() {
		System.out.println("exucuting "+ thisJoinPointStaticPart.getSignature().toString());
	}
}
