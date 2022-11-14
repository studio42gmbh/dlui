// <editor-fold desc="The MIT License" defaultstate="collapsed">
/*
 * The MIT License
 * 
 * Copyright 2022 Studio 42 GmbH ( https://www.s42m.de ).
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
//</editor-fold>
package de.s42.dl.ui.components.button;

import de.s42.log.LogManager;
import de.s42.log.Logger;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 *
 * @author Benjamin Schiller
 */
public class DLButtonUI extends BasicButtonUI
{

	private final static Logger log = LogManager.getLogger(DLButtonUI.class.getName());

	public final static Color ACTIVE_COLOR = new Color(220, 220, 220, 255);
	public final static Color DARK_COLOR = Color.darkGray;

	@Override
	public void installUI(JComponent c)
	{
		super.installUI(c);

		c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void uninstallUI(JComponent c)
	{
		super.uninstallUI(c);
	}
	
	@Override
	public void paint(Graphics g, JComponent c)
	{
		Button button = (Button) c.getClientProperty("DLComponent");
		JButton jButton = (JButton) c;
		ButtonModel model = jButton.getModel();

		String text = jButton.getText();

		Font font = jButton.getFont();

		FontMetrics metrics = c.getFontMetrics(font);

		LineMetrics lineMetrics = metrics.getLineMetrics(text.substring(0, 1), g);
		Rectangle2D strBounds = metrics.getStringBounds(text, g);
		

		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		g.setFont(c.getFont());

		int x = 0;
		int y = 0;
		int w = c.getWidth() - 1;
		int h = c.getHeight() - 1;
		
		//int yOff = (c.getHeight() - (int)strBounds.getHeight()) / 2 + (int)strBounds.getHeight() - (int)lineMetrics.getDescent();
		
		log.debug(lineMetrics.getAscent(), lineMetrics.getDescent(), lineMetrics.getHeight(), c.getHeight());
		
		//int a = (int)lineMetrics.getAscent();
		int yOff = 5 + (int)lineMetrics.getAscent();//(c.getHeight() - a) / 2 + a;
		int xOff = (c.getWidth()- (int)strBounds.getWidth()) / 2;
		
		int tx = x + xOff;
		int ty = y + yOff;

		if (model.isRollover()) {
			g.setColor(ACTIVE_COLOR);
			g.fillRoundRect(x, y, w, h, 30, 30);
			g.setColor(DARK_COLOR);
			g.drawString(text, tx, ty);
		} else {
			g.setColor(ACTIVE_COLOR);
			g2D.setStroke(new BasicStroke(2));
			g.drawRoundRect(x + 1, y + 1, w - 2, h - 2, 30, 30);
			g.drawString(text, tx, ty);
		}
	}
}
