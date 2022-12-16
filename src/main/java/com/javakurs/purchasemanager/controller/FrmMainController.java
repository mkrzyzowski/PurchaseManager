package com.javakurs.purchasemanager.controller;

import com.javakurs.purchasemanager.enums.WindowType;
import com.javakurs.purchasemanager.helper.MsgHelper;
import com.javakurs.purchasemanager.helper.WindowHelper;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FrmMainController {

    public Button btnLogOut;

    public void btnLogOutClicked(ActionEvent actionEvent) {
        MsgHelper.showYesOrNoAlert("Wybierz tak lub nie","Czy na pewno chcesz przejść do ekranu logowania?","",btnLogOut);
        WindowHelper.openWindow(WindowType.FRM_LOGIN);
    }

    public void btnLeaveClicked(ActionEvent actionEvent) {
        MsgHelper.showYesOrNoAlert("Wybierz tak lub nie","Czy na pewno chcesz wyjść z programu?","",btnLogOut);
    }

    public void menItemCloseClicked(ActionEvent actionEvent) {
        MsgHelper.showYesOrNoAlert("Wybierz tak lub nie","Czy na pewno chcesz wyjść z programu?","",btnLogOut);
    }
}