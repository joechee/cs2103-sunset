package cs2103.aug11.t11j2.fin.storage;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class Serializer {
	Yaml yaml;
	
	public Serializer() {
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		yaml = new Yaml(options);
	}
	
	public void serialize(Iterator<Object> objects, String filename) throws IOException{
		File saveFile = new File(filename);
		if (!saveFile.exists()) {
			saveFile.createNewFile();
		}
		FileWriter writer = new FileWriter(saveFile);
		yaml.dumpAll(objects,writer);
		writer.close();
	
	}
	
	public List<Object> unserialize(String filename) throws IOException {
		InputStream file = new FileInputStream(filename);
		ArrayList<Object> deserializedObjects = new ArrayList<Object>();
		for (Object data: yaml.loadAll(file)) {
			deserializedObjects.add(data);
		}
		file.close();
		return deserializedObjects;
	}

}
