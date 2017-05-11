package minions;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import jason.environment.grid.GridWorldView;

class BuildingView extends GridWorldView {

	private static final long serialVersionUID = 1L;

	public BuildingView(BuildingModel model) {
        super(model, "Building", 600);
        defaultFont = new Font("Arial", Font.BOLD, 18); // change default font
        setVisible(true);
        repaint();
    }

    /** draw application objects */
    @Override
    public void draw(Graphics g, int x, int y, int object) {
        switch (object) {
            case BuildingModel.GARB: drawGarb(g, x, y);  break;
            case BuildingModel.STATION: drawStation(g, x, y); break;
        }
    }

	@Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        String label;
        switch(id) {
        case 0: label = "Kevin"; break;
        case 1: label = "Bob"; break;
        default: label = "Stuart"; break;
        }
        c = Color.yellow;

        super.drawAgent(g, x, y, c, -1);
        g.setColor(Color.black);

        super.drawString(g, x, y, defaultFont, label);
        repaint();
    }
	
    private void drawStation(Graphics g, int x, int y) {
    	g.setColor(Color.blue);
    	g.fillRect(cellSizeW * x, cellSizeH * y, cellSizeW, cellSizeH);
    	g.setColor(Color.white);
        drawString(g, x, y, defaultFont, "Station");
	}

    public void drawGarb(Graphics g, int x, int y) {
    	g.setColor(Color.gray);
    	g.fillRect(cellSizeW * x, cellSizeH * y, cellSizeW, cellSizeH);
        g.setColor(Color.white);
        drawString(g, x, y, defaultFont, "G");
    }

} 
