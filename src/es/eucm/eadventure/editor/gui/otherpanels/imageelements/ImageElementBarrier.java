package es.eucm.eadventure.editor.gui.otherpanels.imageelements;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import es.eucm.eadventure.editor.control.controllers.scene.BarrierDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.ElementReferenceDataControl;

public class ImageElementBarrier extends ImageElement {

	private BarrierDataControl barrierDataControl;
	
	public ImageElementBarrier(BarrierDataControl barrierDataControl) {
		this.barrierDataControl = barrierDataControl;
		image = new BufferedImage(barrierDataControl.getWidth(), barrierDataControl.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		fillImage();
	}
	
	private void fillImage() {
		Graphics2D g = (Graphics2D) image.getGraphics();
		AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
		g.setComposite(alphaComposite);
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, image.getWidth(null), image.getHeight(null));
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, image.getWidth(null) - 1, image.getHeight(null) - 1);
	}
	
	@Override
	public void changePosition(int x, int y) {
		int width = barrierDataControl.getWidth();
		int height = barrierDataControl.getHeight();
		barrierDataControl.setBarrier(x - width / 2, y - height, width, height);
	}

	@Override
	public ElementReferenceDataControl getElementReferenceDataControl() {
		return null;
	}

	@Override
	public int getLayer() {
		return -1;
	}

	@Override
	public float getScale() {
		return 1.0f;
	}

	@Override
	public int getX() {
		return barrierDataControl.getX() + barrierDataControl.getWidth() / 2;
	}

	@Override
	public int getY() {
		return barrierDataControl.getY() + barrierDataControl.getHeight();
	}

	@Override
	public void recreateImage() {
		image = new BufferedImage(barrierDataControl.getWidth(), barrierDataControl.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		fillImage();
	}

	@Override
	public void setScale(float scale) {
	}

	@Override
	public boolean canRescale() {
		return false;
	}

	@Override
	public boolean canResize() {
		return true;
	}

	@Override
	public void changeSize(int width, int height) {
		int x = barrierDataControl.getX();
		int y = barrierDataControl.getY();
		barrierDataControl.setBarrier(x, y, width, height);
	}

	@Override
	public int getHeight() {
		return barrierDataControl.getHeight();
	}

	@Override
	public int getWidth() {
		return barrierDataControl.getWidth();
	}

	@Override
	public boolean transparentPoint(int x, int y) {
		return false;
	}

}
