package se2203b.assignments.ifinance;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;


public class AccountGroupsController{

    @FXML
    private Button btnExit;

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtGroupName;

    @FXML
    private TreeView<Group> treeView;

    @FXML
    private MenuItem btnAdd;

    @FXML
    private MenuItem btnChange;

    @FXML
    private MenuItem btnDelete;


    private Connection conn;

    int optionSelected = 0;

    IFinanceController iFinanceController;
    private GroupAdapter groupAdapter;
    ArrayList<Integer> IDs = new ArrayList<>();
    private AccountCategoryAdapter accountCategoryAdapter;


    public void setIFinanceController(IFinanceController controller) {
        iFinanceController = controller;
    }
    public void setAdapters(AccountCategoryAdapter ac, GroupAdapter groups) throws SQLException {
        accountCategoryAdapter = ac;
        groupAdapter = groups;
        buildData();
    }
    public void buildData() throws SQLException {
        TreeItem root = new TreeItem(null);

        AccountCategory assetsAC = new AccountCategory("Assets", "Debit");
        AccountCategory liabilitiesAC = new AccountCategory("Liabilities", "Credit");
        AccountCategory incomeAC = new AccountCategory("Income", "Credit");
        AccountCategory expensesAC = new AccountCategory("Expenses", "Debit");

        Group assetsVar = new Group();
        assetsVar.setName("Assets");
        assetsVar.setElement(assetsAC);

        Group liabilitiesVar = new Group();
        liabilitiesVar.setName("Liabilities");
        liabilitiesVar.setElement(liabilitiesAC);

        Group incomeVar = new Group();
        incomeVar.setName("Income");
        incomeVar.setElement(incomeAC);

        Group expensesVar = new Group();
        expensesVar.setName("Expenses");
        expensesVar.setElement(expensesAC);

        TreeItem<Group> assetsTI = new TreeItem<>(assetsVar);
        TreeItem<Group> liabilitiesTI = new TreeItem<>(liabilitiesVar);
        TreeItem<Group> incomeTI = new TreeItem<>(incomeVar);
        TreeItem<Group> expensesTI = new TreeItem<>(expensesVar);

        treeView.setRoot(root);
        treeView.setShowRoot(false);
        root.getChildren().addAll(assetsTI, liabilitiesTI, incomeTI, expensesTI);

        IDs = groupAdapter.returnID();

        for (int i = 0; i < IDs.size(); i++) {
            Group id = groupAdapter.returnGroup(IDs.get(i));
            TreeItem<Group> idTI = new TreeItem<>(id);

            if (id.getParent().getID() == 0)
            {
                if (id.getElement().getName().equals("Assets"))
                {
                    assetsTI.getChildren().add(idTI);
                }
                else if (id.getElement().getName().equals("Liabilities"))
                {
                    liabilitiesTI.getChildren().add(idTI);
                }
                else if (id.getElement().getName().equals("Income"))
                {
                    incomeTI.getChildren().add(idTI);
                }
                else if (id.getElement().getName().equals("Expenses"))
                {
                    expensesTI.getChildren().add(idTI);
                }
            }
            else
            {
                if (id.getParent().getElement().getName().equals("Assets"))
                {
                    recursive(assetsTI.getChildren(), idTI);
                }
                else if (id.getParent().getElement().getName().equals("Liabilities"))
                {
                    recursive(liabilitiesTI.getChildren(), idTI);
                }
                else if (id.getParent().getElement().getName().equals("Income"))
                {
                    recursive(incomeTI.getChildren(), idTI);
                }
                else if (id.getParent().getElement().getName().equals("Expenses"))
                {
                    recursive(expensesTI.getChildren(), idTI);
                }
            }
        }
    }

    public void recursive(ObservableList<TreeItem<Group>> groupList, TreeItem<Group> idTI)
    {
        for (int i = 0; i < groupList.size(); i++) {
            TreeItem<Group> subGroup = groupList.get(i);
            if (subGroup.getChildren().isEmpty())
            {
                if (idTI.getValue().getParent().getID() == subGroup.getValue().getID())
                {
                    subGroup.getChildren().add(idTI);
                }
            }
            else
            {
                if (idTI.getValue().getParent().getID() == subGroup.getValue().getID())
                {
                    subGroup.getChildren().add(idTI);
                }
                else
                {
                    recursive(subGroup.getChildren(), idTI);
                }
            }
        }
    }


    @FXML
    void addGroup() throws SQLException {
        Group item = treeView.getSelectionModel().getSelectedItem().getValue();
        TreeItem<Group> itemTI = treeView.getSelectionModel().getSelectedItem();
        int id = groupAdapter.getMax();
        Group newChild = new Group(id, txtGroupName.getText(), item, item.getElement());
        groupAdapter.insertGroup(newChild, item);
        txtGroupName.setText("");
        itemTI.getChildren().add(new TreeItem<>(newChild));
        //buildData();
        txtGroupName.setDisable(true);
        btnSave.setDisable(true);
        optionSelected = 0;
    }

    @FXML
    void changeGroup() throws SQLException {
        Group item = treeView.getSelectionModel().getSelectedItem().getValue();
        txtGroupName.setText(item.getName());
        groupAdapter.modifyGroup(item.getID(), txtGroupName.getText());
        txtGroupName.setText("");
        buildData();
        txtGroupName.setDisable(true);
        btnSave.setDisable(true);
        optionSelected = 0;
    }

    @FXML
    void deleteGroup() throws SQLException {
        Group item = treeView.getSelectionModel().getSelectedItem().getValue();
        TreeItem<Group> itemTI = treeView.getSelectionModel().getSelectedItem();
        TreeItem<Group> itemTIParent = itemTI.getParent();
        itemTIParent.getChildren().remove(itemTI);
        groupAdapter.deleteGroup(item.getID());
    }

    @FXML
    public void unlockSideAdd() {
        txtGroupName.setDisable(false);
        btnSave.setDisable(false);

        optionSelected = 1;
    }

    public void unlockSideChange() {
        txtGroupName.setDisable(false);
        btnSave.setDisable(false);

        Group item = treeView.getSelectionModel().getSelectedItem().getValue();
        txtGroupName.setText(item.getName());

        optionSelected = 2;
    }

    public void save() throws SQLException {
        if (optionSelected == 1)
        {
            addGroup();
        }
        else if (optionSelected == 2)
        {
            changeGroup();
        }
    }

    public void check()
    {
        TreeItem<Group> item = treeView.getSelectionModel().getSelectedItem();


        if (item.getValue().getID() == 0)
        {
            btnDelete.setDisable(true);
            btnChange.setDisable(true);
        }
        else if (item.getChildren().size() > 0)
        {
            btnDelete.setDisable(true);
        }
        else
        {
            btnChange.setDisable(false);
            btnDelete.setDisable(false);
        }
    }
    public void exit() {
        // Get current stage reference
        iFinanceController.disableMenuItems();
        Stage stage = (Stage) btnExit.getScene().getWindow();
        // Close stage
        stage.close();
    }
}
