import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class DecodeMapper extends Mapper<LongWritable,Text,Text,Text> {
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		/*String[] data = value.toString().split("\t");
		String source = data[0];
		String target = data[1];
		context.write(new Text(source), new Text(target));
		context.write(new Text(target), new Text("#"));*/
		
		WikiPage page = new WikiPage(value.toString());
		String link;
		Text source = new Text(page.title());
		do {
			link = page.link();
			if (link != null) {
				context.write(source, new Text(link) );
				context.write(new Text(link), new Text("#"));
			} else {
				context.write(source, new Text("#") );
			}
		} while (link != null);
		
	}
}
