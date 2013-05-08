package org.fmi.zog.aspects;

import java.awt.event.ActionEvent;

import javax.swing.JToggleButton;

import org.apache.log4j.Logger;
import org.fmi.zog.presentation.Zog;

/**
 * Aspect with the roll of enforcing validations. 
 */
public aspect BusinessRulesAspect {
	private Logger log = Logger.getLogger(BusinessRulesAspect.class);

	
	pointcut toolsAction(ActionEvent e) :
		execution(* org.fmi.zog.presentation.Zog.actionPerformed(..)) &&
		args(e);
	
	Object around(ActionEvent e) : toolsAction(e) {
		if(!(e.getSource() instanceof JToggleButton)) {
			return proceed(e);
		}
		
		if (Zog.paint != null 
		&& (Zog.paint.tabPanel.getTabCount() > 0 || Zog.paint.tabPanel.getSelectedIndex() != -1)) {
			return proceed(e);
		}
		
		
		String buttonName = "unknown";
		JToggleButton buttonClicked = (JToggleButton)e.getSource();
		if(buttonClicked != null) {
			buttonName = buttonClicked.getToolTipText();
			buttonClicked.setSelected(false);
		}
		
		log.error("Cannot use this Paint Tool("+ buttonName +"). Because no tab is selected.");
		return proceed(null);
	}
	
}
