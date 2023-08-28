package lu.pcy113.pdr.engine.graph.shader;
public class ShaderModuleData {
	
		private String file;
		private int type;
		
		public ShaderModuleData(String f, int t) {
			this.file = f;
			this.type = t;
		}
		
		public String getFile() {return file;}
		public int getType() {return type;}
		public void setFile(String file) {this.file = file;}
		public void setType(int type) {this.type = type;}
		
	}