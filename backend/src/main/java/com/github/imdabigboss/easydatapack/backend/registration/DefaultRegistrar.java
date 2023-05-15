package com.github.imdabigboss.easydatapack.backend.registration;

import com.github.imdabigboss.easydatapack.backend.EasyDatapack;

public class DefaultRegistrar extends GenericRegistrar {
    public DefaultRegistrar(EasyDatapack datapack) {
        super(datapack);
    }

    @Override
    public void register() {
        this.registerCustomComponents();
        this.finalizeRegistration();
    }
}
