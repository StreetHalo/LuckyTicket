package com.get.lucky.lt.views.main;

class MainPresenter {
    private boolean takePic = false;
    private MainViewInterface mainViewInterface;


    void attach(MainViewInterface mainViewInterface){
        this.mainViewInterface = mainViewInterface;
    }

     void checkNumbers() {
        if(mainViewInterface.getCamFragment().isVisible()) {
                mainViewInterface.startCamWork();
        }
       else  mainViewInterface.getKeyboardFragment().checkResult();

    }

    void detach(){
        mainViewInterface = null;
    }
}
