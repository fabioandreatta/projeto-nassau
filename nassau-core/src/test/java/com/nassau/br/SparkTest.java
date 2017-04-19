package com.nassau.br;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nassau.br.spark.SparkConfig;
import com.nassau.br.spark.SparkTemplate;

import scala.Tuple2;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SparkConfig.class})
public class SparkTest implements Serializable {
	private static final long serialVersionUID = 6897937908933464485L;
	
	@Autowired
	private SparkTemplate spark;
	
	@Test
	public void testSpark() {
		JavaStreamingContext jssc = spark.createSparkStreamingContext(10);
		JavaPairInputDStream<String, SerializedDFe> messages = spark.createDirectKafkaDStream(jssc, Arrays.asList("incoming.dfe"));
				
		messages.foreachRDD(new VoidFunction<JavaPairRDD<String, SerializedDFe>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void call(JavaPairRDD<String, SerializedDFe> rdd) throws Exception {
				rdd.foreachPartition(new VoidFunction<Iterator<Tuple2<String, SerializedDFe>>>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void call(Iterator<Tuple2<String, SerializedDFe>> items) throws Exception {
						while (items.hasNext()) {
							SerializedDFe dfe = items.next()._2;
							System.out.println("DFe: (" + dfe.id() + ", " + dfe.getSerializedData() + ")");
						}
					}
				});
			}
		});
		
		try {
			jssc.start();
			jssc.awaitTerminationOrTimeout(600000);
		} catch (InterruptedException e) {}
		jssc.stop();
	}
}
