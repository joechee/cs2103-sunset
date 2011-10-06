package cs2103.aug11.t11j2.fin.ui;

import cs2103.aug11.t11j2.fin.parser.CommandResult;

public interface IUserInterface {
	void mainLoop();
	// Actually, I don't think updateContext needs to be a UI interface method. It should not be publicly accessible.
	// void updateContext(CommandResult feedback);
}
