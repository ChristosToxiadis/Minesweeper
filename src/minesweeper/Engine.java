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
	
	///////� ��������� ��������� ��� ������ ������ ���������� �� ������ ��� �������� �����.���������� ��� ������ ��� ����� ��� ���� ������.\\\\\\\
	public int[][] put_bombs(int[] button, JButton[][] ar ,JButton cell){
		find_position(ar,cell);
		Random rand = new Random();
		int[][] bombs = new int[this.bombs][2];
		int[][] array = getNeighboors(ar,cell);//����� � ������� �������� ��� ���������� � getNeighboors(ar,cell) 
		boolean[][] check = new boolean[row][col];//������� ���������� �� ��� button ���� graphs. ���������� ��� ������������� ��� �� ��� ������������� 2 � �����. �����\\\\
		int rows, col;
		int j;
		boolean appr = true;
		for(int i = 0; i < this.bombs; i++){//������������� ��� ���������� ������ �� ������������� � ����������(���������������) ������� ������.
			do{
				rows = rand.nextInt(this.row);
				col = rand.nextInt(this.col);
				for(j = 0; j < array.length; j++)
					if(array[j][0] == rows && array[j][1] == col){
						appr = false;//�� ���� �������� ���� ��� ����� ������������� � ���������� ������.(�� appr ������� false ��� ��� ������� ��� while)
						break;//�� ����� ��� ����� �� ������ ������������� ���������� ����� ��� �� ���� ��� �������� ����� ��� ���� �������� ���
					}
				if(j == array.length && (array[j-1][0] != rows && array[j-1][1] != col))//�� ��� ����� ��� break ��� �� ��������� �������� ������ ��� ������� ����� ��
					appr = true;
			}while(!appr || (rows == button[0] && col == button[1]) || check[rows][col]);//������������� ��� ���������� �� ����� ������ � �������� � ����� ������.
			bombs[i][0] = rows;
			bombs[i][1] = col;
			check[rows][col] = true;//�������� ��� ������. ���� �� ��� ����������� ����� ������������� ��� ���� �����
		}
		return bombs;
	}
	///////��������� ��� ����������� ��� �� ������� ������� ��� ���� ������� ��� ���� ���������: �� � ����� ������� � �����
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
	///////////���������� ���������, �������� ���� ���� ���������� ���� ����� �������.
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
	///////////������� ��� ������������� ��� ������������� ��������\\\\\\\\\\\\\
	private void find_position(JButton[][] button, JButton cell){
		for(int i = 0; i < row; i++)
			for(int j = 0; j < col; j++)
				if(button[i][j] == cell){
					sel_row = i;
					sel_col = j;
					break;
				}
	}
	////////////������� ����� ��� ���� �������� ��� ������ ����� �����(����������� ���� ������ ��� �������� � ���������)\\\\\\\\\\\\\\
	public int countNeighboors(JButton[][] button, JButton cell){
		int counter = 0;
		int[][] neighboors = getNeighboors(button,cell);
		for(int i = 0; i < neighboors.length; i++)
			if(button[neighboors[i][0]][neighboors[i][1]].getForeground() == Color.WHITE)
				counter++;
		return counter;
	}
	///////���������� 2-�������� ������ [����.��������][2] ���� ��� ���� ������� ���� ��� �������������\\\\\\\\\\\\\\\\
	private int[][] getNeighboors(JButton[][] button, JButton cell){
		JButton help;
		for(int i = 0; i < neighbors.length; i++)
			for(int j = 0; j < neighbors[i].length; j++)
				neighbors[i][j] = -1;//����������� ��� ������ ��� �������� �� -1 ���� �� ����� ������ ���. ����. �� ����� ����� ���� ����� �������� �� �������� ����.
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