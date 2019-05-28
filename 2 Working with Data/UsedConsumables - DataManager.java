package com.company.clinic.web.consumable;

import com.company.clinic.entity.Consumable;
import com.company.clinic.service.ConsumablesService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.Install;
import com.haulmont.cuba.gui.screen.LoadDataBeforeShow;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.Target;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.util.List;

@UiController("clinic_UsedConsumables")
@UiDescriptor("used-consumables.xml")
public class UsedConsumables extends Screen {

    @Inject
    private DataManager dataManager;

    @Inject
    private CollectionContainer<Consumable> consumablesDc;

    @Subscribe
    private void onBeforeShow(BeforeShowEvent event) {
        LoadContext<Consumable> loadContext = new LoadContext<>(Consumable.class)
                .setQuery(new LoadContext.Query(
                        "select distinct c from clinic_Visit v join v.consumables c " +
                                "where @between(c.createTs, now-7, now+1, day)"
                )).setView(View.LOCAL);
        List<Consumable> consumables = dataManager.loadList(loadContext);
        consumablesDc.setItems(consumables);
    }
}