package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		ArrayList<String> chain = new ArrayList<String>();
		if(!g.map.containsKey(p1)) return null;
		if(!g.map.containsKey(p2)) return null;
		int startPoint = g.map.get(p1);
		int endPoint = g.map.get(p2);
		Queue<Integer> num = new Queue<Integer>();
		boolean[] v = new boolean[g.map.size()];
		int[] prev = new int[g.map.size()];
		//BFS
		num.enqueue(startPoint);
		while(!num.isEmpty()) {
			int val = num.dequeue();
			v[val] = true;
			Person curr = g.members[val];
			Friend next = curr.first;
			while(next != null) {
				if(!v[next.fnum]) {
					num.enqueue(next.fnum);
					v[next.fnum] = true;
					prev[next.fnum] = val; 
				}
				next = next.next;
			}
			//if p2 is reached, stop looking
			if(v[endPoint]) break;
		}
		//puts in prev nodes into chain arrayList
		if(v[endPoint]) {
			int check = endPoint;
			chain.add(g.members[endPoint].name);
			while(prev[check] != 0) {
				chain.add(g.members[prev[check]].name);
				check = prev[check];
			}
			if(startPoint == 0) {
				chain.add(g.members[startPoint].name);
			}
		}
		//reverses the arrayList
		for (int i = 0; i < chain.size()/2; i++) {
			String temp = chain.get(i);
			chain.set(i, chain.get(chain.size() - i - 1)); 
            chain.set(chain.size() - i - 1, temp); 
		}
		return chain;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	private static boolean hasFalse(boolean[] v) {
		for(boolean a : v) {
			if(!a) return true;
		}
		return false;
	}
	
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		ArrayList<ArrayList<String>> cliques = new ArrayList<ArrayList<String>>();
		Queue<Integer> num = new Queue<Integer>();
		boolean[] v = new boolean[g.map.size()];
		int start = 0;
		while(hasFalse(v)) {
			for(int i = 0; i < v.length; i++) {
				if(!v[i]) {
					start = i;
					break;
				}
			}
			//BFS
			num.enqueue(start);
			while(!num.isEmpty()) {
				int val = num.dequeue();
				v[val] = true;
				Person curr = g.members[val];
				Friend next = curr.first;
				//BFS for students at specific school
				if(curr.student) {
					if(curr.school.equals(school)) {
					ArrayList<String> groups = new ArrayList<String>();
					Queue<Integer> newNum = new Queue<Integer>();
					newNum.enqueue(val);
					while(!newNum.isEmpty()) {
						val = newNum.dequeue();
						groups.add(g.members[val].name);
						v[val] = true;
						curr = g.members[val];
						next = curr.first;
						while(next != null) {
							if(!v[next.fnum] && g.members[next.fnum].student) {
								if(g.members[next.fnum].school.equals(school)) {
									newNum.enqueue(next.fnum);
									v[next.fnum] = true;
								}
							}
							next = next.next;
						}
					}
					cliques.add(groups);
					}
				}
				//continue regular BFS
				while(next != null) {
					if(!v[next.fnum]) {
						num.enqueue(next.fnum);
						v[next.fnum] = true;
					}
					next = next.next;
				}
			}
		}
		return cliques;
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		if(g.members[0] == null) return null;
		
		ArrayList<ArrayList<Integer>> index = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i<g.map.size(); i++) {
			index.add(i, null);
		}
		ArrayList<String> connectors = new ArrayList<String>();
		Stack<Integer> dfsStack = new Stack<Integer>();
		boolean[] visited = new boolean[g.map.size()];
		boolean[] vConnect = new boolean[g.map.size()];
		int[] dfsNum = new int[g.map.size()];
		int[] back = new int[g.map.size()];
		int iterate = 1;
		int start = 0;
		
		while(hasFalse(visited)) {
			for(int i = 0; i < visited.length; i++) {
				if(!visited[i]) {
					start = i;
					break;
				}
			}
			dfsStack.push(start);
			dfsNum[start] = iterate;
			back[start] = dfsNum[start];
			visited[start] = true;
			while(!dfsStack.isEmpty()) {
				int currNode = dfsStack.peek();
				int nextNum = g.members[currNode].first.fnum;
				Person curr = g.members[currNode];
				if(curr.first != null && !visited[nextNum]) {
					dfsStack.push(nextNum);
					visited[nextNum] = true;
					iterate++;
					dfsNum[nextNum] = iterate;
					back[nextNum] = dfsNum[nextNum];
				}
				else if(curr.first != null && visited[nextNum]) {
					Friend neighbor = curr.first;
					dfsStack.pop();
					ArrayList<Integer> links = new ArrayList<Integer>();
					while(neighbor != null) {		
						links.add(neighbor.fnum);
						
						if(!visited[neighbor.fnum]) {
							dfsStack.push(currNode);
							dfsStack.push(neighbor.fnum);
							visited[neighbor.fnum] = true;
							iterate++;
							dfsNum[neighbor.fnum] = iterate;
							back[neighbor.fnum] = dfsNum[neighbor.fnum];
						}
						//finding the connector if not starting node
						else if(dfsNum[currNode] <= back[neighbor.fnum]) {
							if(!g.members[currNode].equals(g.members[0])) {
								if(!connectors.contains(g.members[currNode].name)) {
									connectors.add(g.members[currNode].name);
								}
							}
							else if(g.members[currNode].equals(g.members[0])&& vConnect[currNode]) {
								if(index.get(currNode) != null) {
									for(int i = 0; i < index.get(currNode).size(); i++) {
										boolean test = false;
										for(int j = 0; j < index.size(); j++) {
											if(j == currNode) continue;
											if(index.get(j) != null) {
												for(int k = 0; k < index.get(j).size(); k++) {
													if(index.get(j).contains(index.get(currNode).get(i))) {
														test = true;
														break;
													}
													if(test) break;
												}
											}
											
										}
										if(!test) 
											if(!connectors.contains(g.members[currNode].name)) {
												connectors.add(g.members[currNode].name);
											}
									}
								}
								
							}
							else {
								vConnect[currNode] = true;
							}
									
						}
						//changes the dfsNum and back vars accordingly
						else {
							if(dfsNum[currNode] > back[neighbor.fnum]) {
								back[currNode] = Math.min(back[currNode], back[neighbor.fnum]);
							}
							else {
								back[currNode] = Math.min(back[currNode], dfsNum[neighbor.fnum]);
							}
						}
						neighbor = neighbor.next;
					}
					index.set(currNode, links);
				}
				else {
					return null;
				}
			}
		}
		return connectors;
		
	}
}

