package cs2103.aug11.t11j2.fin.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class Serializer {
	/**
	 * Serializer serializes an object and dumps it into a file using YAML.
	 * @author Joe Chee
	 */
	Yaml yaml;

	public Serializer() {
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		yaml = new Yaml(options);
	}
	/**
	 * Takes in an Iterator of objects and dumps it into a file.
	 * @param objects
	 * @param filename
	 * @throws IOException
	 * @see #unserialize
	 */

	public void serialize(Iterator<Object> objects, String filename)
			throws IOException {
		File saveFile = new File(filename);
		if (!saveFile.exists()) {
			saveFile.createNewFile();
		}
		FileWriter writer = new FileWriter(saveFile);
		yaml.dumpAll(objects, writer);
		writer.close();

	}
	/**
	 * Takes in the filename of the file, and unserializes the file
	 * returns all the serialized objects in the file as a list.
	 * 
	 * @param filename
	 * @return List of Objects
	 * @throws IOException
	 */

	public List<Object> unserialize(String filename) throws IOException {
		InputStream fs = new FileInputStream(filename);
		ArrayList<Object> deserializedObjects = new ArrayList<Object>();
		for (Object data : yaml.loadAll(fs)) {
			deserializedObjects.add(data);
		}
		fs.close();
		return deserializedObjects;
	}
	public void createFile(String filename) throws IOException {
		File newFile = new File(filename);
		newFile.createNewFile();
		
	}

}
