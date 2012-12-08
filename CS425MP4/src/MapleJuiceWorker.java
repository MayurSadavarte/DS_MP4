import java.util.Vector;


public class MapleJuiceWorker implements Runnable {
	Machine machine=null;
	String sdfs_exe=null;
	Integer no_reduce=null;
	String red_op_file=null;
	String sdfs_prefix=null;
	Vector<String> map_ip_files=null;
	boolean map=false;
	boolean reduce=false;
	
	public MapleJuiceWorker(Machine m, String red_exe, Integer no_red_tasks, String op_file, String red_prefix) {
		machine = m;
		sdfs_exe = red_exe;
		no_reduce=no_red_tasks;
		red_op_file = op_file;
		sdfs_prefix=red_prefix;
		reduce=true;
	}
	
	public MapleJuiceWorker(Machine m, String map_exe, Vector<String> map_files, String map_prefix) {
		machine = m;
		sdfs_exe = map_exe;
		map_ip_files = map_files;
		sdfs_prefix = map_prefix;
		map=true;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(map) {
			machine.mapleJuiceListener.processMapleCommand(sdfs_exe, map_ip_files, sdfs_prefix);
		} else if(reduce) {
			machine.mapleJuiceListener.processJuiceCommand(sdfs_exe, no_reduce, red_op_file, sdfs_prefix);
		}
		
	}

}
