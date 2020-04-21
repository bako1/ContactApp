package no.ntnu.idata2001.contacts.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

public interface AddressBook extends Serializable, Iterable<ContactDetails> {
    void addContact(ContactDetails contact);

    void removeContact(String phoneNumber);

    Collection<ContactDetails> getAllContacts();

    @Override
    Iterator<ContactDetails> iterator();

    void close();
    void changeDetails(String oldPhone, ContactDetails contact);
}
