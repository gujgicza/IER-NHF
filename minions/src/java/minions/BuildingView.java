package minions;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import jason.environment.grid.GridWorldView;

class BuildingView extends GridWorldView {

	private static final long serialVersionUID = 1L;

	public BuildingView(final BuildingModel model) {
        super(model, "Building", 600);
        defaultFont = new Font("Arial", Font.BOLD, 18); // change default font
        setVisible(true);
        repaint();
	}
	
	public void addClickListener(final Building building) {
        
		getCanvas().addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int col = e.getX() / cellSizeW;
				int lin = e.getY() / cellSizeH;
				if (col >= 0 && lin >= 0 && col < getModel().getWidth() && lin < getModel().getHeight()) {
					building.addGarbage(col, lin);
					update(col, lin);
					System.out.println("Added garbage: " + col + lin);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
	}
	
    /** draw application objects */
    @Override
    public void draw(Graphics g, int x, int y, int object) {
        switch (object) {
            case BuildingModel.GARB: drawGarb(g, x, y);  break;
            case BuildingModel.STATION: drawStation(g, x, y); break;
        }
        //repaint();
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
        //repaint();
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
