package main.envelope;

import java.util.ArrayList;
import java.util.Collection;

import main.note.Note;
import main.note.Notes;

/**
 * <p> Basic {@link Envelopes} implementation.
 * 
 * @author paulodamaso
 *
 */
public final class SimpleEnvelopes implements Envelopes {

	private final Notes origin; 
	
	public SimpleEnvelopes(Notes origin) {
		this.origin = origin;
	}

	@Override
	public Collection<Envelope> iterate() {
		ArrayList<Envelope> ret = new ArrayList<Envelope>();
		for (Note note : origin.notes()) {
			ret.add(new SimpleEnvelope(note));
		}
		return ret;
	}

	@Override
	public Envelope add(String note) {
		return new SimpleEnvelope(origin.add(note));
	}
}
