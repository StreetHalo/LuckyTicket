package com.example.mihailov.lt.dagger;

import com.example.mihailov.lt.fragments.CamFragment;

import dagger.Component;

@Component(modules = CamModule.class)
public interface CamComponent {

    void inject(CamFragment camFragment);

}
