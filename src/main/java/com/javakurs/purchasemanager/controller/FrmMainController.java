package com.javakurs.purchasemanager.controller;

import com.javakurs.purchasemanager.enums.WindowType;
import com.javakurs.purchasemanager.helper.WindowHelper;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FrmMainController {

    public Button btnLogOut;

    public void btnLogOutClicked(ActionEvent actionEvent) {
        WindowHelper.openWindow(WindowType.FRM_LOGIN);
        ((Stage) btnLogOut.getScene().getWindow()).close();
    }

    public void btnLeaveClicked(ActionEvent actionEvent) {
        ((Stage) btnLogOut.getScene().getWindow()).close();
    }
}