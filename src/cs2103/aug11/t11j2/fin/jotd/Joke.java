package cs2103.aug11.t11j2.fin.jotd;

import java.io.IOException;
import java.util.*;

import cs2103.aug11.t11j2.fin.storage.Serializer;

public class Joke {
	List<String> jokeList ;
	Serializer jokeSerial;

	public Joke() throws IOException {
		jokeList = new ArrayList<String>();
		jokeSerial = new Serializer();
		//System.out.println(jokeSerial.unserialize("Joke.yaml").get(1));
		List<Object> serialList = jokeSerial.unserialize("Joke.yaml");
		Iterator<Object> serialListIterator = serialList.iterator();
		while (serialListIterator.hasNext()) {
			jokeList.add((String) serialListIterator.next());
		}
		
		
	}
	

	public String generate() {
		Random RNG = new Random();
		return jokeList.get(RNG.nextInt(jokeList.size()));
	}
		
	public void addJoke(String joke) throws IOException {
		jokeList.add(joke);
		saveJokes();
	}
	
	public void saveJokes() throws IOException {
		jokeSerial.serialize((new ArrayList<Object>(jokeList)).iterator(),"Joke.yaml");
	}
		
			
		
}
