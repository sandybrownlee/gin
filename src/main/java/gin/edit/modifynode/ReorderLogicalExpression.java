package gin.edit.modifynode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.expr.Expression;

import gin.SourceFile;
import gin.SourceFileTree;

/**
 * exploit short-circuiting behaviour in logical expressions by swapping order of nodes within them
 * currently just swaps within binary statements.
 * better would be to look at the whole expression and explore all possible reorderings
 * this would then capture this kind of thing: (a || b) || c
 */
public class ReorderLogicalExpression extends ModifyNodeEdit {
    private final int targetNode;
    
    /**
     * 
     * @param sourceFile to create an edit for
     * @param rng random number generator, used to choose the target statements
     * @throws NoApplicableNodesException if no suitable nodes found
     */
     // @param sourceNodes is the list of possible nodes for modification; these won't be
     //           modified, just used for reference
     // @param r is needed to choose a node and a suitable replacement 
     //        (keeps this detail out of Patch class)
    public ReorderLogicalExpression(SourceFile sourceFile, Random rng) throws NoApplicableNodesException {
        SourceFileTree sf = (SourceFileTree)sourceFile;
                       
        List<Integer> nodes = new ArrayList<>();         
        
        int expression = 6;
        switch(expression) {
        case 1:
        	// A list of all AND OR operator nodes
            nodes = getOperatorNodes(sf, rng);       	
        case 2:
        	 // A list of lists, each consisting of the AND OR operator nodes within each condition. 
            // Note, only multiple operator conditions are returned. 
            nodes = getShortCircuitingOperatorNodes(sf, rng);
        case 3:
        	// As getShortCircuitingOperatorNodes, but returns the leaf nodes of the operators
            nodes = getShortCircuitingLeafNodes(sf, rng);   
        case 4:
        	// As getShortCircuitingLeafNodes, but returns leaf nodes of matching operators
        	nodes = getShortCircuitingRelevantLeafNodes(sf, rng);
        case 5:
        	// As getShortCircuitingLeafNodes, but returns OR operators instead of its leaves
        	nodes = getShortCircuitingLeafNodesPreserveOR(sf, rng);   
        }           
        
        if (nodes.isEmpty()) {
            throw new NoApplicableNodesException();
        }                       
        
        this.targetNode = nodes.get(rng.nextInt(nodes.size()));
    }
    
    public List<Integer> getOperatorNodes(SourceFileTree sf, Random rng){    
    	// get the static list of nodes that might be applicable (the BinaryStatements)
        // then look for the ones that have AND OR operators 
        List<Integer> nodes = new ArrayList<>(); 

        for (Integer i : sf.getNodeIDsByClass(true, BinaryExpr.class)) {
            BinaryExpr n = (BinaryExpr)(sf.getNode(i));
            if ((n.getOperator() == Operator.AND) || (n.getOperator() == Operator.OR)) {
                nodes.add(i);
            }
        }
        
        return nodes;   	
    }
   	////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<Integer> getShortCircuitingOperatorNodes(SourceFileTree sf, Random rng){    

		System.out.println("Short Circuiting ########################################################"); 
		List<List<Integer>>  short_circuit_indexes = sf.getShortCircuitingNodeIDs();   
		// Choose condition
		List<Integer> condition_nodes = short_circuit_indexes.get(rng.nextInt(short_circuit_indexes.size()));
		
		// Printing (unnecessary)
		System.out.println("Potential ANDs and ORs nodes: " + short_circuit_indexes + " - Selected: " + condition_nodes);  
		for (Integer this_op : condition_nodes) {
			System.out.println("For switch: " + sf.getNode(this_op).toString());
		}	 
		
		return condition_nodes;
	}    
	////////////////////////////////////////////////////////////////////////////////////
	public List<Integer> getShortCircuitingLeafNodes(SourceFileTree sf, Random rng) {
		System.out.println("Start Leaves ########################################################"); 
		List<List<Integer>>  short_circuit_leaf_indexes = sf.getShortCircuitingLeafIDs(); 		
		//Choose condition
		List<Integer> leaf_nodes = short_circuit_leaf_indexes.get(rng.nextInt(short_circuit_leaf_indexes.size()));		
		// Print details (unnecessary)
		System.out.println("All leaves of Condition: " + short_circuit_leaf_indexes + " - Selected: " + leaf_nodes);  	
		for (Integer this_op : leaf_nodes) {
			System.out.println(sf.getNode(this_op).toString());
		}
		System.out.println("End Leaves ########################################################");
		
		return leaf_nodes;    	
	}
	////////////////////////////////////////////////////////////////////////////////////
	public List<Integer> getShortCircuitingRelevantLeafNodes(SourceFileTree sf, Random rng) {
	
		System.out.println("Start Smart Leaves ########################################################"); 
		List<List<Integer>>  short_circuit_smart_leaf_indexes = sf.getShortCircuitingLeafIDsByType(); 
		
		//Choose condition
		List<Integer> leaf_nodes = short_circuit_smart_leaf_indexes.get(rng.nextInt(short_circuit_smart_leaf_indexes.size()));
		
		// Printing (unnecessary)
		System.out.println("All leaf nodes of Condition: " + short_circuit_smart_leaf_indexes + " Selected: " + leaf_nodes);  
		for (Integer this_op : leaf_nodes) {
			System.out.println(sf.getNode(this_op).toString());
		}
		System.out.println("End Leaves ########################################################");
		
		return leaf_nodes;
	} 
	////////////////////////////////////////////////////////////////////////////////////	
	public List<Integer> getShortCircuitingLeafNodesPreserveOR(SourceFileTree sf, Random rng) {
		System.out.println("Start Leaves ########################################################"); 
		List<List<Integer>>  short_circuit_leaf_indexes = sf.getShortCircuitingLeafIDsPreserveOR(); 		
		//Choose condition
		List<Integer> leaf_nodes = short_circuit_leaf_indexes.get(rng.nextInt(short_circuit_leaf_indexes.size()));		
		// Print details (unnecessary)
		System.out.println("All leaves of Condition: " + short_circuit_leaf_indexes + " - Selected: " + leaf_nodes);  	
		for (Integer this_op : leaf_nodes) {
			System.out.println(sf.getNode(this_op).toString());
		}
		System.out.println("End Leaves ########################################################");
		
		return leaf_nodes;    	
	}
	////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public SourceFile apply(SourceFile sourceFile) {
        SourceFileTree sf = (SourceFileTree)sourceFile;
        Node node = sf.getNode(this.targetNode);
    
        if (node == null) {
            return sf; // targeting a deleted location just does nothing.
        } else {
            Expression left = ((BinaryExpr)node).getLeft();
            Expression right = ((BinaryExpr)node).getRight();
                
            ((BinaryExpr)node).setLeft(right);
            ((BinaryExpr)node).setRight(left);
            
            sf = sf.replaceNode(this.targetNode, node);
            
            return sf;
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + " swapping child nodes in [" + this.targetNode + "]";
    }
}
