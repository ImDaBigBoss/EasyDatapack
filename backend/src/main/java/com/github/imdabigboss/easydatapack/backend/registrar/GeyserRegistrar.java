package com.github.imdabigboss.easydatapack.backend.registrar;

import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;

public class GeyserRegistrar extends GenericRegistrar {
    public GeyserRegistrar(EasyDatapack easyDatapack, GeyserApi geyserApi) {
        super(easyDatapack);

        GeyserRegistrar self = this;

        EventRegistrar eventRegistrar = new EventRegistrar() {
            @Subscribe
            public void onGeyserDefineCustomItemsEvent(GeyserDefineCustomItemsEvent event) {
                self.registerCustomComponents(event);
            }

            @Subscribe
            public void onGeyserPostInitializeEvent(GeyserPostInitializeEvent event) {
                self.finalizeRegistration();
            }
        };
        geyserApi.eventBus().register(eventRegistrar, eventRegistrar);
    }

    @Override
    public void onEnableInt() {
    }

    @Override
    public void onDisableInt() {
    }
}
