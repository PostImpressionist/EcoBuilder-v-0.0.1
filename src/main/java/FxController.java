import channels.ChannelType;
import datafulfiller.DataSaver;
import datafulfiller.ModulesData;
import exceptions.*;
import hardware.modules.Module;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import logic.ModulesCounter;
import userInteractors.ConsoleInteractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FxController {

    public GridPane simpleChannelsGrid; // Grid for simple channels
    public GridPane wideChannelGrid;    // Grid for detailed channels
    public CheckBox detailedIOChecked;  // detailed channels chosen
    public CheckBox IPIOChecked;        // IP-IO modules chosen
    public CheckBox handModulesCheked;  // modules with hand switches chosen
    public CheckBox ASPChecked;         // AS-P controller type chosen
    public CheckBox ASBChecked;         // AS-B controller type chosen
    public CheckBox MPCChecked;         // MP-C controller type chosen
    public CheckBox RPCChecked;         // RP-C controller type chosen
    public TextField cabinetName;       // name of DDC cabinet
    public TextField pathToResultFolder; // path to result folder where to create file with results

    public TextField DIchannelInput;     // digital inputs amount input
    public TextField DOchannelInput;     // digital outputs amount input
    public TextField UIchannelInput;    // universal inputs amount input
    public TextField AOchannelInput;        // analogue inputs amount input
    // Inputs from wide GRID
    public TextField DIchannelWide;         // digital inputs amount input
    public TextField DOformCWide;           // DOformC inputs amount input
    public TextField DOformAWide;           // DOrormA inputs amount input
    public TextField AOCurrentChannelWide;  // AOCurrent inputs amount input
    public TextField AOvoltChannelWide;     // AOvolt inputs amount input
    public TextField UIchannelWide;         // UI inputs amount input
    public TextField TIchannelWide;         // TI inputs amount input
    public TextField reserve;               // reserve in % from all IOs


    private boolean isPressedTariffLoader = false;  // welcome scene button to start tariff parsing
    private boolean isPressedFireLogic = false;     // second scene button to start calculations
    private Model model = new Model();


    @FXML
    public Label bottomTextLable = new Label();
    @FXML
    public TextField pathToTariff = new TextField();


    public FxController() {
    }


    ////////////////////   First step scene methods
    public void buttonTariffLoader() {
        bottomConsolePrint("Working on tariff file parsing...");
        System.out.println("nextStep");
        if (!isPressedTariffLoader) {
            System.out.println("Button clicked!");
            isPressedTariffLoader = true;
            String path = pathToTariff.getText();
            try {
                if (model.dataInitFromTariff(path)) {
                    // Thread.currentThread().sleep(1000);
                    bottomConsolePrint("Данные успешно выгружены из тарифа. " +
                            "Задайте настройки расчёта и нажмите кнопку \"Рассчитать\"");
                    changeScene("/second.fxml");

                }

            } catch (TariffParsingException | IOException e) {
                bottomConsolePrint(e + " \nВведите корректный путь к файлу тарифа");
                e.printStackTrace();
                isPressedTariffLoader = false;
            }
        }//end if
    }


    ////////////////////   Second step scene methods    ///////////////////////////////////////////////
    @FXML
    public void fireLogicRoutine(ActionEvent actionEvent) throws ModuleCreationException, NoModuleForSuchChannelType, LogicOperationException, ModuleDataLoadException {
        ConsoleInteractor.writeMessage("fire button pressed. State =  " + isPressedFireLogic);
        if (isPressedFireLogic) return; // do nothing if button were already fired and calculation is started

        isPressedFireLogic = true;
        Map<ChannelType, Integer> taskInfo = new HashMap<>();

        //check all data inputs are correct
        String incorrectDataField = inputDataVerifiedForSecondScene();

        if (!"ok".equals(incorrectDataField)) {
            bottomConsolePrint("Проверьте корректность данных в поле ввода " + incorrectDataField);
            isPressedFireLogic = false;
            return;
        }
        bottomConsolePrint("Ведённые данные корректны. Расчёт начат.");
        // ----------------------------------------------------------------------------------------------------
        // -- different cases to choose right methode to start calculations


        if (!detailedIOChecked.isSelected()) {
            taskInfo.put(ChannelType.DI, Integer.parseInt(DIchannelInput.getText()));
            taskInfo.put(ChannelType.DO, Integer.parseInt(DOchannelInput.getText()));
            taskInfo.put(ChannelType.UI, Integer.parseInt(UIchannelInput.getText()));
            taskInfo.put(ChannelType.AO, Integer.parseInt(AOchannelInput.getText()));
        } else {
            taskInfo.put(ChannelType.DI, Integer.parseInt(DIchannelWide.getText()));
            taskInfo.put(ChannelType.DOformC, Integer.parseInt(DOformCWide.getText()));
            taskInfo.put(ChannelType.DOformA, Integer.parseInt(DOformAWide.getText()));
            taskInfo.put(ChannelType.AOcurrent, Integer.parseInt(AOCurrentChannelWide.getText()));
            taskInfo.put(ChannelType.AOvolt, Integer.parseInt(AOvoltChannelWide.getText()));
            taskInfo.put(ChannelType.UI, Integer.parseInt(UIchannelWide.getText()));
            taskInfo.put(ChannelType.TI, Integer.parseInt(TIchannelWide.getText()));
        }

        int reserv = Integer.parseInt(reserve.getText());
        if (reserv != 0) {
            for (ChannelType key : taskInfo.keySet()) {
                taskInfo.merge(key, (taskInfo.get(key) * reserv) / 100, Integer::sum);
            }
        }
        ModulesCounter modulesCounter = new ModulesCounter();
        Set<Module> moduleSet = modulesCounter.modulesCalc(taskInfo, handModulesCheked.isSelected());

        moduleSet.forEach(System.out::println);


        DataSaver.saveCulculations(pathToResultFolder.getText(), moduleSet, cabinetName.getText());

        bottomConsolePrint("Расчёт закончен");
    }

    // if IO channels detalization is checked up
    @FXML
    public void detailedIOMode(ActionEvent actionEvent) {
        // checkBox is selected
        if (detailedIOChecked.isSelected()) {
            simpleChannelsGrid.setVisible(false);
            wideChannelGrid.setVisible(true);
        } else {
            simpleChannelsGrid.setVisible(true);
            wideChannelGrid.setVisible(false);
        }
    }

    // if IPIO checkBox selected
    @FXML
    public void IPIOMode(ActionEvent actionEvent) {
        if (!IPIOChecked.isSelected()) {
            ASPChecked.setDisable(false);
            ASBChecked.setDisable(false);
            MPCChecked.setDisable(false);
            RPCChecked.setDisable(false);
            handModulesCheked.setDisable(false);
        } else {
            ASPChecked.setDisable(true);
            ASPChecked.setSelected(false);
            ASBChecked.setDisable(true);
            ASBChecked.setSelected(false);
            MPCChecked.setDisable(true);
            MPCChecked.setSelected(false);
            RPCChecked.setDisable(true);
            RPCChecked.setSelected(false);
            handModulesCheked.setDisable(true);
            handModulesCheked.setSelected(false);
        }
    }

    // if handMode outputs channels checkBox selected
    @FXML
    public void handOutsMode(ActionEvent actionEvent) {
        IPIOChecked.setSelected(false);     // uncheck IPIOmodules
        IPIOChecked.setDisable(handModulesCheked.isSelected());
    }

    @FXML
    public void ASPMode(ActionEvent actionEvent) {
        if (ASPChecked.isSelected()) {
            ASBChecked.setDisable(true);
            MPCChecked.setDisable(true);
            RPCChecked.setDisable(true);

            ASBChecked.setSelected(false);
            MPCChecked.setSelected(false);
            RPCChecked.setSelected(false);

        } else {
            ASBChecked.setDisable(false);
            MPCChecked.setDisable(false);
            RPCChecked.setDisable(false);
        }
    }

    @FXML
    public void ASBMode(ActionEvent actionEvent) {
        if (ASBChecked.isSelected()) {
            ASPChecked.setDisable(true);
            MPCChecked.setDisable(true);
            RPCChecked.setDisable(true);

            ASPChecked.setSelected(false);
            MPCChecked.setSelected(false);
            RPCChecked.setSelected(false);

        } else {
            ASPChecked.setDisable(false);
            MPCChecked.setDisable(false);
            RPCChecked.setDisable(false);
        }
    }

    @FXML
    public void MPCMode(ActionEvent actionEvent) {
        if (MPCChecked.isSelected()) {
            ASPChecked.setDisable(true);
            ASBChecked.setDisable(true);
            RPCChecked.setDisable(true);

            ASPChecked.setSelected(false);
            ASBChecked.setSelected(false);
            RPCChecked.setSelected(false);

        } else {
            ASPChecked.setDisable(false);
            ASBChecked.setDisable(false);
            RPCChecked.setDisable(false);
        }
    }

    @FXML
    public void RPCMode(ActionEvent actionEvent) {
        if (RPCChecked.isSelected()) {
            ASPChecked.setDisable(true);
            ASBChecked.setDisable(true);
            MPCChecked.setDisable(true);
            handModulesCheked.setDisable(true);

            ASPChecked.setSelected(false);
            ASBChecked.setSelected(false);
            MPCChecked.setSelected(false);
            handModulesCheked.setSelected(false);

        } else {
            ASPChecked.setDisable(false);
            ASBChecked.setDisable(false);
            MPCChecked.setDisable(false);
            handModulesCheked.setDisable(false);
        }
    }

    ////////////////////   Service methods   ///////////////////////////////////////////////

    // scene changer
    public void changeScene(String sceneFxmlShortUrl) throws IOException {
        Parent secondSceneParent = new FXMLLoader().load(getClass().getResource(sceneFxmlShortUrl)); //"/second.fxml"
        Scene secondScene = new Scene(secondSceneParent);
        StarterClass.mainStage.setScene(secondScene);
    }

    // bottom service console printer
    public void bottomConsolePrint(String s) {
        bottomTextLable.setText(s);
    }

    // data verification for second scene before fire main logic. It returns first Occur of dataField mismatch
    private String inputDataVerifiedForSecondScene() {
        String result = "ok";

        // verify correction IO channels input counts
        if (detailedIOChecked.isSelected()) {
            if (!DIchannelWide.getText().matches("\\d+")) return DIchannelWide.getId();
            if (!DOformCWide.getText().matches("\\d+")) return DOformCWide.getId();
            if (!DOformAWide.getText().matches("\\d+")) return DOformAWide.getId();
            if (!AOCurrentChannelWide.getText().matches("\\d+")) return AOCurrentChannelWide.getId();
            if (!AOvoltChannelWide.getText().matches("\\d+")) return AOvoltChannelWide.getId();
            if (!UIchannelWide.getText().matches("\\d+")) return UIchannelWide.getId();
            if (!TIchannelWide.getText().matches("\\d+")) return TIchannelWide.getId();

        } else {

            if (!DIchannelInput.getText().matches("\\d+")) return DIchannelInput.getId();
            if (!DOchannelInput.getText().matches("\\d+")) return DOchannelInput.getId();
            if (!AOchannelInput.getText().matches("\\d+")) return AOchannelInput.getId();
            if (!UIchannelInput.getText().matches("\\d+")) return UIchannelInput.getId();
        }

        // verify correction of reserve field. Should be from 0 to 100 only
        String reserv = reserve.getText();
        if (reserv == null || ("").equals(reserv) || !reserv.matches("\\d{1,3}") || Integer.parseInt(reserv) > 100)
            return reserve.getId();

        // verify any controller is checked
        if (!(ASBChecked.isSelected() || ASPChecked.isSelected() || MPCChecked.isSelected() ||
                RPCChecked.isSelected()) && !IPIOChecked.isSelected())
            return "выбора типа контроллеров или IPIO модулей";

        // verify path to result folder is correct
        if (pathToResultFolder.getText().isEmpty() || !Files.isDirectory(Paths.get(pathToResultFolder.getText())))
            return pathToResultFolder.getId();


        return result;
    }


}
//           C:\DISK D\tariff\Tariff Moscow 2021 - 4 (19.07.2021) - Digital Buildings.xlsb