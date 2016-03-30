package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import database.MySQLdatabase;

public class Model {
	
	private static final int maxDepth = 10;

	private MySQLdatabase db = null;
	private Map<Integer, ReactionCol> map = new HashMap<>();
	private Map<String, Integer> nettoReaction = new HashMap<>();
	private Stack<Integer> stack = new Stack<Integer>();
	private List<Integer> reactionIDSeq = new ArrayList<>(maxDepth);
	
	private int count = 1;
	
	private String desired;
	
	public Model(){
		
		db = new MySQLdatabase();		
		
	}

	public MySQLdatabase getDatabase() {
		return db;
	}

	
	public void setUpSynth(ArrayList<String> resources, String desired){
//		this.resourceList = resources;
		this.desired = desired;		
		
//		// the iterative part
//		int depth = 0;
//		while (!goalFound) {
//			retroSynth(desiredDepth, depth);
//			System.out.println();
//			depth++;
//		}
		
		test(desired);
		
	}
 	
 	public void test(String formula){
 		for(Integer reactionID : db.getReactionIDs(formula)){
 			stack.push(reactionID);
 		}
 		int currentID = stack.pop(); 		
 		if(!map.containsKey(currentID) && currentID != 73){
 			List<Pair> list = new ArrayList<>();
 			for(String chem : db.getChemicals(currentID)){
// 				System.out.println("chem = " + chem);
 				int coefficientPM = db.getCoefficient(currentID, chem);
 				list.add(new Pair(chem, coefficientPM));
 				
 				if (nettoReaction.containsKey(chem))
 					nettoReaction.put(chem, nettoReaction.get(chem) + coefficientPM);
 				else {
 					nettoReaction.put(chem, coefficientPM);					
 				
	 				if (coefficientPM < 0 /* reactant */ && count <= 20 && !abundant(chem)) {
	 					
	 					this.reactionIDSeq.add(currentID);
	 					System.out.println("ReactionID = " + currentID + "\tFormula = " + chem + "     \tCoefficient = " + coefficientPM);
	 					count++;
	 					test(chem);
	 				}
 				}
 			}
 			
 			map.put(currentID, new ReactionCol(currentID, list));
 		}
 		return;
 	}

	private boolean abundant(String chem) {
		return (chem.equals("NaCl") || chem.equals("O2") || chem.equals("H2O"));
	}
 	
	public void setDesiredChemical(String formula) {
		this.desired = formula;
	}
	
	public List<Integer> getReactionIDSeq() {
		return this.reactionIDSeq;
	}
	
	public Map<String, Integer> getNetReactionMap() {
		return this.nettoReaction;
	}
 	
}
