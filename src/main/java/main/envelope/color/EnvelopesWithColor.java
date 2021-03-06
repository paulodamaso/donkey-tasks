package main.envelope.color;

import java.util.Collection;

import main.envelope.Envelope;
import main.envelope.Envelopes;

/**
 * <p> Decorated collection of {@link Envelope} which can retrieve color data when available.
 * 
 * @author paulodamaso
 *
 */
public interface EnvelopesWithColor extends Envelopes {

	/**
	 * <p> Retrieves all {@link Envelope} instances with color data from this collection.
	 * 
	 * @return
	 */
	public Collection<EnvelopeWithColor> iterateInColor();
	
	/**
	 * <p> Retrieves the collection being decorated by this {@link EnvelopesWithColor} object.
	 * 
	 * @return
	 */
	public Envelopes origin();
}
