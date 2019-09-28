# Flink CEP Examples

## First one (AlertsGeoHashRepSubJob)

There is a IoT device counting the numbers of events in a zone (for example the number of bicycles crossing a point). These events are sent to a queue, serialized as avro type events

These events are read from a flink job. First the job calculate the diffence between the number of events of two signals. Then the enriched events pass to a CEP pattern and if two consecutive events has an increment of 30% the job send an alarm. This is calculted per zone.

This is simulate using ``GeoHashEventsGenerator`` class invoked from ``AlertGeoHashRepSubJob`` main method

```java
DataStream<GeoHashEvent> inputEventStream = env.addSource(new GeoHashEventsGenerator(PAUSE, NUMBER_OF_EVENTS_STD, NUMBER_OF_EVENTS_MEAN, NUMBER_OF_ZONES))
```
where

```java
private static final int PAUSE = 15000; 
private static final int NUMBER_OF_EVENTS_STD = 100;
private static final int NUMBER_OF_EVENTS_MEAN = 180;
private static final int NUMBER_OF_ZONES = 2;
private static final int DELTA_LIMIT = 30;
```

 * **PAUSE** is the miliseconds between two events are fired
 * **NUMBER_OF_EVENTS_STD** is the standar deviation of the gaussian that is using to create the random count
 * **NUMBER_OF_EVENTS_MEAN** is the mean of the gaussian
 * **NUMBER_OF_ZONES** is the number of distinct zones where events are created
 * **DELTA_LIMIT** is the upper limit fot the diffrence between two consecutive events, in the same zone

## Second one (EventTime)

There is a IoT device counting the numbers of events in a zone (for example the number of bicycles crossing a point). These events are sent to a queue, serialized as avro type events. This is simulate as events send as a text to a localhost socket. To start the socket run this command

```bash
nc -lk 9999
```

Then you can start the flink job from you IDE and when you copy and paste some events into the sockets the events are processed by the job. The important thing is that this job is configured to use the event time not the process time.

The jobs are

```java
net.zylklab.flink.sandbox.event_time_example.job.EventTimeWindowGeoHashSubJob
net.zylklab.flink.sandbox.event_time_example.job.EventTimeSlideWindowGeoHashSubJob
```

where

```java
private static final Time WINDOW_TIME_SIZE = Time.of(30, TimeUnit.SECONDS);
private static final Time WINDOW_TIME_SLIDE = Time.of(30, TimeUnit.SECONDS);
private static final Time ALLOWED_LATENESS_TIME = Time.of(30, TimeUnit.SECONDS);
private static final long MAX_OUT_OF_ORDERNESS_MS = 3500l;
```

 * **WINDOW_TIME_SIZE** the size (time) of the window 
 * **WINDOW_TIME_SLIDE** the slide (time) of the window
 * **ALLOWED_LATENESS_TIME** the time that an already processed window can be reprocessed if an event belonging to this window arrives
 * **MAX_OUT_OF_ORDERNESS_MS** the maximum time it takes for a window to launch for the first time, allows waiting for messy events to arrive without getting lost

### Results


 |window type| window size | window slide | window allow lateness | max out of orderness | number of events| file |
 |-----------|-------------|-------- -----|-----------------------|----------------------|-----------------|------|
 |Normal| 5 seconds | - | 0 seconds | 0 ms | 5 windows with 5 events per window | orderer messages|
 