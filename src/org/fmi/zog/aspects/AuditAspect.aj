package org.fmi.zog.aspects;

public aspect AuditAspect {
	
	pointcut auditUIChanges() :
		call(org.fmi.zog.presentation.* *(..));
	
	before() : auditUIChanges() {
		System.out.println("Log");
	}
}
