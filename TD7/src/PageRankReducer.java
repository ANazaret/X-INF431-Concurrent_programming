import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class PageRankReducer extends Reducer <Text, Text, Text, Text> {
	@Override
	public void reduce(Text page, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		double d = 0.85;
		long N = context.getConfiguration().getLong("numberOfPages", 1);
		
		double popularity = 0.0;
		
		String source;
		double pop;
		int degree;
		String buts = "";
		for(Text val : values) {
			String[] data = val.toString().split("\t");
			if (data.length == 1) {
				//Pas de tabulations, c'est la description du lien
				buts = data[0];
			} else {
				source = data[0];
				pop = Double.parseDouble(data[1]);
				degree = Integer.parseInt(data[2]);
				popularity += pop/degree;
			}
			
		}
		
		popularity *= d;
		popularity += (1-d)/N;
		
		context.write(page, new Text(String.valueOf(popularity) + "\t" + buts));
	}
}
