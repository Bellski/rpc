package ru.bellski;

import org.gwtproject.rpc.serialization.api.SerializationStreamReader;
import org.gwtproject.rpc.serialization.api.SerializationStreamWriter;
import org.gwtproject.rpc.serialization.api.SerializationWiring;
import org.gwtproject.rpc.serialization.api.TypeSerializer;
import org.gwtproject.rpc.serialization.stream.string.StringSerializationStreamReader;
import org.gwtproject.rpc.serialization.stream.string.StringSerializationStreamWriter;

import java.io.Serializable;
import java.util.Arrays;

public class TestSerialize {

    public static class MyStreet extends Street implements Serializable {

        public MyStreet() {
        }

        public MyStreet(String name) {
            super(name);
        }
    }


    public static class Street implements Serializable {
        public String name;

        public Street() {
        }

        public Street(String name) {
            this.name = name;
        }
    }

    public static class Address implements Serializable {
        Street[] streets;

        public Street[] getStreets() {
            return streets;
        }

        public void setStreets(Street[] streets) {
            this.streets = streets;
        }
    }

    @SerializationWiring
    public interface AddressSerializer {
        TypeSerializer createSerializer();

        void writeAddress(Address address, SerializationStreamWriter writer);

        Address readAddress(SerializationStreamReader reader);
    }

    public static void main(String[] args) {
        final AddressSerializer addressSerializer = new AddressSerializer_Impl();
        final TypeSerializer serializer = addressSerializer.createSerializer();
        final StringSerializationStreamWriter serializationStreamWriter = new StringSerializationStreamWriter(serializer);

        serializationStreamWriter.prepareToWrite();
        final Address address = new Address();
        address.setStreets(new Street[] {new MyStreet("My Street")});

        addressSerializer.writeAddress(address, serializationStreamWriter);

        System.out.println("serializer checkSum: " + serializer.getChecksum());
        System.out.println("serializer payload: " + serializationStreamWriter.toString());

        final TypeSerializer deserializer = addressSerializer.createSerializer();

        System.out.println("deserializer checkSum: " + deserializer.getChecksum());

        final StringSerializationStreamReader stringSerializationStreamReader = new StringSerializationStreamReader(deserializer, serializationStreamWriter.toString());
        final Address desAddress = addressSerializer.readAddress(stringSerializationStreamReader);

        System.out.println(Arrays.asList(desAddress.streets));
    }
}
