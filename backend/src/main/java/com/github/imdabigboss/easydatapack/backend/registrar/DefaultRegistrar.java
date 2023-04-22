package com.github.imdabigboss.easydatapack.backend.registrar;

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
