package com.company.clinic.listener;

import com.company.clinic.entity.Visit;
import com.google.common.base.Strings;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.app.events.AttributeChanges;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EmailException;
import com.haulmont.cuba.core.global.EmailInfo;
import com.haulmont.cuba.core.global.MetadataTools;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.inject.Inject;
import java.util.UUID;

@Component("clinic_VisitChangedListener")
public class VisitChangedListener {

    @Inject
    private Logger log;
    @Inject
    private DataManager dataManager;
    @Inject
    private MetadataTools metadataTools;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void visitChanged(EntityChangedEvent<Visit, UUID> event) {
        AttributeChanges changes = event.getChanges();
        if (changes.isChanged("date")) {
            Id<Visit, UUID> entityId = event.getEntityId();
            Visit visit = dataManager.load(entityId).view("visit-edit").one();

            sendDateChangedEmail(visit);
        }
    }

    protected void sendDateChangedEmail(Visit visit) {
        String email = visit.getVeterinarian().getUser().getEmail();
        if (!Strings.isNullOrEmpty(email)) {
            EmailInfo emailInfo = new EmailInfo(
                    email, // recipients
                    "Visit date has been changed", // subject
                    createEmailBody(visit)
            );
            log.info(emailInfo.toString());
        }
    }

    protected String createEmailBody(Visit visit) {
        return "Pet: " + metadataTools.getInstanceName(visit.getPet()) +
                "\nDescription: " + visit.getDescription() +
                "\nNew date: " + visit.getDate();
    }
}
