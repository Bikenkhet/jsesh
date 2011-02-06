package jsesh.graphics.export.pdfExport;

import java.awt.Font;
import java.awt.Graphics2D;
import java.io.OutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * utility class representing a pdf document.
 * 
 * @author rosmord
 * 
 */
public class PDFDocumentWriterAux {
	private PDFExportPreferences pdfExportPreferences;
	private Document document;
	private PdfWriter pdfWriter;
	private Font font;

	public PDFDocumentWriterAux(PDFExportPreferences prefs, OutputStream out,
			float docWidth, float docHeight, String comment) {
		this.pdfExportPreferences = prefs;
		document = new Document();
		// Font stuff...
		// BaseFont bf= null;
		// Build fonts to use
		// try {
		// bf = BaseFont.createFont("/jseshResources/fonts/MDCTranslitLC.ttf",
		// BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		// } catch (DocumentException e1) {
		// // Just ignore and continue. It's not a huge deal...
		// e1.printStackTrace();
		// }
		/*
		 * translitFont = new Font(bf, 12); romanFont =
		 * FontFactory.getFont(FontFactory.TIMES, 12, Font.NORMAL); talicFont =
		 * FontFactory .getFont(FontFactory.TIMES, 12, Font.ITALIC); boldFont =
		 * FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD);
		 */
		DefaultFontMapper fontMapper = new DefaultFontMapper();
		
		//FontFactory.registerDirectories();
		//font = new java.awt.Font("times-roman", Font.PLAIN, 12);

                fontMapper.putName("MDCTranslitLC",
				new DefaultFontMapper.BaseFontParameters(
						"/jseshResources/fonts/MDCTranslitLC.ttf"));

                
		//FontFactory.register("Serif");
		// for (Iterator i = FontFactory.getRegisteredFonts().iterator(); i
		// .hasNext();) {
		// System.out.println(i.next());
		// }
		document.setPageSize(new Rectangle(docWidth, docHeight));

		try {
			pdfWriter = PdfWriter.getInstance(document, out);
		} catch (DocumentException e) {
			// Should not happen.
			throw new RuntimeException(e);
		}
		// The position of this call is important.
		setHeader(comment);
	}

	private void setHeader(String comment) {
		document.addTitle(pdfExportPreferences.getTitle());
		document.addAuthor(pdfExportPreferences.getAuthor());
		//document.addSubject(pdfExportPreferences.getSubject());
		document.addKeywords(pdfExportPreferences.getKeywords());
		// A specific field might be a solution. Explore to see if it works everywhere.
		//document.addHeader("MDC", comment);
		document.addCreator("JSesh");
		document.addSubject(comment);
	}

	public PdfWriter getPdfWriter() {
		return pdfWriter;
	}

	public Document getDocument() {
		return document;
	}

	public void open() {
		document.open();
	}

	public void close() {
		document.close();
	}
	
	public Graphics2D createGraphics(float width, float height) {
		return pdfWriter.getDirectContent().createGraphicsShapes(
				width, height);
	}

	public Font getFont() {
		return font;
	}
}