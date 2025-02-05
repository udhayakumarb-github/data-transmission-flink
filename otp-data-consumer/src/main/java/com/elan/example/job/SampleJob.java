package com.elan.example.job;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

public class SampleJob {
    public static void main(String args[]) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Person> personDataStream = env.fromCollection(getPersonList(), TypeInformation.of(Person.class));

        KeyedStream<Person, String> personStringKeyedStream = personDataStream.keyBy(Person::getCity);

        personStringKeyedStream.

        env.execute("Person Job Executed");

    }

    private static List<Person> getPersonList() {

        List<Person> personList = new ArrayList<>();

        personList.add(new Person(1, "Udhayakumar", "Baskaran", "Viluppuram"));
        personList.add(new Person(2, "Sivapriya", "Gunanithi", "Viluppuram"));
        personList.add(new Person(3, "Sathish", "Kumar", "Chennai"));
        personList.add(new Person(4, "Jaya", "Prabha", "Kumbakonam"));
        personList.add(new Person(5, "Saravanan", "Kumar", "Chennai"));

        return personList;
    }

    private void processVillupuram(DataStream<Person> villupuarmPerson) {
        // some logic
    }

    private void processChennai(DataStream<Person> chennaiPerson) {
        // some logic
    }

    private void processKumbakonam(DataStream<Person> kumnakonamPerson) {
        // some logic
    }
}
