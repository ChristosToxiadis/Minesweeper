package minesweeper;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

public class App extends JFrame implements MouseListener{
	Engine engine;
	Graphics gui;
	int counter;
	boolean first_time = true;
	int rows, col, bombs;
	
	public static void main(String[] args) {
		new App();
	}
	
	
	public App(){
		setTitle("Ναρκαλιευτής");
		Dialog ob = new Dialog(this);
		counter = rows * col - bombs;
		gui = new Graphics(rows,col);
		gui.setMouseListener(this);
		getContentPane().add(gui, BorderLayout.CENTER);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public void mouseClicked(MouseEvent ev) {}
	
	public void mouseEntered(MouseEvent ev) {}
	
	public void mouseExited(MouseEvent ev) {}
	
	public void mousePressed(MouseEvent ev) {}
	
	public void mouseReleased(MouseEvent ev) {
		int value, choice;
		if(ev.getButton() == 1){
			if(first_time){//Αυτος ο κώδικας εκτελείται την πρώτη φορά που πατάει ο χρήστης ένα κουμπί
				int[] ar = gui.get_first_button_clicked((JButton)ev.getComponent());// Επιστρέφει ένα διθέσιο πίνακα με τις συντεταγμένες του κουμπιού που πατήθηκε πρώτο
				engine = new Engine(rows,col,bombs);
				int[][] array = engine.put_bombs(ar,gui.getJButton(),(JButton)ev.getComponent());//Επισρέφει 2-διάστατο πίνακα[αριθ.βομβ][2] όπου για κάθε βόμβα έχει συντετ. 
				gui.setForeground(array);//βάφει το foreground των κελιών με βόμβα άσπρο
				first_time = false;
				value = engine.check_button(gui.getJButton(), (JButton)ev.getComponent());
				gui.open(engine.getMap(), engine.getCounter());
				counter -= engine.getCorrect() + engine.getCounter();
				engine.setCorrect();// μηδενίζει τον μετρητή
			}
			else{
				if(ev.getComponent().getBackground() == Color.YELLOW)//Απαγορέυει το δεξί κλικ σε κελί με σημαία
					return;
				value = engine.check_button(gui.getJButton(), (JButton)ev.getComponent());
				if(value == -1){
					gui.game_over();
					choice = JOptionPane.showConfirmDialog(null, "Τέλος Παιχνιδιού. Θέλετε να ξαναπαίξετε;","ΧΑΣΑΤΕ",JOptionPane.YES_NO_OPTION);
					start_new_game(choice);
				}
				else if(value > 0){
					counter -= gui.paint((JButton)ev.getComponent(), value);
				}
				else{
					gui.open(engine.getMap(), engine.getCounter());
					counter -= engine.getCorrect() + engine.getCounter();
					engine.setCorrect();
				}
				if(counter <= 0) {
					choice = JOptionPane.showConfirmDialog(null, "ΝΙΚΗΣΕΣ!!!!!!! Θέλετε να ξαναπαίξετε;","ΝΙΚΗΣΑΤΕ",JOptionPane.YES_NO_OPTION);
					start_new_game(choice);
				}
				
			}
		}
		else if(ev.getButton() == 3)
			gui.setFlag((JButton)ev.getComponent());
	}
	
	
	private void start_new_game(int choice) {
		if(choice == 1)
			System.exit(0);
		else{
			engine.game_over();
			gui.setDefault();
			counter = rows * col - bombs;
			getContentPane().remove(gui);
			getContentPane().add(gui, BorderLayout.CENTER);
			first_time = true;
		}
	}
	
	
	class NumberLimits extends PlainDocument{
		
		public void insertString(int start, String str, AttributeSet a) throws BadLocationException{
			char[] num = str.toCharArray();
			char[] text = new char[num.length];
			int counter = 0;
			for(int i = 0; i < num.length; i++)
				if(Character.isDigit(num[i]))
					text[counter++] = num[i];
				else 
					java.awt.Toolkit.getDefaultToolkit().beep();
			super.insertString(start, new String(text,0,counter), a);
		}
	}
	
	class Dialog extends JDialog implements ActionListener, FocusListener{
		JPanel panel = new JPanel(new GridLayout(5,1));
		JPanel panel1 = new JPanel(new FlowLayout());
		JPanel panel2 = new JPanel(new FlowLayout());
		JPanel panel3 = new JPanel(new FlowLayout());
		JPanel panel4 = new JPanel(new GridLayout(1,2));
		JPanel panel5 = new JPanel();
		
		JLabel rows_label = new JLabel("Συμπληρώστε το ύψος (9-24)");
		JLabel col_label = new JLabel("Συμπληρώστε το πλάτος (9-30)");
		JLabel bombs_label = new JLabel("Συμπληρώστε τον αριθμό ναρκών");
		JTextField rows_text = new JTextField(new NumberLimits(),"12",8);
		JTextField col_text = new JTextField(new NumberLimits(), "11", 8);
		JTextField bombs_text = new JTextField(new NumberLimits(), "16", 8);
		JButton button_cancel = new JButton("Ακύρωση");
		JButton button_ok = new JButton("ΟΚ");
		
		
		public Dialog(Frame frame){
			super(frame,"ΕΠΙΛΟΓΕΣ",true);
			button_cancel.addActionListener(this);
			button_ok.addActionListener(this);
			button_cancel.setFocusable(false);
			button_ok.setFocusable(false);
			rows_text.addFocusListener(this);
			col_text.addFocusListener(this);
			bombs_text.addFocusListener(this);
			panel1.add(rows_label);
			panel1.add(rows_text);
			panel2.add(col_label);
			panel2.add(col_text);
			panel3.add(bombs_label);
			panel3.add(bombs_text);
			panel4.add(button_cancel);
			panel4.add(button_ok);
			panel.add(panel1);
			panel.add(panel2);
			panel.add(panel3);
			panel.add(panel5);
			panel.add(panel4);
			getContentPane().add(panel, BorderLayout.CENTER);
			setSize(600,500);
			setVisible(true);
		}
		
		public void actionPerformed(ActionEvent ev){
			if(ev.getSource().equals(button_ok)){
				rows = Integer.parseInt(rows_text.getText());
				col = Integer.parseInt(col_text.getText());
				bombs = Integer.parseInt(bombs_text.getText());
			}
			else if(ev.getSource().equals(button_cancel))
				System.exit(0);
			setVisible(false);
		}
		
		public void focusGained(FocusEvent ev) {
			
		}
		
		public void focusLost(FocusEvent ev) {
			if(ev.getSource().equals(rows_text)){
				if(rows_text.getText().length() > 8 || Integer.parseInt(rows_text.getText()) > 24 || rows_text.getText().length()  > 8)
					rows_text.setText("24");
				else if(Integer.parseInt(rows_text.getText()) < 9)
					rows_text.setText("9");
			}
			else if(ev.getSource().equals(col_text)){
				if(col_text.getText().length() > 8 || Integer.parseInt(col_text.getText()) > 30)
					col_text.setText("30");
				else if(Integer.parseInt(col_text.getText()) < 9)
					col_text.setText("9");
			}
			else{
				if(bombs_text.getText().length() > 8 || Integer.parseInt(bombs_text.getText()) > 668)
					bombs_text.setText("668");
				else if(Integer.parseInt(bombs_text.getText()) < 10)
					bombs_text.setText("10");
			}
		}
		
	}
	
}