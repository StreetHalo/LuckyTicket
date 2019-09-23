package com.get.lucky.lt.dagger;

import com.get.lucky.lt.fragments.CamFragment;

import dagger.Component;

@Component(modules = CamModule.class)
public interface CamComponent {

    void inject(CamFragment camFragment);

}
