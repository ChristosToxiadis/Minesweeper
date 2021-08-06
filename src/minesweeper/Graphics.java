package minesweeper;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Graphics extends JPanel{
	JButton[][] button;
	int rows, col;
	String[] numbers = {"one","two","three","four","five","six","seven","eight"};
	
	public Graphics(int rows, int col){
		System.out.println(rows + " and " + col);
		this.rows = rows;
		this.col = col;
		setPreferredSize(new Dimension(830,705));
		setLayout(new GridLayout(rows,col));
		button = new JButton[rows][col];
		for(int i = 0; i < rows; i ++)
			for(int j = 0; j < col; j++){
				button[i][j] = new JButton();
				button[i][j].setFocusable(false);
				add(button[i][j]);
			}
	}
	
	public void setMouseListener(MouseListener ev){
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < col; j++)
				button[i][j].addMouseListener(ev);
	}
	//////Τοποθετεί σημαία και βάφει το χρώμα μόνο αν δεν έχει ανοιχτεί ήδη το κελί. Σε περίπτωση που υπάρχει σημαία αφαιρεί αυτην και το χρώμα\\\\\\
	public void setFlag(JButton button){
		if(button.getBackground() == Color.YELLOW){
			button.setBackground(new JButton().getBackground());
			button.setIcon(null);
			return;
		}
		if(button.getBackground() != new JButton().getBackground())
			return;
		button.setBackground(Color.YELLOW);
		Image g = new ImageIcon(this.getClass().getResource("/pics/flag.png")).getImage();
		button.setIcon(new ImageIcon(g));
	}
	
	public void setForeground(int[][] bombs){//Αυτή η συνάρτηση βάφει το foreground των κουμπίων που έχουν νάρκη έτσι ώστε να αναγνωρίζονται αμέσως
		for(int i = 0;  i < bombs.length; i++) //ο πίνκας bombs έχει γραμμές όσες κι οι νάρκες και 2 στήλες: μία για την γραμμή του κουμπιού και μία για την στήλη
			button[bombs[i][0]][bombs[i][1]].setForeground(Color.WHITE);
	}
	
	public int[] get_first_button_clicked(JButton button){// Αυτή η συνάρτηση επιστρέφει την γραμμή και στήλη του κουμπιού που πατήθηκε πρώτο
		int[] array = new int[2];
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < col; j++)
				if(this.button[i][j] == button){
					array[0] = i;
					array[1] = j;
					return array;
				}
		return array;
	}
	
	public JButton[][] getJButton(){
		return button;
	}
	//////Καλείται όταν πατιέται το δεξί κλικ. Επιστρέφει 1 αν δεν έχει ανοιχτεί 0 σε αντίθετη περίπτωση\\\\\\\\\\\\\\\\\\\\\\\\\
	public int paint(JButton cell, int num){
		if(cell.getBackground() == new JButton().getBackground()){
			cell.setBackground(Color.GRAY);
			Image icon = new ImageIcon(this.getClass().getResource("/pics/"+numbers[num-1]+".png")).getImage();
			cell.setIcon(new ImageIcon(icon));
			return 1;
		}
		else
			return 0;
	}
	
	public void open(int[] map, int num){
		int row, col, type;
		for(int i = 0; i < num; i++){
			row = col = type = 0;
			type = map[i] % 10;
			map[i] /= 10;
			col = (map[i] % 10) - 1;
			map[i] /= 10;
			while(map[i] % 10 != 0){
				row = (map[i] % 10) * 10 + col;
				col = row;
				map[i] /= 10;
			}
			map[i] /= 10;
			row = --map[i];
			paint(button[row][col],type);
		}
	}
	
	public void game_over(){
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < col; j++)
				if(button[i][j].getForeground() == Color.WHITE){
					Image icon = new ImageIcon(this.getClass().getResource("/pics/bomb.png")).getImage();
					button[i][j].setBackground(Color.RED);
					button[i][j].setIcon(new ImageIcon(icon));
				}
	}
	
	public void setDefault(){
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < col; j++){
				button[i][j].setBackground(new JButton().getBackground());
				button[i][j].setForeground(new JButton().getForeground());
				button[i][j].setIcon(null);
			}
	}
	
}