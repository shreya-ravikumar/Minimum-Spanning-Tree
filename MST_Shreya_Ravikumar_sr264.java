

/*
Analysis of Algorithms - CSE 531
Project 1 - Implementing Heap DS and Finding MST using Prim's Algorithm
Name : Shreya Ravikumar
UBIT Person Number : 5020 5664
UBIT Name : sr264@buffalo.edu
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
//HEAP DATA STRUCTURE IMPLEMENTATION USING INSERT, REMOVE AND HEAPIFYUP AND HEAPIFYDOWN
class HeapDS {
	private int heaparray[];  //heap array
	private int heappos[]; //position of elements in heap data structure
	private int dist[];  
	private int N; //heap size
	
	public HeapDS(int size, int _dist[], int _heappos[])  //constructor
	{
		N=0;
		heaparray=new int[size+1];
		dist=_dist;
		heappos=_heappos;
	}
	public boolean isempty()
	{
		return N==0;
	}
	
	public void heapifyup(int k)
	{
		int v=heaparray[k];
		heaparray[0]=0;
		dist[0]=Integer.MIN_VALUE;
		while(dist[v]<dist[heaparray[k/2]])
		{
	    	 heaparray[k] = heaparray[k / 2];
	    	 heappos[heaparray[k]] = k;
	    	 k = k / 2;
	     }
	     heaparray[k] = v;
	     heappos[v] = k;
		
	}
	 public void heapifydown( int k) 
	 {
	     int v, j=0;
	     v = heaparray[k];  
	     
	     while( k <= N/2) 
	     {
	    	 j = 2 * k;
	    	 if(j < N && dist[heaparray[j]] > dist[heaparray[j + 1]])
	    		 j++;
	    	 if(dist[v] <= dist[heaparray[j]]) 
	    	    break;
	    	heaparray[k] = heaparray[j];
	    	heappos[heaparray[k]] = k;
	    	k = j;
	     }
	     heaparray[k] = v;
	     heappos[v] = k;
	 }
	 public void insertinheap(int ele) 
	 {
	     heaparray[++N] = ele;
	     heapifyup(N);
	 }

	 public int removeroot() //remove and adjust the position of heap and the array accordingly
	 {   
	     int v = heaparray[1];
	     heappos[v] = 0; // v is no longer in heap
	     heaparray[N+1] = 0;  // put null node into empty spot
	     
	     heaparray[1] = heaparray[N--];
	     heapifydown(1);
	     return v;
	 } 
}
class constructgraph
{ 	class node
	{
	  int vertex;
	  int weight;
	  node next;
	
	}
  int V, E;
  node adj[]; //adjacency list
  node first;  //head or first node to be made to point at first
  int minspantree[];
  int visited[];
  int id;
 //CONSTRUST GRAPH 
  public constructgraph(String file)  throws IOException
  {    
 	  //File ofile = new File("output.txt"); //output file
      int u, v;
      int e, weight;
      node temp;
      FileReader fr = new FileReader(file);
 	  BufferedReader reader = new BufferedReader(fr);
 	  String splits = " +";  // multiple whitespace as delimiter
 	  String line = reader.readLine();        
      String[] parts = line.split(splits);
      //System.out.println("parts of the file print " + parts[0] + parts[1]);
      
      V = Integer.parseInt(parts[0]); //vertices initialized from first value
      E = Integer.parseInt(parts[1]);  //edges initialized from second value int he file read
      
      // create first node
      first = new node(); 
      first.next = first;
      
      adj = new node[V+1];        
      for(v = 1; v <= V; v++)
          adj[v] = first;               
      
      //System.out.println("Edges");
      for(e = 1; e <= E; ++e)
      {
          line = reader.readLine();
          parts = line.split(splits);
          u = Integer.parseInt(parts[0]);
          v = Integer.parseInt(parts[1]); 
          weight = Integer.parseInt(parts[2]);
          //add to list
          temp = adj[u];   //from u to v
          adj[u] = new node();
 		  adj[u].vertex = v;
 		  adj[u].weight = weight;
 		  adj[u].next = temp;
 			  			
          temp = adj[v];  //from v to u
 		  adj[v] = new node();
 		  adj[v].vertex = u;
 		  adj[v].weight = weight;
 		  adj[v].next = temp;
          
      }	       
  }
//PRIM'S ALGORITHM FUNCTION 
  	public void Prim_st(int s, File file) throws FileNotFoundException
 	{
 	  int v;
      int sum = 0;
      int  dist[], parent[], pos[];
      node temp;
      dist = new int[V+1];
      parent = new int[V+1];
      pos = new int[V+1];
      
      for(v = 0; v <= V; v++) {
     	 dist[v] = Integer.MAX_VALUE;   //assign max value of integer to the distance
     	 parent[v] = 0;
     	 pos[v] = 0;	 
      }
      HeapDS heap = new HeapDS(V+1,dist,pos); //create heap and move these elements to the new data structure
      heap.insertinheap(s);
      dist[s] = 0;
      HeapDS newheap =  new HeapDS(V, dist, pos);
      newheap.insertinheap(s);
      
      while (!heap.isempty())
      {
          v = heap.removeroot();
          //System.out.println("\n Adding edge {" + parent[v] + "}--([" + dist[v] + "})--{" + v + "}");
          //output.write(parent[v] + "-----" + v + "------" + dist[v]);
          //calculates the sum of the weights
          sum += dist[v];
          dist[v] = -dist[v];
          for (temp = adj[v]; temp != first; temp = temp.next)
          { if (temp.weight < dist[temp.vertex])
              {
               dist[temp.vertex] = temp.weight;
               parent[temp.vertex] = v;
               if (pos[temp.vertex] == 0)    //if position of the vertex is empty then insert next vertex
                  {
                  heap.insertinheap(temp.vertex);
                  }
                  else 
                  {
                   heap.heapifyup(pos[temp.vertex]);  
                  }
              }
          }
       minspantree = parent; 
      
    }
      //System.out.print("Weight of MST total = " + sum + "\n");
      PrintWriter output=new PrintWriter(file);
      //System.out.print("MST parent array :\n");
      output.write(sum+"\n");
      for(int v1 = 1; v1 <= V; ++v1)
      {  if(dist[v1]==0) //if distance is zero no printing
     	    continue;
         else
         {
          //System.out.println(v1 + " -> " + minspantree[v1] + "---->" + (dist[v1])); //-1
          output.write(v1 + " " + minspantree[v1]+ " " + ((-1)*dist[v1]) +"\n");
         }
      }
      System.out.println("");
      output.write(" ");
      output.close();
      //return sum;
 	}  
}


//MAIN CLASS CALLING THE GRAPH CONSTRUCTION AND PRIM ALGORITHM FUNCTION
public class MST_Shreya_Ravikumar_sr264 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int s = 1;    //start from first vertex
		constructgraph graph1 = new constructgraph("input.txt"); //call constructor to create graph
	    File file=new File("output.txt");
	    graph1.Prim_st(s,file); //call min span tree result function

	}

}


/*
References:
1.Introduction to Algorithms by Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, Clifford Stein
2.http://www.cse.buffalo.edu/~shil/courses/CSE531/Slides/Greedy-NA.pdf
3.https://en.wikipedia.org/wiki/Heap_(data_structure)
4.https://en.wikipedia.org/wiki/Prim%27s_algorithm
*/
