package shannon.arielle;

import java.io.*;
import java.util.ArrayList;

public class ReadExample
{
	private static double[] frequencies = new double[]{16.35, 17.32, 18.35, 19.45, 20.6, 21.83, 23.12, 24.5, 25.96, 27.5, 29.14, 30.87, 32.7, 34.65, 36.71, 38.89, 41.2, 43.65, 46.25, 49.0, 51.91, 55.0, 58.27, 61.74, 65.41, 69.3, 73.42, 77.78, 82.41, 87.31, 92.5, 98.0, 103.8, 110.0, 116.5, 123.5, 130.8, 138.6, 146.8, 155.6, 164.8, 174.6, 185.0, 196.0, 207.7, 220.0, 233.1, 246.9, 261.6, 277.2, 293.7, 311.1, 329.6, 349.2, 370.0, 392.0, 415.3, 440.0, 466.2, 493.9, 523.3, 554.4, 587.3, 622.3, 659.3, 698.5, 740.0, 784.0, 830.6, 880.0, 932.3, 987.8, 1047.0, 1109.0, 1175.0, 1245.0, 1319.0, 1397.0, 1480.0, 1568.0, 1661.0, 1760.0, 1865.0, 1976.0, 2093.0, 2217.0, 2349.0, 2489.0, 2637.0, 2794.0, 2960.0, 3136.0, 3322.0, 3520.0, 3729.0, 3951.0, 4186.0, 4435.0, 4699.0, 4978.0, 5274.0, 5588.0, 5920.0, 6272.0, 6645.0, 7040.0, 7459.0, 7902.0};
	private static int bsearch(double f){
		int s=0, e=frequencies.length-1;
		while(s<e){
			int m= (s+e)/2;
			if(frequencies[m]>f){
				e = m;
			}
			else{
				s = m+1;
			}
		}
		if (e==0 || frequencies[e]-f < f-frequencies[e-1]){
			return e%12;
		}
		else{
			return s%12;
		}
	}
	
	public static double[] go(File args)
	{
		try
		{
			// Open the wav file specified as the first argument
			WavFile wavFile = WavFile.openWavFile(args);

			// Display information about the wav file
			//wavFile.display();

			// Get the number of audio channels in the wav file
			int numChannels = wavFile.getNumChannels();
			//System.out.println("N")
			// Create a buffer of 100 frames
			int[] buffer = new int[100 * numChannels];
			double[] res;
			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			long numFrames = wavFile.getNumFrames();
			int x, st = 0; 
			if (numFrames<441000){ res = new double[(int) numFrames]; x = 1; }
			else{ x = (int) numFrames/441000; res = new double[441000];}
			do
			{
				// Read frames into buffer
				framesRead = wavFile.readFrames(buffer, 100);
				
				// Loop through frames and look for minimum and maximum value
				for (int s=0 ; s<framesRead * numChannels ; s++)
				{	
					if (st%x == 0 && st/x<res.length){ res[st/x] = buffer[s];}
					if (buffer[s] > max) max = buffer[s];
					if (buffer[s] < min) min = buffer[s];
					st+= 1;
					//System.out.println(buffer[s]);
				}
			}
			while (framesRead != 0);
			double[] fftres = new double[res.length];
			ComplexFFT.transform(res, fftres);
			//System.out.println(res[0]);
			// Close the wavFile
			wavFile.close();
			double[] arr;
			for(int i=0; i<res.length; i++){
				res[i] = Math.hypot(res[i], fftres[i]);
			}
			arr = freqs(res);
			//System.out.println(arr.length);
			//for(double i: arr){System.out.println(i);}
			// Output the minimum and maximum value
			System.out.printf("Min: %f, Max: %f\n", min, max);
			return arr;
		}
		catch (Exception e)
		{
			System.err.println(e);
			return new double[10];
		}
	}
	private static double[] freqs(double[] vls){
		double[] arr = new double[12];
		for(int i=1; i<vls.length-1; i++){
			if (vls[i-1]<vls[i] && vls[i+1]<vls[i]){
				arr[bsearch(i)]+=vls[i];
			}
		}
		return arr;
	}
}
