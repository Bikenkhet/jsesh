/*
 * Created on 30 sept. 2004 by rosmord
 * This code can be distributed under the Gnu Library Public Licence.
 **/
package jsesh.editor.actions.move;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jsesh.editor.JMDCEditor;
import jsesh.editor.actionsUtils.EditorAction;

/**
 * TODO describe type
 * 
 * @author rosmord
 * 
 */
@SuppressWarnings("serial")
public class GoLeftAction extends EditorAction {

	public GoLeftAction(JMDCEditor editor) {
		super(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (editor.getDrawingSpecifications().getTextDirection().isLeftToRight()) {
			if (editor.getDrawingSpecifications().getTextOrientation()
					.isHorizontal())
				editor.getWorkflow().cursorPrevious();
			else
				editor.getWorkflow().cursorUp();
		} else {
			if (editor.getDrawingSpecifications().getTextOrientation()
					.isHorizontal()) {
				editor.getWorkflow().cursorNext();
			} else {
				editor.getWorkflow().cursorDown();
			}
		}
	}

}