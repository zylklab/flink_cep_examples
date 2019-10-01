package net.zylklab.flink.sandbox.cep_examples.util.generator;

import java.util.Random;

import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import net.zylklab.flink.sandbox.cep_examples.pojo.GeoHashEvent;

import org.apache.flink.configuration.Configuration;

public class GeoHashEventsGenerator extends RichParallelSourceFunction<GeoHashEvent>  {
	
	private static final long serialVersionUID = 9052538134820704899L;
	private boolean running = true;
	private final long pause;
	private final int numberOfEventsStd;
	private final int numberOfEventsMean;
	private final int numberOfZones;
	private Random random;
	

	public GeoHashEventsGenerator(long pause, int numberOfEventsStd, int numberOfEventsMean, int numberOfZones) {
		this.pause = pause;
		this.numberOfEventsStd = numberOfEventsStd;
		this.numberOfEventsMean = numberOfEventsMean;
		this.numberOfZones = numberOfZones;
	}

	@Override
	public void open(Configuration configuration) {
		random = new Random();
	}

	public void run(SourceContext<GeoHashEvent> sourceContext) throws Exception {
		while (running) {
			GeoHashEvent event = null;
			int numberOfEvents = Math.abs(new Double(random.nextGaussian() * numberOfEventsStd + numberOfEventsMean).intValue());
			int zone = random.nextInt(this.numberOfZones);
			event = new GeoHashEvent("geohash-00"+zone,numberOfEvents, System.currentTimeMillis());
			sourceContext.collect(event);
			Thread.sleep(pause);
		}
	}
	
	public void cancel() {
		running = false;
	}

}
