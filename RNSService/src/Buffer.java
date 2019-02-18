import java.util.Vector;

public class Buffer {

	private Vector<Object> data;

	  public  void add(Object o) {
	    data.add(o);
	  }

	  public synchronized Object get() {
	    Object obj = null;

	    if (data.size()!=0) {
	    	
	      obj = data.elementAt(0);
	      
	      data.removeElementAt(0);
	    }
	    return obj;
	  }

}
