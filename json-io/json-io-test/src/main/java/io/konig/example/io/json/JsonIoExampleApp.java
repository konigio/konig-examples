package io.konig.example.io.json;

import java.io.InputStream;
import java.io.StringWriter;

import com.example.io.canonical.schema.CreativeWorkJsonReader;
import com.example.io.shapes.CreativeWorkShapeJsonWriter;
import com.example.model.schema.CreativeWork;

public class JsonIoExampleApp {

	public static void main(String[] args) throws Exception {
		
		InputStream input = JsonIoExampleApp.class.getClassLoader().getResourceAsStream("data.json");
		
		CreativeWorkJsonReader reader = new CreativeWorkJsonReader();
		CreativeWork work = reader.read(input);
		
		StringWriter out = new StringWriter();
		CreativeWorkShapeJsonWriter writer = new CreativeWorkShapeJsonWriter();
		writer.write(work, out);
		
		out.close();
		
		System.out.println(out.toString());
	}

}
