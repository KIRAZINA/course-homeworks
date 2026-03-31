package app;

/**
 * Address class represents user's address information.
 * Immutable design: all fields are final, no setters.
 */
public final class Address {
    private final String street;
    private final String city;
    private final String country;

    public Address(String street, String city, String country) {
        this.street = street;
        this.city = city;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s", street, city, country);
    }
}
