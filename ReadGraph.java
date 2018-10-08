import java.io.*;
import java.util.*;

		class ColEdge
			{
			int u;
            int v;
            //int c;
			}

		class Node{
			int[] neighbours;
			int connections;
			boolean visited;
			int color;
			public Node(int n){
				this.neighbours = new int[n-1];
				this.connections = 0;
				this.visited = false;
				this.color = 0;
			}
			public int getColor(){
				return color;
			}
	
			public void setColor(int color){
				this.color = color;
			}
			
			public boolean getVisited() {
				return visited;	
			}
	
			public void setVisited(boolean visited) {
				this.visited = visited;
			}

		}

		
public class ReadGraph{
		
		public final static boolean DEBUG = true;
		
		public final static String COMMENT = "//";
		
		public static void main( String args[] )
			{
			if( args.length < 1 )
				{
				System.out.println("Error! No filename specified.");
				System.exit(0);
				}

				
			String inputfile = args[0];
			
			boolean seen[] = null;
			
			//! n is the number of vertices in the graph
			int n = -1;
			
			//! m is the number of edges in the graph
			int m = -1;
			
			//! e will contain the edges of the graph
			ColEdge e[] = null;
			
			try 	{ 
			    	FileReader fr = new FileReader(inputfile);
			        BufferedReader br = new BufferedReader(fr);

			        String record = new String();
					
					//! THe first few lines of the file are allowed to be comments, staring with a // symbol.
					//! These comments are only allowed at the top of the file.
					
					//! -----------------------------------------
			        while ((record = br.readLine()) != null)
						{
						if( record.startsWith("//") ) continue;
						break; // Saw a line that did not start with a comment -- time to start reading the data in!
						}
	
					if( record.startsWith("VERTICES = ") )
						{
						n = Integer.parseInt( record.substring(11) );					
						if(DEBUG) System.out.println(COMMENT + " Number of vertices = "+n);
						}

					seen = new boolean[n+1];	
						
					record = br.readLine();
					
					if( record.startsWith("EDGES = ") )
						{
						m = Integer.parseInt( record.substring(8) );					
						if(DEBUG) System.out.println(COMMENT + " Expected number of edges = "+m);
						}

					e = new ColEdge[m];	
												
					for( int d=0; d<m; d++)
						{
						if(DEBUG) System.out.println(COMMENT + " Reading edge "+(d+1));
						record = br.readLine();
						String data[] = record.split(" ");
						if( data.length != 2 )
								{
								System.out.println("Error! Malformed edge line: "+record);
								System.exit(0);
								}
						e[d] = new ColEdge();
						
						e[d].u = Integer.parseInt(data[0]);
						e[d].v = Integer.parseInt(data[1]);

						seen[ e[d].u ] = true;
						seen[ e[d].v ] = true;
						
						if(DEBUG) System.out.println(COMMENT + " Edge: "+ e[d].u +" "+e[d].v);
				
						}
									
					String surplus = br.readLine();
					if( surplus != null )
						{
						if( surplus.length() >= 2 ) if(DEBUG) System.out.println(COMMENT + " Warning: there appeared to be data in your file after the last edge: '"+surplus+"'");						
						}
					
					}
			catch (IOException ex)
				{ 
		        // catch possible io errors from readLine()
			    System.out.println("Error! Problem reading file "+inputfile);
				System.exit(0);
				}

			for( int x=1; x<=n; x++ )
				{
				if( seen[x] == false )
					{
					if(DEBUG) System.out.println(COMMENT + " Warning: vertex "+x+" didn't appear in any edge : it will be considered a disconnected vertex on its own.");
					}
				}

			//! At this point e[0] will be the first edge, with e[0].u referring to one endpoint and e[0].v to the other
			//! e[1] will be the second edge...
			//! (and so on)
			//! e[m-1] will be the last edge
			//! 
			//! there will be n vertices in the graph, numbered 1 to n

			//! INSERT YOUR CODE HERE!
            
            //int max_colors;
            //int[] colors_used = new int[n];
            //int[] vertice_color = new int[n];
			Node[] graph = new Node[n];
			for(int i=0;i<n;i++){
				graph[i]=new Node(n);
			}

			graph = neighbours(graph,e);
			int[] r = Bound(graph);
			int upperBound = r[1];
			System.out.println("upper bound "+upperBound);
			/*for(int i=0;i<n;i++){
				System.out.print(graph[i].connections+" ");	
			}
			System.out.println();
			for(int j=0;j<n;j++){
				for(int i=0;i<n-1;i++){
					System.out.print(graph[j].neighbours[i]+" ");	
				}
				System.out.println();			
			}*/
			System.out.println("CN "+chromaticNumber(graph, 0, 0));
			int lowerBound = r[0];
			//System.out.println();
			System.out.println("lower bound "+lowerBound);
			//int c = 0;
			//c = chromaticNumber(graph, 0, c);
			//System.out.println("CN"+chromaticNumber(graph, 0, 0));

		}

		public static Node[] neighbours(Node[] graph,ColEdge[] e) {
			int a,b;
			for (int i=0;i<e.length;i++){
				a = nextPosInArray(graph[(e[i].u)-1].neighbours);
				graph[(e[i].u)-1].neighbours[a] = (e[i].v);
				b = nextPosInArray(graph[(e[i].v)-1].neighbours);
				graph[(e[i].v)-1].neighbours[b] = (e[i].u);
			}	
			return graph;
		}

		public static int nextPosInArray(int[] array) {
			for(int i=0;i<array.length;i++)
				if(array[i]==0)
					return i;
			return -1;
		}

		public static int inArray(int[] array) {
			int c = 0;
			for(int i=0;i<array.length;i++)
				if(array[i]!=0)
					c++;
			return c;		
		}

		public static int[] Bound(Node[] graph){
			int[]r =new int[2];
			r[1] = 0;
			r[0] = graph.length;
			for(int i=0;i<graph.length;i++){
				graph[i].connections = inArray(graph[i].neighbours);
				if (graph[i].connections > r[1])
					r[1] = graph[i].connections;
				else if(graph[i].connections < r[0]) 
					r[0] = graph[i].connections;
			}
			r[1]++;
			if (r[0]<2)
				r[0]++;
			return r;
		}
		
		public static int chromaticNumber(Node[] graph,int i,int c) {
			if (!graph[i].visited){
				if(i==0){
					graph[i].setVisited(true);
					graph[i].setColor(1);
				}
				else {
					graph[i].setVisited(true);
					graph[i].setColor(1);
					int j = 0;
					//boolean done = false;
					//System.out.println("Node "+(i+1)+" c "+graph[i].connections);
					while ((j<graph[i].connections) /*&&(!done)*/){
						//System.out.println("Node "+(i+1)+" n "+graph[i].neighbours[j]);
						//System.out.println("test node "+(i+1)+" col is "+graph[i].color);
						//System.out.println("test node2 "+graph[i].neighbours[j]+" col is "+graph[graph[i].neighbours[j]-1].color);
						if ((graph[i].neighbours[j]!=0) && (graph[i].color==graph[graph[i].neighbours[j]-1].color)){
							//System.out.println("test node2 "+graph[i].neighbours[j]+" col is "+graph[graph[i].neighbours[j]-1].color);
							graph[i].setColor(graph[i].color+1);
							//System.out.println("Node "+(i+1)+" col "+graph[i].color);
							j=0;
							if (c < graph[i].color)
								c=graph[i].color;
							//System.out.println("CN "+c);
						}
						else j++;
						//System.out.println("CN_2 "+c);
					}
				}
			}
			//System.out.println("CN_3 "+c);
			if (i<graph.length-1){
				return chromaticNumber(graph, i+1,c);
			}
			return c;
		}

		


		
}