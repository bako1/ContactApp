package no.ntnu.idata2001.contacts.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

public class AddressBookDBHandler implements Iterable<ContactDetails>, Serializable,AddressBook  {
    private final EntityManagerFactory emf;


    public AddressBookDBHandler() {
        this.emf = Persistence.createEntityManagerFactory("contacts-pu");
    }

    @Override
    public void addContact(ContactDetails contact) {
        EntityManager em = this.emf.createEntityManager();
        try {

            em.getTransaction().begin();
            em.persist(contact);// contact into persistence context
            em.getTransaction().commit();//store in database }finally{ closeEM(em); }
        } finally {

            close();
        }
    }




    @Override
    public void removeContact(String phoneNumber) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            String delete = "DELETE FROM ContactDetails contact " +
                    "WHERE contact.phone ='"+phoneNumber+"'";
            em.createQuery(delete)
                    .executeUpdate();
            em.getTransaction().commit();

        } finally {
            close();
        }


    }

    @Override
    public Collection<ContactDetails> getAllContacts() {


        EntityManager  em = emf.createEntityManager();
        Query q = em.createQuery
                ("SELECT OBJECT(a) FROM ContactDetails a");


        return q.getResultList();
    }

    @Override
    public Iterator<ContactDetails> iterator() {

        return getAllContacts().iterator();

    }

    @Override
    public void close() {
          emf.close();
        EntityManager em = emf.createEntityManager();

         em.close();

    }




    @Override
    public void changeDetails(String oldPhone, ContactDetails contact){
        EntityManager em = this.emf.createEntityManager();
        try {

            em.getTransaction().begin();


            String updateQuery =
                      "UPDATE ContactDetails " +
                              "SET name='"+contact.getName()+"',phone='"+contact.getPhone()
                              +"',address="+contact.getAddress()+"', 'WHERE phone='"+oldPhone+"'";

              em.createQuery(updateQuery).executeUpdate();

              em.getTransaction().commit();


        }finally{

        }

    }
}
