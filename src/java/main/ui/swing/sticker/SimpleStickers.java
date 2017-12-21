package main.ui.swing.sticker;

import java.util.ArrayList;

import main.Task;
import main.Tasks;

/**
 * <p> Basic implementation for {@link Stickers}.
 * 
 * @author paulodamaso
 *
 */
public class SimpleStickers implements Stickers {
	
	private final Tasks tasks;
	private final ArrayList<Sticker> stickers; 

	public SimpleStickers(Tasks tasks) {
		this.tasks = tasks;
		this.stickers = new ArrayList<Sticker>();
		for (Task task : tasks.iterate()) {
			stickers.add(new Sticker(task));
		}
	}

	@Override
	public void print() {
		for (Sticker stk : iterate()) {
			stk.print();
		}
	}

	@Override
	public Iterable<Sticker> iterate() {
		return stickers;
	}

	@Override
	public Sticker add(String task) {
		return new Sticker(tasks.add(task));
	}

}