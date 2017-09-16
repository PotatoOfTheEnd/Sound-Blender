package shannon.arielle;

import java.util.ArrayList;
import java.util.Arrays;

public class FrequencyAnalyzer {
	private boolean[][] sKeys, fKeys;
	private int[] sOrder, fOrder;
	private ArrayList<ArrayList<Integer>> adj;
	FrequencyAnalyzer(){
		sKeys = new boolean[6][5];
		fKeys = new boolean[6][5];
		fOrder = new int[]{10, 3, 8, 1, 6};
		sOrder = new int[]{6, 1, 8, 3, 10};
		for(int i=0; i<6; i++){
			for(int j=0; j<i; j++){
				sKeys[i][j]=true;
				fKeys[i][j]=true;
			}//C C# D Eb E F F# G G# A Bb B
		}
		adj = new ArrayList<>();
		int[] circleOfFifths = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 11, 10, 9, 8};
		for(int i=0; i<12; i++){adj.add(new ArrayList<Integer>());}
		for(int i=0; i<12; i++){
			adj.get(circleOfFifths[i]).add(circleOfFifths[(i-1+12)%12]);
			adj.get(circleOfFifths[i]).add(circleOfFifths[(i+1)%12]);
				//(5/14) (6/13) (7/12)
		}
		adj.add(new ArrayList<Integer>(Arrays.asList(7, 6, 11)));
		adj.add(new ArrayList<Integer>(Arrays.asList(6, 7, 5)));
		adj.add(new ArrayList<Integer>(Arrays.asList(4, 3, 5)));
		adj.get(5).add(14); adj.get(6).add(13); adj.get(7).add(12);
		
		//			0, 			1, 			2, 			3, 				4, 			5, 				6, 				7	
		//C maj/A min, G maj/E min, D maj/B min, A maj/F# min, E maj/C# min, B maj/G# min, F# maj/D# min, C# maj/A# min
		//C maj/A min, F maj/D min, Bb maj/G min, Eb maj/C min, Ab maj/F min, Db maj/Bb min, Gb maj/Eb min, Cb maj/Ab min
								//C# D# F# G# A#
								//Db Eb Gb Ab Bb
			
	}
	private int flatsError(int[] notes, int key){
		int tt = 0;
		for(int i=0; i<5; i++){
			if (fKeys[key][i]){
				tt+= notes[fOrder[i]+1];
			}
			else{
				tt+=notes[fOrder[i]];
			}
		}
		return tt;
	}
	//C Db D Eb E F Gb G Ab A Bb B
	private Pair Flats(int[] notes){
		//Bb (at 10) is the first one
		//Also, Cb = B and Fb = E
		int error = 0;
		int bestkey = 7;
		//First check for the keys with Cb and Fb
		int[] drkeys = new int[]{1, 6, 8};
		for(int i: drkeys){
			error += notes[i+1];
		}
		error += notes[0];
		if (notes[5]<notes[4]){
			error+=notes[5];
		}
		else{
			error+=notes[4];
			bestkey = 6;
		}
		for(int i=0; i<6; i++){
			int tp = flatsError(notes, i);
			if (tp < error){
				error = tp;
				bestkey =i;
			}
		}
		return new Pair(error, bestkey);
	}
	private int sharpsError(int[] notes, int key){
		int tt = 0;
		for(int i=0; i<5; i++){
			if (sKeys[key][i]){
				tt+= notes[sOrder[i]-1];
			}
			else{
				tt+=notes[sOrder[i]];
			}
		}
		return tt;
	}
	//C C# D Eb E F F# G G# A Bb B
	private Pair Sharps(int[] notes){
		//F# (at 6) is the first one
		//Also, E# = F and B# = C
		int error = 0;
		int bestkey = 7;
		//First check for the keys with E# and B#
		int[] drkeys = new int[]{3, 8, 10};
		for(int i: drkeys){
			error += notes[i-1];
		}
		error += notes[4];
		if (notes[0]>notes[11]){
			error+=notes[11];
		}
		else{
			error+=notes[0];
			bestkey = 6;
		}
		for(int i=0; i<6; i++){
			int tp = sharpsError(notes, i);
			if (tp < error){
				error = tp;
				bestkey =i;
			}
		}
		return new Pair(error, bestkey);
	}
	public int key(int[] notes){
		Pair a = Sharps(notes), b=Flats(notes);
		if (a.f > b.f){
			if(b.s ==0){ return 0; }
			else{ return b.s+7; }
		}
		else{
			return a.s;
		}
	}
}
