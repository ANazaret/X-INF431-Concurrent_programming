import java.io.IOException;
import java.util.LinkedList;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class SortReducer extends Reducer <FloatWritable, Text, FloatWritable, Text> {
	@Override
	public void reduce(FloatWritable rank, Iterable<Text> pages, Context context) {
		LinkedList<String> res = new LinkedList<>();
		
		for (Text t : pages)
			res.add(t.toString());
		
		try {
			context.write(rank, new Text(String.join(",", res)));
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
