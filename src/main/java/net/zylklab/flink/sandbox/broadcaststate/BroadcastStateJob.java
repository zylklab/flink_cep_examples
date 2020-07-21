package net.zylklab.flink.sandbox.broadcaststate;

import java.util.Properties;

import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.BroadcastConnectedStream;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.flink.sandbox.broadcaststate.pojo.Event;
import net.zylklab.flink.sandbox.broadcaststate.pojo.Limit;
import net.zylklab.flink.sandbox.cep_examples.util.avro.AvroDeserializationSchema;

public class BroadcastStateJob {

	private static final Logger log = LoggerFactory.getLogger(BroadcastStateJob.class);
	
	private static final String BOOTSTRAP_SERVERS = "amaterasu001.bigdata.zylk.net:6667, amaterasu002.bigdata.zylk.net:6667";
	private static final String GROUP_ID = "flink-broadcast-state";
	
	private static final String LIMIT_TOPIC = "FLINK-BCS-LIMITS";
	private static final String EVENT_TOPIC = "FLINK-BCS-EVENTS";
	
	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		BroadcastStateJob s = new BroadcastStateJob();
		s.addJob(env);
		env.execute("Flink Broadcasted State Job");
		
	}
	
	private void addJob(StreamExecutionEnvironment env) {
		
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", BOOTSTRAP_SERVERS);
		props.setProperty("group.id", GROUP_ID);
		
		MapStateDescriptor<String, Limit> bcedDescriptor = new MapStateDescriptor<String, Limit>("limits", Types.STRING, Types.POJO(Limit.class));
		BroadcastStream<Limit> bcedLimit = env
				.addSource(new FlinkKafkaConsumer<>(LIMIT_TOPIC, new AvroDeserializationSchema<>(Limit.class), props))
				.broadcast(bcedDescriptor);
		
		DataStreamSource<Event> eventStream = env.addSource(new FlinkKafkaConsumer<>(EVENT_TOPIC, new AvroDeserializationSchema<>(Event.class), props));
//				.keyBy((KeySelector<Event, String>) event -> event.getVarId());
		
		SingleOutputStreamOperator<Event> processStream = eventStream
				.connect(bcedLimit)
				.process(new BroadcastProcessFunction<Event, Limit, Event>() {

					private static final long serialVersionUID = 1L;

					@Override
					public void processBroadcastElement(Limit limit,
							BroadcastProcessFunction<Event, Limit, Event>.Context ctx,
							Collector<Event> out) throws Exception {
						
						BroadcastState<String, Limit> bcState = ctx.getBroadcastState(bcedDescriptor);
						bcState.put(limit.getVarId(), limit);
						
					}

					@Override
					public void processElement(Event event,
							BroadcastProcessFunction<Event, Limit, Event>.ReadOnlyContext ctx,
							Collector<Event> out) throws Exception {
						
						ReadOnlyBroadcastState<String, Limit> mapState = ctx.getBroadcastState(bcedDescriptor);
						String key = event.getVarId();
						if (mapState.contains(key)) {
							Limit limit = mapState.get(key);
							if (event.getValue() > limit.getMinLimit() && event.getValue() < limit.getMaxLimit()) {
//								out.collect(event);
								log.info("Event OK");
							} else {
								log.info("Event out of limits");
							}
						} else {
							log.info(String.format("Limit for this event ID (%s) has not been set yet", event.getVarId()));
//							out.collect(event);
						}
					}
				});
		
		processStream.print();
		
	}
}
