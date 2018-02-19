package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.AcquirerMerchant;
import com.avantir.blowfish.model.AcquirerTerminalParameter;
import com.avantir.blowfish.repository.AcquirerMerchantRepository;
import com.avantir.blowfish.repository.AcquirerTerminalParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class AcquirerTerminalParameterService {

    @Autowired
    private AcquirerTerminalParameterRepository acquirerTerminalParameterRepository;


    @Transactional(readOnly=true)
    public AcquirerTerminalParameter findById(Long id) {

        try
        {
            //Optional<AcquirerMerchant> optional = acquirerMerchantRepository.findById(id);
            //return optional.orElse(null);
            return acquirerTerminalParameterRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public AcquirerTerminalParameter findByAcquirerId(Long acquirerId) {

        try
        {
            return acquirerTerminalParameterRepository.findByAcquirerId(acquirerId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



}