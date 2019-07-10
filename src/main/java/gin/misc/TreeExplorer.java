package gin.misc;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;

import gin.SourceFileTree;

public class TreeExplorer {
	
	public void tester()
	{
		System.out.println("Called");
		Scanner in = new Scanner(System.in);
		System.out.println("Enter Node of Interest");
		int selected_node = in.nextInt();
		System.out.println("Node Selected: ");
		System.out.println(selected_node);
	}
	
	public void getDetailsOfSelectedNode(SourceFileTree sf)
	{				
		int selected_node = 0;
		Boolean keep_checking = true;		
		Scanner in = new Scanner(System.in);
		System.out.println("Node or Branch? (n/b)");
		String node_or_branch = in.nextLine();
							
		while(keep_checking) {
			System.out.println("Enter Index of Node of Interest");
			selected_node = in.nextInt();
			System.out.println("Node Selected:     "+ selected_node);
			System.out.println("Details:     " + sf.getNode(selected_node).toString());
			
			if (node_or_branch.equals("b")) {
				System.out.println("Children:     ");
				List<Integer> children = new ArrayList<>();
				getChildren(sf.getNode(selected_node), children);		
			}
			
			
			
			System.out.println("Continue? (y/n)");
			String reply = in.nextLine();
			if(in.nextLine().equals("n")) {
				keep_checking = false;
				System.out.println("Continuing GI");
			}			
		}				
	}
	
	public List<Integer> getChildren(Node input_node, List<Integer> operator_nodes) {
	
		for(Node child: input_node.getChildNodes()) {    
			System.out.println(child.toString());
		}			
		for(Node child: input_node.getChildNodes()) {    		
			getChildren(child, operator_nodes);
		}
				
		return operator_nodes;
	}	

}
