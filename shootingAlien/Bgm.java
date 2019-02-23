package shootingAlien;

import javax.sound.midi.*;
import java.io.*;

public class Bgm
{
	private static final Bgm bg = new Bgm();
	
	private final String folderPath = "bgm/";
	private String fileName;
	private Sequence seq;
	private Sequencer seqr;
	
	private Bgm ()
	{
		this.fileName = null;
		this.seq = null;
		this.seqr = null;
	}
	
	public static Bgm getInstance()
	{
		return bg;
	}
	
	public void setBGM (String fileName)
	{
		this.fileName = fileName;
	}
	
	public void start()
	{
		try
		{
			seq = MidiSystem.getSequence(this.getClass().getResource(folderPath + fileName));
			seqr = MidiSystem.getSequencer();
			
			seqr.open();
			seqr.setSequence(seq);
			seqr.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			
			/*
				set VOLUME
				if (sequencer instanceof Synthesizer) 
				{
					Synthesizer synthesizer = (Synthesizer)sequencer;
					MidiChannel[] channels = synthesizer.getChannels();

					// gain is a value between 0 and 1 (loudest)
					double gain = 0.9D;
					for (int i=0; i<channels.length; i++) 
					{
						channels[i].controlChange(7, (int)(gain * 127.0));
					}
				}
			*/
			seqr.start();
		}
		catch(IOException e)
		{e.printStackTrace();}
		catch(InvalidMidiDataException e)
		{e.printStackTrace();}
		catch(MidiUnavailableException e)
		{e.printStackTrace();}
	}
	
	public void stop()
	{
		seqr.stop();
		seqr.close();
	}
}


