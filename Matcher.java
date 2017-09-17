package shannon.arielle;

public class Matcher {
	private boolean[][] adj;
	private boolean[] visited;
	private boolean[] hasCh;
	private int[] par;
	private int[] keys;
	Matcher(){
		adj = new boolean[15][15];
		int[] circleOfFifths = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 11, 10, 9, 8};
		for(int i=0; i<12; i++){
			adj[circleOfFifths[i]][circleOfFifths[(i-1+12)%12]]=true;
			adj[circleOfFifths[i]][circleOfFifths[(i+1)%12]]=true;
			adj[circleOfFifths[i]][circleOfFifths[i]]=true;
				//(5/14) (6/13) (7/12)
		}
		adj[12][7]=true; adj[12][6]=true; adj[12][11]=true; adj[12][12]=true;
		adj[13][7]=true; adj[13][6]=true; adj[13][5]=true; adj[13][13]=true;
		adj[14][3]=true; adj[14][4]=true; adj[14][5]=true; adj[14][14]=true;
		adj[5][14]=true; adj[6][13]=true; adj[7][12]=true;
		
		//			0, 			1, 			2, 			3, 				4, 			5, 				6, 				7	
		//C maj/A min, G maj/E min, D maj/B min, A maj/F# min, E maj/C# min, B maj/G# min, F# maj/D# min, C# maj/A# min
		//C maj/A min, F maj/D min, Bb maj/G min, Eb maj/C min, Ab maj/F min, Db maj/Bb min, Gb maj/Eb min, Cb maj/Ab min

	}
	private boolean match(int cn){
		if(visited[cn]){
			return false;
		}
		visited[cn]=true;
		for(int i=0; i<keys.length; i++){
			if(adj[keys[i]][keys[cn]] && (par[i]<0 || match(i))){
				par[i]=cn; 
				hasCh[i]=true;
				return true;
			}
		}
		return false;
	}
	public int[] arrange(int[] seq){
		keys = seq;
		par = new int[seq.length];
		hasCh = new boolean[seq.length];
		for(int i=0; i<seq.length; i++){ par[i]=-1; }
		for(int i=0; i<seq.length; i++){
			visited = new boolean[seq.length];
			match(i);
		}
		int[] ret = new int[seq.length];
		int stval = 0;
		for(int i=0; i<seq.length; i++){
			if (!hasCh[i]){
				int tp = i;
				while(tp>0){
					ret[stval]=tp;
					stval++;
					tp = par[tp];
				}
			}
		}
		return ret;
	}
	
}
