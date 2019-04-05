# Flink CEP Examples

## First one (AlertsGeoHashRepSubJob)

There is a IoT device counting the numbers of events in a zone (for example the number of bicycles crossing a point). These events are sent to a queue, serialized as avro type events

These events are read from a flink job. First the job calculate the diffence between the number of events of two signals. Then the enriched events pass to a CEP pattern and if two consecutive events has an increment of 30% the job send an alarm. This is calculted per zone.

This is simulate using ``GeoHashEventsGenerator`` class

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

 