
import java.io.IOException;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class PageRankMapper extends Mapper<LongWritable,Text,Text,Text> {
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] data = value.toString().split("\t");	
		String source = data[0];
		String popularity = data[1];
		String[] targets;
		if (data.length == 3)
			targets = data[2].split(",");
		else
			targets = new String[0];
		
			
		
		Text outputvalue = new Text(String.join("\t", source,popularity,String.valueOf(targets.length)));
		
		for (String t: targets) {
			context.write(new Text(t), outputvalue);
		}
		
		context.write(new Text(source), new Text(data.length == 3 ? data[2] : ""));
	}
}
