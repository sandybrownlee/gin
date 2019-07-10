public class Triangle {

    static final int INVALID = 0;
    static final int SCALENE = 1;
    static final int EQUALATERAL = 2;
    static final int ISOCELES = 3;

    public static int classifyTriangle(int a, int b, int c) {

        delay();
        
        
        if(delayBoolean(60) && delayBoolean(30) && false)
        {
        	// Good 60, false, 30
        	// Better: 30, false, 60
        	// Best: false, either
        	System.out.println("Shouldn't get here");
        }
        
        if(delayBoolean(30) && (delayBoolean(60) && false))
        {
        	// Good: (60, false) 30
        	// Better: 60, (30, false)
        	// Best: either, (false, either)
        	System.out.println("Shouldn't get here either");
        }
        
        
        if(delayBoolean(30) && (false || delayBoolean(60)))
        {
        	// Best: any, (any, false)
        	//System.out.println("a");
        	int az = 1;  
        }
        
        //System.out.println("--------------------------");
        
        /*
        if(displayMethodName("a", true) && displayMethodName("b", false)  || displayMethodName("c", true)){
        	System.out.println("TRUE");
        	int az = 1;  
        }
        else{
        	System.out.println("FALSE"); 
        	int az = 1;  
        }
        
        if(displayMethodName("a", false) || displayMethodName("b", true)  && displayMethodName("c", true)){
        	System.out.println("TRUE");
        	int az = 1;  
        }
        else{
        	System.out.println("FALSE"); 
        	int az = 1;  
        }
        
        if(displayMethodName("a3", true) || displayMethodName("b3", false)  && displayMethodName("c3", true)){
        	System.out.println("TRUE");
        	int az = 1;  
        }
        else{
        	System.out.println("FALSE");  
        	int az = 1;        	
        }
        
        System.out.println("--------------------------");
               
        
        if(delayBoolean(30) && false || delayBoolean(30)){
        	// Not sure!
        	System.out.println("a");
        }
        
        if(false || delayBoolean(30) && delayBoolean(30)){
        	// Not sure
        	System.out.println("a");
        }
        
        if((false || delayBoolean(30)) && (false || delayBoolean(60))){
        	System.out.println("a");
        }
        
        */
        
       
        
        // Sort the sides so that a <= b <= c
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }

        if (a > c) {
            int tmp = a;
            a = c;
            c = tmp;
        }

        if (b > c) {
            int tmp = b;
            b = c;
            c = tmp;
        }

        if (a + b <= c) {
            return INVALID;
        } else if (a == b && b == c) {
            return EQUALATERAL;
        } else if (a == b || b == c) {
            return ISOCELES;
        } else {
            return SCALENE;
        }

    }

    private static void delay() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {

        }
    }
    
    private static boolean delayBoolean(int delay_length) {
        try {
            Thread.sleep(delay_length);
        } catch (InterruptedException e) {

        }
        
        return true;
    }
    
    private static boolean displayMethodName(String call_name, boolean for_return) {
        
    	
    	System.out.println(call_name);
        
        return for_return;
    }
    
    

}
