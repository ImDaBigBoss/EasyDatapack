package com.github.imdabigboss.easydatapack.backend.registration;

import com.github.imdabigboss.easydatapack.api.items.CustomItem;
import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;
import org.geysermc.geyser.api.item.custom.CustomItemData;
import org.geysermc.geyser.api.item.custom.CustomItemOptions;

public class GeyserRegistrar extends GenericRegistrar {
    private GeyserDefineCustomItemsEvent event;

    public GeyserRegistrar(EasyDatapack datapack, GeyserApi geyserApi) {
        super(datapack);

        //TODO: Find a non-hacky way to do this
        EventRegistrar eventRegistrar = new EventRegistrar() {};
        geyserApi.eventBus().register(eventRegistrar, eventRegistrar);
        geyserApi.eventBus().subscribe(eventRegistrar, GeyserDefineCustomItemsEvent.class, this::onGeyserDefineCustomItemsEvent);
        geyserApi.eventBus().subscribe(eventRegistrar, GeyserPostInitializeEvent.class, this::onGeyserPostInitializeEvent);
    }

    @Subscribe
    public void onGeyserDefineCustomItemsEvent(GeyserDefineCustomItemsEvent event) {
        this.event = event;
        this.registerCustomComponents();
    }

    @Subscribe
    public void onGeyserPostInitializeEvent(GeyserPostInitializeEvent event) {
        this.finalizeRegistration();
    }

    @Override
    public void itemRegistered(CustomItem item) {
        CustomItemOptions options = CustomItemOptions.builder()
                .customModelData(item.getCustomModelData())
                .build();

        CustomItemData data = CustomItemData.builder()
                .name(item.getNamespaceKey())
                .customItemOptions(options)
                .displayName(item.getName())
                .build();

        this.event.register(item.getBaseMaterial().getKey().toString(), data);
    }
}
