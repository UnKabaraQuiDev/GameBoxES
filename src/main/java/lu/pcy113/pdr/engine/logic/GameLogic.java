package lu.pcy113.pdr.engine.logic;

import lu.pcy113.pdr.engine.GameEngine;

public interface GameLogic {

	void init(GameEngine e);

	void input(float dTime);

	void update(float dTime);

	void render(float dTime);

}
