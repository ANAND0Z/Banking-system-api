package org.bank.bankingsystem.dao;


import lombok.RequiredArgsConstructor;
import org.bank.bankingsystem.model.Beneficiary;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BeneficiaryDao {

    private final SessionFactory sessionFactory;

    public void save(Beneficiary beneficiary) {

        sessionFactory
                .getCurrentSession()
                .persist(beneficiary);
    }

    public Optional<Beneficiary> findById(Long id) {

        return Optional.ofNullable(
                sessionFactory
                        .getCurrentSession()
                        .get(Beneficiary.class, id)
        );
    }

    public List<Beneficiary> findAll() {

        return sessionFactory
                .getCurrentSession()
                .createQuery(
                        "FROM Beneficiary",
                        Beneficiary.class)
                .getResultList();
    }

    public void delete(Beneficiary beneficiary) {

        sessionFactory
                .getCurrentSession()
                .remove(beneficiary);
    }
}