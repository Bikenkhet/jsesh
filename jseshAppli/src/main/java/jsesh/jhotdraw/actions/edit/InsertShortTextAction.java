package jsesh.jhotdraw.actions.edit;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import jsesh.jhotdraw.viewClass.JSeshView;
import jsesh.jhotdraw.actions.BundleHelper;

import org.jhotdraw_7_6.app.Application;
import org.jhotdraw_7_6.app.View;
import org.jhotdraw_7_6.app.action.AbstractViewAction;

@SuppressWarnings("serial")
public class InsertShortTextAction extends AbstractViewAction {

    public static final String ID = "edit.insertShortText";

    public InsertShortTextAction(Application app, View view) {
        super(app, view);
        BundleHelper.getInstance().configure(this);
    }

    public void actionPerformed(ActionEvent e) {
        JSeshView v = (JSeshView) getActiveView();
        if (v != null) {
            String s = JOptionPane.showInputDialog(v.getComponent(), "Enter short text");
            if (s != null && !"".equals(s)) {
                String protectedText = s.replaceAll("\\\\", "\\\\");
                protectedText = protectedText.replaceAll("\"", "\\\\\"");
                v.insertMDC("\"" + protectedText + "\"");
            }
        }
    }
}
