package org.bank.bankingsystem.service;


import lombok.RequiredArgsConstructor;
import org.bank.bankingsystem.dao.AccountDao;
import org.bank.bankingsystem.dao.BeneficiaryDao;
import org.bank.bankingsystem.dto.BeneficiaryRequest;
import org.bank.bankingsystem.exception.ResourceNotFoundException;
import org.bank.bankingsystem.model.Account;
import org.bank.bankingsystem.model.Beneficiary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryDao beneficiaryDao;
    private final AccountDao accountDao;

    public Beneficiary addBeneficiary(
            BeneficiaryRequest request) {

        Account account =
                accountDao.findByAccountNumber(
                                request.getAccountNumber())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found"));

        Beneficiary beneficiary =
                Beneficiary.builder()
                        .beneficiaryName(
                                request.getBeneficiaryName())
                        .beneficiaryAccountNumber(
                                request.getBeneficiaryAccountNumber())
                        .bankName(
                                request.getBankName())
                        .account(account)
                        .build();

        beneficiaryDao.save(beneficiary);

        return beneficiary;
    }
}
