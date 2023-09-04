package lu.pcy113.pdr.engine.logic;

public interface GameLogic {
	
	void init();
	
	void input(float dTime);
	
	void update(float dTime);
	
	void render(float dTime);
	
}
