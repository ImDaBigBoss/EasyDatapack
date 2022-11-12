package com.github.imdabigboss.easydatapack.backend.registrar;

import com.github.imdabigboss.easydatapack.backend.EasyDatapack;

public class DefaultRegistrar extends GenericRegistrar {
    public DefaultRegistrar(EasyDatapack easyDatapack) {
        super(easyDatapack);
    }

    @Override
    public void onEnableInt() {
        this.registerCustomComponents(null);
        this.finalizeRegistration();
    }

    @Override
    public void onDisableInt() {

    }
}
