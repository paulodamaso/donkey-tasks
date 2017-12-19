package main.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import main.Printable;
import main.PrintableTask;
import main.Task;


/**
 * <p> A {@link Task} modeled like a Post-It sticker.
 * 
 * @author paulodamaso
 *
 */
/* @todo #6 it would be interesting to allow the use to change some features (font, font size, fonr style, backcolor) 
 */
public final class PostItTask extends JDialog implements PrintableTask {
	
	private final JTextArea txtDescription;
	private final Task task;
	
	public PostItTask(Task task) {
		super();
		this.task = task;
		
		//formatting the textarea
		this.txtDescription = new JTextArea(description());
		this.txtDescription.setLineWrap(true);
        Font font = new Font("Segoe Script", Font.BOLD, 20);
        this.txtDescription.setFont(font);
        this.txtDescription.setBackground(new Color(204, 194, 16));
        this.txtDescription.setSize(300, 150);
        this.txtDescription.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        //adding the textarea
        this.getContentPane().add(this.txtDescription, BorderLayout.CENTER);
	}

	@Override
	public void print() {
		if (!this.isVisible()) {
			this.pack();
			this.setVisible(true);
		}
	}

	@Override
	public int id() {
		return task.id();
	}

	@Override
	public String description() {
		return task.description();
	}

}