package fr.vallfeur.ggt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener{

	boolean running = false;
	int mouseX1, mouseY1, mouseX2, mouseY2, S_W = 600, S_H = 600, multiplier = 1;
	Timer timer;
	Color color = Color.green;
	List<Line> lines = new ArrayList<>();
	
	GamePanel() {
		this.setPreferredSize(new Dimension(S_W, S_H));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addMouseListener(new MouseAdapt());
		this.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(e.getWheelRotation() < 0){
					multiplier=multiplier+1;
				}else{
					if(multiplier != 1){
						multiplier=multiplier-1;
					}
				}
			}
		});
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_R){
					color = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
				}
				if(e.getKeyCode() == KeyEvent.VK_S){
					try{
						FileWriter writer = new FileWriter(new File("file.txt"));
						for(Line l : lines){
							writer.write(l.x1+","+l.y1+","+l.x2+","+l.y2+","+l.color.getRed()+","+l.color.getGreen()+","+l.color.getBlue()+"\n");
						}
						writer.close();
					}catch(IOException ex){
						ex.printStackTrace();
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_I){
					try{
						Scanner reader = new Scanner(new File("file.txt"));
						lines.clear();
						while(reader.hasNextLine()){
							String[] line = reader.nextLine().split(",");
							int x1 = Integer.parseInt(line[0]);
							int y1 = Integer.parseInt(line[1]);
							int x2 = Integer.parseInt(line[2]);
							int y2 = Integer.parseInt(line[3]);
							Color color = new Color(Integer.parseInt(line[4]), Integer.parseInt(line[5]), Integer.parseInt(line[6]));
							lines.add(new Line(x1, y1, x2, y2, color));
						}
						reader.close();
					}catch(FileNotFoundException ex){
						ex.printStackTrace();
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_C){
					lines.clear();
				}
				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
					if(lines.size() > 0){
						lines.remove(lines.size()-1);
					}
				}
			}
		});
		startGame();
	}
	
	public void startGame(){
		running = true;
		timer = new Timer(0, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		draw(g);
		
	}
	
	public void draw(Graphics g){
		g.setColor(color);
		
		try{
			g.drawLine(mouseX1*multiplier, mouseY1*multiplier, this.getMousePosition().x, this.getMousePosition().y);
		}catch(NullPointerException ex){}
		
		for(Line l : lines){
			g.setColor(l.color);
			g.drawLine(l.x1*multiplier, l.y1*multiplier, l.x2*multiplier, l.y2*multiplier);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	public class MouseAdapt extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1){
				mouseX1 = e.getX()/multiplier;
				mouseY1 = e.getY()/multiplier;
			}else if(e.getButton() == MouseEvent.BUTTON3){
				mouseX2 = e.getX()/multiplier;
				mouseY2 = e.getY()/multiplier;
				lines.add(new Line(mouseX1, mouseY1, mouseX2, mouseY2, color));
			}else if(e.getButton() == MouseEvent.BUTTON2){
				mouseX2 = e.getX()/multiplier;
				mouseY2 = e.getY()/multiplier;
				lines.add(new Line(mouseX1, mouseY1, mouseX2, mouseY2, color));
				mouseX1 = e.getX()/multiplier;
				mouseY1 = e.getY()/multiplier;
			}

			super.mouseClicked(e);
		}
	}
	
}
