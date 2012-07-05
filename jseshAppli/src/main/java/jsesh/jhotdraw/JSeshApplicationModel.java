/*
Copyright Serge Rosmorduc
contributor(s) : Serge J. P. Thomas for the fonts
serge.rosmorduc@qenherkhopeshef.org

This software is a computer program whose purpose is to edit ancient egyptian hieroglyphic texts.

This software is governed by the CeCILL license under French law and
abiding by the rules of distribution of free software.  You can  use, 
modify and/ or redistribute the software under the terms of the CeCILL
license as circulated by CEA, CNRS and INRIA at the following URL
"http://www.cecill.info". 

As a counterpart to the access to the source code and  rights to copy,
modify and redistribute granted by the license, users are provided only
with a limited warranty  and the software's author,  the holder of the
economic rights,  and the successive licensors  have only  limited
liability. 

In this respect, the user's attention is drawn to the risks associated
with loading,  using,  modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean  that it is complicated to manipulate,  and  that  also
therefore means  that it is reserved for developers  and  experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards their
requirements in conditions enabling the security of their systems and/or 
data to be ensured and,  more generally, to use and operate it in the 
same conditions as regards security. 

The fact that you are presently reading this means that you have had
knowledge of the CeCILL license and that you accept its terms.
 */
package jsesh.jhotdraw;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.io.File;

import javax.swing.ActionMap;
import javax.swing.JOptionPane;

import jsesh.editor.JMDCEditor;
import jsesh.editor.MDCModelTransferableBroker;
import jsesh.graphics.export.EMFExporter;
import jsesh.graphics.export.EPSExporter;
import jsesh.graphics.export.HTMLExporter;
import jsesh.graphics.export.MacPictExporter;
import jsesh.graphics.export.RTFExportPreferences;
import jsesh.graphics.export.SVGExporter;
import jsesh.graphics.export.WMFExporter;
import jsesh.graphics.export.pdfExport.PDFExportPreferences;
import jsesh.jhotdraw.actions.BundleHelper;
import jsesh.jhotdraw.actions.JSeshApplicationActionsID;
import jsesh.jhotdraw.actions.application.JSeshAboutAction;
import jsesh.jhotdraw.actions.application.JSeshApplicationPreferenceAction;
import jsesh.jhotdraw.actions.edit.InsertShortTextAction;
import jsesh.jhotdraw.actions.edit.JSeshClearSelectionAction;
import jsesh.jhotdraw.actions.edit.JSeshSelectAllAction;
import jsesh.jhotdraw.actions.edit.SelectCopyPasteConfigurationAction;
import jsesh.jhotdraw.actions.file.ApplyModelAction;
import jsesh.jhotdraw.actions.file.EditDocumentPreferencesAction;
import jsesh.jhotdraw.actions.file.ExportAsBitmapAction;
import jsesh.jhotdraw.actions.file.ExportAsHTMLAction;
import jsesh.jhotdraw.actions.file.ExportAsPDFAction;
import jsesh.jhotdraw.actions.file.ExportAsRTFAction;
import jsesh.jhotdraw.actions.file.GenericExportAction;
import jsesh.jhotdraw.actions.file.ImportNewSignAction;
import jsesh.jhotdraw.actions.file.ImportPDFAction;
import jsesh.jhotdraw.actions.file.ImportRTFAction;
import jsesh.jhotdraw.actions.file.QuickPDFExportAction;
import jsesh.jhotdraw.actions.file.QuickPDFSelectExportFolderAction;
import jsesh.jhotdraw.actions.file.SetAsModelAction;
import jsesh.jhotdraw.actions.text.EditGroupAction;
import jsesh.jhotdraw.actions.text.InsertElementAction;
import jsesh.jhotdraw.actions.windows.ToggleGlyphPaletteAction;
import jsesh.jhotdraw.applicationPreferences.model.ExportPreferences;
import jsesh.jhotdraw.applicationPreferences.model.FontInfo;
import jsesh.jhotdraw.applicationPreferences.ui.ApplicationPreferencesPresenter;
import jsesh.mdc.constants.SymbolCodes;
import jsesh.mdc.model.TopItemList;
import jsesh.mdcDisplayer.clipboard.JSeshPasteFlavors;
import jsesh.mdcDisplayer.clipboard.MDCClipboardPreferences;
import jsesh.mdcDisplayer.clipboard.MDCModelTransferable;
import jsesh.mdcDisplayer.preferences.DrawingSpecification;
import jsesh.swing.signPalette.HieroglyphPaletteListener;
import jsesh.swing.signPalette.PalettePresenter;

import org.jhotdraw_7_6.app.Application;
import org.jhotdraw_7_6.app.DefaultApplicationModel;
import org.jhotdraw_7_6.app.MenuBuilder;
import org.jhotdraw_7_6.app.View;
import org.jhotdraw_7_6.app.action.app.AboutAction;
import org.jhotdraw_7_6.app.action.app.OpenApplicationFileAction;
import org.jhotdraw_7_6.app.action.edit.ClearSelectionAction;
import org.jhotdraw_7_6.app.action.edit.CopyAction;
import org.jhotdraw_7_6.app.action.edit.CutAction;
import org.jhotdraw_7_6.app.action.edit.DeleteAction;
import org.jhotdraw_7_6.app.action.edit.DuplicateAction;
import org.jhotdraw_7_6.app.action.edit.PasteAction;
import org.jhotdraw_7_6.app.action.edit.SelectAllAction;
import org.jhotdraw_7_6.gui.JFileURIChooser;
import org.jhotdraw_7_6.gui.URIChooser;
import org.jhotdraw_7_6.gui.filechooser.ExtensionFileFilter;
import org.qenherkhopeshef.jhotdrawChanges.ActiveViewAwareApplication;
import org.qenherkhopeshef.swingUtils.portableFileDialog.FileExtensionFilter;

/**
 * JHotdraw-specific model for the application.
 * <p>
 * We have decided not to follow Apple guidelines for the "no-document" window,
 * that is, Only the menus relevant to document creation are proposed there.
 * </p>
 * TODO for Windows (check for mac) : ensure that the palette is correctly displayed, in sync with its menu.
 * 
 * TODO check consistency and file export system in particular. TODO before
 * 
 * LATER....
 * 
 * TODO decide something about drawingspecifications, mutability, etc...
 * currently it's too complex and not coherent. Use a builder of the complex
 * immutable objects, as the current copy/change system might be expansive in
 * some cases.
 * 
 * Improve the import sign dialog... the "ok" button is misleading.
 * 
 * TODO improve the hieroglyphic menu system... should use a regular button, not
 * a bad-looking out-of-place toolbar (may wait a little)
 * 
 * TODO after release : fix the import/export/file reading to use proper threads
 * and display... prevent actions in the JMDCEditor widget when not in the EDT.
 * 
 * TODO check uses of JFileChooser, and replace when needed by
 * PortableFileDialog (in particular in exports).
 * 
 * Add to the "text menu" : center vertically/horizontally (will insert stuff
 * around sign)
 * 
 * @author Serge Rosmorduc
 * 
 */
@SuppressWarnings("serial")
public class JSeshApplicationModel extends DefaultApplicationModel {

	// View and Help Menu. Tools (insert new Sign ?)
	
	/**
	 * Prefix for action names which insert a symbol with a SymbolCode. Should
	 * move in some other class.
	 */
	public static final String INSERT_CODE = "INSERT_CODE_";

	/**
	 * Some actions require a knowledge of the application...
	 */
	private Application application;

	/**
	 * Everything which is a) application-level and b) non specific to JHotdraw
	 * is delegated to {@link JSeshApplicationBase}.
	 */
	private JSeshApplicationBase jseshBase = new JSeshApplicationBase();

	/**
	 * Deals with copy/paste. Needs to know the current view to work correctly.
	 */
	private MyTransferableBroker transferableBroker = new MyTransferableBroker();

	private PalettePresenter palettePresenter;
	
	@Override
	public void initApplication(Application a) {
		super.initApplication(a);
		this.application = a;
		((ActiveViewAwareApplication)a).initSecondaryWindow(palettePresenter.getDialog());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jhotdraw_7_6.app.DefaultApplicationModel#initView(org.jhotdraw_7_6
	 * .app.Application, org.jhotdraw_7_6.app.View)
	 */
	@Override
	public void initView(Application a, View v) {
		super.initView(a, v);
		DrawingSpecification drawingSpecifications = jseshBase
				.getDefaultDrawingSpecifications();
		JSeshView jSeshView = (JSeshView) v;
		jSeshView.setDrawingSpecifications(drawingSpecifications);
		jSeshView.setMDCModelTransferableBroker(transferableBroker);
		System.out.println(getFontInfo());
		jSeshView.setFontInfo(getFontInfo());
	}

	

//	@Override
//	public List<JToolBar> createToolBars(Application a, View p) {
//		List<JToolBar> toolbars = new ArrayList<JToolBar>();
//		JToolBar hoolbar = new JToolBar("hieroglyphs");
//		hoolbar.add(createHieroglyphicPalette());
//		// toolbars.add(hoolbar);
//		toolbars.add(hoolbar);
//		return toolbars;
//	}

	/*
	 * Note that createActionMap is in the application model and not in the view
	 * model, because for Mac OS X, all actions are supposed to be created, even
	 * if there is no view.
	 * 
	 * <p> We don't follow this exactly. Currently, some actions are fetched
	 * from our editor object, which doesn't follow the JHotdraw framework.
	 * Anyway, if we want to respect the system, we will need to create an
	 * action factory for the JMDCEditor object. The action factory will be able
	 * to create actions for both an existing editor and a null one. </p>
	 * (non-Javadoc)
	 * 
	 * @see org.jhotdraw_7_6.app.DefaultApplicationModel#createActionMap(org.
	 * jhotdraw_7_4_1.app.Application, org.jhotdraw_7_6.app.View)
	 */
	public ActionMap createActionMap(Application a, View v) {
		JMDCEditor editor = null;
		JSeshView jseshView = (JSeshView) v;
		if (jseshView != null) {
			editor = jseshView.getEditor();
		}

		ActionMap map = super.createActionMap(a, v);

		if (v == null) {
			// Application-level actions
			// Only on mac ?
			map.put(OpenApplicationFileAction.ID,
					new OpenApplicationFileAction(a));
			map.put(JSeshApplicationPreferenceAction.ID,
					new JSeshApplicationPreferenceAction(a));
			map.put(AboutAction.ID, new JSeshAboutAction(a));
			map.put(ImportPDFAction.ID, new ImportPDFAction(a));
			map.put(ImportRTFAction.ID, new ImportRTFAction(a));
			map.put(ImportNewSignAction.ID, new ImportNewSignAction(a));
			// palette ...

			palettePresenter = new PalettePresenter();
			palettePresenter.setHieroglyphPaletteListener(new MyHieroglyphicPaletteListener());

			map.put(ToggleGlyphPaletteAction.ID, new ToggleGlyphPaletteAction(a, palettePresenter.getDialog(), null));

			map.remove(DuplicateAction.ID);
			map.remove(DeleteAction.ID);
			map.remove(CopyAction.ID);
			map.remove(CutAction.ID);
			map.remove(PasteAction.ID);

			map.remove(SelectAllAction.ID);
			map.remove(ClearSelectionAction.ID);

		} else {
			map.put(SelectAllAction.ID, new JSeshSelectAllAction(a, jseshView));
			map.put(ClearSelectionAction.ID, new JSeshClearSelectionAction(a, jseshView));
			// View level actions
			map.put(ExportAsBitmapAction.ID, new ExportAsBitmapAction(a, v));
			map.put(EditDocumentPreferencesAction.ID,
					new EditDocumentPreferencesAction(a, v));
			map.put(SetAsModelAction.ID, new SetAsModelAction(a, v));
			map.put(ApplyModelAction.ID, new ApplyModelAction(a, jseshView));
			map.put(JSeshApplicationActionsID.EXPORT_WMF,
					new GenericExportAction(a, jseshView, new WMFExporter(),
							JSeshApplicationActionsID.EXPORT_WMF));

			map.put(JSeshApplicationActionsID.EXPORT_EMF,
					new GenericExportAction(a, jseshView, new EMFExporter(),
							JSeshApplicationActionsID.EXPORT_EMF));

			map.put(JSeshApplicationActionsID.EXPORT_MACPICT,
					new GenericExportAction(a, jseshView,
							new MacPictExporter(),
							JSeshApplicationActionsID.EXPORT_MACPICT));

			map.put(JSeshApplicationActionsID.EXPORT_SVG,
					new GenericExportAction(a, jseshView, new SVGExporter(),
							JSeshApplicationActionsID.EXPORT_SVG));

			map.put(JSeshApplicationActionsID.EXPORT_EPS,
					new GenericExportAction(a, jseshView, new EPSExporter(),
							JSeshApplicationActionsID.EXPORT_EPS));

			map.put(ExportAsPDFAction.ID, new ExportAsPDFAction(a, jseshView));
			map.put(ExportAsRTFAction.ID, new ExportAsRTFAction(a, jseshView));
			map.put(ExportAsHTMLAction.ID, new ExportAsHTMLAction(a, jseshView));
			map.put(QuickPDFExportAction.ID, new QuickPDFExportAction(a,
					jseshView));
			map.put(QuickPDFSelectExportFolderAction.ID,
					new QuickPDFSelectExportFolderAction(a));

			map.put(InsertShortTextAction.ID, new InsertShortTextAction(a, v));

			for (SelectCopyPasteConfigurationAction action : SelectCopyPasteConfigurationAction
					.buildActions(a, jseshView)) {
				map.put(action.getID(), action);
			}

			map.put(EditGroupAction.ID, new EditGroupAction(editor));

			addInsertAction(map, a, v,
					JSeshApplicationActionsID.INSERT_FULL_SHADING,
					SymbolCodes.FULLSHADE);
			addInsertAction(map, a, v,
					JSeshApplicationActionsID.INSERT_HORIZONTAL_SHADING,
					SymbolCodes.HORIZONTALSHADE);
			addInsertAction(map, a, v,
					JSeshApplicationActionsID.INSERT_VERTICAL_SHADING,
					SymbolCodes.VERTICALSHADE);
			addInsertAction(map, a, v,
					JSeshApplicationActionsID.INSERT_QUARTER_SHADING,
					SymbolCodes.QUATERSHADE);

			// Ecdotic signs. Codes from 100 to 113.
			for (int i = 100; i <= 113; i++) {
				map.put(INSERT_CODE + i, InsertElementAction
						.buildInsertElementActionWithIcon(a, jseshView,
								INSERT_CODE + i, i));
			}
		}
		return map;
	}

	@Override
	protected MenuBuilder createMenuBuilder() {
		return new JSeshMenuBuilder();
	}

	private void addInsertAction(ActionMap map, Application a, View jseshView,
			String id, int code) {
		map.put(id, new InsertElementAction(a, jseshView, id, code));
	}

	@Override
	public URIChooser createOpenChooser(Application a, View v) {
		JFileURIChooser chooser = new JFileURIChooser();
		String description = BundleHelper.getInstance().getLabel(
				"file.glyphFile.text");
//		chooser.addChoosableFileFilter(new ExtensionFileFilter(description,
//				"gly"));
		String pdfDescription = BundleHelper.getInstance().getLabel(
				"file.pdfFile.text");

		chooser.addChoosableFileFilter(new FileExtensionFilter(new String[] {"pdf"}, pdfDescription) );
		chooser.addChoosableFileFilter(new FileExtensionFilter(new String[] {"gly", "hie"}, description) );
		return chooser;
	}

	@Override
	public URIChooser createSaveChooser(Application a, View v) {
		JFileURIChooser chooser = new JFileURIChooser();

		String pdfDescription = BundleHelper.getInstance().getLabel(
				"file.pdfFile.text");

		String description = BundleHelper.getInstance().getLabel(
				"file.glyphFile.text");
		chooser.addChoosableFileFilter(new FileExtensionFilter(new String[] {"pdf"}, pdfDescription) );

		chooser.addChoosableFileFilter(new ExtensionFileFilter(description,
				"gly"));
		return chooser;
	}

	private class MyHieroglyphicPaletteListener implements
			HieroglyphPaletteListener {

		public void signSelected(String code) {
			if (application.getActiveView() != null
					&& application.getActiveView() instanceof JSeshView) {
				JSeshView currentView = (JSeshView) application.getActiveView();
				currentView.insertCode(code);
			} else {
				System.err.println(application.getActiveView());
			}
		}
	}

	/**
	 * @param configurationNumber
	 * @see jsesh.jhotdraw.JSeshApplicationBase#selectCopyPasteConfiguration(int)
	 */
	public void selectCopyPasteConfiguration(ExportType exportType) {
		jseshBase.selectCopyPasteConfiguration(exportType);
	}

	public File getCurrentDirectory() {
		return jseshBase.getCurrentDirectory();
	}

	public void setCurrentDirectory(File directory) {
		if (!directory.isDirectory())
			throw new RuntimeException("Bug : " + directory.getAbsolutePath()
					+ " should be a directory");
		jseshBase.setCurrentDirectory(directory);
	}

	public PDFExportPreferences getPDFExportPreferences() {
		return jseshBase.getPDFExportPreferences();
	}

	public RTFExportPreferences getRTFExportPreferences(ExportType exportType) {
		return jseshBase.getRTFExportPreferences(exportType);
	}

	public HTMLExporter getHTMLExporter() {
		return jseshBase.getHTMLExporter();
	}

	public File getQuickPDFExportFolder() {
		return jseshBase.getQuickPDFExportFolder();
	}

	public void setQuickPDFExportFolder(File folder) {
		jseshBase.setQuickPDFExportFolder(folder);
	}

	public void setMessage(String string) {
		JSeshView view = (JSeshView) application.getActiveView();
		if (view != null) {
			view.setMessage(string);
		}
	}

	/**
	 * Display the preferences dialog.
	 */
	public void showPreferencesEditor() {
		ApplicationPreferencesPresenter applicationPreferencesPresenter = new ApplicationPreferencesPresenter();
		Component parentComponent = null;
		View activeView = application.getActiveView();
		if (activeView!= null) 
			parentComponent= 	activeView.getComponent();
		applicationPreferencesPresenter.loadPreferences(this);
		if (applicationPreferencesPresenter.showDialog(parentComponent) == JOptionPane.OK_OPTION) {
			applicationPreferencesPresenter.updatePreferences(this);
		}
	}

	public FontInfo getFontInfo() {
		return jseshBase.getFontInfo();
	}

	/**
	 * Change the fonts used by JSesh.
	 * 
	 * @param fontInfo
	 */
	public void setFontInfo(FontInfo fontInfo) {
		jseshBase.setFontInfo(fontInfo);
		for (View v : application.views()) {
			JSeshView view = (JSeshView) v;
			view.setFontInfo(fontInfo);
		}
	}

	public MDCClipboardPreferences getClipboardPreferences() {
		return jseshBase.getClipboardPreferences();
	}

	public void setClipboardPreferences(MDCClipboardPreferences prefs) {
		jseshBase.setClipboardPreferences(prefs);
	}

	/**
	 * Returns a copy of the current export preferences.
	 * 
	 * @return
	 */
	public ExportPreferences getExportPreferences() {
		return jseshBase.getExportPreferences();
	}

	public void setExportPreferences(ExportPreferences exportPreferences) {
		jseshBase.setExportPreferences(exportPreferences);
	}

	@Override
	public void destroyApplication(Application a) {
		palettePresenter.getDialog().savePreferences();
		jseshBase.savePreferences();
	}

	private class MyTransferableBroker implements MDCModelTransferableBroker {
		public MDCModelTransferable buildTransferable(TopItemList top) {
			return buildTransferable(top,
					JSeshPasteFlavors
							.getTransferDataFlavors(getClipboardPreferences()));

		}

		public MDCModelTransferable buildTransferable(TopItemList top,
				DataFlavor[] dataFlavors) {

			MDCModelTransferable result = new MDCModelTransferable(dataFlavors,
					top);
			DrawingSpecification currentDrawingSpecifications = ((JSeshView) application
					.getActiveView()).getDrawingSpecifications();
			result.setDrawingSpecifications(currentDrawingSpecifications);
			result.setRtfPreferences(jseshBase.getCurrentRTFPreferences());
			result.setClipboardPreferences(jseshBase.getClipboardPreferences());
			return result;
		}
	}

	public void setDefaultDrawingSpecifications(
			DrawingSpecification drawingSpecifications) {
		jseshBase.setDefaultDrawingSpecifications(drawingSpecifications);
	}

	/**
	 * Returns a copy of the current default drawing specifications.
	 * 
	 * @return
	 */
	public DrawingSpecification getDefaultDrawingSpecifications() {
		return jseshBase.getDefaultDrawingSpecifications();
	}

}
