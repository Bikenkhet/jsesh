package org.qenherkhopeshef.guiFramework;

import javax.swing.KeyStroke;
import javax.swing.UIDefaults;

/**
 * Application defaults.
 * <p>
 * from <em>Asserting Control Over the GUI: Commands, Defaults, and Resource Bundles</em>
 * by Hans Muller
 * 
 */
public class AppDefaults extends UIDefaults {
	
	private static final long serialVersionUID = 7289206292792859095L;

	public KeyStroke getKeyStroke(String key) {
		return KeyStroke.getKeyStroke(getString(key));
	}

	public Integer getKeyCode(String key) {
		KeyStroke ks = getKeyStroke(key);
		return (ks != null) ? ks.getKeyCode() : null;
	}
}