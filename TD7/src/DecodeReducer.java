import java.io.IOException;
import java.util.LinkedList;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class DecodeReducer extends Reducer <Text, Text, Text, Text> {
	@Override
	public void reduce(Text page, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		LinkedList<String> res = new LinkedList<>();		
		String tmp;
		for (Text t : values) {
			tmp = t.toString();
			if (!tmp.equals("#"))
				res.add(t.toString());
		}
		
		context.getCounter(MyCounters.NumberOfPages).increment(1);		
		context.write(page, new Text("1.0\t" +  String.join(",", res)));
		
	}
}
