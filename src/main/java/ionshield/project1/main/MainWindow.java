package ionshield.project1.main;

import ionshield.project1.graphics.GraphDisplay;
import ionshield.project1.math.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainWindow {
    private JPanel rootPanel;
    private JTextArea log;
    private JTextField c0Field;
    private JButton calculateButton;
    private GraphDisplay graph;
    private JTextField p1Field;
    private JTextField m0Field;
    private JComboBox modeSel;
    private JTextField t0Field;
    private JTextField deltaField;
    
    public static final String TITLE = "Project-1";
    
    private MainWindow() {
        initComponents();
    }
    
    private void initComponents() {
        calculateButton.addActionListener(e -> calculate());
    }
    
    
    
    private void calculate() {
        try {
            log.setText("");
            
            double c0 = Double.parseDouble(c0Field.getText());
            double m0 = Double.parseDouble(m0Field.getText());
            double t0 = Double.parseDouble(t0Field.getText());
            double p1 = Double.parseDouble(p1Field.getText());
            double delta = Double.parseDouble(deltaField.getText());
            
            int steps = 0;
            delta = Math.abs(delta);
            
            Evaporator evaporator = new Evaporator();
            Interpolator result = null;
            List<PointDouble> points = new ArrayList<>();
            switch (modeSel.getSelectedIndex()) {
                case 0:
                    if (p1 < c0) {
                        delta *= -1;
                    }
                    steps = (int)Math.round((p1 - c0) / delta);
                    double currC = c0;
                    for (int i = 0; i <= steps; i++) {
                        
                        evaporator.calculate(currC, m0, t0);
                        PointDouble point = new PointDouble(currC, evaporator.getcOut());
                        points.add(point);
                        log.append("\n" + point.toString(3));
                        
                        currC += delta;
                    }
                    
                    result = new LinearInterpolator(points);
                    break;
                    
                case 1:
                    if (p1 < m0) {
                        delta *= -1;
                    }
                    steps = (int)Math.round((p1 - m0) / delta);
                    double currM = m0;
                    for (int i = 0; i <= steps; i++) {
        
                        evaporator.calculate(c0, currM, t0);
                        PointDouble point = new PointDouble(currM, evaporator.getcOut());
                        points.add(point);
                        log.append("\n" + point.toString(3));
        
                        currM += delta;
                    }
    
                    result = new LinearInterpolator(points);
                    break;
                    
                default:
                    if (p1 < t0) {
                        delta *= -1;
                    }
                    steps = (int)Math.round((p1 - t0) / delta);
                    double currT = t0;
                    for (int i = 0; i <= steps; i++) {
        
                        evaporator.calculate(c0, m0, currT);
                        PointDouble point = new PointDouble(currT, evaporator.getcOut());
                        points.add(point);
                        log.append("\n" + point.toString(3));
        
                        currT += delta;
                    }
    
                    result = new LinearInterpolator(points);
                    break;
            }
            updateGraph(result);
        }
        catch (NumberFormatException e) {
            log.append("\nInvalid input format");
        }
    }
    
    private void updateGraph(Interpolator result) {
        try {
            if (result == null) {
                graph.setInterpolators(new ArrayList<>());
                graph.repaint();
                return;
            }
            graph.setMinX(result.lower());
            graph.setMaxX(result.upper());
            graph.setMinY(result.lowerVal());
            graph.setMaxY(result.upperVal());
            
            List<Interpolator> results = Collections.singletonList(result);
            graph.setInterpolators(results);
            //graph.setInterpolatorsHighligthed(Collections.singletonList(results.get(results.size() - 1)));
    
            graph.repaint();
        }
        catch (NumberFormatException e) {
            log.append("\nInvalid input format");
        }
    }
    
    
    public static void main(String[] args) {
        JFrame frame = new JFrame(TITLE);
        MainWindow gui = new MainWindow();
        frame.setContentPane(gui.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
