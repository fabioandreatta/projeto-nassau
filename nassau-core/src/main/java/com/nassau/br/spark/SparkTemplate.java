package com.nassau.br.spark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.nassau.br.SerializedDFe;
import com.nassau.br.kafka.SerializedDFeDeserializer;

import kafka.serializer.StringDecoder;

/**
 * Spark encapsulation class
 * @author fsantos
 */
@Component
public class SparkTemplate implements Serializable {
	private static final long serialVersionUID = -8491078609345043472L;
	
	@Autowired
	private SparkConf configuration;
	
	/**
	 * Creates a Spark Streaming Context
	 * 	TODO we should think about initializing Spring in the executors in a different way
	 * 
	 * @param duration
	 * @return
	 */
	public JavaStreamingContext createSparkStreamingContext(long duration) {
		JavaStreamingContext jssc = new JavaStreamingContext(configuration, Durations.seconds(duration));
		JavaSparkContext jsc = jssc.sparkContext();
		List<Integer> items = new ArrayList<Integer>();
		for (int i = 0; i < 1000; i++) items.add(i);
		JavaRDD<Integer> rdd = jsc.parallelize(items);
		rdd.foreachPartition(new VoidFunction<Iterator<Integer>>() {
			private static final long serialVersionUID = 7140466245497655555L;

			@SuppressWarnings("resource")
			@Override
			public void call(Iterator<Integer> items) throws Exception {
				new AnnotationConfigApplicationContext(SparkConfig.class);
			}
		});
		return jssc;
	}

	/**
	 * Create a Kafka direct stream
	 * @param jssc
	 * @param topics
	 * @return
	 */
	public JavaPairInputDStream<String, SerializedDFe> createDirectKafkaDStream(JavaStreamingContext jssc, List<String> topics) {
		Set<String> topicsSet = new HashSet<String>(topics);
	    Map<String, String> kafkaParams = new HashMap<String, String>();
	    kafkaParams.put("zookeeper.connect", 	"s0767.ms:2181,s0768.ms:2181,s0769.ms:2181");
	    kafkaParams.put("group.id", 			"nassau-module");
	    kafkaParams.put("bootstrap.servers", 	"s0767.ms:6667,s0769.ms:6667");
	    return KafkaUtils.createDirectStream(
		        jssc,
		        String.class,
		        SerializedDFe.class,
		        StringDecoder.class,
		        SerializedDFeDeserializer.class,
		        kafkaParams,
		        topicsSet
		    );
	}
}
