import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class CurrencyConverterApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Map<String, Double> rates = new HashMap<>();
    private String apiKey = "07b0302303c24d0e7f4714ffe7723018"; // Replace with your actual API key

    public CurrencyConverterApp() {
        super("Currency Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add pages
        mainPanel.add(createHomePage(), "Home");
        mainPanel.add(createConverterPage(), "Converter");
        mainPanel.add(createHistoricalPage(), "Historical");

        add(mainPanel);

        // Fetch exchange rates for all functionalities
        fetchExchangeRates();
    }

    private JPanel createHomePage() {
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        homePanel.setBackground(new Color(240, 248, 255)); // Light blue background

        JLabel welcomeLabel = new JLabel("Welcome to Currency Converter", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(25, 42, 86)); // Navy text
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Easily convert and view historical exchange rates", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(90, 90, 90)); // Gray text
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Center buttons with spacing
        buttonPanel.setOpaque(false); // Transparent background

        JButton goToConverter = new JButton("Go to Converter");
        JButton goToHistorical = new JButton("View Historical Rates");

        // Style buttons
        styleButton(goToConverter, new Color(41, 128, 185)); // Blue button
        styleButton(goToHistorical, new Color(34, 153, 84)); // Green button

        goToConverter.addActionListener(e -> cardLayout.show(mainPanel, "Converter"));
        goToHistorical.addActionListener(e -> cardLayout.show(mainPanel, "Historical"));

        buttonPanel.add(goToConverter);
        buttonPanel.add(goToHistorical);

        // Add components to home panel with spacing
        homePanel.add(welcomeLabel);
        homePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Vertical spacing
        homePanel.add(subtitleLabel);
        homePanel.add(Box.createRigidArea(new Dimension(0, 30))); // Vertical spacing
        homePanel.add(buttonPanel);

        return homePanel;
    }

    // Helper method to style buttons
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 40)); // Uniform button size
    }

    private JPanel createConverterPage() {
        JPanel converterPanel = new JPanel();
        converterPanel.setLayout(new BoxLayout(converterPanel, BoxLayout.Y_AXIS));
        converterPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        converterPanel.setBackground(new Color(240, 248, 255)); // Light blue background

        JLabel titleLabel = new JLabel("Currency Converter", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(25, 42, 86)); // Navy text

        JComboBox<String> fromCurrency = new JComboBox<>();
        JComboBox<String> toCurrency = new JComboBox<>();

        JTextField amountField = new JTextField(5); // Reduced width for amount input
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        amountField.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2));
        amountField.setBackground(Color.WHITE);
        amountField.setMaximumSize(new Dimension(100, 25)); // Set fixed size

        JButton convertButton = new JButton("Convert");
        convertButton.setBackground(new Color(41, 128, 185)); // Blue button
        convertButton.setForeground(Color.WHITE);
        convertButton.setFocusPainted(false);
        convertButton.setFont(new Font("Arial", Font.BOLD, 14));
        convertButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel resultLabel = new JLabel("<html><center>Result will appear here.</center></html>", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2), // Blue border
                BorderFactory.createEmptyBorder(10, 10, 10, 10)             // Padding inside the box
        ));
        resultLabel.setOpaque(true);
        resultLabel.setBackground(Color.WHITE); // White background for the box
        resultLabel.setForeground(new Color(34, 153, 84)); // Green text for results
        resultLabel.setMaximumSize(new Dimension(400, 60)); // Restrict box size

        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(192, 57, 43)); // Red button
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add spacing and alignment
        converterPanel.add(titleLabel);
        converterPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Vertical spacing
        converterPanel.add(new JLabel("From Currency:"));
        converterPanel.add(fromCurrency);
        converterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        converterPanel.add(new JLabel("To Currency:"));
        converterPanel.add(toCurrency);
        converterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        converterPanel.add(new JLabel("Amount:"));
        converterPanel.add(amountField);
        converterPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        converterPanel.add(convertButton);
        converterPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        converterPanel.add(resultLabel);
        converterPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        converterPanel.add(backButton);

        // Populate currency dropdowns when rates are fetched
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                while (rates.isEmpty()) {
                    try {
                        Thread.sleep(100); // Wait for rates to load
                    } catch (InterruptedException ignored) {
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                for (String currency : rates.keySet()) {
                    fromCurrency.addItem(currency);
                    toCurrency.addItem(currency);
                }
            }
        };
        worker.execute();

        // Convert button action
        convertButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String from = fromCurrency.getSelectedItem().toString();
                String to = toCurrency.getSelectedItem().toString();
                double rateFrom = rates.getOrDefault(from, 1.0);
                double rateTo = rates.getOrDefault(to, 1.0);
                double result = amount * rateTo / rateFrom;
                resultLabel.setText(String.format("<html><center>%.2f %s = %.2f %s</center></html>", amount, from, result, to));
            } catch (Exception ex) {
                resultLabel.setText("<html><center>Invalid input. Please try again.</center></html>");
            }
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        return converterPanel;
    }


    private JPanel createHistoricalPage() {
        JPanel historicalPanel = new JPanel();
        historicalPanel.setLayout(new BoxLayout(historicalPanel, BoxLayout.Y_AXIS));
        historicalPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        historicalPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        JLabel titleLabel = new JLabel("Historical Exchange Rates", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(39, 60, 117)); // Navy text

        JComboBox<String> fromCurrency = new JComboBox<>();
        JComboBox<String> toCurrency = new JComboBox<>();

        JTextField dateField = new JTextField(5); // Reduced width
        dateField.setFont(new Font("Arial", Font.PLAIN, 14));
        dateField.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2));
        dateField.setBackground(Color.WHITE);
        dateField.setMaximumSize(new Dimension(100, 25)); // Set fixed size

        JButton fetchButton = new JButton("Get Historical Rates");
        JLabel resultLabel = new JLabel("<html><center>Historical rate details will appear here.</center></html>", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2), // Blue border
                BorderFactory.createEmptyBorder(10, 10, 10, 10)             // Padding inside the box
        ));
        resultLabel.setOpaque(true);
        resultLabel.setBackground(Color.WHITE); // White background for the box
        resultLabel.setForeground(new Color(46, 204, 113)); // Green text for results
        resultLabel.setMaximumSize(new Dimension(400, 60)); // Restrict box size

        // Styling for buttons
        fetchButton.setBackground(new Color(52, 152, 219)); // Blue button
        fetchButton.setForeground(Color.WHITE);
        fetchButton.setFocusPainted(false);
        fetchButton.setFont(new Font("Arial", Font.BOLD, 14));
        fetchButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(192, 57, 43)); // Red button
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components with spacing and alignment
        historicalPanel.add(titleLabel);
        historicalPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Vertical spacing
        historicalPanel.add(new JLabel("From Currency:"));
        historicalPanel.add(fromCurrency);
        historicalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        historicalPanel.add(new JLabel("To Currency:"));
        historicalPanel.add(toCurrency);
        historicalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        historicalPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        historicalPanel.add(dateField);
        historicalPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        historicalPanel.add(fetchButton);
        historicalPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        historicalPanel.add(resultLabel);
        historicalPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        historicalPanel.add(backButton);

        // Populate currency dropdowns when rates are fetched
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                while (rates.isEmpty()) {
                    try {
                        Thread.sleep(100); // Wait for rates to load
                    } catch (InterruptedException ignored) {
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                for (String currency : rates.keySet()) {
                    fromCurrency.addItem(currency);
                    toCurrency.addItem(currency);
                }
            }
        };
        worker.execute();

        // Fetch button action
        fetchButton.addActionListener(e -> {
            String date = dateField.getText();
            if (!date.isEmpty()) {
                fetchHistoricalRates(date, fromCurrency.getSelectedItem().toString(), toCurrency.getSelectedItem().toString(), resultLabel);
            } else {
                JOptionPane.showMessageDialog(historicalPanel, "Please enter a valid date.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        return historicalPanel;
    }

    private void fetchExchangeRates() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    String url = "https://data.fixer.io/api/latest?access_key=" + apiKey;

                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONObject ratesJson = jsonResponse.getJSONObject("rates");

                    for (String currency : ratesJson.keySet()) {
                        rates.put(currency, ratesJson.getDouble(currency));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                // Do something after the rates are fetched, e.g., enable UI components or notify the user
            }
        };
        worker.execute();
    }


    private void fetchHistoricalRates(String date, String from, String to, JLabel resultLabel) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    String url = "https://data.fixer.io/api/" + date + "?access_key=" + apiKey;

                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("GET");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    if (jsonResponse.getBoolean("success")) {
                        JSONObject historicalRates = jsonResponse.getJSONObject("rates");

                        double historicalRateFrom = historicalRates.getDouble(from);
                        double historicalRateTo = historicalRates.getDouble(to);
                        double historicalRate = historicalRateTo / historicalRateFrom;

                        resultLabel.setText(String.format("Historical Rate (%s): %.4f", date, historicalRate));
                    } else {
                        JOptionPane.showMessageDialog(CurrencyConverterApp.this, "Failed to fetch historical data.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(CurrencyConverterApp.this, "Failed to fetch historical rates: " + e.getMessage());
                }
                return null;
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CurrencyConverterApp().setVisible(true));
    }
}
