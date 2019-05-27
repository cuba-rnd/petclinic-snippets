package com.company.clinic.listener;

import com.company.clinic.entity.Visit;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("clinic_VisitEntityListener")
public class VisitEntityListener implements BeforeInsertEntityListener<Visit> {

    @Inject
    private UniqueNumbersService uniqueNumbersService;

    @Override
    public void onBeforeInsert(Visit entity, EntityManager entityManager) {
        long visitNumber = uniqueNumbersService.getNextNumber(entity.getVeterinarian().getUser().getLogin());
        entity.setNumber(String.format("%d-%tD", visitNumber, entity.getDate()));
    }
}
