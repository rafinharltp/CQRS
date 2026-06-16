package ui;

import command.CreateProductCommand;
import commandhandler.CreateProductHandler;
import model.Product;
import query.GetProductsQuery;
import queryhandler.GetProductsHandler;
import repository.ProductRepository;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainFrame extends JFrame {

    private JTextField txtName;
    private JTextField txtPrice;

    private JTextArea txtLog;

    private JLabel lblCommands;
    private JLabel lblQueries;
    private JLabel lblStatus;

    private DefaultListModel<String> productModel;

    private FlowPanel flowPanel;

    private int commandsExecuted;
    private int queriesExecuted;

    private final ProductRepository repository;

    private final CreateProductHandler commandHandler;

    private final GetProductsHandler queryHandler;

    public MainFrame() {

        repository = new ProductRepository();

        commandHandler =
                new CreateProductHandler(repository);

        queryHandler =
                new GetProductsHandler(repository);

        initialize();
    }

    private void initialize() {

        setTitle("CQRS Visual Demo");

        setSize(1100, 800);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        //-------------------------
        // TOPO
        //-------------------------

        JPanel top = new JPanel(
                new GridLayout(3,2)
        );

        top.add(new JLabel("Nome do Produto:"));

        txtName = new JTextField();

        top.add(txtName);

        top.add(new JLabel("Preço:"));

        txtPrice = new JTextField();

        top.add(txtPrice);

        JButton btnSave =
                new JButton("COMMAND - CADASTRAR");

        JButton btnSearch =
                new JButton("QUERY - CONSULTAR");

        top.add(btnSave);

        top.add(btnSearch);

        add(top, BorderLayout.NORTH);

        //-------------------------
        // CENTRO
        //-------------------------

        flowPanel = new FlowPanel();

        flowPanel.setPreferredSize(
                new Dimension(600,500)
        );

        productModel =
                new DefaultListModel<>();

        JList<String> productList =
                new JList<>(productModel);

        JSplitPane split =
                new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        flowPanel,
                        new JScrollPane(productList)
                );

        split.setDividerLocation(650);

        add(split, BorderLayout.CENTER);

        //-------------------------
        // RODAPÉ
        //-------------------------

        JPanel bottom =
                new JPanel(
                        new BorderLayout()
                );

        JPanel stats =
                new JPanel();

        lblCommands =
                new JLabel("Commands: 0");

        lblQueries =
                new JLabel("Queries: 0");

        stats.add(lblCommands);

        stats.add(lblQueries);

        bottom.add(
                stats,
                BorderLayout.NORTH
        );

        txtLog =
                new JTextArea(10,20);

        txtLog.setEditable(false);

        bottom.add(
                new JScrollPane(txtLog),
                BorderLayout.CENTER
        );

        lblStatus =
                new JLabel(
                        "Status: Sistema iniciado"
                );

        lblStatus.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        bottom.add(
                lblStatus,
                BorderLayout.SOUTH
        );

        add(bottom, BorderLayout.SOUTH);

        //-------------------------
        // EVENTOS
        //-------------------------

        btnSave.addActionListener(
                e -> saveProduct()
        );

        btnSearch.addActionListener(
                e -> searchProducts()
        );
    }

    private void saveProduct() {

        try {

            animateCommandFlow();

            String name =
                    txtName.getText();

            double price =
                    Double.parseDouble(
                            txtPrice.getText()
                    );

            updateStatus(
                    "Recebendo dados"
            );

            log("INPUT recebido");

            CreateProductCommand command =
                    new CreateProductCommand(
                            name,
                            price
                    );

            updateStatus(
                    "Criando Command"
            );

            log(
                    "CreateProductCommand criado"
            );

            commandHandler.handle(
                    command
            );

            updateStatus(
                    "Executando Command Handler"
            );

            log(
                    "CreateProductHandler executado"
            );

            updateStatus(
                    "Persistindo dados"
            );

            log(
                    "Repository.save()"
            );

            commandsExecuted++;

            lblCommands.setText(
                    "Commands: "
                            + commandsExecuted
            );

            updateStatus(
                    "Produto cadastrado"
            );

            log(
                    "Produto salvo com sucesso"
            );

            log(
                    "--------------------------------"
            );

            txtName.setText("");

            txtPrice.setText("");

        }
        catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Dados inválidos!"
            );
        }
    }

    private void searchProducts() {

        animateQueryFlow();

        updateStatus(
                "Recebendo consulta"
        );

        log(
                "INPUT consulta"
        );

        GetProductsQuery query =
                new GetProductsQuery();

        updateStatus(
                "Criando Query"
        );

        log(
                "GetProductsQuery criado"
        );

        List<Product> products =
                queryHandler.handle(
                        query
                );

        updateStatus(
                "Executando Query Handler"
        );

        log(
                "GetProductsHandler executado"
        );

        updateStatus(
                "Consultando Repository"
        );

        log(
                "Repository.findAll()"
        );

        queriesExecuted++;

        lblQueries.setText(
                "Queries: "
                        + queriesExecuted
        );

        productModel.clear();

        for(Product product : products) {

            productModel.addElement(
                    product.toString()
            );
        }

        updateStatus(
                "Consulta concluída"
        );

        log(
                products.size()
                        + " produto(s) encontrados"
        );

        log(
                "--------------------------------"
        );
    }

    private void animateCommandFlow() {

        new Thread(() -> {

            try {

                flowPanel.setActiveStage(
                        CQRSStage.INPUT
                );

                Thread.sleep(700);

                flowPanel.setActiveStage(
                        CQRSStage.COMMAND
                );

                Thread.sleep(700);

                flowPanel.setActiveStage(
                        CQRSStage.COMMAND_HANDLER
                );

                Thread.sleep(700);

                flowPanel.setActiveStage(
                        CQRSStage.REPOSITORY
                );

            }
            catch(Exception ignored){}
        }).start();
    }

    private void animateQueryFlow() {

        new Thread(() -> {

            try {

                flowPanel.setActiveStage(
                        CQRSStage.INPUT
                );

                Thread.sleep(700);

                flowPanel.setActiveStage(
                        CQRSStage.QUERY
                );

                Thread.sleep(700);

                flowPanel.setActiveStage(
                        CQRSStage.QUERY_HANDLER
                );

                Thread.sleep(700);

                flowPanel.setActiveStage(
                        CQRSStage.REPOSITORY
                );

            }
            catch(Exception ignored){}
        }).start();
    }

    private void updateStatus(
            String status) {

        lblStatus.setText(
                "Status: " + status
        );
    }

    private void log(
            String text) {

        String time =
                LocalTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "HH:mm:ss"
                                )
                        );

        txtLog.append(
                "[" + time + "] "
                        + text
                        + "\n"
        );
    }
}