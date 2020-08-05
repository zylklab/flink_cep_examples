package net.zylklab.flink.sandbox.unordered_events;

import java.util.HashMap;

import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.flink.sandbox.broadcaststate.pojo.Event;

/**
 * This Class provides an implementation on top of Flink's KeyedProcessFunction that acts as a buffer for ordering unordered events
 */
public class BufferedKeyedProcessFunction extends KeyedProcessFunction<String, Event, Event>{

	private static final long serialVersionUID = 1L;
	
	private static final Logger _log = LoggerFactory.getLogger(BufferedKeyedProcessFunction.class); 

	private HashMap<Long, Event> eventsMap = new HashMap<Long, Event>();

	@Override
	public void processElement(
			Event in,
			KeyedProcessFunction<String, Event, Event>.Context ctx,
			Collector<Event> out)
					throws Exception {
		
		Long ts = in.getTs();
		if (ctx.timerService().currentWatermark() < ts) {
			// Put event in the map keyed by its timestamp
			eventsMap.put(ts, in);
			
			// Register an Event Timer to be triggered when the watermark reaches this timestamp
			ctx.timerService().registerEventTimeTimer(ts);
		} else {
			_log.warn("Current watermark has already passed this event!");
		}
	}
	
	@Override
	public void onTimer(
			long timestamp,
			KeyedProcessFunction<String, Event, Event>.OnTimerContext ctx,
			Collector<Event> out)
					throws Exception {
		
		// Emit event with this timestamp
		if (eventsMap.containsKey(timestamp)) {
			out.collect(eventsMap.get(timestamp));
			eventsMap.remove(timestamp);
		} else {
			_log.info("onTimer triggered but no value set for timestamp " + timestamp);
		}
	}
	
		
}
