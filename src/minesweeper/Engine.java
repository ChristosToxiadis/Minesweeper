package minesweeper;
import java.awt.Color;
import java.util.*;
import javax.swing.*;

public class Engine {
	int[][] neighbors = new int[8][2];
	int row, col, bombs;
	int sel_row, sel_col;
	int counter = 0, correct_counter;
	int[] map;
	
	public Engine(int rows, int col, int bombs){
		this.row = rows;
		this.col = col;
		this.bombs = bombs;
		map = new int[(row-1)*(col-1)];
		correct_counter = 0;
	}
	
	public void setMap(int map){
		int i;
		for(i = 0; i < counter; i++)
			if(map == this.map[i])
				break;
		if(i == counter)
			this.map[counter++] = map;
	}
	
	public void setCorrect(){
		correct_counter = 0;
	}
	
	public int[] getMap(){
		return map;
	}
	
	public int getCounter(){
		return counter;
	}
	
	public int getCorrect(){
		return correct_counter;
	}
	
	///////Η συνάρτηση τοποθετεί τις νάρκες τυχαία εξαιρώντας το κουμπί που πατήθηκε πρώτο.Επιστρέφει την γραμμή και στήλη της κάθε νάρκης.\\\\\\\
	public int[][] put_bombs(int[] button, JButton[][] ar ,JButton cell){
		find_position(ar,cell);
		Random rand = new Random();
		int[][] bombs = new int[this.bombs][2];
		int[][] array = getNeighboors(ar,cell);//Είναι ο πίνακας γειτόνων που δημιουργεί η getNeighboors(ar,cell) 
		boolean[][] check = new boolean[row][col];//Πίνακας παράλληλος με τον button στην graphs. Αποθηκεύει τις συντεταγμένες για να μην δημιουργηθούν 2 η περισ. φορές\\\\
		int rows, col;
		int j;
		boolean appr = true;
		for(int i = 0; i < this.bombs; i++){//Επαναλαμβάνει την διαδικασία ωσότου να δημιουργηθούν ο επιθυμιτός(προκαθορισμένος) αριθμός βομβών.
			do{
				rows = rand.nextInt(this.row);
				col = rand.nextInt(this.col);
				for(j = 0; j < array.length; j++)
					if(array[j][0] == rows && array[j][1] == col){
						appr = false;//Αν ένας γείτονας έχει τις ίδιες συντεταγμένες η διαδικασία επαναλ.(το appr γινεται false και δεν περνάει την while)
						break;//Με αυτόν τον τρόπο οι βόμβες τοποθετούνται οπουδήποτε εκτός από το κελί που πατήθηκε πρώτο και τους γείτονές του
					}
				if(j == array.length && (array[j-1][0] != rows && array[j-1][1] != col))//Αν δεν έφασε στο break κια τα τελευταία στιοχεία τηρούν την συνθήκη είναι οκ
					appr = true;
			}while(!appr || (rows == button[0] && col == button[1]) || check[rows][col]);//Επαναλαμβάνει όσο επιλέγεται το πρώτο κουμπί ή γειτονες ή ίδιες συντετ.
			bombs[i][0] = rows;
			bombs[i][1] = col;
			check[rows][col] = true;//Τσεκάρει τις συντετ. ώστε να μην δημιουρηθού ίδιες συντεταγμένες για άλλη βόμβα
		}
		return bombs;
	}
	///////Κανονίζει τις συναρτήσεις που θα κληθούν ανάλογα τον τύπο κομπιού που έχει επιλεχθεί: με ή χωρίς γείτονα ή βόμβα
	public int check_button(JButton[][] button, JButton cell){
		find_position(button,cell);
		if(cell.getForeground() == Color.WHITE)
			return -1;
		int count = countNeighboors(button,cell);
		counter = 0;
		if(count > 0)
			return count;
		rec(button,cell);
		return 0;
	}
	///////////Αναδρομική συνάρτηση, καλείται μόνο όταν επιλέγεται κελί χωρίς γείτονα.
	public void rec(JButton[][] button, JButton cell){
		find_position(button,cell);
		int count = countNeighboors(button,cell);
		if(cell.getBackground() == Color.YELLOW)
			return;
		if(cell.getForeground() == Color.WHITE)
			return;
		if(cell.getBackground() == Color.GRAY)
			return;
		int[][] neighboor = getNeighboors(button,cell);
		if(count != 0){
			int map;
			map = (sel_col + 1 > 9) ? (sel_row + 1) * 1000 : (sel_row + 1) * 100;
			map += sel_col + 1;
			map = map * 10 + count;
			setMap(map);
			return;
		}
		cell.setBackground(Color.GRAY);
		correct_counter++;
		for(int i = 0; i < neighboor.length; i++)
			rec(button,button[neighboor[i][0]][neighboor[i][1]]);
	}
	///////////Βρίσκει τις συντεταγμένες του συγκεκριμένου κουμπιού\\\\\\\\\\\\\
	private void find_position(JButton[][] button, JButton cell){
		for(int i = 0; i < row; i++)
			for(int j = 0; j < col; j++)
				if(button[i][j] == cell){
					sel_row = i;
					sel_col = j;
					break;
				}
	}
	////////////Μετράει πόσοι από τους γείτονες του κελιόυ έχουν βόμβα(τουλάχιστον ένας αλλιώς δεν καλείται η συνάρτηση)\\\\\\\\\\\\\\
	public int countNeighboors(JButton[][] button, JButton cell){
		int counter = 0;
		int[][] neighboors = getNeighboors(button,cell);
		for(int i = 0; i < neighboors.length; i++)
			if(button[neighboors[i][0]][neighboors[i][1]].getForeground() == Color.WHITE)
				counter++;
		return counter;
	}
	///////Επιστρέφει 2-διάστατο πίνακα [αριθ.γειτόνων][2] όπου για κάθε γείτονα έχει τις συντεταγμένες\\\\\\\\\\\\\\\\
	private int[][] getNeighboors(JButton[][] button, JButton cell){
		JButton help;
		for(int i = 0; i < neighbors.length; i++)
			for(int j = 0; j < neighbors[i].length; j++)
				neighbors[i][j] = -1;//Αρχικοποιέι των πίνακα των γειτώνων με -1 ώστε να φανεί πόσους γει. έχει. Θα φανεί καθώς όσοι είναι γείτονες θα αλλάξουν τιμή.
		int counter = 0;
		
		try{
			help = button[sel_row-1][sel_col-1];
			neighbors[counter][0] = sel_row - 1;
			neighbors[counter][1] = sel_col - 1;
			counter++;
		}
		catch(Exception ex){/*System.out.println(ex.toString() + " " + ex.getMessage() + "!1");*/}

		try{
			help = button[sel_row-1][sel_col];
			neighbors[counter][0] = sel_row - 1;
			neighbors[counter][1] = sel_col;
			counter++;
		}
		catch(Exception ex){/*System.out.println(ex.toString() + " " + ex.getMessage() + "!1");*/}
		
		try{
			help = button[sel_row-1][sel_col+1];
			neighbors[counter][0] = sel_row - 1;
			neighbors[counter][1] = sel_col + 1;
			counter++;
		}
		catch(Exception ex){/*System.out.println(ex.toString() + " " + ex.getMessage() + "!1");*/}
		
		try{
			help = button[sel_row][sel_col-1];
			neighbors[counter][0] = sel_row;
			neighbors[counter][1] = sel_col - 1;
			counter++;
		}
		catch(Exception ex){/*System.out.println(ex.toString() + " " + ex.getMessage() + "!1");*/}
		
		try{
			help = button[sel_row][sel_col+1];
			neighbors[counter][0] = sel_row;
			neighbors[counter][1] = sel_col + 1;
			counter++;
		}
		catch(Exception ex){/*System.out.println(ex.toString() + " " + ex.getMessage() + "!1");*/}
		
		try{
			help = button[sel_row+1][sel_col-1];
			neighbors[counter][0] = sel_row + 1;
			neighbors[counter][1] = sel_col - 1;
			counter++;
		}
		catch(Exception ex){/*System.out.println(ex.toString() + " " + ex.getMessage() + "!1");*/}
		
		try{
			help = button[sel_row+1][sel_col];
			neighbors[counter][0] = sel_row + 1;
			neighbors[counter][1] = sel_col;
			counter++;
		}
		catch(Exception ex){/*System.out.println(ex.toString() + " " + ex.getMessage() + "!1");*/}
		
		try{
			help = button[sel_row+1][sel_col+1];
			neighbors[counter][0] = sel_row + 1;
			neighbors[counter][1] = sel_col + 1;
			counter++;
		}
		catch(Exception ex){/*System.out.println(ex.toString() + " " + ex.getMessage() + "!1");*/}
		int[][] array = new int[counter][2];
		counter = 0;
		for(int i = 0; i < 8; i++)
			if(neighbors[i][0] >= 0 && neighbors[i][1] >= 0){
				array[counter][0] = neighbors[i][0];
				array[counter++][1] = neighbors[i][1];
			}
		return array;
	}
	
	public void game_over(){
		row = col = bombs = sel_row = sel_col = counter = 0;
	}
	
}