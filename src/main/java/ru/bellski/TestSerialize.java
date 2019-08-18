package ru.bellski;

import org.gwtproject.rpc.serialization.api.SerializationStreamReader;
import org.gwtproject.rpc.serialization.api.SerializationStreamWriter;
import org.gwtproject.rpc.serialization.api.SerializationWiring;
import org.gwtproject.rpc.serialization.api.TypeSerializer;
import org.gwtproject.rpc.serialization.stream.string.StringSerializationStreamWriter;

import java.io.Serializable;
import java.util.ArrayList;

public class TestSerialize {

    public static class Street implements Serializable {
        public String name;

        public Street() {
        }

        public Street(String name) {
            this.name = name;
        }
    }

    public static class Address implements Serializable {
        ArrayList<Street> streets = new ArrayList<>();

        public ArrayList<Street> getStreets() {
            return streets;
        }

        public void addStreet(Street street) {
            streets.add(street);
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
        StringSerializationStreamWriter serializationStreamWriter = new StringSerializationStreamWriter(
                addressSerializer.createSerializer()
        );
        serializationStreamWriter.prepareToWrite();

        final Address address = new Address();
        address.addStreet(new Street("My Street"));

        addressSerializer.writeAddress(address, serializationStreamWriter);

        System.out.println(serializationStreamWriter.toString());
    }
}
