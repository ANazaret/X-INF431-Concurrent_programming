import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class SortMapper extends Mapper<LongWritable,Text,FloatWritable,Text> {
	@Override
	public void map(LongWritable key, Text value, Context context) {
		String[] data = value.toString().split("\t");
		String source = data[0];
		float pr = Float.parseFloat(data[1]);
		
		try {
			context.write(new FloatWritable(pr), new Text(source));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
