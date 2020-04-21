package no.ntnu.idata2001.contacts.model;

import org.eclipse.persistence.exceptions.DatabaseException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.logging.Level;

import static org.eclipse.persistence.jpa.jpql.parser.Expression.SET;


public class AddressBookDBHandler implements Iterable<ContactDetails>, Serializable,AddressBook  {
    private final EntityManagerFactory emf;
    private EntityManager em;
    private  static final Logger logger = Logger.getLogger(AddressBookDBHandler.class.getName());


    public AddressBookDBHandler() {
        this.emf = Persistence.createEntityManagerFactory("contacts-pu");
    }

    @Override
    public void addContact(ContactDetails contact) {
      em = this.emf.createEntityManager();

        try {
                 em.getTransaction().begin();
                 em.persist(contact);// contact into persistence context
                 em.getTransaction().commit();//store in database


        } catch (DatabaseException dbe){
            logger.info(dbe.getMessage());
        }
        finally {

            close();
        }
    }




    @Override
    public void removeContact(String phoneNumber) {

        em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            String delete = "DELETE FROM ContactDetails contact " +
                    "WHERE contact.phone ='" + phoneNumber + "'";
            em.createQuery(delete)
                    .executeUpdate();
            em.getTransaction().commit();


        } catch (DatabaseException de){
            logger.info(de.getMessage());
        }
        finally {
            close();
        }
    }





    @Override
    public Collection<ContactDetails> getAllContacts() {
         em = emf.createEntityManager();
        Query q = em.createQuery
                ("SELECT OBJECT(a) FROM ContactDetails a");


        return q.getResultList();
    }

    @Override
    public Iterator<ContactDetails> iterator() {

        return getAllContacts().iterator();

    }


    @Override
    public void changeDetails(String oldPhone, ContactDetails contact){
         em = this.emf.createEntityManager();
        try {

            em.getTransaction().begin();
           em.createQuery("UPDATE ContactDetails c SET c.name='"+contact.getName()+"'," +
                   " c.phone='"+contact.getPhone()+"',c.address='"+contact.getAddress()+"' WHERE c.phone='"+oldPhone+"'")
                   .executeUpdate();
              em.getTransaction().commit();



        }catch (DatabaseException dbe){
            logger.info(dbe.getMessage());
        }

        finally{
            close();

        }

    }

    @Override
    public void close() {
        em = emf.createEntityManager();

        try {

            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();

                }


        }catch (DatabaseException dbe){
            logger.fine(dbe.getMessage());

        }
    }
}
