package cs2103.aug11.t11j2.fin.parser;
/**
 * 
 * @author Joe Chee
 *
 */

public class HelpTablePair {
	private String usage;
	private String description;
	public HelpTablePair(String usage, String description) {
		this.usage = usage;
		this.description = description;
	}

	public String getUsage() {
		return usage;
	}
	public String getDescription() {
		return description;
	}
}

